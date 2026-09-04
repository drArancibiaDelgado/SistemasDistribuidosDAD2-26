package bo.edu.usfx.jgroups;
import java.io.Serializable;

public class Puja implements Serializable {
    private final String articulo;
    private final String postor;
    private final double monto;

    public Puja(String articulo, String postor, double monto) {
        this.articulo = articulo;
        this.postor = postor;
        this.monto = monto;
    }

    public String getArticulo() { return articulo; }
    public String getPostor() { return postor; }
    public double getMonto() { return monto; }

    @Override
    public String toString() {
        return postor + " pujo " + monto;
    }
}