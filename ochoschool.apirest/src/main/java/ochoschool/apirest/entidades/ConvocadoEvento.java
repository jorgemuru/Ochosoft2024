package ochoschool.apirest.entidades;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="CONVOCADO_EVENTO")
public class ConvocadoEvento implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_CONVOCADOEVENTO", initialValue = 500, sequenceName = "SEQ_ID_CONVOCADOEVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_CONVOCADOEVENTO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idConvocadoEvento;
    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name = "idconvocatoriaevento", nullable = false)
    private ConvocatoriaEvento convocatoriaEvento;
    private String estadoAsistencia;
    private Double nota;
    private String registraAsistencia;
    private Date fechaHoraRegAsis;
    private String token;
    
	public Long getIdConvocadoEvento() {
		return idConvocadoEvento;
	}
	public void setIdConvocadoEvento(Long idConvocadoEvento) {
		this.idConvocadoEvento = idConvocadoEvento;
	}
	public Estudiante getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	public String getEstadoAsistencia() {
		return estadoAsistencia;
	}
	
	public ConvocatoriaEvento getConvocatoriaEvento() {
		return convocatoriaEvento;
	}
	public void setConvocatoriaEvento(ConvocatoriaEvento convocatoriaEvento) {
		this.convocatoriaEvento = convocatoriaEvento;
	}
	public void setEstadoAsistencia(String estadoAsistencia) {
		this.estadoAsistencia = estadoAsistencia;
	}
	public Double getNota() {
		return nota;
	}
	public void setNota(Double nota) {
		this.nota = nota;
	}
	public String getRegistraAsistencia() {
		return registraAsistencia;
	}
	public void setRegistraAsistencia(String registraAsistencia) {
		this.registraAsistencia = registraAsistencia;
	}
	public Date getFechaHoraRegAsis() {
		return fechaHoraRegAsis;
	}
	public void setFechaHoraRegAsis(Date fechaHoraRegAsis) {
		this.fechaHoraRegAsis = fechaHoraRegAsis;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public ConvocadoEvento(Long idConvocadoEvento, Estudiante estudiante, ConvocatoriaEvento convocatoria, String estadoAsistencia, Double nota,
			String registraAsistencia, Date fechaHoraRegAsis, String token) {
		super();
		this.idConvocadoEvento = idConvocadoEvento;
		this.estudiante = estudiante;
		this.convocatoriaEvento = convocatoria;
		this.estadoAsistencia = estadoAsistencia;
		this.nota = nota;
		this.registraAsistencia = registraAsistencia;
		this.fechaHoraRegAsis = fechaHoraRegAsis;
		this.token = token;
	}
	
	public ConvocadoEvento(Estudiante estudiante, ConvocatoriaEvento convocatoria, String estadoAsistencia, Double nota, String registraAsistencia, Date fechaHoraRegAsis, String token) {
		super();
		this.estudiante = estudiante;
		this.convocatoriaEvento = convocatoria;
		this.estadoAsistencia = estadoAsistencia;
		this.nota = nota;
		this.registraAsistencia = registraAsistencia;
		this.fechaHoraRegAsis = fechaHoraRegAsis;
		this.token = token;
	}
	
	public ConvocadoEvento() {
	}

	
}//fin clase
