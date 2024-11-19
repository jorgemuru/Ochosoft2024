package ochoschool.apirest.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="PLANTILLAS_CONSTANCIA")
public class PlantillaConstancia implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_ID_PLANTILLACONSTANCIA", initialValue = 500, sequenceName = "SEQ_ID_PLANTILLACONSTANCIA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_PLANTILLACONSTANCIA")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idPlantillaConstancia;
	@ManyToOne
	@JoinColumn(name = "idTipo", nullable = false)
	private Tipo tipo;
	private byte[] modelo;
	private String campos;
    private String activo;
    
    //Getters y Setters
	public Long getIdPlantillaConstancia() {
		return idPlantillaConstancia;
	}
	public void setIdPlantillaConstancia(Long idPlantillaConstancia) {
		this.idPlantillaConstancia = idPlantillaConstancia;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public byte[] getModelo() {
		return modelo;
	}
	public void setModelo(byte[] modelo) {
		this.modelo = modelo;
	}
	public String getCampos() {
		return campos;
	}
	public void setCampos(String campos) {
		this.campos = campos;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	public PlantillaConstancia(Long idPlantillaConstancia, Tipo tipo, byte[] modelo, String campos,
			String activo) {
		super();
		this.idPlantillaConstancia = idPlantillaConstancia;
		this.tipo = tipo;
		this.modelo = modelo;
		this.campos = campos;
		this.activo = activo;
	}
	public PlantillaConstancia(Tipo tipo, byte[] modelo, String campos, String activo) {
		super();
		this.tipo = tipo;
		this.modelo = modelo;
		this.campos = campos;
		this.activo = activo;
	}
	public PlantillaConstancia() {
	}
    
    
}//fin clase
