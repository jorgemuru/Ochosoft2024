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
import ochoschool.apirest.entidades.Modalidad;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ModalidadDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public ModalidadDAO() {
	}

	public Modalidad crearModalidad(Modalidad m) {
		try {
			userTransaction.begin();
			Modalidad modalidad = em.merge(m);
			em.flush();
			userTransaction.commit();

			return modalidad;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Modalidad: " + e.getMessage(), e);
		}

	}// crearModalidad

	public List<Modalidad> listarModalidades() throws PersistenceException {
		try {
			String query = "SELECT i FROM Modalidad i ";
			TypedQuery<Modalidad> listadoQuery = em.createQuery(query, Modalidad.class);
			List<Modalidad> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarModalidad(Long id) {
		try {
			Modalidad m = em.find(Modalidad.class, id);
			if (m != null) {
				userTransaction.begin();
				em.remove(em.merge(m));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar la Modalidad: " + e.getMessage(), e);
		}
	}// eliminarModalidad

	public Modalidad find(String descripcion) throws Exception {
		List<Modalidad> lista = listarModalidades();
		for (Modalidad unaModalidad : lista) {
			if (unaModalidad.getDescripcion().equals(descripcion)) {
				return unaModalidad;
			}
		}
		return null;
	}

	public Boolean modificarModalidad(Modalidad modalidad) {
		try {
			userTransaction.begin();
			em.merge(modalidad);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Modalidad: " + e.getMessage(), e);
		}
	}

	public Modalidad buscarModalidadId(Long id) {
		try {
			TypedQuery<Modalidad> query = em.createQuery("SELECT i FROM Modalidad i WHERE i.id LIKE :id1", Modalidad.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener la Modalidad: " + e.getMessage(), e);
		}
	}
	
}//fin clase
