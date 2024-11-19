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
import ochoschool.apirest.entidades.Usuario;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class UsuarioDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public UsuarioDAO() {
	}

	public Usuario crearUsuario(Usuario u) {
		try {
			userTransaction.begin();
			Usuario usuario = em.merge(u);
			em.flush();
			userTransaction.commit();

			return usuario;
			
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el usuario: " + e.getMessage(), e);
		}

	}//crearUsuario

	public List<Usuario> listarUsuarios(String nombre, String apellido, String usuario, Long rol)
			throws PersistenceException {
		try {
			String query = "SELECT u FROM Usuario u ";
			String queryCriterio = "";
			if (nombre != null && !nombre.contentEquals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(u.primer_nombre) like '%"
						+ nombre.toLowerCase() + "%' ";
			}
			if (apellido != null && !apellido.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(u.primer_apellido) like '%"
						+ apellido.toLowerCase() + "%' ";
			}
			if (usuario != null && !usuario.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(u.usuario) like '%"
						+ usuario.toLowerCase() + "%' ";
			}
			if (!queryCriterio.contentEquals("")) {
				query += " where " + queryCriterio;
			}
			TypedQuery<Usuario> listadoQuery = em.createQuery(query, Usuario.class);
			List<Usuario> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean deshabilitarUsuario(Long id) {
		try {
			Usuario u = buscarUsuarioId(id);
			userTransaction.begin();
			u.setActivo('N');
			em.merge(u);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al deshabilitar al usuario: " + e.getMessage(), e);
		}
	}//deshabilitarUsuario
	
	public Boolean eliminarUsuario(Long id) {
		try {
			Usuario u = em.find(Usuario.class, id);
			if (u != null) {
				userTransaction.begin();
				em.remove(em.merge(u));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Usuario: " + e.getMessage(), e);
		}

	}//eliminarUsuario

	public Usuario find(String nombreU) throws Exception {
		List<Usuario> listaUsuarios = listarUsuarios("", "", "", 0L);
		for (Usuario usuario : listaUsuarios) {
			if (usuario.getUsuario().equals(nombreU)) {
				return usuario;
			}
		}
		return null;
	}

	public Boolean modificarUsuario(Usuario usu) {
		try {
			userTransaction.begin();
			em.merge(usu);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el usuario: " + e.getMessage(), e);
		}
	}

	public Usuario buscarUsuarioNombre(String nombreU) {
		try {
			TypedQuery<Usuario> query = em
					.createQuery("SELECT u FROM Usuario u WHERE u.usuario LIKE :nombre", Usuario.class)
					.setParameter("nombre", nombreU);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el usuario: " + e.getMessage(), e);
		}
	}

	public Usuario buscarUsuarioId(Long id) {
		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.id LIKE :id1", Usuario.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el usuario: " + e.getMessage(), e);
		}
	}
	

/*
	public RolUsuario obtenerRolPorId(String id) {
		try {
			TypedQuery<RolUsuario> query = em
					.createQuery("SELECT r FROM RolUsuario r WHERE r.rol LIKE :id1", RolUsuario.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el rol: " + e.getMessage(), e);
		}
	}
*/

}//fin clase
