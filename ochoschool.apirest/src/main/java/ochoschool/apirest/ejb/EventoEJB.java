package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.EventoDAO;
import ochoschool.apirest.entidades.Evento;

@Stateless
public class EventoEJB {
	@Inject
	private EventoDAO eventoDAO;

    public List<Evento> obtenerTodosLosEventos() {
        return eventoDAO.listarEventos();
    }

	public Evento crearEvento(Evento evento) {
		return eventoDAO.crearEvento(evento);
	}

	public Evento obtenerEventoPorId(Long id) {
		return eventoDAO.buscarEventoId(id);
	}

	public Evento actualizarEvento(Evento evento) {
		if(eventoDAO.modificarEvento(evento)) {
			return evento;
		}else{
			return null;
		}
	}

	public Boolean eliminarEvento(Long id) {
		return eventoDAO.eliminarEvento(id);
	}
	
}//fin clase
