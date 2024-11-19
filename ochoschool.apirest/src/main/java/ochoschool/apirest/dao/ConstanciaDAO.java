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
import ochoschool.apirest.entidades.Constancia;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ConstanciaDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public ConstanciaDAO() {
	}

	public Constancia crearConstancia(Constancia c) {
		try {
			userTransaction.begin();
			Constancia constancia = em.merge(c);
			em.flush();
			userTransaction.commit();

			return constancia;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Constancia: " + e.getMessage(), e);
		}

	}// crearConstancia

	public List<Constancia> listarConstancias() throws PersistenceException {
		try {
			String query = "SELECT i FROM Constancia i ";
			TypedQuery<Constancia> listadoQuery = em.createQuery(query, Constancia.class);
			List<Constancia> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarConstancia(Long id) {
		try {
			Constancia c = em.find(Constancia.class, id);
			if (c != null) {
				userTransaction.begin();
				em.remove(em.merge(c));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar la Cosntancia: " + e.getMessage(), e);
		}
	}// eliminarConstancia

	public Constancia find(String estado) throws Exception {
		List<Constancia> lista = listarConstancias();
		for (Constancia unaConst: lista) {
			if (unaConst.getEstado().getDescripcion().equals(estado)) {
				return unaConst;
			}
		}
		return null;
	}

	public Boolean modificarConstancia(Constancia constancia) {
		try {
			userTransaction.begin();
			em.merge(constancia);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Constancia: " + e.getMessage(), e);
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

	public Constancia buscarConstanciaId(Long id) {
		try {
			TypedQuery<Constancia> query = em.createQuery("SELECT i FROM Constancia i WHERE i.id LIKE :id1", Constancia.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Estado: " + e.getMessage(), e);
		}
	}
	
}//fin clase
