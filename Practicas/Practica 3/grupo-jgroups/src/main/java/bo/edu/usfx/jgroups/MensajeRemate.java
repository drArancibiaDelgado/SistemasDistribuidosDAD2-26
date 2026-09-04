package bo.edu.usfx.jgroups;
import java.io.Serializable;

public class MensajeRemate implements Serializable {
    private final TipoMensaje tipo;
    private final Object contenido;

    public MensajeRemate(TipoMensaje tipo, Object contenido) {
        this.tipo = tipo;
        this.contenido = contenido;
    }

    public TipoMensaje getTipo() { return tipo; }
    public Object getContenido() { return contenido; }
}