/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aforo;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;

public class NodoPuerta implements Receiver {

    private JChannel canal;
    private String nombrePuerta;
    private int aforoMaximo;
    
    private final AtomicInteger ocupacionActual = new AtomicInteger(0);
    private View vistaAnterior;

    public void iniciar(String nombre, int aforo) throws Exception {
        this.nombrePuerta = nombre;
        this.aforoMaximo = aforo;

        canal = new JChannel();
        canal.name(nombrePuerta);
        canal.setReceiver(this);
        canal.connect("AforoSIS258"); // Nombre del grupo exigido

        // Pide el estado replicado al unirse (max 10 segundos)
        canal.getState(null, 10000);

        leerTeclado();
        canal.close();
    }

    private void leerTeclado() throws Exception {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Comandos: /entrar n | /salir n | /estado");
        String linea;

        while ((linea = teclado.readLine()) != null) {
            String[] partes = linea.split(" ");
            String comando = partes[0];

            if (comando.equals("/entrar")) {
                int n = Integer.parseInt(partes[1]);
                Address coordinador = canal.getView().getCoord();
                
                // Envía SOLICITUD por unicast solo al coordinador
                MensajeAforo msg = new MensajeAforo(TipoMensaje.SOLICITUD, nombrePuerta, n);
                canal.send(new ObjectMessage(coordinador, msg));
                System.out.println("Solicitud enviada al coordinador...");

            } else if (comando.equals("/salir")) {
                int n = Integer.parseInt(partes[1]);
                
                // Difunde SALIDA por multicast a todos
                MensajeAforo msg = new MensajeAforo(TipoMensaje.SALIDA, nombrePuerta, n);
                canal.send(new ObjectMessage(null, msg));

            } else if (comando.equals("/estado")) {
                System.out.println("Estado actual: " + ocupacionActual.get() + "/" + aforoMaximo);
                if (ocupacionActual.get() == aforoMaximo) {
                    System.out.println("AFORO COMPLETO");
                }
            }
        }
    }

    @Override
    public void viewAccepted(View vista) {
        if (vistaAnterior != null) {
            Address[][] cambios = View.diff(vistaAnterior, vista);
            for (Address a : cambios[0]) {
                System.out.println("** ENTRÓ la puerta: " + a);
            }
            for (Address a : cambios[1]) {
                System.out.println("** SALIÓ la puerta: " + a);
            }
        }
        vistaAnterior = vista;
        System.out.println("** Coordinador actual: " + vista.getCoord());
    }

    @Override
    public void receive(Message msg) {
        MensajeAforo msgAforo = (MensajeAforo) msg.getObject();

        if (msgAforo.getTipo() == TipoMensaje.SOLICITUD) {
            // Solo el coordinador decide
            if (canal.getAddress().equals(canal.getView().getCoord())) {
                if (ocupacionActual.get() + msgAforo.getPersonas() <= aforoMaximo) {
                    MensajeAforo aceptado = new MensajeAforo(TipoMensaje.ACEPTADO, msgAforo.getPuerta(), msgAforo.getPersonas());
                    try {
                        canal.send(new ObjectMessage(null, aceptado)); // Multicast
                    } catch (Exception e) {}
                } else {
                    MensajeAforo rechazado = new MensajeAforo(TipoMensaje.RECHAZADO, msgAforo.getPuerta(), msgAforo.getPersonas());
                    try {
                        canal.send(new ObjectMessage(msg.getSrc(), rechazado)); // Unicast a quien lo pidió
                    } catch (Exception e) {}
                }
            }
        } 
        else if (msgAforo.getTipo() == TipoMensaje.ACEPTADO) {
            int nuevaOcupacion = ocupacionActual.addAndGet(msgAforo.getPersonas());
            System.out.println("Ingreso ACEPTADO desde puerta " + msgAforo.getPuerta() + " (+" + msgAforo.getPersonas() + ").");
            if (nuevaOcupacion == aforoMaximo) {
                System.out.println("AFORO COMPLETO");
            }
        } 
        else if (msgAforo.getTipo() == TipoMensaje.RECHAZADO) {
            System.out.println("Ingreso RECHAZADO: Se supera el aforo maximo.");
        } 
        else if (msgAforo.getTipo() == TipoMensaje.SALIDA) {
            int nuevaOcupacion = ocupacionActual.addAndGet(-msgAforo.getPersonas());
            if (nuevaOcupacion < 0) {
                ocupacionActual.set(0); // Evitar aforos negativos por error humano
            }
            System.out.println("Salida registrada desde puerta " + msgAforo.getPuerta() + " (-" + msgAforo.getPersonas() + ").");
        }
    }

    @Override
    public void getState(OutputStream salida) throws Exception {
        // El coordinador manda la ocupacion a la puerta nueva
        Util.objectToStream(ocupacionActual.get(), new DataOutputStream(salida));
    }

    @Override
    public void setState(InputStream entrada) throws Exception {
        // La puerta nueva recibe la ocupacion
        Integer estadoRecibido = (Integer) Util.objectFromStream(new DataInputStream(entrada));
        ocupacionActual.set(estadoRecibido);
        System.out.println("** Estado sincronizado. Ocupación actual: " + ocupacionActual.get());
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java NodoPuerta <nombre> <aforoMaximo>");
            return;
        }
        
        try {
            String nombre = args[0];
            int aforo = Integer.parseInt(args[1]);
            new NodoPuerta().iniciar(nombre, aforo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}