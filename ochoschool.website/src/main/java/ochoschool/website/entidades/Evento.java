package ochoschool.website.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="EVENTOS")
public class Evento implements Serializable{

	private static final long serialVersionUID = 1L;

    
    private Long idEvento;
    private String titulo;
    private EstadoEvento estadoEvento;
    private TipoEvento tipoEvento;
    private Long  fechaHoraInicio;
    private Long  fechaHoraFin;
    private Modalidad modalidad;
    private Itr itr;
    private String locacion; //Dentro (Salon, Lab, etc.) o fuera de un ITR (Sala de ..., Centro de ..., etc).
    private char activo;
    private char habilitado;
    private List<Tutor> tutores;

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
    	System.out.println("titulo seteado: " + titulo);
        this.titulo = titulo;
    }

    public EstadoEvento getEstadoEvento() {
        return estadoEvento;
    }

    public void setEstadoEvento(EstadoEvento estado) {
        this.estadoEvento = estado;
    }

    public Long  getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Long  fechaHoraInicio) {
    	System.out.println("fecha inicio seteada " + fechaHoraInicio );
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Long  getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Long  fechaHoraFin) {
    	System.out.println("fecha fin seteada " + fechaHoraFin );
        this.fechaHoraFin = fechaHoraFin;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        System.out.println("Estableciendo modalidad: " + modalidad);
        this.modalidad = modalidad;
    }
    public Itr getItr() {
        return itr;
    }

    public void setItr(Itr itr) {
    	 System.out.println("Estableciendo itr: " + itr);
        this.itr = itr;
    }

    public String getLocacion() {
    	
        return locacion;
    }

    public void setLocacion(String locacion) {
    	 System.out.println("Estableciendo locacion: " + locacion);
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
    	 System.out.println("Estableciendo tutor: " + tutores);
        this.tutores = tutores;
    }

    public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		 System.out.println("Estableciendo tipoEvento: " + tipoEvento.getDescripcion());
		this.tipoEvento = tipoEvento;
	}

	
	public char getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(char habilitado) {
		this.habilitado = habilitado;
	}

	public Evento(Long idEvento, String titulo, TipoEvento tipo, EstadoEvento estado, Long  fechaHoraInicio, Long  fechaHoraFin,
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

    public Evento(String titulo, TipoEvento tipo, EstadoEvento estado, Long  fechaHoraInicio, Long  fechaHoraFin, Modalidad modalidad, Itr itr,
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
