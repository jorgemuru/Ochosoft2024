package ochoschool.website.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_ROL", initialValue = 500, sequenceName = "SEQ_ID_ROL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_ROL")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idRol;
	@Column(unique = true, nullable = false)
	private String descripcion;

	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Rol(Long idRol, String descripcion) {
		this.idRol = idRol;
		this.descripcion = descripcion;
	}

	public Rol(String descripcion) {
		this.descripcion = descripcion;
	}

	public Rol() {
	}

}// fin clase
