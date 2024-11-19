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
import ochoschool.apirest.entidades.EstadoEvento;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class EstadoEventoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public EstadoEventoDAO() {
	}

	public EstadoEvento crearEstadoEvento(EstadoEvento ee) {
		try {
			userTransaction.begin();
			EstadoEvento estado = em.merge(ee);
			em.flush();
			userTransaction.commit();

			return estado;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Estado: " + e.getMessage(), e);
		}

	}// crearEstadoEvento

	public List<EstadoEvento> listarEstadosEvento() throws PersistenceException {
		try {
			String query = "SELECT i FROM EstadoEvento i ";
			TypedQuery<EstadoEvento> listadoQuery = em.createQuery(query, EstadoEvento.class);
			List<EstadoEvento> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarEstadoEvento(Long id) {
		try {
			EstadoEvento ee = em.find(EstadoEvento.class, id);
			if (ee != null) {
				userTransaction.begin();
				em.remove(em.merge(ee));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Estado: " + e.getMessage(), e);
		}
	}// eliminarEstado

	public EstadoEvento find(String descripcion) throws Exception {
		List<EstadoEvento> listaEstados = listarEstadosEvento();
		for (EstadoEvento unEstadoEvento : listaEstados) {
			if (unEstadoEvento.getDescripcion().equals(descripcion)) {
				return unEstadoEvento;
			}
		}
		return null;
	}

	public Boolean modificarEstadoEvento(EstadoEvento estado) {
		try {
			userTransaction.begin();
			em.merge(estado);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Estado: " + e.getMessage(), e);
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

	public EstadoEvento buscarEstadoEventoId(Long id) {
		try {
			TypedQuery<EstadoEvento> query = em.createQuery("SELECT i FROM EstadoEvento i WHERE i.id LIKE :id1", EstadoEvento.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Estado: " + e.getMessage(), e);
		}
	}
	
}//fin clase
