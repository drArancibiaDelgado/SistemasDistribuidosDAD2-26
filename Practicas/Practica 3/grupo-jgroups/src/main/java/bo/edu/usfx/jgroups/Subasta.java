package bo.edu.usfx.jgroups;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subasta implements Serializable {
    private final String articulo;
    private final double precioBase;
    private final long tiempoCierreAbsoluto;
    private final String creador;
    private boolean activa;
    private final List<Puja> historialPujas;

    public Subasta(String articulo, double precioBase, int segundosDuracion, String creador) {
        this.articulo = articulo;
        this.precioBase = precioBase;
        // Se guarda el instante exacto en el futuro en milisegundos
        this.tiempoCierreAbsoluto = System.currentTimeMillis() + (segundosDuracion * 1000L);
        this.creador = creador;
        this.activa = true;
        this.historialPujas = new ArrayList<>();
    }

    public void agregarPuja(Puja puja) {
        historialPujas.add(puja);
    }

    public double getMejorPuja() {
        if (historialPujas.isEmpty()) return precioBase;
        return historialPujas.get(historialPujas.size() - 1).getMonto();
    }

    public Puja getUltimaPuja() {
        if (historialPujas.isEmpty()) return null;
        return historialPujas.get(historialPujas.size() - 1);
    }

    public void cerrar() { this.activa = false; }
    public boolean isActiva() { return activa; }
    public String getArticulo() { return articulo; }
    public long getTiempoCierreAbsoluto() { return tiempoCierreAbsoluto; }
    public List<Puja> getHistorialPujas() { return historialPujas; }
}