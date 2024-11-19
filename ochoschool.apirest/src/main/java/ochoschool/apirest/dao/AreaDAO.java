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
import ochoschool.apirest.entidades.Area;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class AreaDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public AreaDAO() {
	}

	public Area crearArea(Area a) {
		try {
			userTransaction.begin();
			Area area = em.merge(a);
			em.flush();
			userTransaction.commit();

			return area;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el area: " + e.getMessage(), e);
		}

	}// crearArea

	public List<Area> listarAreas() throws PersistenceException {
		try {
			String query = "SELECT a FROM Area a ";
			TypedQuery<Area> listadoQuery = em.createQuery(query, Area.class);
			List<Area> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarArea(Long id) {
		try {
			Area a = em.find(Area.class, id);
			if (a != null) {
				userTransaction.begin();
				em.remove(em.merge(a));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Area: " + e.getMessage(), e);
		}
	}// eliminarArea

	public Area find(String descripcion) throws Exception {
		List<Area> listaAreas = listarAreas();
		for (Area unArea : listaAreas) {
			if (unArea.getDescripcion().equals(descripcion)) {
				return unArea;
			}
		}
		return null;
	}

	public Boolean modificarArea(Area area) {
		try {
			userTransaction.begin();
			em.merge(area);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el area: " + e.getMessage(), e);
		}
	}

	/*
	 * public Area buscarArea(String modo) { try { TypedQuery<Evento> query = em
	 * .createQuery("SELECT e FROM Evento e WHERE e.evento LIKE :modalidad",
	 * Evento.class) .setParameter("modalidad", modo); return
	 * query.getSingleResult(); } catch (NoResultException ex) { return null; }
	 * catch (Exception e) { throw new
	 * PersistenceException("Ocurrió un error al obtener el evento: " +
	 * e.getMessage(), e); } }
	 */

	public Area buscarAreaId(Long id) {
		try {
			TypedQuery<Area> query = em.createQuery("SELECT a FROM Area a WHERE a.id LIKE :id1", Area.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el area: " + e.getMessage(), e);
		}
	}

}// fin clase
