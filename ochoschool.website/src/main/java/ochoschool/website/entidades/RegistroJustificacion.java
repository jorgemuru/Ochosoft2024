package ochoschool.website.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;


public class RegistroJustificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_REGISTROJUSTIFICACION", initialValue = 500, sequenceName = "SEQ_ID_REGISTROJUSTIFICACION")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_REGISTROJUSTIFICACION")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idRegistroJustificacion;
	@ManyToOne
	@JoinColumn(name = "idJustificacion", nullable = false)
	private Justificacion justificacion;
	@ManyToOne
	@JoinColumn(name = "idAnalista", nullable = false)
	private Analista analista;
	//@Temporal(TemporalType.DATE)
	private Date fechaHora;
	private String detalle;
	public Long getIdRegistroJustificacion() {
		return idRegistroJustificacion;
	}
	public void setIdRegistroJustificacion(Long idRegistroJustificacion) {
		this.idRegistroJustificacion = idRegistroJustificacion;
	}
	public Justificacion getJustificacion() {
		return justificacion;
	}
	public void setJustificacion(Justificacion justificacion) {
		this.justificacion = justificacion;
	}
	public Analista getAnalista() {
		return analista;
	}
	public void setAnalista(Analista analista) {
		this.analista = analista;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	
	public RegistroJustificacion(Long idRegistroJustificacion, Justificacion justificacion, Analista analista,
			Date fechaHora, String detalle) {
		super();
		this.idRegistroJustificacion = idRegistroJustificacion;
		this.justificacion = justificacion;
		this.analista = analista;
		this.fechaHora = fechaHora;
		this.detalle = detalle;
	}
	
	public RegistroJustificacion(Justificacion justificacion, Analista analista, Date fechaHora, String detalle) {
		super();
		this.justificacion = justificacion;
		this.analista = analista;
		this.fechaHora = fechaHora;
		this.detalle = detalle;
	}
	
	public RegistroJustificacion() {
		
	}

}// fin clase
