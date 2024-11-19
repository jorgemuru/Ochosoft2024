package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ESTADOS_EVENTOS")
public class EstadoEvento implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_ESTADOEVENTO", initialValue = 500, sequenceName = "SEQ_ID_ESTADOEVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_ESTADOEVENTO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idEstadoEvento;
    @Column(unique = true, nullable = false)
    private String descripcion;
    
	public Long getIdEstadoEvento() {
		return idEstadoEvento;
	}
	public void setIdEstadoEvento(Long idEstadoEvento) {
		this.idEstadoEvento = idEstadoEvento;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstadoEvento() {
	}
	public EstadoEvento(Long idEstadoEvento, String descripcion) {
		super();
		this.idEstadoEvento = idEstadoEvento;
		this.descripcion = descripcion;
	}
	public EstadoEvento(String descripcion) {
		super();
		this.descripcion = descripcion;
	}

	
    
}//fin clase
