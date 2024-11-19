package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.LocalidadDAO;
import ochoschool.apirest.entidades.Localidad;

@Stateless
public class LocalidadEJB {
	@Inject
	private LocalidadDAO localidadDAO;

    public List<Localidad> obtenerTodosLasLocalidades() {
        return localidadDAO.listarLocalidades();
    }

	public Localidad crearLocalidad(Localidad localidad) {
		return localidadDAO.crearLocalidad(localidad);
	}

	public Localidad obtenerLocalidadPorId(Long id) {
		return localidadDAO.buscarLocalidadId(id);
	}

	public Localidad actualizarLocalidad(Localidad localidad) {
		if(localidadDAO.modificarLocalidad(localidad)) {
			return localidad;
		}else{
			return null;
		}
	}

	public Boolean eliminarLocalidad(Long id) {
		return localidadDAO.eliminarLocalidad(id);
	}
	
}//fin clase
