package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="EVENTOS")
public class Evento implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_EVENTO", initialValue = 500, sequenceName = "SEQ_ID_EVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_EVENTO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idEvento;
    @Column(nullable = false, length = 50)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "idEstadoEvento", nullable = false)
    private EstadoEvento estadoEvento;
    @ManyToOne
    @JoinColumn(name = "idTipoEvento", nullable = false)
    private TipoEvento tipoEvento;
    @Column(nullable = false)
    private Date fechaHoraInicio;
    @Column(nullable = false)
    private Date fechaHoraFin;
    @ManyToOne
    @JoinColumn(name = "idModalidad", nullable = false)
    private Modalidad modalidad;
    @ManyToOne
    @JoinColumn(name = "idItr", nullable = false)
    private Itr itr;
    private String locacion; //Dentro (Salon, Lab, etc.) o fuera de un ITR (Sala de ..., Centro de ..., etc).
    private char activo;
    private char habilitado;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "EVENTO_TUTOR",
            joinColumns = @JoinColumn(name = "idevento"),
            inverseJoinColumns = @JoinColumn(name = "idtutor"))
    private List<Tutor> tutores;
    @Column(nullable = true)
    private Integer idEstudiante;

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public EstadoEvento getEstadoEvento() {
        return estadoEvento;
    }

    public void setEstadoEvento(EstadoEvento estado) {
        this.estadoEvento = estado;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public Itr getItr() {
        return itr;
    }

    public void setItr(Itr itr) {
        this.itr = itr;
    }

    public String getLocacion() {
        return locacion;
    }

    public void setLocacion(String locacion) {
        this.locacion = locacion;
    }

    public char getActivo() {
		return activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
	}

	public List<Tutor> getTutores() {
        return tutores;
    }

    public void setTutores(List<Tutor> tutores) {
        this.tutores = tutores;
    }

    public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	
	public char getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(char habilitado) {
		this.habilitado = habilitado;
	}

	public Evento(Long idEvento, String titulo, TipoEvento tipo, EstadoEvento estado, Date fechaHoraInicio, Date fechaHoraFin,
                  Modalidad modalidad, Itr itr, String locacion, char activo, char habilitado, List<Tutor> tutores) {
        this.idEvento = idEvento;
        this.titulo = titulo;
        this.tipoEvento = tipo;
        this.estadoEvento = estado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.modalidad = modalidad;
        this.itr = itr;
        this.locacion = locacion;
        this.activo = activo;
        this.habilitado = habilitado;
        this.tutores = tutores;
    }

    public Evento(String titulo, TipoEvento tipo, EstadoEvento estado, Date fechaHoraInicio, Date fechaHoraFin, Modalidad modalidad, Itr itr,
                  String locacion, char activo, char habilitado, List<Tutor> tutores) {
        this.titulo = titulo;
        this.tipoEvento = tipo;
        this.estadoEvento = estado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.modalidad = modalidad;
        this.itr = itr;
        this.locacion = locacion;
        this.activo = activo;
        this.habilitado = habilitado;
        this.tutores = tutores;
    }

    public Evento() {
    }
  
    
}//fin clase
