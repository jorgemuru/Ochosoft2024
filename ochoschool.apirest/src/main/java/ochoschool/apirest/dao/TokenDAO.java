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
import ochoschool.apirest.entidades.Token;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class TokenDAO {
	@PersistenceContext
	private EntityManager em;
	@Resource
	private UserTransaction userTransaction;

	public TokenDAO() {
	}

	public Token crearToken(Token t) {
		try {
			userTransaction.begin();
			Token token = em.merge(t);
			em.flush();
			userTransaction.commit();

			return token;

		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al crear el Token: " + e.getMessage(), e);
		}

	}// crearToken

	public List<Token> listarTokens() throws PersistenceException {
		try {
			String query = "SELECT i FROM Token i ";
			TypedQuery<Token> listadoQuery = em.createQuery(query, Token.class);
			List<Token> resultados = listadoQuery.getResultList();
			return resultados;
		} catch (PersistenceException e) {
			throw new PersistenceException("No se pudo hacer la consulta." + e.getMessage(), e);
		}
	}

	public Boolean eliminarToken(Long id) {
		try {
			Token t = em.find(Token.class, id);
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
			throw new PersistenceException("Ocurrió un error al eliminar el Token: " + e.getMessage(), e);
		}
	}// eliminarToken

	public Token find(String valor) throws Exception {
		List<Token> lista = listarTokens();
		for (Token unToken : lista) {
			if (unToken.getValor().equals(valor)) {
				return unToken;
			}
		}
		return null;
	}

	public Boolean modificarToken(Token token) {
		try {
			userTransaction.begin();
			em.merge(token);
			em.flush();
			userTransaction.commit();
			return true;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al modificar el Token: " + e.getMessage(), e);
		}
	}

	public Token buscarTokenId(Long id) {
		try {
			TypedQuery<Token> query = em
					.createQuery("SELECT i FROM Tipo i WHERE i.id LIKE :id1", Token.class)
					.setParameter("id1", id);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		} catch (Exception e) {
			throw new PersistenceException("Ocurrió un error al obtener el Token: " + e.getMessage(), e);
		}
	}
	
	
}//fin clase
