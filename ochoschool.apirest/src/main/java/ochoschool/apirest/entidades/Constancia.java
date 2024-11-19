package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CONSTANCIAS")
public class Constancia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_CONSTANCIA", initialValue = 500, sequenceName = "SEQ_ID_CONSTANCIA")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_CONSTANCIA")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idConstancia;
	@Column(nullable = false, length = 50)
	//@Temporal(TemporalType.DATE)
	private Date fechaHora;
	@ManyToOne
	@JoinColumn(name = "idEvento", nullable = false)
	private Evento evento;
	@ManyToOne
	@JoinColumn(name = "idEstudiante", nullable = false)
	private Estudiante estudiante;
	@ManyToOne
	@JoinColumn(name = "idEstado", nullable = false)
	private Estado estado;
	@ManyToOne
	@JoinColumn(name = "idTipo", nullable = false)
	private Tipo tipo;
	
	public Long getIdConstancia() {
		return idConstancia;
	}
	public void setIdConstancia(Long idConstancia) {
		this.idConstancia = idConstancia;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Estudiante getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Constancia() {
		
	}
	public Constancia(Long idConstancia, Date fechaHora, Evento evento, Estudiante estudiante,
			Estado estado, Tipo tipo) {
		super();
		this.idConstancia = idConstancia;
		this.fechaHora = fechaHora;
		this.evento = evento;
		this.estudiante = estudiante;
		this.estado = estado;
		this.tipo = tipo;
	}
	
	public Constancia(Date fechaHora, Evento evento, Estudiante estudiante,
			Estado estado, Tipo tipo) {
		super();
		this.fechaHora = fechaHora;
		this.evento = evento;
		this.estudiante = estudiante;
		this.estado = estado;
		this.tipo = tipo;
	}
	
	
	
}// fin clase
