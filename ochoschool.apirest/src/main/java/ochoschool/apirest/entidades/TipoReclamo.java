package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="TIPOS_RECLAMOS")
public class TipoReclamo implements Serializable{
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_TIPORECLAMO", initialValue = 500, sequenceName = "SEQ_ID_TIPORECLAMO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_TIPORECLAMO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idTipoReclamo;
    @Column(unique = true, nullable = false)
    private String descripcion;
	public Long getIdTipoReclamo() {
		return idTipoReclamo;
	}
	public void setIdTipoReclamo(Long idTipoReclamo) {
		this.idTipoReclamo = idTipoReclamo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public TipoReclamo(Long idTipoReclamo, String descripcion) {
		super();
		this.idTipoReclamo = idTipoReclamo;
		this.descripcion = descripcion;
	}
	public TipoReclamo(String descripcion) {
		super();
		this.descripcion = descripcion;
	}
	public TipoReclamo() {
	}
    
}//fin clase
