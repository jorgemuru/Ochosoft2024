package ochoschool.website.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

public class Localidad implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idLocalidad;
	private Departamento departamento;
	private String nombre;

	public Long getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Long idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Localidad(Long idLocalidad, Departamento departamento, String nombre) {
		this.idLocalidad = idLocalidad;
		this.departamento = departamento;
		this.nombre = nombre;
	}

	public Localidad(Departamento departamento, String nombre) {
		this.departamento = departamento;
		this.nombre = nombre;
	}

	public Localidad() {
	}

	// Constructor adicional para manejar el caso de 'N/A'
	public Localidad(String nombre) {
		this.nombre = nombre;
		// Puedes inicializar otros campos si es necesario
	}

	public Localidad(Long idLocalidad, String nombre) {
		this.idLocalidad = idLocalidad;
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return this.nombre;
		// return (this != null) ? nombre : "Localidad Desconocido";
	}

}// fin clase
