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
import ochoschool.apirest.entidades.Tutor;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class TutorDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public TutorDAO() {
	}

	public Tutor crearTutor(Tutor t) {
		try {
			userTransaction.begin();
			Tutor tutor = em.merge(t);
			em.flush();
			userTransaction.commit();

			return tutor;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el tutor: " + e.getMessage(), e);
		}

	}// crearTutor

	public List<Tutor> listarTutores(String nombre, String apellido, String usuario, Long rol)
			throws PersistenceException {
		try {
			String query = "SELECT t FROM Tutor t ";
			String queryCriterio = "";
			if (nombre != null && !nombre.contentEquals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(t.primer_nombre) like '%"
						+ nombre.toLowerCase() + "%' ";
			}
			if (apellido != null && !apellido.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(t.primer_apellido) like '%"
						+ apellido.toLowerCase() + "%' ";
			}
			if (usuario != null && !usuario.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(t.usuario) like '%"
						+ usuario.toLowerCase() + "%' ";
			}
			if (!queryCriterio.contentEquals("")) {
				query += " where " + queryCriterio;
			}

			TypedQuery<Tutor> listadoQuery = em.createQuery(query, Tutor.class);
			List<Tutor> resultados = listadoQuery.getResultList();
			return resultados;

		} catch (PersistenceException e) {
			throw new PersistenceException("No es posible realizar la consulta." + e.getMessage(), e);
		}
	}

	public Boolean deshabilitarTutor(Long id) {
		try {
			Tutor t = buscarTutorId(id);
			userTransaction.begin();
			t.setActivo('N');
			em.merge(t);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al deshabilitar al tutor: " + e.getMessage(), e);
		}
	}//deshabilitarTutor
	
	public Boolean eliminarTutor(Long id) {
		try {
			Tutor t = em.find(Tutor.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar al Tutor: " + e.getMessage(), e);
		}
		
	}//eliminarTutor

	public Tutor find(String nombreU) throws Exception {
		List<Tutor> listaTutores = listarTutores("", "", "", 0L);
		for (Tutor tutor : listaTutores) {
			if (tutor.getUsuario().equals(nombreU)) {
				return tutor;
			}
		}
		return null;

	}

	public Boolean modificarTutor(Tutor tut) {
		try {
			userTransaction.begin();
			em.merge(tut);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar al tutor: " + e.getMessage(), e);
		}
	}

	public Tutor buscarTutorNombre(String nombreU) {
		try {
			TypedQuery<Tutor> query = em
					.createQuery("SELECT t FROM Tutor t WHERE t.usuario LIKE :nombre", Tutor.class)
					.setParameter("nombre", nombreU);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al tutor: " + e.getMessage(), e);
		}
	}

	public Tutor buscarTutorId(Long id) {
		try {
			TypedQuery<Tutor> query = em.createQuery("SELECT t FROM Tutor t WHERE t.id LIKE :id1", Tutor.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al tutor: " + e.getMessage(), e);
		}
	}
	
}//fin clase
