package ochoschool.apirest.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ESTUDIANTES")
public class Estudiante extends Usuario{
	private static final long serialVersionUID = 1L;
	private String generacion;
    private int semestre;
	private Long idUsuario;

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Estudiante(String generacion, int semestre) {
        this.generacion = generacion;
        this.semestre = semestre;
    }

    public Estudiante(Long idUsuario, String generacion) {
    	this.idUsuario = idUsuario;
        this.generacion = generacion;
    }
    public Estudiante(Long idUsuario, int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
                      String segundo_apellido, String nombre_usuario, String clave, Date fecha_nacimiento, Genero genero,
                      Departamento departamento, Localidad localidad, Itr itr,  String telefono, String mail_institucional,
                      String mail_personal, char activo, char confirmado, String tipo_usuario, String generacion, int semestre) {
        super(idUsuario, documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nombre_usuario,
                clave, fecha_nacimiento, genero, departamento, localidad, itr, telefono, mail_institucional, mail_personal,
                activo, confirmado, tipo_usuario);
        this.generacion = generacion;
        this.semestre = semestre;
    }

    public Estudiante(int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
                      String segundo_apellido, String nombre_usuario, String clave, Date fecha_nacimiento, Genero genero,
                      Departamento departamento, Localidad localidad, Itr itr, String telefono, String mail_institucional,
                      String mail_personal, char activo, char confirmado, String tipo_usuario, String generacion, int semestre) {
        super(documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nombre_usuario, clave,
                fecha_nacimiento, genero, departamento, localidad, itr, telefono, mail_institucional, mail_personal, activo,
                confirmado, tipo_usuario);
        this.generacion = generacion;
        this.semestre = semestre;
    }

    public Estudiante() {
        super();
    }

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

}//fin clase
