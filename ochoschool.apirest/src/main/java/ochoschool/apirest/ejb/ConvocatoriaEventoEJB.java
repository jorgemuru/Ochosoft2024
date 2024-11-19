package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.ConvocatoriaEventoDAO;
import ochoschool.apirest.entidades.ConvocatoriaEvento;

@Stateless
public class ConvocatoriaEventoEJB {
	@Inject
	private ConvocatoriaEventoDAO convocatoriaEventoDAO;

    public List<ConvocatoriaEvento> obtenerTodasLasConvocatorias() {
        return convocatoriaEventoDAO.listarConvocatoriasEvento();
    }

	public ConvocatoriaEvento crearConvocatoriaEvento(ConvocatoriaEvento convocatoriaEvento) {
		return convocatoriaEventoDAO.crearConvocatoriaEvento(convocatoriaEvento);
	}

	public ConvocatoriaEvento obtenerConvocatoriaEventoPorId(Long id) {
		return convocatoriaEventoDAO.buscarConvocatoriaEventoId(id);
	}

	public ConvocatoriaEvento actualizarConvocatoriaEvento(ConvocatoriaEvento convocatoriaEvento) {
		if(convocatoriaEventoDAO.modificarConvocatoriaEvento(convocatoriaEvento)) {
			return convocatoriaEvento;
		}else{
			return null;
		}
	}

	public Boolean eliminarConvocatoriaEvento(Long id) {
		return convocatoriaEventoDAO.eliminarConvocatoriaEvento(id);
	}
}//fin clase
