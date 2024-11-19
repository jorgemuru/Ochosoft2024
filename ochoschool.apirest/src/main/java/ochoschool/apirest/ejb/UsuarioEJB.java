package ochoschool.apirest.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ochoschool.apirest.dao.UsuarioDAO;
import ochoschool.apirest.entidades.Usuario;

@Stateless
public class UsuarioEJB {

	@Inject
	private UsuarioDAO usuarioDAO;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDAO.listarUsuarios(null, null, null, null);
    }

	public Usuario crearUsuario(Usuario usuario) {
		return usuarioDAO.crearUsuario(usuario);
	}

	public Usuario obtenerUsuarioPorId(Long id) {
		return usuarioDAO.buscarUsuarioId(id);
	}

	public Usuario actualizarUsuario(Usuario usuario) {
		if(usuarioDAO.modificarUsuario(usuario)) {
			return usuario;
		}else{
			return null;
		}
	}

	public Boolean eliminarUsuario(Long id) {
		return usuarioDAO.eliminarUsuario(id);
	}
	
	public Boolean deshabilitarUsuario(Long id) {
		return usuarioDAO.deshabilitarUsuario(id);
	}

}//fin clase
