package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="RECLAMOS")
public class Reclamo implements Serializable{

	 private static final long serialVersionUID = 1L;

	    @Id
	    @SequenceGenerator(name = "SEQ_ID_RECLAMO", initialValue = 500, sequenceName = "SEQ_ID_RECLAMO")
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_RECLAMO")
	    @Column(unique = true, nullable = false, precision = 38)
	    private Long idReclamo;
	    private String titulo;
	    private String descripcion;
	    //@Temporal(TemporalType.DATE)
	    private Date fechaHora;
	    @ManyToOne
	    @JoinColumn(name = "idEvento", nullable = true)
	    private Evento evento;
	    @ManyToOne
	    @JoinColumn(name = "idEstudiante", nullable = false)
	    private Estudiante estudiante;
	    @ManyToOne
	    @JoinColumn(name = "idEstado", nullable = false)
	    private Estado estado;
	    @ManyToOne
	    @JoinColumn(name = "idTipoReclamo", nullable = false)
	    private TipoReclamo tipoReclamo;
	    
	    @Column(name = "NOMBRE", nullable = true)
	    private String nombre;
	    @Column(name = "SEMESTRE", nullable = true)
	    private Integer semestre;
	    @Column(name = "FECHA_REALIZACION", nullable = true)
	    private Date fechaRealizacion;
	    @ManyToOne
		@JoinColumn(name = "idTutor", nullable = true)
	    private Tutor tutor;    
	    @Column(name = "CREDITOS", nullable = true)
	    private Integer creditos;
	    

	    public Long getIdReclamo() {
	        return idReclamo;
	    }

	    public void setIdReclamo(Long idReclamo) {
	        this.idReclamo = idReclamo;
	    }

	    public String getTitulo() {
	        return titulo;
	    }

	    public void setTitulo(String titulo) {
	        this.titulo = titulo;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
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
	    
	    public TipoReclamo getTipoReclamo() {
			return tipoReclamo;
		}

		public void setTipoReclamo(TipoReclamo tipoReclamo) {
			this.tipoReclamo = tipoReclamo;
		}
		
		

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public Integer getSemestre() {
			return semestre;
		}

		public void setSemestre(Integer semestre) {
			this.semestre = semestre;
		}

		public Date getFechaRealizacion() {
			return fechaRealizacion;
		}

		public void setFechaRealizacion(Date fechaRealizacion) {
			this.fechaRealizacion = fechaRealizacion;
		}

		public Tutor getTutor() {
			return tutor;
		}

		public void setTutor(Tutor tutor) {
			this.tutor = tutor;
		}

		public Integer getCreditos() {
			return creditos;
		}

		public void setCreditos(Integer creditos) {
			this.creditos = creditos;
		}

		public Reclamo(Long idReclamo, String titulo, String descripcion, Date fechaHora, Evento evento,
	                   Estudiante estudiante, Estado estado, TipoReclamo tipo) {
	        this.idReclamo = idReclamo;
	        this.titulo = titulo;
	        this.descripcion = descripcion;
	        this.fechaHora = fechaHora;
	        this.evento = evento;
	        this.estudiante = estudiante;
	        this.estado = estado;
	        this.tipoReclamo = tipo;
	    }

	    public Reclamo(String titulo, String descripcion, Date fechaHora, Evento evento, Estudiante estudiante,
	                   Estado estado, TipoReclamo tipo) {
	        this.titulo = titulo;
	        this.descripcion = descripcion;
	        this.fechaHora = fechaHora;
	        this.evento = evento;
	        this.estudiante = estudiante;
	        this.estado = estado;
	        this.tipoReclamo = tipo;
	    }

	    public Reclamo() {
	    }

		public Reclamo(Long idReclamo, String titulo, String descripcion, Date fechaHora, Evento evento,
				Estudiante estudiante, Estado estado, TipoReclamo tipoReclamo, String nombre, Integer semestre,
				Date fechaRealizacion, Tutor tutor, Integer creditos) {
			super();
			this.idReclamo = idReclamo;
			this.titulo = titulo;
			this.descripcion = descripcion;
			this.fechaHora = fechaHora;
			this.evento = evento;
			this.estudiante = estudiante;
			this.estado = estado;
			this.tipoReclamo = tipoReclamo;
			this.nombre = nombre;
			this.semestre = semestre;
			this.fechaRealizacion = fechaRealizacion;
			this.tutor = tutor;
			this.creditos = creditos;
		}
	    
	    
	    
	    
	    
}//fin clase
