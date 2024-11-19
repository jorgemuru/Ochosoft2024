package ochoschool.apirest.entidades;

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

@Entity
@Table(name="LOCALIDADES")
public class Localidad implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_LOCALIDAD", initialValue = 500, sequenceName = "SEQ_ID_LOCALIDAD")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_LOCALIDAD")
	private Long idLocalidad;
	@ManyToOne
	@JoinColumn(name = "idDepartamento", nullable = false)
	private Departamento departamento;
	@Column(unique = true, nullable = false)
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

}//fin clase
