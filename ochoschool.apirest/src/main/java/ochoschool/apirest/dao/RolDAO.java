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
import ochoschool.apirest.entidades.Rol;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class RolDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public RolDAO() {
	}

	public Rol crearRol(Rol r) {
		try {
			userTransaction.begin();
			Rol rol = em.merge(r);
			em.flush();
			userTransaction.commit();

			return rol;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el rol: " + e.getMessage(), e);
		}

	}// crearRol

	public List<Rol> listarRoles() throws PersistenceException {
		try {
			String query = "SELECT r FROM Rol r ";
			TypedQuery<Rol> listadoQuery = em.createQuery(query, Rol.class);
			List<Rol> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarRol(Long id) {
		try {
			Rol r = em.find(Rol.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar el Rol: " + e.getMessage(), e);
		}
		
	}// eliminarRol

	public Rol find(String descripcion) throws Exception {
		List<Rol> listaRoles = listarRoles();
		for (Rol unRol : listaRoles) {
			if (unRol.getDescripcion().equals(descripcion)) {
				return unRol;
			}
		}
		return null;
	}

	public Boolean modificarRol(Rol rol) {
		try {
			userTransaction.begin();
			em.merge(rol);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el rol: " + e.getMessage(), e);
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

	public Rol buscarRolId(Long id) {
		try {
			TypedQuery<Rol> query = em.createQuery("SELECT r FROM Rol r WHERE r.id LIKE :id1", Rol.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el rol: " + e.getMessage(), e);
		}
	}

}// fin clase
