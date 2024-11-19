package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.RegistroConstanciaDAO;
import ochoschool.apirest.entidades.RegistroConstancia;

@Stateless
public class RegistroConstanciaEJB {
	@Inject
	private RegistroConstanciaDAO registroConstanciaDAO;

    public List<RegistroConstancia> obtenerTodosLosRegistrosConstancia() {
        return registroConstanciaDAO.listarRegistrosConstancia();
    }

	public RegistroConstancia crearRegistroConstancia(RegistroConstancia registroConstancia) {
		return registroConstanciaDAO.crearRegistroConstancia(registroConstancia);
	}

	public RegistroConstancia obtenerRegistroConstanciaPorId(Long id) {
		return registroConstanciaDAO.buscarRegistroConstanciaId(id);
	}

	public RegistroConstancia actualizarRegistroConstancia(RegistroConstancia registroConstancia) {
		if(registroConstanciaDAO.modificarRegistroConstancia(registroConstancia)) {
			return registroConstancia;
		}else{
			return null;
		}
	}

	public Boolean eliminarRegistroConstancia(Long id) {
		return registroConstanciaDAO.eliminarRegistroConstancia(id);
	}
	
}//fin clase
