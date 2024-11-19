package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.AreaDAO;
import ochoschool.apirest.entidades.Area;

@Stateless
public class AreaEJB {
	@Inject
	private AreaDAO areaDAO;

    public List<Area> obtenerTodosLasAreas() {
        return areaDAO.listarAreas();
    }

	public Area crearArea(Area area) {
		return areaDAO.crearArea(area);
	}

	public Area obtenerAreaPorId(Long id) {
		return areaDAO.buscarAreaId(id);
	}

	public Area actualizarArea(Area area) {
		if(areaDAO.modificarArea(area)) {
			return area;
		}else{
			return null;
		}
	}

	public Boolean eliminarArea(Long id) {
		return areaDAO.eliminarArea(id);
	}
	
}//fin clase
