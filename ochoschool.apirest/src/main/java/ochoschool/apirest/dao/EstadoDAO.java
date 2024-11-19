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
import ochoschool.apirest.entidades.Estado;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class EstadoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public EstadoDAO() {
	}

	public Estado crearEstado(Estado es) {
		try {
			userTransaction.begin();
			Estado estado = em.merge(es);
			em.flush();
			userTransaction.commit();

			return estado;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Estado: " + e.getMessage(), e);
		}

	}// crearEstado

	public List<Estado> listarEstados() throws PersistenceException {
		try {
			String query = "SELECT i FROM Estado i ";
			TypedQuery<Estado> listadoQuery = em.createQuery(query, Estado.class);
			List<Estado> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarEstado(Long id) {
		try {
			Estado es = em.find(Estado.class, id);
			if (es != null) {
				userTransaction.begin();
				em.remove(em.merge(es));
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

	public Estado find(String descripcion) throws Exception {
		List<Estado> listaEstados = listarEstados();
		for (Estado unEstado : listaEstados) {
			if (unEstado.getDescripcion().equals(descripcion)) {
				return unEstado;
			}
		}
		return null;
	}

	public Boolean modificarEstado(Estado estado) {
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

	public Estado buscarEstadoId(Long id) {
		try {
			TypedQuery<Estado> query = em.createQuery("SELECT i FROM Estado i WHERE i.id LIKE :id1", Estado.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Estado: " + e.getMessage(), e);
		}
	}
	
}//fin clase
