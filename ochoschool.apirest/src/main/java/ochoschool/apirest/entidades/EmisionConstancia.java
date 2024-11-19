package ochoschool.apirest.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="EMISIONES_CONSTANCIA")
public class EmisionConstancia implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_ID_EMISIONCONSTANCIA", initialValue = 500, sequenceName = "SEQ_ID_EMISIONCONSTANCIA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_EMISIONCONSTANCIA")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idEmisionConstancia;
    @ManyToOne
    @JoinColumn(name = "idAnalista", nullable = false)
    private Analista analista;
    @ManyToOne
    @JoinColumn(name = "idConstancia", nullable = false)
    private Constancia constancia;
    @ManyToOne
    @JoinColumn(name = "idPlantillaConstancia", nullable = false)
    private PlantillaConstancia plantilla;
	private byte[] archivoConstancia;
	private Date fechaEmision;
    private String activo;
	
    //Getters and setters
    public Long getIdEmisionConstancia() {
		return idEmisionConstancia;
	}
	public void setIdEmisionConstancia(Long idEmisionConstancia) {
		this.idEmisionConstancia = idEmisionConstancia;
	}
	public Analista getAnalista() {
		return analista;
	}
	public void setAnalista(Analista analista) {
		this.analista = analista;
	}
	public Constancia getConstancia() {
		return constancia;
	}
	public void setConstancia(Constancia constancia) {
		this.constancia = constancia;
	}
	public PlantillaConstancia getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(PlantillaConstancia plantilla) {
		this.plantilla = plantilla;
	}
	public byte[] getArchivoConstancia() {
		return archivoConstancia;
	}
	public void setArchivoConstancia(byte[] archivoConstancia) {
		this.archivoConstancia = archivoConstancia;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	public EmisionConstancia(Long idEmisionConstancia, Analista analista, Constancia constancia,
			PlantillaConstancia plantilla, byte[] archivoConstancia, Date fechaEmision, String activo) {
		super();
		this.idEmisionConstancia = idEmisionConstancia;
		this.analista = analista;
		this.constancia = constancia;
		this.plantilla = plantilla;
		this.archivoConstancia = archivoConstancia;
		this.fechaEmision = fechaEmision;
		this.activo = activo;
	}
	public EmisionConstancia(Analista analista, Constancia constancia, PlantillaConstancia plantilla,
			byte[] archivoConstancia, Date fechaEmision, String activo) {
		super();
		this.analista = analista;
		this.constancia = constancia;
		this.plantilla = plantilla;
		this.archivoConstancia = archivoConstancia;
		this.fechaEmision = fechaEmision;
		this.activo = activo;
	}
	public EmisionConstancia() {
	}
	
     
}//fin clase
