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
import ochoschool.apirest.entidades.ConvocatoriaEvento;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class ConvocatoriaEventoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public ConvocatoriaEventoDAO() {
	}

	public ConvocatoriaEvento crearConvocatoriaEvento(ConvocatoriaEvento ce) {
		try {
			userTransaction.begin();
			ConvocatoriaEvento convocatoria = em.merge(ce);
			em.flush();
			userTransaction.commit();

			return convocatoria;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear la Convocatoria: " + e.getMessage(), e);
		}

	}// crearConvocatoriaEvento

	public List<ConvocatoriaEvento> listarConvocatoriasEvento() throws PersistenceException {
		try {
			String query = "SELECT i FROM ConvocatoriaEvento i ";
			TypedQuery<ConvocatoriaEvento> listadoQuery = em.createQuery(query, ConvocatoriaEvento.class);
			List<ConvocatoriaEvento> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarConvocatoriaEvento(Long id) {
		try {
			ConvocatoriaEvento ce = em.find(ConvocatoriaEvento.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar la Convocatoria: " + e.getMessage(), e);
		}
	}// eliminarConvocatoria

	public ConvocatoriaEvento find(String analista) throws Exception {
		List<ConvocatoriaEvento> lista = listarConvocatoriasEvento();
		for (ConvocatoriaEvento unaConvocatoriaEvento : lista) {
			if (unaConvocatoriaEvento.getAnalista().getUsuario().equals(analista)) {
				return unaConvocatoriaEvento;
			}
		}
		return null;
	}

	public Boolean modificarConvocatoriaEvento(ConvocatoriaEvento convocatoria) {
		try {
			userTransaction.begin();
			em.merge(convocatoria);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar la Convocatoria: " + e.getMessage(), e);
		}
	}

	public ConvocatoriaEvento buscarConvocatoriaEventoId(Long id) {
		try {
			TypedQuery<ConvocatoriaEvento> query = em.createQuery("SELECT i FROM ConvocatoriaEvento i WHERE i.id LIKE :id1", ConvocatoriaEvento.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener la Convocatoria: " + e.getMessage(), e);
		}
	}
	
	
}//fin clase
