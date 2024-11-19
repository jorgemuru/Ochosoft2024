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
import ochoschool.apirest.entidades.EmisionConstancia;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class EmisionConstanciaDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public EmisionConstanciaDAO() {
	}

	public EmisionConstancia crearEmisionConstancia(EmisionConstancia r) {
		try {
			userTransaction.begin();
			EmisionConstancia emisionConstancia = em.merge(r);
			em.flush();
			userTransaction.commit();

			return emisionConstancia;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Emision: " + e.getMessage(), e);
		}

	}// crearEmisionConstancia

	public List<EmisionConstancia> listarEmisionesConstancia() throws PersistenceException {
		try {
			String query = "SELECT r FROM EmisionConstancia r";
			TypedQuery<EmisionConstancia> listadoQuery = em.createQuery(query, EmisionConstancia.class);
			List<EmisionConstancia> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarEmisionConstancia(Long id) {
		try {
			EmisionConstancia r = em.find(EmisionConstancia.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar la Emision: " + e.getMessage(), e);
		}

	}// eliminarEmisionConstancia

	public EmisionConstancia find(String analista) throws Exception {
		List<EmisionConstancia> lista = listarEmisionesConstancia();
		for (EmisionConstancia unaEmi : lista) {
			if (unaEmi.getAnalista().getUsuario().equals(analista)) {
				return unaEmi;
			}
		}
		return null;
	}

	public Boolean modificarEmisionConstancia(EmisionConstancia emisionConstancia) {
		try {
			userTransaction.begin();
			em.merge(emisionConstancia);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Emision: " + e.getMessage(), e);
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

	public EmisionConstancia buscarEmisionConstanciaId(Long id) {
		try {
			TypedQuery<EmisionConstancia> query = em.createQuery("SELECT r FROM EmisionConstancia r WHERE r.id LIKE :id1", EmisionConstancia.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener la Emision: " + e.getMessage(), e);
		}
	}
	
}//fin clase
