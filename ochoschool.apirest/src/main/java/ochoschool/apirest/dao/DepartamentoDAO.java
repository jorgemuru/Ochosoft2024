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
import ochoschool.apirest.entidades.Departamento;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class DepartamentoDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public DepartamentoDAO() {
	}

	public Departamento crearDepartamento(Departamento d) {
		try {
			userTransaction.begin();
			Departamento departamento = em.merge(d);
			em.flush();
			userTransaction.commit();

			return departamento;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Departamento: " + e.getMessage(), e);
		}

	}// crearDepartamento

	public List<Departamento> listarDepartamentos() throws PersistenceException {
		try {
			String query = "SELECT d FROM Departamento d ";
			TypedQuery<Departamento> listadoQuery = em.createQuery(query, Departamento.class);
			List<Departamento> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarDepartamento(Long id) {
		try {
			Departamento d = em.find(Departamento.class, id);
			if (d != null) {
				userTransaction.begin();
				em.remove(em.merge(d));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el Departamento: " + e.getMessage(), e);
		}
	}// eliminarDepartamento

	public Departamento find(String nombre) throws Exception {
		List<Departamento> lista = listarDepartamentos();
		for (Departamento unDepa : lista) {
			if (unDepa.getNombre().equals(nombre)) {
				return unDepa;
			}
		}
		return null;
	}

	public Boolean modificarDepartamento(Departamento departamento) {
		try {
			userTransaction.begin();
			em.merge(departamento);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Departamento: " + e.getMessage(), e);
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

	public Departamento buscarDepartamentoId(Long id) {
		try {
			TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d WHERE d.id LIKE :id1", Departamento.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Departamento: " + e.getMessage(), e);
		}
	}

}//fin clase
