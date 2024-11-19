package ochoschool.apirest.entidades;

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
@Table(name="TOKENS")
public class Token implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_ID_TOKEN", initialValue = 500, sequenceName = "SEQ_ID_TOKEN")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_TOKEN")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idToken;
    @Column(unique = true, nullable = false)
    private String valor;
    @ManyToOne
    @JoinColumn(name = "idEvento", nullable = false)
    private Evento evento;
    @Column(nullable = false)
    private int validez;
    @Column(nullable = false)
    private String generadoPor;
    private Date fechaHora;
    
    //Getters y Setters
	public Long getIdToken() {
		return idToken;
	}
	public void setIdToken(Long idToken) {
		this.idToken = idToken;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public int getValidez() {
		return validez;
	}
	public void setValidez(int validez) {
		this.validez = validez;
	}
	public String getGeneradoPor() {
		return generadoPor;
	}
	public void setGeneradoPor(String generadoPor) {
		this.generadoPor = generadoPor;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	public Token(Long idToken, String valor, Evento evento, int validez, String generadoPor, Date fechaHora) {
		super();
		this.idToken = idToken;
		this.valor = valor;
		this.evento = evento;
		this.validez = validez;
		this.generadoPor = generadoPor;
		this.fechaHora = fechaHora;
	}
	
	public Token(String valor, Evento evento, int validez, String generadoPor, Date fechaHora) {
		super();
		this.valor = valor;
		this.evento = evento;
		this.validez = validez;
		this.generadoPor = generadoPor;
		this.fechaHora = fechaHora;
	}
	
	public Token() {
		super();
	}
    
    
    
}//fin clase
