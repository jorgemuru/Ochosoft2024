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
import ochoschool.apirest.entidades.Estudiante;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class EstudianteDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public EstudianteDAO() {
	}

	public Estudiante crearEstudiante(Estudiante es) {
		try {
			userTransaction.begin();
			Estudiante estudiante = em.merge(es);
			em.flush();
			userTransaction.commit();

			return estudiante;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el estudiante: " + e.getMessage(), e);
		}

	}// crearAnalista

	public List<Estudiante> listarEstudiantes(String nombre, String apellido, String usuario, Long rol)
			throws PersistenceException {
		try {
			String query = "SELECT e FROM Estudiante e ";
			String queryCriterio = "";
			if (nombre != null && !nombre.contentEquals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(e.primer_nombre) like '%"
						+ nombre.toLowerCase() + "%' ";
			}
			if (apellido != null && !apellido.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(e.primer_apellido) like '%"
						+ apellido.toLowerCase() + "%' ";
			}
			if (usuario != null && !usuario.equals("")) {
				queryCriterio += (!queryCriterio.isEmpty() ? " and " : "") + " lower(e.usuario) like '%"
						+ usuario.toLowerCase() + "%' ";
			}
			if (!queryCriterio.contentEquals("")) {
				query += " where " + queryCriterio;
			}

			TypedQuery<Estudiante> listadoQuery = em.createQuery(query, Estudiante.class);
			List<Estudiante> resultados = listadoQuery.getResultList();
			return resultados;

		} catch (PersistenceException e) {
			throw new PersistenceException("No es posible realizar la consulta." + e.getMessage(), e);
		}
	}

	public Boolean deshabilitarEstudiante(Long id) {
		try {
			Estudiante es = buscarEstudianteId(id);
			userTransaction.begin();
			es.setActivo('N');
			em.merge(es);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al deshabilitar al estudiante: " + e.getMessage(), e);
		}
	}//deshabilitarEstudiante
	
	public Boolean eliminarEstudiante(Long id) {
		try {
			Estudiante es = em.find(Estudiante.class, id);
			if (es != null) {
				userTransaction.begin();
				em.remove(em.merge(es));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Estudiante: " + e.getMessage(), e);
		}
		
	}//eliminarEstudiante

	public Estudiante find(String nombreU) throws Exception {
		List<Estudiante> listaEstudiantes = listarEstudiantes("", "", "", 0L);
		for (Estudiante estudiante : listaEstudiantes) {
			if (estudiante.getUsuario().equals(nombreU)) {
				return estudiante;
			}
		}
		return null;

	}

	public Boolean modificarEstudiante(Estudiante estu) {
		try {
			userTransaction.begin();
			em.merge(estu);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar al estudiante: " + e.getMessage(), e);
		}
	}

	public Estudiante buscarEstudianteNombre(String nombreU) {
		try {
			TypedQuery<Estudiante> query = em
					.createQuery("SELECT e FROM Estudiante e WHERE e.usuario LIKE :nombre", Estudiante.class)
					.setParameter("nombre", nombreU);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al estudiante: " + e.getMessage(), e);
		}
	}

	public Estudiante buscarEstudianteId(Long id) {
		try {
			TypedQuery<Estudiante> query = em.createQuery("SELECT e FROM Estudiante e WHERE e.id LIKE :id1", Estudiante.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al estudiante: " + e.getMessage(), e);
		}
	}
	
}//fin clase
