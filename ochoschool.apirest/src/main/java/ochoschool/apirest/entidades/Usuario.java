package ochoschool.apirest.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "USUARIOS")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_ID_USUARIO", initialValue = 500, sequenceName = "SEQ_ID_USUARIO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_USUARIO")
	@Column(unique = true, nullable = false, precision = 38)
	private Long idUsuario;
	@Column(unique = true, nullable = false)
	private int documento;
	@Column(nullable = false)
	private String primer_nombre;
	private String segundo_nombre;
	@Column(nullable = false)
	private String primer_apellido;
	private String segundo_apellido;
	@Column(unique = true, nullable = false)
	private String usuario;
	private String clave;
	//@Temporal(TemporalType.DATE)
	private Date fecha_nacimiento;

	@ManyToOne
	@JoinColumn(name = "idGenero")
	private Genero genero;
	@ManyToOne
	@JoinColumn(name = "idItr")
	private Itr itr;	
	@ManyToOne
	@JoinColumn(name = "idDepartamento")
	private Departamento departamento;
	@ManyToOne
	@JoinColumn(name = "idLocalidad")
	private Localidad localidad;

	private String telefono;
	private String mail_institucional;
	private String mail_personal;

	private char activo;
	private char confirmado;
	private String tipo_usuario;

//Getters and Setters
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getDocumento() {
		return documento;
	}

	public void setDocumento(int documento) {
		this.documento = documento;
	}

	public String getPrimer_nombre() {
		return primer_nombre;
	}

	public void setPrimer_nombre(String primer_nombre) {
		this.primer_nombre = primer_nombre;
	}

	public String getSegundo_nombre() {
		return segundo_nombre;
	}

	public void setSegundo_nombre(String segundo_nombre) {
		this.segundo_nombre = segundo_nombre;
	}

	public String getPrimer_apellido() {
		return primer_apellido;
	}

	public void setPrimer_apellido(String primer_apellido) {
		this.primer_apellido = primer_apellido;
	}

	public String getSegundo_apellido() {
		return segundo_apellido;
	}

	public void setSegundo_apellido(String segundo_apellido) {
		this.segundo_apellido = segundo_apellido;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMail_institucional() {
		return mail_institucional;
	}

	public void setMail_institucional(String mail_institucional) {
		this.mail_institucional = mail_institucional;
	}

	public String getMail_personal() {
		return mail_personal;
	}

	public void setMail_personal(String mail_personal) {
		this.mail_personal = mail_personal;
	}

	public char getActivo() {
		return activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
	}

	public char getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(char confirmado) {
		this.confirmado = confirmado;
	}

	public String getTipo_usuario() {
		return tipo_usuario;
	}

	public void setTipo_usuario(String tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}
	
	public Itr getItr() {
		return itr;
	}

	public void setItr(Itr itr) {
		this.itr = itr;
	}

	public Usuario() {
	}

	public Usuario(Long idUsuario, int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
			String segundo_apellido, String usuario, String clave, Date fecha_nacimiento, Genero genero,
			Departamento departamento, Localidad localidad, Itr itr, String telefono, String mail_institucional,
			String mail_personal, char activo, char confirmado, String tipo_usuario) {
		this.idUsuario = idUsuario;
		this.documento = documento;
		this.primer_nombre = primer_nombre;
		this.segundo_nombre = segundo_nombre;
		this.primer_apellido = primer_apellido;
		this.segundo_apellido = segundo_apellido;
		this.usuario = usuario;
		this.clave = clave;
		this.fecha_nacimiento = fecha_nacimiento;
		this.genero = genero;
		this.departamento = departamento;
		this.localidad = localidad;
		this.itr = itr;
		this.telefono = telefono;
		this.mail_institucional = mail_institucional;
		this.mail_personal = mail_personal;
		this.activo = activo;
		this.confirmado = confirmado;
		this.tipo_usuario = tipo_usuario;
	}

	public Usuario(int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
			String segundo_apellido, String usuario, String clave, Date fecha_nacimiento, Genero genero,
			Departamento departamento, Localidad localidad, Itr itr, String telefono, String mail_institucional,
			String mail_personal, char activo, char confirmado, String tipo_usuario) {
		this.documento = documento;
		this.primer_nombre = primer_nombre;
		this.segundo_nombre = segundo_nombre;
		this.primer_apellido = primer_apellido;
		this.segundo_apellido = segundo_apellido;
		this.usuario = usuario;
		this.clave = clave;
		this.fecha_nacimiento = fecha_nacimiento;
		this.genero = genero;
		this.departamento = departamento;
		this.localidad = localidad;
		this.itr = itr;
		this.telefono = telefono;
		this.mail_institucional = mail_institucional;
		this.mail_personal = mail_personal;
		this.activo = activo;
		this.confirmado = confirmado;
		this.tipo_usuario = tipo_usuario;
	}

}//fin clase
