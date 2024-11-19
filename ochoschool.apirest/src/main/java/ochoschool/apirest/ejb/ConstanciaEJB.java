package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.ConstanciaDAO;
import ochoschool.apirest.entidades.Constancia;

@Stateless
public class ConstanciaEJB {
	@Inject
	private ConstanciaDAO constanciaDAO;

    public List<Constancia> obtenerTodasLasConstancias() {
        return constanciaDAO.listarConstancias();
    }

	public Constancia crearConstancia(Constancia constancia) {
		return constanciaDAO.crearConstancia(constancia);
	}

	public Constancia obtenerConstanciaPorId(Long id) {
		return constanciaDAO.buscarConstanciaId(id);
	}

	public Constancia actualizarConstancia(Constancia constancia) {
		if(constanciaDAO.modificarConstancia(constancia)) {
			return constancia;
		}else{
			return null;
		}
	}

	public Boolean eliminarConstancia(Long id) {
		return constanciaDAO.eliminarConstancia(id);
	}
	
}//fin clase
