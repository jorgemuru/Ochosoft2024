package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.RolDAO;
import ochoschool.apirest.entidades.Rol;

@Stateless
public class RolEJB {
	@Inject
	private RolDAO rolDAO;

	public List<Rol> obtenerTodosLosRoles() {
		return rolDAO.listarRoles();
	}

	public Rol crearRol(Rol rol) {
		return rolDAO.crearRol(rol);
	}

	public Rol obtenerRolPorId(Long id) {
		return rolDAO.buscarRolId(id);
	}

	public Rol actualizarRol(Rol rol) {
		if (rolDAO.modificarRol(rol)) {
			return rol;
		} else {
			return null;
		}
	}

	public Boolean eliminarRol(Long id) {
		return rolDAO.eliminarRol(id);
	}

}// fin clase
