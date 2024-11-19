package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.ItrDAO;
import ochoschool.apirest.entidades.Itr;

@Stateless
public class ItrEJB {
	@Inject
	private ItrDAO itrDAO;

    public List<Itr> obtenerTodosLosItr() {
        return itrDAO.listarItrs();
    }

	public Itr crearItr(Itr itr) {
		return itrDAO.crearItr(itr);
	}

	public Itr obtenerItrPorId(Long id) {
		return itrDAO.buscarItrId(id);
	}

	public Itr actualizarItr(Itr itr) {
		if(itrDAO.modificarItr(itr)) {
			return itr;
		}else{
			return null;
		}
	}

	public Boolean eliminarItr(Long id) {
		return itrDAO.eliminarItr(id);
	}
	
}//fin clase
