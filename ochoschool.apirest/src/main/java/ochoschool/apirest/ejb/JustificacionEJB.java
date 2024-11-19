package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.JustificacionDAO;
import ochoschool.apirest.entidades.Justificacion;

@Stateless
public class JustificacionEJB {
	@Inject
	private JustificacionDAO justificacionDAO;

    public List<Justificacion> obtenerTodasLasJustificaciones() {
        return justificacionDAO.listarJustificaciones();
    }

	public Justificacion crearJustificacion(Justificacion justificacion) {
		return justificacionDAO.crearJustificacion(justificacion);
	}

	public Justificacion obtenerJustificacionPorId(Long id) {
		return justificacionDAO.buscarJustificacionId(id);
	}

	public Justificacion actualizarJustificacion(Justificacion justificacion) {
		if(justificacionDAO.modificarJustificacion(justificacion)) {
			return justificacion;
		}else{
			return null;
		}
	}

	public Boolean eliminarJustificacion(Long id) {
		return justificacionDAO.eliminarJustificacion(id);
	}
	
}//fin clase
