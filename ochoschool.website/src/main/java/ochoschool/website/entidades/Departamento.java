package ochoschool.website.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

public class Departamento implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idDepartamento;
	private String nombre;

	public Long getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(Long idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Departamento(Long idDepartamento, String nombre) {
		this.idDepartamento = idDepartamento;
		this.nombre = nombre;
	}

	public Departamento(String nombre) {
		this.nombre = nombre;
	}

	public Departamento() {
	}

	@Override
	public String toString() {
		return this.nombre;
		//return (this != null) ? nombre : "Departamento Desconocido";
	}

	
}//fin clase
