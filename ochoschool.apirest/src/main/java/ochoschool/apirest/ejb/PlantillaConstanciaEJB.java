package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.PlantillaConstanciaDAO;
import ochoschool.apirest.entidades.PlantillaConstancia;

@Stateless
public class PlantillaConstanciaEJB {
	@Inject
	private PlantillaConstanciaDAO plantillaConstanciaDAO;

    public List<PlantillaConstancia> obtenerTodasLasPlantillasConstancia() {
        return plantillaConstanciaDAO.listarPlantillasConstancia();
    }

	public PlantillaConstancia crearPlantillaConstancia(PlantillaConstancia plantillaConstancia) {
		return plantillaConstanciaDAO.crearPlantillaConstancia(plantillaConstancia);
	}

	public PlantillaConstancia obtenerPlantillaConstanciaPorId(Long id) {
		return plantillaConstanciaDAO.buscarPlantillaConstanciaId(id);
	}

	public PlantillaConstancia actualizarPlantillaConstancia(PlantillaConstancia plantillaConstancia) {
		if(plantillaConstanciaDAO.modificarPlantillaConstancia(plantillaConstancia)) {
			return plantillaConstancia;
		}else{
			return null;
		}
	}

	public Boolean eliminarPlantillaConstancia(Long id) {
		return plantillaConstanciaDAO.eliminarPlantillaConstancia(id);
	}
	
}//fin clase
