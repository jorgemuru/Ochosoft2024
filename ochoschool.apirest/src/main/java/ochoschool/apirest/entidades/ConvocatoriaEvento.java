package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="CONVOCATORIA_EVENTO")
public class ConvocatoriaEvento implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_CONVOCATORIAEVENTO", initialValue = 500, sequenceName = "SEQ_ID_CONVOCATORIAEVENTO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_CONVOCATORIAEVENTO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idConvocatoriaEvento;
    @ManyToOne
    @JoinColumn(name = "idEvento", nullable = false)
    private Evento evento;
    @ManyToOne
    @JoinColumn(name = "idAnalista", nullable = false)
    private Analista analista;
    private char autogestion;
    
	public Long getIdConvocatoriaEvento() {
		return idConvocatoriaEvento;
	}
	public void setIdConvocatoriaEvento(Long idConvocatoriaEvento) {
		this.idConvocatoriaEvento = idConvocatoriaEvento;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Analista getAnalista() {
		return analista;
	}
	public void setAnalista(Analista analista) {
		this.analista = analista;
	}

	/*
	 * public List<ConvocadoEvento> getConvocados() { return convocados; } public
	 * void setConvocados(List<ConvocadoEvento> convocados) { this.convocados =
	 * convocados; }
	 */
	public char getAutogestion() {
		return autogestion;
	}
	public void setAutogestion(char autogestion) {
		this.autogestion = autogestion;
	}
	public ConvocatoriaEvento(Long idConvocatoriaEvento, Evento evento, Analista analista, char autogestion) {
		//List<ConvocadoEvento> convocados
		super();
		this.idConvocatoriaEvento = idConvocatoriaEvento;
		this.evento = evento;
		this.analista = analista;
		//this.convocados = convocados;
		this.autogestion = autogestion;
	}
	public ConvocatoriaEvento(Evento evento, Analista analista, char autogestion) {
		//List<ConvocadoEvento> convocados
		super();
		this.evento = evento;
		this.analista = analista;
		//this.convocados = convocados;
		this.autogestion = autogestion;
	}
	public ConvocatoriaEvento() {
	}
    
    
}//fin clase
