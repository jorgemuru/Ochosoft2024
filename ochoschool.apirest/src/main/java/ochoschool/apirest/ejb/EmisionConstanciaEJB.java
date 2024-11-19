package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.EmisionConstanciaDAO;
import ochoschool.apirest.entidades.EmisionConstancia;

@Stateless
public class EmisionConstanciaEJB {
	@Inject
	private EmisionConstanciaDAO emisionConstanciaDAO;

    public List<EmisionConstancia> obtenerTodasLasEmisionesConstancia() {
        return emisionConstanciaDAO.listarEmisionesConstancia();
    }

	public EmisionConstancia crearEmisionConstancia(EmisionConstancia emisionConstancia) {
		return emisionConstanciaDAO.crearEmisionConstancia(emisionConstancia);
	}

	public EmisionConstancia obtenerEmisionConstanciaPorId(Long id) {
		return emisionConstanciaDAO.buscarEmisionConstanciaId(id);
	}

	public EmisionConstancia actualizarEmisionConstancia(EmisionConstancia emisionConstancia) {
		if(emisionConstanciaDAO.modificarEmisionConstancia(emisionConstancia)) {
			return emisionConstancia;
		}else{
			return null;
		}
	}

	public Boolean eliminarEmisionConstancia(Long id) {
		return emisionConstanciaDAO.eliminarEmisionConstancia(id);
	}
	
}//fin clase
