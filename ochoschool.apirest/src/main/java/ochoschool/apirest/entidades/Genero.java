package ochoschool.apirest.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GENEROS")
public class Genero implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "SEQ_ID_GENERO", initialValue = 500, sequenceName = "SEQ_ID_GENERO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_GENERO")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idGenero;
	@Column(unique = true, nullable = false)
	private String nombre;

	// Getters and setters
	public Long getIdGenero() {
		return idGenero;
	}

	public void setIdGenero(Long idGenero) {
		this.idGenero = idGenero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Genero(Long idGenero, String nombre) {
		this.idGenero = idGenero;
		this.nombre = nombre;
	}

	public Genero(String nombre) {
		this.nombre = nombre;
	}

	public Genero() {
	}

}//fin clase
