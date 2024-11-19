package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.EstudianteDAO;
import ochoschool.apirest.entidades.Estudiante;

@Stateless
public class EstudianteEJB {
	@Inject
	private EstudianteDAO estudianteDAO;

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDAO.listarEstudiantes(null, null, null, null);
    }

	public Estudiante crearEstudiante(Estudiante estudiante) {
		return estudianteDAO.crearEstudiante(estudiante);
	}

	public Estudiante obtenerEstudiantePorId(Long id) {
		return estudianteDAO.buscarEstudianteId(id);
	}

	public Estudiante actualizarEstudiante(Estudiante estudiante) {
		if(estudianteDAO.modificarEstudiante(estudiante)) {
			return estudiante;
		}else{
			return null;
		}
	}

	public Boolean eliminarEstudiante(Long id) {
		return estudianteDAO.eliminarEstudiante(id);
	}
	
	public Boolean deshabilitarEstudiante(Long id) {
		return estudianteDAO.deshabilitarEstudiante(id);
	}
	
}//fin clase
