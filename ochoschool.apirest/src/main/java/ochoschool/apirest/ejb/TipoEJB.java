package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.TipoDAO;
import ochoschool.apirest.entidades.Tipo;

@Stateless
public class TipoEJB {
	@Inject
	private TipoDAO tipoDAO;

    public List<Tipo> obtenerTodosLosTipos() {
        return tipoDAO.listarTipos();
    }

	public Tipo crearTipo(Tipo tipo) {
		return tipoDAO.crearTipo(tipo);
	}

	public Tipo obtenerTipoPorId(Long id) {
		return tipoDAO.buscarTipoId(id);
	}

	public Tipo actualizarTipo(Tipo tipo) {
		if(tipoDAO.modificarTipo(tipo)) {
			return tipo;
		}else{
			return null;
		}
	}

	public Boolean eliminarTipo(Long id) {
		return tipoDAO.eliminarTipo(id);
	}
	
}//fin clase
