package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "REGISTRO_CONSTANCIAS")
public class RegistroConstancia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_REGISTROCONSTANCIA", initialValue = 500, sequenceName = "SEQ_ID_REGISTROCONSTANCIA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_REGISTROCONSTANCIA")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idRegistroConstancia;
	@ManyToOne
	@JoinColumn(name = "idConstancia", nullable = false)
	private Constancia constancia;
	@ManyToOne
	@JoinColumn(name = "idAnalista", nullable = false)
	private Analista analista;
	//@Temporal(TemporalType.DATE)
	private Date fechaHora;
	private String detalle;

	public Long getIdRegistroConstancia() {
		return idRegistroConstancia;
	}

	public void setIdRegistroConstancia(Long idRegistroConstancia) {
		this.idRegistroConstancia = idRegistroConstancia;
	}

	public Constancia getConstancia() {
		return constancia;
	}

	public void setConstancia(Constancia constancia) {
		this.constancia = constancia;
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

	public RegistroConstancia() {

	}

	public RegistroConstancia(Long idRegistroConstancia, Constancia constancia, Analista analista, Date fechaHora,
			String detalle) {
		super();
		this.idRegistroConstancia = idRegistroConstancia;
		this.constancia = constancia;
		this.analista = analista;
		this.fechaHora = fechaHora;
		this.detalle = detalle;
	}

	public RegistroConstancia(Constancia constancia, Analista analista, Date fechaHora, String detalle) {
		super();
		this.constancia = constancia;
		this.analista = analista;
		this.fechaHora = fechaHora;
		this.detalle = detalle;
	}

}// fin clase
