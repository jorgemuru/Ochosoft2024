package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "JUSTIFICACIONES")
public class Justificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_JUSTIFICACION", initialValue = 500, sequenceName = "SEQ_ID_JUSTIFICACION")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_JUSTIFICACION")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idJustificacion;
	@Column(nullable = false, length = 50)
	private String Detalle;
	//@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date fechaHora;
    @ManyToOne
    @JoinColumn(name = "idConvocadoEvento", nullable = false)
    //@JsonBackReference
    private ConvocadoEvento convocadoEvento;
	@ManyToOne
	@JoinColumn(name = "idEstado", nullable = false)
	private Estado estado;
	@Lob
	@Column(name = "archivo_adjunto")
	private byte[] archivoAdjunto;

	public Long getIdJustificacion() {
		return idJustificacion;
	}

	public void setIdJustificacion(Long idJustificacion) {
		this.idJustificacion = idJustificacion;
	}

	public String getDetalle() {
		return Detalle;
	}

	public void setDetalle(String detalle) {
		Detalle = detalle;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public ConvocadoEvento getConvocadoEvento() {
		return convocadoEvento;
	}

	public void setConvocadoEvento(ConvocadoEvento convocadoEvento) {
		this.convocadoEvento = convocadoEvento;
	}

	public byte[] getArchivoAdjunto() {
		return archivoAdjunto;
	}

	public void setArchivoAdjunto(byte[] archivoAdjunto) {
		this.archivoAdjunto = archivoAdjunto;
	}

	public Justificacion() {

	}

	public Justificacion(Long idJustificacion, String detalle, Date fechaHora, Estado estado, ConvocadoEvento convocado, byte[] archivoAdjunto) {
		super();
		this.idJustificacion = idJustificacion;
		Detalle = detalle;
		this.fechaHora = fechaHora;
		this.estado = estado;
		this.convocadoEvento = convocado;
		this.archivoAdjunto = archivoAdjunto;
	}

	public Justificacion(String detalle, Date fechaHora, Estado estado, ConvocadoEvento convocado, byte[] archivoAdjunto) {
		super();
		Detalle = detalle;
		this.fechaHora = fechaHora;
		this.estado = estado;
		this.convocadoEvento = convocado;
		this.archivoAdjunto = archivoAdjunto;
	}

}// fin clase
