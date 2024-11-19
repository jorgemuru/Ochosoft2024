package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.TutorDAO;
import ochoschool.apirest.entidades.Tutor;

@Stateless
public class TutorEJB {
	@Inject
	private TutorDAO tutorDAO;

    public List<Tutor> obtenerTodosLosTutores() {
        return tutorDAO.listarTutores(null, null, null, null);
    }

	public Tutor crearTutor(Tutor tutor) {
		return tutorDAO.crearTutor(tutor);
	}

	public Tutor obtenerTutorPorId(Long id) {
		return tutorDAO.buscarTutorId(id);
	}

	public Tutor actualizarTutor(Tutor tutor) {
		if(tutorDAO.modificarTutor(tutor)) {
			return tutor;
		}else{
			return null;
		}
	}

	public Boolean eliminarTutor(Long id) {
		return tutorDAO.eliminarTutor(id);
	}
	
	public Boolean deshabilitarTutor(Long id) {
		return tutorDAO.deshabilitarTutor(id);
	}
	
}//fin clase
