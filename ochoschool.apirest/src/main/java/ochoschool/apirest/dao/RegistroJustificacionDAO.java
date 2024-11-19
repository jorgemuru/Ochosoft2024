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
import ochoschool.apirest.entidades.RegistroJustificacion;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class RegistroJustificacionDAO {


	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public RegistroJustificacionDAO() {
	}

	public RegistroJustificacion crearRegistroJustificacion(RegistroJustificacion r) {
		try {
			userTransaction.begin();
			RegistroJustificacion registroJustificacion = em.merge(r);
			em.flush();
			userTransaction.commit();

			return registroJustificacion;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Registro: " + e.getMessage(), e);
		}

	}// crearRegistroJustificacion

	public List<RegistroJustificacion> listarRegistrosJustificacion() throws PersistenceException {
		try {
			String query = "SELECT r FROM RegistroJustificacion r";
			TypedQuery<RegistroJustificacion> listadoQuery = em.createQuery(query, RegistroJustificacion.class);
			List<RegistroJustificacion> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarRegistroJustificacion(Long id) {
		try {
			RegistroJustificacion r = em.find(RegistroJustificacion.class, id);
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

	}// eliminarRegistroJustificacion

	public RegistroJustificacion find(String detalle) throws Exception {
		List<RegistroJustificacion> lista = listarRegistrosJustificacion();
		for (RegistroJustificacion unRegis : lista) {
			if (unRegis.getDetalle().equals(detalle)) {
				return unRegis;
			}
		}
		return null;
	}

	public Boolean modificarRegistroJustificacion(RegistroJustificacion registroJustificacion) {
		try {
			userTransaction.begin();
			em.merge(registroJustificacion);
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

	public RegistroJustificacion buscarRegistroJustificacionId(Long id) {
		try {
			TypedQuery<RegistroJustificacion> query = em.createQuery("SELECT r FROM RegistroJustificacion r WHERE r.id LIKE :id1", RegistroJustificacion.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Registro: " + e.getMessage(), e);
		}
	}
	
}//fin clase
