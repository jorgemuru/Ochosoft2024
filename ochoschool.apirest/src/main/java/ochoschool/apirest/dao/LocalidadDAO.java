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
import ochoschool.apirest.entidades.Localidad;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class LocalidadDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public LocalidadDAO() {
	}

	public Localidad crearLocalidad(Localidad l) {
		try {
			userTransaction.begin();
			Localidad localidad = em.merge(l);
			em.flush();
			userTransaction.commit();

			return localidad;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Localidad: " + e.getMessage(), e);
		}

	}// crearLocalidad

	public List<Localidad> listarLocalidades() throws PersistenceException {
		try {
			String query = "SELECT l FROM Localidad l ";
			TypedQuery<Localidad> listadoQuery = em.createQuery(query, Localidad.class);
			List<Localidad> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarLocalidad(Long id) {
		try {
			Localidad l = em.find(Localidad.class, id);
			if (l != null) {
				userTransaction.begin();
				em.remove(em.merge(l));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar la Localidad: " + e.getMessage(), e);
		}
	}// eliminarLocalidad

	public Localidad find(String nombre) throws Exception {
		List<Localidad> lista = listarLocalidades();
		for (Localidad unaLoc : lista) {
			if (unaLoc.getNombre().equals(nombre)) {
				return unaLoc;
			}
		}
		return null;
	}

	public Boolean modificarLocalidad(Localidad localidad) {
		try {
			userTransaction.begin();
			em.merge(localidad);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Localidad: " + e.getMessage(), e);
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

	public Localidad buscarLocalidadId(Long id) {
		try {
			TypedQuery<Localidad> query = em.createQuery("SELECT l FROM Localidad l WHERE l.id LIKE :id1", Localidad.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener la Localidad: " + e.getMessage(), e);
		}
	}

}//fin clase
