package ochoschool.apirest.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.AnalistaDAO;
import ochoschool.apirest.entidades.Analista;

@Stateless
public class AnalistaEJB {
	@Inject
	private AnalistaDAO analistaDAO;

    public List<Analista> obtenerTodosLosAnalistas() {
        return analistaDAO.listarAnalistas(null, null, null, null);
    }

	public Analista crearAnalista(Analista analista) {
		return analistaDAO.crearAnalista(analista);
	}

	public Analista obtenerAnalistaPorId(Long id) {
		return analistaDAO.buscarAnalistaId(id);
	}

	public Analista actualizarAnalista(Analista analista) {
		if(analistaDAO.modificarAnalista(analista)) {
			return analista;
		}else{
			return null;
		}
	}

	public Boolean eliminarAnalista(Long id) {
		return analistaDAO.eliminarAnalista(id);
	}

	public Boolean deshabilitarAnalista(Long id) {
		return analistaDAO.deshabilitarAnalista(id);
	}
	
}//fin clase
