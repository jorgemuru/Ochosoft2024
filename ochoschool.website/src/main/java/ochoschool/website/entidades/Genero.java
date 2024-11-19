package ochoschool.website.entidades;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

public class Genero implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idGenero;
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

	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idGenero, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genero other = (Genero) obj;
		return Objects.equals(idGenero, other.idGenero) && Objects.equals(nombre, other.nombre);
	}
	
	
	

}//fin clase
