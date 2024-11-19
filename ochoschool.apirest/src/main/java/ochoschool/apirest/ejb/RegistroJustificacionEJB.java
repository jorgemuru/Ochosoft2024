package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.RegistroJustificacionDAO;
import ochoschool.apirest.entidades.RegistroJustificacion;

@Stateless
public class RegistroJustificacionEJB {
	@Inject
	private RegistroJustificacionDAO registroJustificacionDAO;

    public List<RegistroJustificacion> obtenerTodosLosRegistrosJustificacion() {
        return registroJustificacionDAO.listarRegistrosJustificacion();
    }

	public RegistroJustificacion crearRegistroJustificacion(RegistroJustificacion registroJustificacion) {
		return registroJustificacionDAO.crearRegistroJustificacion(registroJustificacion);
	}

	public RegistroJustificacion obtenerRegistroJustificacionPorId(Long id) {
		return registroJustificacionDAO.buscarRegistroJustificacionId(id);
	}

	public RegistroJustificacion actualizarRegistroJustificacion(RegistroJustificacion registroJustificacion) {
		if(registroJustificacionDAO.modificarRegistroJustificacion(registroJustificacion)) {
			return registroJustificacion;
		}else{
			return null;
		}
	}

	public Boolean eliminarRegistroJustificacion(Long id) {
		return registroJustificacionDAO.eliminarRegistroJustificacion(id);
	}
	
}//fin clase
