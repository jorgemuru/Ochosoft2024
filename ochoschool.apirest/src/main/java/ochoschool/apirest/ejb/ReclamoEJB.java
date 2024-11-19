package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.ReclamoDAO;
import ochoschool.apirest.entidades.Reclamo;

@Stateless
public class ReclamoEJB {
	@Inject
	private ReclamoDAO reclamoDAO;

    public List<Reclamo> obtenerTodosLosReclamos() {
        return reclamoDAO.listarReclamos();
    }

	public Reclamo crearReclamo(Reclamo reclamo) {
		return reclamoDAO.crearReclamo(reclamo);
	}

	public Reclamo obtenerReclamoPorId(Long id) {
		return reclamoDAO.buscarReclamoId(id);
	}

	public Reclamo actualizarReclamo(Reclamo reclamo) {
		if(reclamoDAO.modificarReclamo(reclamo)) {
			return reclamo;
		}else{
			return null;
		}
	}

	public Boolean eliminarReclamo(Long id) {
		return reclamoDAO.eliminarReclamo(id);
	}
	
	public List<Reclamo> obtenerReclamosXItr(long idItr){
		return reclamoDAO.obtenerReclamosXItr(idItr);
	}
	public List<Reclamo> obtenerReclamosXEvento(long idEvento){
		return reclamoDAO.obtenerReclamosXEvento(idEvento);
	}
	public List<Reclamo> obtenerReclamosXGen(String generacion){
		return reclamoDAO.obtenerReclamosXGeneracion(generacion);
	}
	public List<Reclamo> obtenerReclamosXAnio(int anio){
		return reclamoDAO.obtenerReclamosXAnio(anio);
	}
	public List<Reclamo> obtenerReclamosXTipo(String tipo){
		return reclamoDAO.obtenerReclamosXTipo(tipo);
	}
	
}//finclase
