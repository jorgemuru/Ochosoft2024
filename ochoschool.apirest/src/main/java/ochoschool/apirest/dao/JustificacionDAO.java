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
import ochoschool.apirest.entidades.Justificacion;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class JustificacionDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public JustificacionDAO() {
	}

	public Justificacion crearJustificacion(Justificacion j) {
		try {
			userTransaction.begin();
			Justificacion justificacion = em.merge(j);
			em.flush();
			userTransaction.commit();

			return justificacion;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Justificacion: " + e.getMessage(), e);
		}

	}// crearJustificacion

	public List<Justificacion> listarJustificaciones() throws PersistenceException {
		try {
			String query = "SELECT j FROM Justificacion j";
			TypedQuery<Justificacion> listadoQuery = em.createQuery(query, Justificacion.class);
			List<Justificacion> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarJustificacion(Long id) {
		try {
			Justificacion j = em.find(Justificacion.class, id);
			if (j != null) {
				userTransaction.begin();
				em.remove(em.merge(j));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar la Justificacion: " + e.getMessage(), e);
		}

	}// eliminarJustificacion

	public Justificacion find(String detalle) throws Exception {
		List<Justificacion> lista = listarJustificaciones();
		for (Justificacion unaJust : lista) {
			if (unaJust.getDetalle().equals(detalle)) {
				return unaJust;
			}
		}
		return null;
	}

	public Boolean modificarJustificacion(Justificacion justificacion) {
		try {
			userTransaction.begin();
			em.merge(justificacion);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Justificacion: " + e.getMessage(), e);
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

	public Justificacion buscarJustificacionId(Long id) {
		try {
			TypedQuery<Justificacion> query = em.createQuery("SELECT j FROM Justificacion j WHERE j.id LIKE :id1", Justificacion.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener la Justificacion: " + e.getMessage(), e);
		}
	}
	
	
}//fin clase
