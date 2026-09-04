package bo.edu.usfx.jgroups;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import org.jgroups.*;
import org.jgroups.util.Util;

public class RemateNodo implements Receiver {
    private JChannel canal;
    private final String nombreParticipante;

    // REQUISITO 8: Estado compartido protegido. Usamos ConcurrentHashMap para concurrencia segura.
    private final Map<String, Subasta> subastasActivas = new ConcurrentHashMap<>();

    // Temporizadores para cerrar subastas (solo los usará el coordinador)
    private final ScheduledExecutorService temporizadores = Executors.newScheduledThreadPool(4);
    private final Map<String, ScheduledFuture<?>> tareasCierre = new ConcurrentHashMap<>();

    public RemateNodo(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    public void iniciar() throws Exception {
        canal = new JChannel(System.getProperty("config", "udp.xml"));
        canal.name(nombreParticipante);
        canal.setReceiver(this);
        canal.connect("RemateSIS258");
        // REQUISITO 4: Un nodo que entra pide el estado completo al coordinador.
        canal.getState(null, 10000);

        leerTeclado();

        temporizadores.shutdownNow();
        canal.close();
    }

    // ===== MEMBRESÍA Y TRASPASO DE COORDINADOR =====
    @Override
    public void viewAccepted(View vista) {
        System.out.println("** Vista actualizada. Miembros: " + vista.getMembers());

        // REQUISITO 6: Detectar si este nodo es el coordinador actual
        boolean soyCoordinador = vista.getCoord().equals(canal.getAddress());

        if (soyCoordinador) {
            // El coordinador retoma los temporizadores de las subastas abiertas
            for (Subasta subasta : subastasActivas.values()) {
                if (subasta.isActiva() && !tareasCierre.containsKey(subasta.getArticulo())) {
                    programarCierre(subasta);
                }
            }
        }
    }

    // ===== RECEPCIÓN DE MENSAJES (PROTOCOLO) =====
    @Override
    public void receive(Message msg) {
        if (!(msg.getObject() instanceof MensajeRemate)) return;

        MensajeRemate mensaje = msg.getObject();
        boolean soyCoordinador = canal.getView().getCoord().equals(canal.getAddress());

        switch (mensaje.getTipo()) {
            case NUEVA_SUBASTA:
                Subasta nuevaSubasta = (Subasta) mensaje.getContenido();
                subastasActivas.put(nuevaSubasta.getArticulo(), nuevaSubasta);
                System.out.println("\n*** NUEVA SUBASTA: " + nuevaSubasta.getArticulo() + " (Base: " + nuevaSubasta.getMejorPuja() + ") ***");
                if (soyCoordinador) programarCierre(nuevaSubasta);
                break;

            case PROPONER_PUJA: // REQUISITO 5a: Unicast recibido SOLO por el coordinador
                Puja propuesta = (Puja) mensaje.getContenido();
                Subasta sub = subastasActivas.get(propuesta.getArticulo());

                // El coordinador valida
                if (sub != null && sub.isActiva() && propuesta.getMonto() > sub.getMejorPuja()) {
                    try {
                        // Difunde la puja aceptada a TODOS (Multicast)
                        canal.send(new ObjectMessage(null, new MensajeRemate(TipoMensaje.PUJA_ACEPTADA, propuesta)));
                    } catch (Exception e) { e.printStackTrace(); }
                } else if (msg.getSrc().equals(canal.getAddress())) {
                    System.out.println("-> Tu puja fue rechazada (monto insuficiente o subasta cerrada).");
                }
                break;

            case PUJA_ACEPTADA:
                Puja pujaAceptada = (Puja) mensaje.getContenido();
                Subasta s = subastasActivas.get(pujaAceptada.getArticulo());
                if (s != null) {
                    s.agregarPuja(pujaAceptada);
                    System.out.println("\n*** PUJA ACEPTADA en '" + s.getArticulo() + "': " + pujaAceptada.toString() + " ***");
                }
                break;

            case CIERRE_SUBASTA:
                String articuloCerrado = (String) mensaje.getContenido();
                Subasta subastaCerrada = subastasActivas.get(articuloCerrado);
                if (subastaCerrada != null) {
                    subastaCerrada.cerrar();
                    Puja ganadora = subastaCerrada.getUltimaPuja();
                    String msjGanador = (ganadora != null) ? "Ganador: " + ganadora.getPostor() + " por " + ganadora.getMonto() : "Sin pujas. Desierta.";
                    System.out.println("\n*** SUBASTA CERRADA: " + articuloCerrado + " | " + msjGanador + " ***");
                }
                break;
        }
    }

    // ===== LÓGICA DEL COORDINADOR (TEMPORIZADORES) =====
    private void programarCierre(Subasta subasta) {
        long milisegundosFaltantes = subasta.getTiempoCierreAbsoluto() - System.currentTimeMillis();

        if (milisegundosFaltantes <= 0) {
            // REQUISITO 6: Si caducó mientras no había coordinador, se cierra inmediatamente.
            ejecutarCierre(subasta.getArticulo());
        } else {
            ScheduledFuture<?> tarea = temporizadores.schedule(() -> {
                ejecutarCierre(subasta.getArticulo());
            }, milisegundosFaltantes, TimeUnit.MILLISECONDS);
            tareasCierre.put(subasta.getArticulo(), tarea);
        }
    }

    private void ejecutarCierre(String articulo) {
        try {
            canal.send(new ObjectMessage(null, new MensajeRemate(TipoMensaje.CIERRE_SUBASTA, articulo)));
            tareasCierre.remove(articulo);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ===== TRANSFERENCIA DE ESTADO =====
    @Override
    public void getState(OutputStream salida) throws Exception {
        Util.objectToStream(subastasActivas, new DataOutputStream(salida));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setState(InputStream entrada) throws Exception {
        Map<String, Subasta> estadoRecibido = (Map<String, Subasta>) Util.objectFromStream(new DataInputStream(entrada));
        subastasActivas.clear();
        subastasActivas.putAll(estadoRecibido);
        System.out.println("** Estado sincronizado: " + subastasActivas.size() + " subastas cargadas.");
    }

    // ===== INTERFAZ DE CONSOLA =====
    private void leerTeclado() throws Exception {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nComandos: /crear <art> <base> <seg> | /subastas | /pujar <art> <monto> | /estado <art> | /quien | /salir");
        String linea;

        while ((linea = teclado.readLine()) != null) {
            if (linea.equals("/salir")) break;
            String[] args = linea.split(" ");

            try {
                if (linea.startsWith("/crear ") && args.length == 4) {
                    String art = args[1];
                    double base = Double.parseDouble(args[2]);
                    int seg = Integer.parseInt(args[3]);
                    if (subastasActivas.containsKey(art)) {
                        System.out.println("El artículo ya está en subasta.");
                        continue;
                    }
                    Subasta nueva = new Subasta(art, base, seg, nombreParticipante);
                    canal.send(new ObjectMessage(null, new MensajeRemate(TipoMensaje.NUEVA_SUBASTA, nueva)));

                } else if (linea.startsWith("/pujar ") && args.length == 3) {
                    String art = args[1];
                    double monto = Double.parseDouble(args[2]);
                    Puja propuesta = new Puja(art, nombreParticipante, monto);
                    // REQUISITO 5a: Enviar propuesta por Unicast al coordinador actual
                    canal.send(new ObjectMessage(canal.getView().getCoord(), new MensajeRemate(TipoMensaje.PROPONER_PUJA, propuesta)));

                } else if (linea.equals("/subastas")) {
                    System.out.println("--- SUBASTAS ACTIVAS ---");
                    for (Subasta s : subastasActivas.values()) {
                        if (s.isActiva()) {
                            long seg = Math.max(0, (s.getTiempoCierreAbsoluto() - System.currentTimeMillis()) / 1000);
                            String mejorPostor = s.getUltimaPuja() != null ? s.getUltimaPuja().getPostor() : "Nadie";
                            System.out.println("- " + s.getArticulo() + " | Mejor puja: " + s.getMejorPuja() + " (" + mejorPostor + ") | Faltan: " + seg + "s");
                        }
                    }
                } else if (linea.startsWith("/estado ") && args.length == 2) {
                    Subasta s = subastasActivas.get(args[1]);
                    if (s != null) {
                        System.out.println("--- HISTORIAL DE: " + s.getArticulo() + " ---");
                        s.getHistorialPujas().forEach(p -> System.out.println(p.toString()));
                    } else System.out.println("Subasta no encontrada.");
                } else if (linea.equals("/quien")) {
                    System.out.println("Miembros: " + canal.getView().getMembers() + " | Coordinador: " + canal.getView().getCoord());
                }
            } catch (Exception e) {
                System.out.println("Error en comando. Verifique la sintaxis.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String nombre = args.length > 0 ? args[0] : "Participante-" + (int)(Math.random() * 100);
        new RemateNodo(nombre).iniciar();
    }
}