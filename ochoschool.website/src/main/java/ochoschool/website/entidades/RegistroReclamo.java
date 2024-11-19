package ochoschool.website.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="REGISTRO_RECLAMOS")
public class RegistroReclamo implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_REGISTRORECLAMO", initialValue = 500, sequenceName = "SEQ_ID_REGISTRORECLAMO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_REGISTRORECLAMO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idRegistroReclamo;
    @ManyToOne
    @JoinColumn(name = "idReclamo", nullable = false)
    private Reclamo reclamo;
    @ManyToOne
    @JoinColumn(name = "idAnalista", nullable = false)
    private Analista analista;
    //@Temporal(TemporalType.DATE)
    private Date fechaHora;
    private String detalle;

    public Long getIdRegistroReclamo() {
        return idRegistroReclamo;
    }

    public void setIdRegistroReclamo(Long idRegistroReclamo) {
        this.idRegistroReclamo = idRegistroReclamo;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
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

    public RegistroReclamo(Long idRegistroReclamo, Reclamo reclamo, Analista analista, Date fechaHora, String detalle) {
        this.idRegistroReclamo = idRegistroReclamo;
        this.reclamo = reclamo;
        this.analista = analista;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
    }

    public RegistroReclamo(Reclamo reclamo, Analista analista, Date fechaHora, String detalle) {
        this.reclamo = reclamo;
        this.analista = analista;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
    }

    public RegistroReclamo() {
    }
    
}//fin clase
