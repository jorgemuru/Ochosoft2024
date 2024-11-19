package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.TipoEventoDAO;
import ochoschool.apirest.entidades.TipoEvento;

@Stateless
public class TipoEventoEJB {
	@Inject
	private TipoEventoDAO tipoEventoDAO;

    public List<TipoEvento> obtenerTodosLosTiposEvento() {
        return tipoEventoDAO.listarTiposEvento();
    }

	public TipoEvento crearTipoEvento(TipoEvento tipoEvento) {
		return tipoEventoDAO.crearTipoEvento(tipoEvento);
	}

	public TipoEvento obtenerTipoEventoPorId(Long id) {
		return tipoEventoDAO.buscarTipoEventoId(id);
	}

	public TipoEvento actualizarTipoEvento(TipoEvento tipoEvento) {
		if(tipoEventoDAO.modificarTipoEvento(tipoEvento)) {
			return tipoEvento;
		}else{
			return null;
		}
	}

	public Boolean eliminarTipoEvento(Long id) {
		return tipoEventoDAO.eliminarTipoEvento(id);
	}
	
}//fin clase
