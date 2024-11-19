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
import ochoschool.apirest.entidades.RegistroConstancia;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class RegistroConstanciaDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public RegistroConstanciaDAO() {
	}

	public RegistroConstancia crearRegistroConstancia(RegistroConstancia r) {
		try {
			userTransaction.begin();
			RegistroConstancia registroConstancia = em.merge(r);
			em.flush();
			userTransaction.commit();

			return registroConstancia;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Registro: " + e.getMessage(), e);
		}

	}// crearRegistroConstancia

	public List<RegistroConstancia> listarRegistrosConstancia() throws PersistenceException {
		try {
			String query = "SELECT r FROM RegistroConstancia r";
			TypedQuery<RegistroConstancia> listadoQuery = em.createQuery(query, RegistroConstancia.class);
			List<RegistroConstancia> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarRegistroConstancia(Long id) {
		try {
			RegistroConstancia r = em.find(RegistroConstancia.class, id);
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

	}// eliminarRegistroConstancia

	public RegistroConstancia find(String detalle) throws Exception {
		List<RegistroConstancia> lista = listarRegistrosConstancia();
		for (RegistroConstancia unReg : lista) {
			if (unReg.getDetalle().equals(detalle)) {
				return unReg;
			}
		}
		return null;
	}

	public Boolean modificarRegistroConstancia(RegistroConstancia registroConstancia) {
		try {
			userTransaction.begin();
			em.merge(registroConstancia);
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

	public RegistroConstancia buscarRegistroConstanciaId(Long id) {
		try {
			TypedQuery<RegistroConstancia> query = em.createQuery("SELECT r FROM RegistroConstancia r WHERE r.id LIKE :id1", RegistroConstancia.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Registro: " + e.getMessage(), e);
		}
	}
	
}//fin clase
