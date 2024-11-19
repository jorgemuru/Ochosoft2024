package ochoschool.website.entidades;

import java.io.Serializable;
import java.util.Date;


import com.google.gson.JsonObject;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idUsuario;
	private int documento;
	private String primer_nombre;
	private String segundo_nombre;
	private String primer_apellido;
	private String segundo_apellido;
	private String usuario;
	private String clave;
	private Date fecha_nacimiento;
	private Genero genero;
	private Itr itr;
	private Departamento departamento;
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

	public String getDocumentoAString() {
		return String.valueOf(documento);
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

	public String getDepartamento() {

		if (this.departamento == null) {
			return "Departamento no asignado"; // O cualquier valor por defecto
		}
		return this.departamento.toString();
		// return departamento.toString();
	}
	
	public Departamento getDepartamentoAsDpto() {
		return this.departamento;

	}
	
	public Localidad getLocalidadAsLoc() {
		return this.localidad;

	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public String getLocalidad() {

		return this.localidad != null ? this.localidad.toString() : "N/A";
		// return localidad.toString();
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

	public String getItr() {
		return this.itr != null ? this.itr.getNombre() : "N/A";
		// return itr.getNombre();
	}
	
	public Itr getItrAsItr() {
		return this.itr;
	}

	public void setItr(Itr itr) {
		this.itr = itr;
	}

	public Usuario() {
	}

	public Usuario(int documento2, String primer_nombre2, String segundo_nombre2, String primer_apellido2,
			String segundo_apellido2, String nombre_usuario, String clave2, Date fecha_nacimiento2, Genero genero2,
			Departamento departamento2, Localidad localidad2, Itr itr2, String telefono2, String mail_institucional2,
			String mail_personal2, char activo2, char confirmado2, String tipo_usuario2) {
		// TODO Auto-generated constructor stub
	}

	public Usuario(Long idUsuario2, int documento2, String primer_nombre2, String segundo_nombre2,
			String primer_apellido2, String segundo_apellido2, String nombre_usuario, String clave2,
			Date fecha_nacimiento2, Genero genero2, Departamento departamento2, Localidad localidad2, Itr itr2,
			String telefono2, String mail_institucional2, String mail_personal2, char activo2, char confirmado2,
			String tipo_usuario2) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", documento=" + documento + ", primer_nombre=" + primer_nombre
				+ ", segundo_nombre=" + segundo_nombre + ", primer_apellido=" + primer_apellido + ", segundo_apellido="
				+ segundo_apellido + ", usuario=" + usuario + ", clave=" + clave + ", fecha_nacimiento="
				+ fecha_nacimiento + ", genero=" + genero + ", itr=" + itr + ", departamento=" + departamento
				+ ", localidad=" + localidad + ", telefono=" + telefono + ", mail_institucional=" + mail_institucional
				+ ", mail_personal=" + mail_personal + ", activo=" + activo + ", confirmado=" + confirmado
				+ ", tipo_usuario=" + tipo_usuario + "]";
	}
	
	 public String getGeneroNombre() {
	        return genero != null ? genero.getNombre() : null;
	    }
	 public static Usuario convertirJsonAUsuario(JsonObject usuarioJson) {
	        Usuario usuario = new Usuario();

	        // Supongamos que el JsonObject tiene campos como "idUsuario", "documento", etc.
	        // Debes adaptar esto según la estructura real de tu JSON.
	        usuario.setIdUsuario(usuarioJson.get("idUsuario").getAsLong());
	        usuario.setDocumento(usuarioJson.get("documento").getAsInt());
	        usuario.setPrimer_nombre(usuarioJson.get("primerNombre").getAsString());
	        usuario.setSegundo_nombre(usuarioJson.get("segundoNombre").getAsString());
	        // Asigna otros campos según sea necesario

	        return usuario;
	    }
	 
	// public Estudiante getEstudiante() {
	//        if (this instanceof Estudiante) {
	//            return (Estudiante) this;
	//        }
	//        return null; // o lanzar una excepción si es apropiado
	//    }

}// fin clase
