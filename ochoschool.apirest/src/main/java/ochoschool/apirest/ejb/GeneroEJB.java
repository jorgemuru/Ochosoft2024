package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.GeneroDAO;
import ochoschool.apirest.entidades.Genero;

@Stateless
public class GeneroEJB {
	@Inject
	private GeneroDAO generoDAO;

    public List<Genero> obtenerTodosLosGeneros() {
        return generoDAO.listarGeneros();
    }

	public Genero crearGenero(Genero genero) {
		return generoDAO.crearGenero(genero);
	}

	public Genero obtenerGeneroPorId(Long id) {
		return generoDAO.buscarGeneroId(id);
	}

	public Genero actualizarGenero(Genero genero) {
		if(generoDAO.modificarGenero(genero)) {
			return genero;
		}else{
			return null;
		}
	}

	public Boolean eliminarGenero(Long id) {
		return generoDAO.eliminarGenero(id);
	}
	
}//fin clase
