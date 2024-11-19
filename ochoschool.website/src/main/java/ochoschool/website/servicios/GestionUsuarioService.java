package ochoschool.website.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ochoschool.website.beans.UsuarioBean;
import ochoschool.website.entidades.Usuario;

@Stateless
@LocalBean
public class GestionUsuarioService implements Serializable{

	private static final long serialVersionUID = 1L;
	private String BASE_URL = "http://localhost:8080/ochoschool.apirest/";
	public String getBASE_URL() {
		return BASE_URL;
	}
	public void setBASE_URL(String url) {
		BASE_URL = url;
	}
	
	public GestionUsuarioService() {
		super();
	}
	
	/*
	 * public UsuarioP fromUsuario(Usuario u) {
	 * 
	 * UsuarioP usuario = new UsuarioP(u.getIdUsuario(), u.getNombre(),
	 * u.getApellido(), u.getCedula(), u.getMailPersonal(), u.getTelefono(),
	 * u.getMailInstitucional(), u.getContrasenia(), u.getNombreUsuario(),
	 * u.getTipo(), u.getStatus(), fromRolUsuario(u));
	 * 
	 * return usuario;
	 * 
	 * }
	 * 
	 * public RolUsuarioP fromRolUsuario(Usuario u) { return
	 * RolUsuarioP.getEnumByCode(u.getRolUsuario().getRol()); }
	 * 
	 * public Usuario toUsuario(UsuarioP u) {
	 * 
	 * Usuario usuario = new Usuario(u.getId(), u.getNombre(), u.getApellido(),
	 * u.getCedula(), u.getFechaNacimiento(), u.getEmailPersonal(), u.getTelefono(),
	 * u.getEmailInstitucional(), u.getContrasena(), u.getItr(), u.getLocalidad(),
	 * u.getUsuario(), u.getTipo(), u.isActivo(), toRolUsuario(u)); return usuario;
	 * }
	 * 
	 * public RolUsuario toRolUsuario(UsuarioP u) { return
	 * usuariosPersistenciaDAO.obtenerRolPorId(u.getRol().getLabel()); }
	 * 
	 * public List<UsuarioP> obtenerUsuarios(String nombre, String apellido, String
	 * nombreUsuario, String rol) throws ServicioException, PersistenciaException {
	 * RolUsuario rolUsuario = obtenerRolUsuario(rol); List<Usuario> listaUsuarios =
	 * usuariosPersistenciaDAO.listarUsuarios(nombre, apellido, nombreUsuario,
	 * rolUsuario!=null?rolUsuario.getIdRolUsuario():0L); List<UsuarioP>
	 * listaUsuariosP=new ArrayList<UsuarioP>(); for (Usuario usuario :
	 * listaUsuarios) { listaUsuariosP.add(fromUsuario(usuario)); } return
	 * listaUsuariosP; }
	 * 
	 * public RolUsuario obtenerRolUsuario(String rol) throws ServicioException {
	 * return usuariosPersistenciaDAO.obtenerRolPorId(rol); }
	 * 
	 * public UsuarioP buscarUsuarioo(Long id) { Usuario u =
	 * usuariosPersistenciaDAO.buscarUsuarioo(id); if(u==null) { return null; }
	 * return fromUsuario(u); }
	 * 
	 * public UsuarioP buscarUsuariooo(Long id) { Usuario u =
	 * usuariosPersistenciaDAO.buscarUsuariooo(id); if(u==null) { return null; }
	 * return fromUsuario(u); }
	 * 
	 * public UsuarioP buscarUsuario(String usuario) throws ServicioException {
	 * Usuario u = usuariosPersistenciaDAO.buscarUsuario(usuario); if(u==null)
	 * return null; return fromUsuario(u); } // BUSCAR POR CEDULA public UsuarioP
	 * buscarUsuarioPorCi(String cedula) throws ServicioException { Usuario u =
	 * usuariosPersistenciaDAO.buscarUsuarioPorCi(cedula); if(u==null) return null;
	 * return fromUsuario(u); } public UsuarioP autenticarUsuario(String usuario,
	 * String contraseña) throws ServicioException { Usuario u =
	 * usuariosPersistenciaDAO.autenticarUsuario(usuario, contraseña); if(u==null)
	 * return null; return fromUsuario(u); }
	 * 
	 * public UsuarioP agregarUsuario(UsuarioP usuario) { Usuario u =
	 * usuariosPersistenciaDAO.crearUsuario(toUsuario(usuario)); if(u==null) return
	 * null; return fromUsuario(u); }
	 * 
	 * public void modificarUsuario(UsuarioP usuario) {
	 * usuariosPersistenciaDAO.modificarUsuario(toUsuario(usuario)); }
	 * 
	 * public void desactivarUsuario(Long id) {
	 * usuariosPersistenciaDAO.eliminarUsuario(id); }
	 */
	
	
}//fin clase
