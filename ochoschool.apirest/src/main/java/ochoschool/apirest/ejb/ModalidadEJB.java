package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.ModalidadDAO;
import ochoschool.apirest.entidades.Modalidad;

@Stateless
public class ModalidadEJB {
	@Inject
	private ModalidadDAO modalidadDAO;

    public List<Modalidad> obtenerTodasLasModalidades() {
        return modalidadDAO.listarModalidades();
    }

	public Modalidad crearModalidad(Modalidad modalidad) {
		return modalidadDAO.crearModalidad(modalidad);
	}

	public Modalidad obtenerModalidadPorId(Long id) {
		return modalidadDAO.buscarModalidadId(id);
	}

	public Modalidad actualizarModalidad(Modalidad modalidad) {
		if(modalidadDAO.modificarModalidad(modalidad)) {
			return modalidad;
		}else{
			return null;
		}
	}

	public Boolean eliminarModalidad(Long id) {
		return modalidadDAO.eliminarModalidad(id);
	}
	
}//fin clase
