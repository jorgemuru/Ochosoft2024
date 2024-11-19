package ochoschool.apirest.entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="TIPOS")
public class Tipo implements Serializable{

	private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_ID_TIPO", initialValue = 500, sequenceName = "SEQ_ID_TIPO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_TIPO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idTipo;
    @Column(unique = true, nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private String activo;
    
    //Getters y Setters
	public Long getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(Long idTipo) {
		this.idTipo = idTipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
    
    public Tipo() {
   
    }
    
	public Tipo(Long idTipo, String descripcion, String activo) {
		super();
		this.idTipo = idTipo;
		this.descripcion = descripcion;
		this.activo = activo;
	}
	
	public Tipo(String descripcion, String activo) {
		super();
		this.descripcion = descripcion;
		this.activo = activo;
	}
	
}//fin clase
