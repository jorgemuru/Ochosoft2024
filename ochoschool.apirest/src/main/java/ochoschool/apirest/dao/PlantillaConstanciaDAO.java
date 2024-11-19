package ochoschool.apirest.dao;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;
import ochoschool.apirest.entidades.PlantillaConstancia;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class PlantillaConstanciaDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public PlantillaConstanciaDAO() {
	}

	public PlantillaConstancia crearPlantillaConstancia(PlantillaConstancia r) {
		try {
			userTransaction.begin();
			PlantillaConstancia plantillaConstancia = em.merge(r);
			em.flush();
			userTransaction.commit();

			return plantillaConstancia;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Plantilla: " + e.getMessage(), e);
		}

	}// crearPlantillaConstancia

	public List<PlantillaConstancia> listarPlantillasConstancia() throws PersistenceException {
		try {
			String query = "SELECT r FROM PlantillaConstancia r";
			TypedQuery<PlantillaConstancia> listadoQuery = em.createQuery(query, PlantillaConstancia.class);
			List<PlantillaConstancia> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarPlantillaConstancia(Long id) {
		try {
			PlantillaConstancia r = em.find(PlantillaConstancia.class, id);
			if (r != null) {
				userTransaction.begin();
				em.remove(em.merge(r));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar la Plantilla: " + e.getMessage(), e);
		}

	}// eliminarPlantillaConstancia

	public PlantillaConstancia find(String descripcion) throws Exception {
		List<PlantillaConstancia> lista = listarPlantillasConstancia();
		for (PlantillaConstancia unaPlan : lista) {
			if (unaPlan.getTipo().getDescripcion().equals(descripcion)) {
				return unaPlan;
			}
		}
		return null;
	}

	public Boolean modificarPlantillaConstancia(PlantillaConstancia plantillaConstancia) {
		try {
			userTransaction.begin();
			em.merge(plantillaConstancia);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Plantilla: " + e.getMessage(), e);
		}
	}

	/*
	 * public Reclamo buscarEventoModalidad(String modo) { try { TypedQuery<Evento>
	 * query = em
	 * .createQuery("SELECT k FROM Evento k WHERE k.modalidad LIKE :modalidad",
	 * Evento.class) .setParameter("modalidad", modo); return
	 * query.getSingleResult(); } catch (NoResultException ex) { return null; }
	 * catch (Exception e) { throw new
	 * PersistenceException("Ocurrió un error al obtener el evento: " +
	 * e.getMessage(), e); } }
	 */

	public PlantillaConstancia buscarPlantillaConstanciaId(Long id) {
		try {
			TypedQuery<PlantillaConstancia> query = em.createQuery("SELECT r FROM PlantillaConstancia r WHERE r.id LIKE :id1", PlantillaConstancia.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener la Plantilla: " + e.getMessage(), e);
		}
	}
	
}//fin clase
