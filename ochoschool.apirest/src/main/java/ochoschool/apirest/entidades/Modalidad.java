package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="MODALIDADES")
public class Modalidad implements Serializable{
	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_MODALIDAD", initialValue = 500, sequenceName = "SEQ_ID_MODALIDAD")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_MODALIDAD")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idModalidad;
    @Column(unique = true, nullable = false)
    private String descripcion;
	public Long getIdModalidad() {
		return idModalidad;
	}
	public void setIdModalidad(Long idModalidad) {
		this.idModalidad = idModalidad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
    public Modalidad() {
    }
	public Modalidad(Long idModalidad, String descripcion) {
		super();
		this.idModalidad = idModalidad;
		this.descripcion = descripcion;
	}
	public Modalidad(String descripcion) {
		super();
		this.descripcion = descripcion;
	}
    
    
}//fin clase
