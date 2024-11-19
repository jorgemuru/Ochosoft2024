package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.RegistroReclamoDAO;
import ochoschool.apirest.entidades.RegistroReclamo;

@Stateless
public class RegistroReclamoEJB {
	@Inject
	private RegistroReclamoDAO registroReclamoDAO;

    public List<RegistroReclamo> obtenerTodosLosRegistroReclamos() {
        return registroReclamoDAO.listarRegistroReclamos();
    }

	public RegistroReclamo crearRegistroReclamo(RegistroReclamo registroReclamo) {
		return registroReclamoDAO.crearRegistroReclamo(registroReclamo);
	}

	public RegistroReclamo obtenerRegistroReclamoPorId(Long id) {
		return registroReclamoDAO.buscarRegistroReclamoId(id);
	}

	public RegistroReclamo actualizarRegistroReclamo(RegistroReclamo registroReclamo) {
		if(registroReclamoDAO.modificarRegistroReclamo(registroReclamo)) {
			return registroReclamo;
		}else{
			return null;
		}
	}

	public Boolean eliminarRegistroReclamo(Long id) {
		return registroReclamoDAO.eliminarRegistroReclamo(id);
	}
}//fin clase
