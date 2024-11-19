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
import ochoschool.apirest.entidades.TipoEvento;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class TipoEventoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public TipoEventoDAO() {
	}

	public TipoEvento crearTipoEvento(TipoEvento te) {
		try {
			userTransaction.begin();
			TipoEvento tipo = em.merge(te);
			em.flush();
			userTransaction.commit();

			return tipo;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Tipo: " + e.getMessage(), e);
		}

	}// crearTipoEvento

	public List<TipoEvento> listarTiposEvento() throws PersistenceException {
		try {
			String query = "SELECT i FROM TipoEvento i ";
			TypedQuery<TipoEvento> listadoQuery = em.createQuery(query, TipoEvento.class);
			List<TipoEvento> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarTipoEvento(Long id) {
		try {
			TipoEvento te = em.find(TipoEvento.class, id);
			if (te != null) {
				userTransaction.begin();
				em.remove(em.merge(te));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Tipo: " + e.getMessage(), e);
		}
	}// eliminarTipo

	public TipoEvento find(String descripcion) throws Exception {
		List<TipoEvento> listaTipos = listarTiposEvento();
		for (TipoEvento unTipoEvento : listaTipos) {
			if (unTipoEvento.getDescripcion().equals(descripcion)) {
				return unTipoEvento;
			}
		}
		return null;
	}

	public Boolean modificarTipoEvento(TipoEvento tipo) {
		try {
			userTransaction.begin();
			em.merge(tipo);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Tipo: " + e.getMessage(), e);
		}
	}

	public TipoEvento buscarTipoEventoId(Long id) {
		try {
			TypedQuery<TipoEvento> query = em
					.createQuery("SELECT i FROM TipoEvento i WHERE i.id LIKE :id1", TipoEvento.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Tipo: " + e.getMessage(), e);
		}
	}

}// fin clase
