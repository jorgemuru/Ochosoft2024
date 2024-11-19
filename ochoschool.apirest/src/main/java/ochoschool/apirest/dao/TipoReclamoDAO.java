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
import ochoschool.apirest.entidades.TipoReclamo;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class TipoReclamoDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public TipoReclamoDAO() {
	}

	public TipoReclamo crearTipoReclamo(TipoReclamo tr) {
		try {
			userTransaction.begin();
			TipoReclamo tipo = em.merge(tr);
			em.flush();
			userTransaction.commit();

			return tipo;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Tipo: " + e.getMessage(), e);
		}

	}// crearTipoReclamo

	public List<TipoReclamo> listarTiposReclamo() throws PersistenceException {
		try {
			String query = "SELECT i FROM TipoReclamo i ";
			TypedQuery<TipoReclamo> listadoQuery = em.createQuery(query, TipoReclamo.class);
			List<TipoReclamo> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarTipoReclamo(Long id) {
		try {
			TipoReclamo tr = em.find(TipoReclamo.class, id);
			if (tr != null) {
				userTransaction.begin();
				em.remove(em.merge(tr));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Tipo: " + e.getMessage(), e);
		}
	}// eliminarTipoReclamo

	public TipoReclamo find(String descripcion) throws Exception {
		List<TipoReclamo> listaTipos = listarTiposReclamo();
		for (TipoReclamo unTipoReclamo : listaTipos) {
			if (unTipoReclamo.getDescripcion().equals(descripcion)) {
				return unTipoReclamo;
			}
		}
		return null;
	}

	public Boolean modificarTipoReclamo(TipoReclamo tipo) {
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

	public TipoReclamo buscarTipoReclamoId(Long id) {
		try {
			TypedQuery<TipoReclamo> query = em
					.createQuery("SELECT i FROM TipoReclamo i WHERE i.id LIKE :id1", TipoReclamo.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Tipo: " + e.getMessage(), e);
		}
	}
	
}//fin clase
