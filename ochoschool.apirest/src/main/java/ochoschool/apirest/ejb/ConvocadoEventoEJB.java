package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.ConvocadoEventoDAO;
import ochoschool.apirest.entidades.ConvocadoEvento;

@Stateless
public class ConvocadoEventoEJB {
	@Inject
	private ConvocadoEventoDAO convocadoEventoDAO;

    public List<ConvocadoEvento> obtenerTodosLosConvocados() {
        return convocadoEventoDAO.listarConvocadosEvento();
    }

	public ConvocadoEvento crearConvocadoEvento(ConvocadoEvento convocadoEvento) {
		return convocadoEventoDAO.crearConvocadoEvento(convocadoEvento);
	}

	public ConvocadoEvento obtenerConvocadoEventoPorId(Long id) {
		return convocadoEventoDAO.buscarConvocadoEventoId(id);
	}

	public ConvocadoEvento actualizarConvocadoEvento(ConvocadoEvento convocadoEvento) {
		if(convocadoEventoDAO.modificarConvocadoEvento(convocadoEvento)) {
			return convocadoEvento;
		}else{
			return null;
		}
	}

	public Boolean eliminarConvocadoEvento(Long id) {
		return convocadoEventoDAO.eliminarConvocadoEvento(id);
	}
	
    public List<ConvocadoEvento> obtenerAsistenciaEventoEstudiante(long idEvento, long idEstudiante) {
        return convocadoEventoDAO.listarAsistenciaEventoEstudiante(idEvento, idEstudiante);
    }
    public List<ConvocadoEvento> obtenerAsistenciaEstudiante(long idEstudiante) {
        return convocadoEventoDAO.listarAsistenciaEstudiante(idEstudiante);
    }
    public List<ConvocadoEvento> obtenerAsistenciaEvento(long idEvento) {
        return convocadoEventoDAO.listarAsistenciaEvento(idEvento);
    }
    public List<ConvocadoEvento> obtenerConvocatoriasEstudiante(long idEstudiante) {
        return convocadoEventoDAO.listarConvocatoriasEstudiante(idEstudiante);
    }
    public List<ConvocadoEvento> obtenerConvocatoriasEvento(long idEvento) {
        return convocadoEventoDAO.listarConvocatoriasEvento(idEvento);
    }
}//fin clase
