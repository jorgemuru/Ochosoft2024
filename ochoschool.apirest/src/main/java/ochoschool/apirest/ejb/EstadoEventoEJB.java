package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.EstadoEventoDAO;
import ochoschool.apirest.entidades.EstadoEvento;

@Stateless
public class EstadoEventoEJB {
	@Inject
	private EstadoEventoDAO estadoEventoDAO;

    public List<EstadoEvento> obtenerTodosLosEstadosEvento() {
        return estadoEventoDAO.listarEstadosEvento();
    }

	public EstadoEvento crearEstadoEvento(EstadoEvento estadoEvento) {
		return estadoEventoDAO.crearEstadoEvento(estadoEvento);
	}

	public EstadoEvento obtenerEstadoEventoPorId(Long id) {
		return estadoEventoDAO.buscarEstadoEventoId(id);
	}

	public EstadoEvento actualizarEstadoEvento(EstadoEvento estadoEvento) {
		if(estadoEventoDAO.modificarEstadoEvento(estadoEvento)) {
			return estadoEvento;
		}else{
			return null;
		}
	}

	public Boolean eliminarEstadoEvento(Long id) {
		return estadoEventoDAO.eliminarEstadoEvento(id);
	}
	
}//fin clase
