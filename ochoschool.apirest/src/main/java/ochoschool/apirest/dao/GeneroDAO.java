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
import ochoschool.apirest.entidades.Genero;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class GeneroDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public GeneroDAO() {
	}

	public Genero crearGenero(Genero g) {
		try {
			userTransaction.begin();
			Genero genero = em.merge(g);
			em.flush();
			userTransaction.commit();

			return genero;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Genero: " + e.getMessage(), e);
		}

	}// crearGenero

	public List<Genero> listarGeneros() throws PersistenceException {
		try {
			String query = "SELECT g FROM Genero g ";
			TypedQuery<Genero> listadoQuery = em.createQuery(query, Genero.class);
			List<Genero> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarGenero(Long id) {
		try {
			Genero g = em.find(Genero.class, id);
			if (g != null) {
				userTransaction.begin();
				em.remove(em.merge(g));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Genero: " + e.getMessage(), e);
		}
	}// eliminarArea

	public Genero find(String nombre) throws Exception {
		List<Genero> lista = listarGeneros();
		for (Genero unGen : lista) {
			if (unGen.getNombre().equals(nombre)) {
				return unGen;
			}
		}
		return null;
	}

	public Boolean modificarGenero(Genero genero) {
		try {
			userTransaction.begin();
			em.merge(genero);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Genero: " + e.getMessage(), e);
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

	public Genero buscarGeneroId(Long id) {
		try {
			TypedQuery<Genero> query = em.createQuery("SELECT g FROM Genero g WHERE g.id LIKE :id1", Genero.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Genero: " + e.getMessage(), e);
		}
	}
	
	
}//fin clase
