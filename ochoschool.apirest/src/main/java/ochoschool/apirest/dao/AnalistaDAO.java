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
import ochoschool.apirest.entidades.Analista;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class AnalistaDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public AnalistaDAO() {
	}

	public Analista crearAnalista(Analista a) {
		try {
			userTransaction.begin();
			Analista analista = em.merge(a);
			em.flush();
			userTransaction.commit();

			return analista;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el analista: " + e.getMessage(), e);
		}

	}// crearAnalista

	public List<Analista> listarAnalistas(String nombre, String apellido, String usuario, Long rol)
			throws PersistenceException {
		try {
			String query = "SELECT a FROM Analista a ";
			String queryCriterio = "";
			if (nombre != null && !nombre.contentEquals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(a.primer_nombre) like '%"
						+ nombre.toLowerCase() + "%' ";
			}
			if (apellido != null && !apellido.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(a.primer_apellido) like '%"
						+ apellido.toLowerCase() + "%' ";
			}
			if (usuario != null && !usuario.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(a.usuario) like '%"
						+ usuario.toLowerCase() + "%' ";
			}
			if (!queryCriterio.contentEquals("")) {
				query += " where " + queryCriterio;
			}

			TypedQuery<Analista> listadoQuery = em.createQuery(query, Analista.class);
			List<Analista> resultados = listadoQuery.getResultList();
			return resultados;

		} catch (PersistenceException e) {
			throw new PersistenceException("No es posible realizar la consulta." + e.getMessage(), e);
		}
	}

	public Boolean deshabilitarAnalista(Long id) {
		try {
			Analista a = buscarAnalistaId(id);
			userTransaction.begin();
			a.setActivo('N');
			em.merge(a);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al deshabilitar al analista: " + e.getMessage(), e);
		}
	}//deshabilitarAnalista
	
	public Boolean eliminarAnalista(Long id) {
		try {
			Analista a = em.find(Analista.class, id);
			if (a != null) {
				userTransaction.begin();
				em.remove(em.merge(a));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Analista: " + e.getMessage(), e);
		}
	
	}//eliminarAnalista

	public Analista find(String nombreU) throws Exception {
		List<Analista> listaAnalistas = listarAnalistas("", "", "", 0L);
		for (Analista analista : listaAnalistas) {
			if (analista.getUsuario().equals(nombreU)) {
				return analista;
			}
		}
		return null;

	}

	public Boolean modificarAnalista(Analista analis) {
		try {
			userTransaction.begin();
			em.merge(analis);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar al analista: " + e.getMessage(), e);
		}
	}

	public Analista buscarAnalistaNombre(String nombreU) {
		try {
			TypedQuery<Analista> query = em
					.createQuery("SELECT a FROM Analista a WHERE a.usuario LIKE :nombre", Analista.class)
					.setParameter("nombre", nombreU);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al analista: " + e.getMessage(), e);
		}
	}

	public Analista buscarAnalistaId(Long id) {
		try {
			TypedQuery<Analista> query = em.createQuery("SELECT a FROM Analista a WHERE a.id LIKE :id1", Analista.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al analista: " + e.getMessage(), e);
		}
	}

}//fin clase
