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
@Table(name="DEPARTAMENTOS")
public class Departamento implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_DEPARTAMENTO", initialValue = 500, sequenceName = "SEQ_ID_DEPARTAMENTO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_DEPARTAMENTO")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idDepartamento;
	@Column(unique = true, nullable = false)
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

}//fin clase
