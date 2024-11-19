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

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ConvocadoEventoDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public ConvocadoEventoDAO() {
	}

	public ConvocadoEvento crearConvocadoEvento(ConvocadoEvento ce) {
		try {
			userTransaction.begin();
			ConvocadoEvento convocado = em.merge(ce);
			em.flush();
			userTransaction.commit();

			return convocado;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al asignar al Convocado: " + e.getMessage(), e);
		}

	}// crearConvocadoEvento

	public List<ConvocadoEvento> listarConvocadosEvento() throws PersistenceException {
		try {
			String query = "SELECT i FROM ConvocadoEvento i ";
			TypedQuery<ConvocadoEvento> listadoQuery = em.createQuery(query, ConvocadoEvento.class);
			List<ConvocadoEvento> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarConvocadoEvento(Long id) {
		try {
			ConvocadoEvento ce = em.find(ConvocadoEvento.class, id);
			if (ce != null) {
				userTransaction.begin();
				em.remove(em.merge(ce));
				em.flush();
				userTransaction.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar al Convocado: " + e.getMessage(), e);
		}
	}// eliminarConvocado

	public ConvocadoEvento find(String estudiante) throws Exception {
		List<ConvocadoEvento> lista = listarConvocadosEvento();
		for (ConvocadoEvento unConvocadoEvento : lista) {
			if (unConvocadoEvento.getEstudiante().getUsuario().equals(estudiante)) {
				return unConvocadoEvento;
			}
		}
		return null;
	}

	public Boolean modificarConvocadoEvento(ConvocadoEvento convocado) {
		try {
			userTransaction.begin();
			em.merge(convocado);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar al Convocado: " + e.getMessage(), e);
		}
	}

	public ConvocadoEvento buscarConvocadoEventoId(Long id) {
		try {
			TypedQuery<ConvocadoEvento> query = em.createQuery("SELECT i FROM ConvocadoEvento i WHERE i.id LIKE :id1", ConvocadoEvento.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener al Convocado: " + e.getMessage(), e);
		}
	}
	
	public List<ConvocadoEvento> listarAsistenciaEventoEstudiante(long idEvento, long idEstudiante) throws PersistenceException {
		try {
	        String query = "SELECT ce FROM ConvocadoEvento ce " +
	                       "JOIN ce.convocatoriaEvento c " +
	                       "WHERE c.evento.idEvento = :idEvento " +
	                       "AND ce.estudiante.idUsuario = :idUsuario";

	        TypedQuery<ConvocadoEvento> listadoQuery = em.createQuery(query, ConvocadoEvento.class);
	        listadoQuery.setParameter("idEvento", idEvento);
	        listadoQuery.setParameter("idUsuario", idEstudiante);

	        List<ConvocadoEvento> resultados = listadoQuery.getResultList();
	        return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}
	
	public List<ConvocadoEvento> listarAsistenciaEstudiante(long idEstudiante) throws PersistenceException {
	    try {
	        String query = "SELECT ce FROM ConvocadoEvento ce " +
                    "WHERE ce.estudiante.idUsuario = :idUsuario " +
                    "AND ce.estadoAsistencia NOT IN ('Ausencia', 'Ausencia Justificada')";

	        TypedQuery<ConvocadoEvento> listadoQuery = em.createQuery(query, ConvocadoEvento.class);
	        listadoQuery.setParameter("idUsuario", idEstudiante);

	        List<ConvocadoEvento> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}

	public List<ConvocadoEvento> listarAsistenciaEvento(long idEvento) throws PersistenceException {
	    try {
	    	String query = "SELECT ce FROM ConvocadoEvento ce " +
                    "JOIN ce.convocatoriaEvento c " +
                    "WHERE c.evento.idEvento = :idEvento " +
                    "AND ce.estadoAsistencia NOT IN ('Ausencia', 'Ausencia Justificada')";

	        TypedQuery<ConvocadoEvento> listadoQuery = em.createQuery(query, ConvocadoEvento.class);
	        listadoQuery.setParameter("idEvento", idEvento);

	        List<ConvocadoEvento> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}

	public List<ConvocadoEvento> listarConvocatoriasEstudiante(long idEstudiante) throws PersistenceException {
	    try {
	        String query = "SELECT ce FROM ConvocadoEvento ce " +
	                       "WHERE ce.estudiante.idUsuario = :idUsuario";

	        TypedQuery<ConvocadoEvento> listadoQuery = em.createQuery(query, ConvocadoEvento.class);
	        listadoQuery.setParameter("idUsuario", idEstudiante);

	        List<ConvocadoEvento> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}
	
	public List<ConvocadoEvento> listarConvocatoriasEvento(long idEvento) throws PersistenceException {
	    try {
	        String query = "SELECT ce FROM ConvocadoEvento ce " +
	                       "JOIN ce.convocatoriaEvento c " +
	                       "WHERE c.evento.idEvento = :idEvento";

	        TypedQuery<ConvocadoEvento> listadoQuery = em.createQuery(query, ConvocadoEvento.class);
	        listadoQuery.setParameter("idEvento", idEvento);

	        List<ConvocadoEvento> resultados = listadoQuery.getResultList();
	        return resultados;
	    } catch (PersistenceException e) {
	        throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
	    }
	}


	
}//fin clase
