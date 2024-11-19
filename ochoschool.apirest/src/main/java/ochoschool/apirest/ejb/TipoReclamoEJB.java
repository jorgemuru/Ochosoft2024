package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.TipoReclamoDAO;
import ochoschool.apirest.entidades.TipoReclamo;

@Stateless
public class TipoReclamoEJB {
	@Inject
	private TipoReclamoDAO tipoReclamoDAO;

    public List<TipoReclamo> obtenerTodosLosTiposReclamo() {
        return tipoReclamoDAO.listarTiposReclamo();
    }

	public TipoReclamo crearTipoReclamo(TipoReclamo tipoReclamo) {
		return tipoReclamoDAO.crearTipoReclamo(tipoReclamo);
	}

	public TipoReclamo obtenerTipoReclamoPorId(Long id) {
		return tipoReclamoDAO.buscarTipoReclamoId(id);
	}

	public TipoReclamo actualizarTipoReclamo(TipoReclamo tipoReclamo) {
		if(tipoReclamoDAO.modificarTipoReclamo(tipoReclamo)) {
			return tipoReclamo;
		}else{
			return null;
		}
	}

	public Boolean eliminarTipoReclamo(Long id) {
		return tipoReclamoDAO.eliminarTipoReclamo(id);
	}
	
}//fin clase
