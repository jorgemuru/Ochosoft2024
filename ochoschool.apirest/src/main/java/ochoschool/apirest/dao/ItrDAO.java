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
import ochoschool.apirest.entidades.Itr;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ItrDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public ItrDAO() {
	}

	public Itr crearItr(Itr i) {
		try {
			userTransaction.begin();
			Itr itr = em.merge(i);
			em.flush();
			userTransaction.commit();

			return itr;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Itr: " + e.getMessage(), e);
		}

	}// crearItr

	public List<Itr> listarItrs() throws PersistenceException {
		try {
			String query = "SELECT i FROM Itr i ";
			TypedQuery<Itr> listadoQuery = em.createQuery(query, Itr.class);
			List<Itr> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarItr(Long id) {
		try {
			Itr i = em.find(Itr.class, id);
			if (i != null) {
				userTransaction.begin();
				em.remove(em.merge(i));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Itr: " + e.getMessage(), e);
		}
	}// eliminarItr

	public Itr find(String nombre) throws Exception {
		List<Itr> listaItrs = listarItrs();
		for (Itr unItr : listaItrs) {
			if (unItr.getNombre().equals(nombre)) {
				return unItr;
			}
		}
		return null;
	}

	public Boolean modificarItr(Itr itr) {
		try {
			userTransaction.begin();
			em.merge(itr);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Itr: " + e.getMessage(), e);
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

	public Itr buscarItrId(Long id) {
		try {
			TypedQuery<Itr> query = em.createQuery("SELECT i FROM Itr i WHERE i.id LIKE :id1", Itr.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Itr: " + e.getMessage(), e);
		}
	}

}// fin clase
