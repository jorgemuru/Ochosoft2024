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
import ochoschool.apirest.entidades.RegistroReclamo;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class RegistroReclamoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public RegistroReclamoDAO() {
	}

	public RegistroReclamo crearRegistroReclamo(RegistroReclamo r) {
		try {
			userTransaction.begin();
			RegistroReclamo registroReclamo = em.merge(r);
			em.flush();
			userTransaction.commit();

			return registroReclamo;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Registro: " + e.getMessage(), e);
		}

	}// crearRegistroReclamo

	public List<RegistroReclamo> listarRegistroReclamos() throws PersistenceException {
		try {
			String query = "SELECT r FROM RegistroReclamo r";
			TypedQuery<RegistroReclamo> listadoQuery = em.createQuery(query, RegistroReclamo.class);
			List<RegistroReclamo> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarRegistroReclamo(Long id) {
		try {
			RegistroReclamo r = em.find(RegistroReclamo.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar el Registro: " + e.getMessage(), e);
		}

	}// eliminarRegistroReclamo

	public RegistroReclamo find(String detalle) throws Exception {
		List<RegistroReclamo> listaRegistroReclamos = listarRegistroReclamos();
		for (RegistroReclamo unRecla : listaRegistroReclamos) {
			if (unRecla.getDetalle().equals(detalle)) {
				return unRecla;
			}
		}
		return null;
	}

	public Boolean modificarRegistroReclamo(RegistroReclamo registroReclamo) {
		try {
			userTransaction.begin();
			em.merge(registroReclamo);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Registro: " + e.getMessage(), e);
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

	public RegistroReclamo buscarRegistroReclamoId(Long id) {
		try {
			TypedQuery<RegistroReclamo> query = em.createQuery("SELECT r FROM RegistroReclamo r WHERE r.id LIKE :id1", RegistroReclamo.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Registro: " + e.getMessage(), e);
		}
	}
	
}//fin clase
