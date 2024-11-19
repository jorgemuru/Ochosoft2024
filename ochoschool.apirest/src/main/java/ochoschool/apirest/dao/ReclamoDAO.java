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

import ochoschool.apirest.entidades.ConvocadoEvento;
import ochoschool.apirest.entidades.Reclamo;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ReclamoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public ReclamoDAO() {
	}

	public Reclamo crearReclamo(Reclamo r) {
		try {
			userTransaction.begin();
			Reclamo reclamo = em.merge(r);
			em.flush();
			userTransaction.commit();

			return reclamo;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el reclamo: " + e.getMessage(), e);
		}

	}// crearReclamo

	public List<Reclamo> listarReclamos() throws PersistenceException {
		try {
			String query = "SELECT r FROM Reclamo r";
			TypedQuery<Reclamo> listadoQuery = em.createQuery(query, Reclamo.class);
			List<Reclamo> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarReclamo(Long id) {
		try {
			Reclamo r = em.find(Reclamo.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar el Reclamo: " + e.getMessage(), e);
		}

	}// eliminarReclamo

	public Reclamo find(String descripcion) throws Exception {
		List<Reclamo> listaReclamos = listarReclamos();
		for (Reclamo unRecla : listaReclamos) {
			if (unRecla.getDescripcion().equals(descripcion)) {
				return unRecla;
			}
		}
		return null;
	}

	public Boolean modificarReclamo(Reclamo reclamo) {
		try {
			userTransaction.begin();
			em.merge(reclamo);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el reclamo: " + e.getMessage(), e);
		}
	}

	public Reclamo buscarReclamoId(Long id) {
		try {
			TypedQuery<Reclamo> query = em.createQuery("SELECT r FROM Reclamo r WHERE r.id LIKE :id1", Reclamo.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el reclamo: " + e.getMessage(), e);
		}
	}

	public List<Reclamo> obtenerReclamosXItr(long idItr) throws PersistenceException {
	    try {
	    	String query = "SELECT r FROM Reclamo r " +
                    "WHERE r.evento.itr.idItr = :idItr ";

	        TypedQuery<Reclamo> listadoQuery = em.createQuery(query, Reclamo.class);
	        listadoQuery.setParameter("idItr", idItr);

	        List<Reclamo> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}
	public List<Reclamo> obtenerReclamosXEvento(long idEvento) throws PersistenceException {
	    try {
	    	String query = "SELECT r FROM Reclamo r " +
                    "WHERE r.evento.idEvento = :idEvento ";

	        TypedQuery<Reclamo> listadoQuery = em.createQuery(query, Reclamo.class);
	        listadoQuery.setParameter("idEvento", idEvento);

	        List<Reclamo> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}
	public List<Reclamo> obtenerReclamosXGeneracion(String generacion) throws PersistenceException {
	    try {
	    	String query = "SELECT r FROM Reclamo r " +
                    "WHERE r.estudiante.generacion = :generacion ";

	        TypedQuery<Reclamo> listadoQuery = em.createQuery(query, Reclamo.class);
	        listadoQuery.setParameter("generacion", generacion);

	        List<Reclamo> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}
	
	public List<Reclamo> obtenerReclamosXAnio(int anio) throws PersistenceException {
	    try {
	        String query = "SELECT r FROM Reclamo r " +
	                "WHERE YEAR(r.fechaHora) = :anio";

	        TypedQuery<Reclamo> listadoQuery = em.createQuery(query, Reclamo.class);
	        listadoQuery.setParameter("anio", anio);

	        List<Reclamo> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}
	public List<Reclamo> obtenerReclamosXTipo(String tipo) throws PersistenceException {
	    try {
	        String query = "SELECT r FROM Reclamo r " +
	                "WHERE r.tipoReclamo.descripcion = :tipo";

	        TypedQuery<Reclamo> listadoQuery = em.createQuery(query, Reclamo.class);
	        listadoQuery.setParameter("tipo", tipo);

	        List<Reclamo> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}
	
}// fin clase
