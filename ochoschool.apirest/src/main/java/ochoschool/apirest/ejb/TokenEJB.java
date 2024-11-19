package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.TokenDAO;
import ochoschool.apirest.entidades.Token;

@Stateless
public class TokenEJB {
	@Inject
	private TokenDAO tokenDAO;

    public List<Token> obtenerTodosLosTokens() {
        return tokenDAO.listarTokens();
    }

	public Token crearToken(Token token) {
		return tokenDAO.crearToken(token);
	}

	public Token obtenerTokenPorId(Long id) {
		return tokenDAO.buscarTokenId(id);
	}

	public Token actualizarToken(Token token) {
		if(tokenDAO.modificarToken(token)) {
			return token;
		}else{
			return null;
		}
	}

	public Boolean eliminarToken(Long id) {
		return tokenDAO.eliminarToken(id);
	}

}//fin clase
