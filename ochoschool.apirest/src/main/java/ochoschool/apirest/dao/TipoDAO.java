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
import ochoschool.apirest.entidades.Tipo;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class TipoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public TipoDAO() {
	}

	public Tipo crearTipo(Tipo t) {
		try {
			userTransaction.begin();
			Tipo tipo = em.merge(t);
			em.flush();
			userTransaction.commit();

			return tipo;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Tipo: " + e.getMessage(), e);
		}

	}// crearTipo

	public List<Tipo> listarTipos() throws PersistenceException {
		try {
			String query = "SELECT i FROM Tipo i ";
			TypedQuery<Tipo> listadoQuery = em.createQuery(query, Tipo.class);
			List<Tipo> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarTipo(Long id) {
		try {
			Tipo t = em.find(Tipo.class, id);
			if (t != null) {
				userTransaction.begin();
				em.remove(em.merge(t));
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

	public Tipo find(String descripcion) throws Exception {
		List<Tipo> lista = listarTipos();
		for (Tipo unTipo : lista) {
			if (unTipo.getDescripcion().equals(descripcion)) {
				return unTipo;
			}
		}
		return null;
	}

	public Boolean modificarTipo(Tipo tipo) {
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

	public Tipo buscarTipoId(Long id) {
		try {
			TypedQuery<Tipo> query = em
					.createQuery("SELECT i FROM Tipo i WHERE i.id LIKE :id1", Tipo.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Tipo: " + e.getMessage(), e);
		}
	}


}//fin clase
