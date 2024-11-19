package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.EstadoDAO;
import ochoschool.apirest.entidades.Estado;

@Stateless
public class EstadoEJb {
	@Inject
	private EstadoDAO estadoDAO;

    public List<Estado> obtenerTodosLosEstados() {
        return estadoDAO.listarEstados();
    }

	public Estado crearEstado(Estado estado) {
		return estadoDAO.crearEstado(estado);
	}

	public Estado obtenerEstadoPorId(Long id) {
		return estadoDAO.buscarEstadoId(id);
	}

	public Estado actualizarEstado(Estado estado) {
		if(estadoDAO.modificarEstado(estado)) {
			return estado;
		}else{
			return null;
		}
	}

	public Boolean eliminarEstado(Long id) {
		return estadoDAO.eliminarEstado(id);
	}

}//fin clase
