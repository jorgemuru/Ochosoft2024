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
import ochoschool.apirest.entidades.Evento;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class EventoDAO {

	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public EventoDAO() {
	}

	public Evento crearEvento(Evento ev ) {
		try {
			userTransaction.begin();
			Evento evento = em.merge(ev);
			em.flush();
			userTransaction.commit();

			return evento;
			
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el evento: " + e.getMessage(), e);
		}

	}//crearUsuario

	public List<Evento> listarEventos()
			throws PersistenceException {
		try {
			String query = "SELECT k FROM Evento k";
			TypedQuery<Evento> listadoQuery = em.createQuery(query, Evento.class);
			List<Evento> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}
	
	public Boolean eliminarEvento(Long id) {
		try {
			Evento ev = buscarEventoId(id);
			userTransaction.begin();
			em.remove(ev);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al eliminar el evento: " + e.getMessage(), e);
		}
	}//eliminarUsuario

	public Evento find(String titulo) throws Exception {
		List<Evento> listaEventos = listarEventos();
		for (Evento unEven : listaEventos) {
			if (unEven.getTitulo().equals(titulo)) {
				return unEven;
			}
		}
		return null;
	}

	public Boolean modificarEvento(Evento even) {
		try {
			userTransaction.begin();
			em.merge(even);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el evento: " + e.getMessage(), e);
		}
	}

	public Evento buscarEventoModalidad(String modo) {
		try {
			TypedQuery<Evento> query = em
					.createQuery("SELECT k FROM Evento k WHERE k.modalidad LIKE :modalidad", Evento.class)
					.setParameter("modalidad", modo);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el evento: " + e.getMessage(), e);
		}
	}

	public Evento buscarEventoId(Long id) {
		try {
			TypedQuery<Evento> query = em.createQuery("SELECT k FROM Evento k WHERE k.id LIKE :id1", Evento.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el evento: " + e.getMessage(), e);
		}
	}
	
	
}//fin clase
