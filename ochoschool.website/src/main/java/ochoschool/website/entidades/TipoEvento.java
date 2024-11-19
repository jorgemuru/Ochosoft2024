package ochoschool.website.entidades;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="TIPOS_EVENTOS")
public class TipoEvento implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_TIPOEVENTO", initialValue = 500, sequenceName = "SEQ_ID_TIPOEVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_TIPOEVENTO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idTipoEvento;
    @Column(unique = true, nullable = false)
    private String descripcion;

    public Long getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(Long idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoEvento(Long idTipoEvento, String descripcion) {
        this.idTipoEvento = idTipoEvento;
        this.descripcion = descripcion;
    }

    public TipoEvento(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoEvento() {
    }

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, idTipoEvento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoEvento other = (TipoEvento) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(idTipoEvento, other.idTipoEvento);
	}
    
    
    
}//fin clase
