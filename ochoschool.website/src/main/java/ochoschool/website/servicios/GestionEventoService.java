package ochoschool.website.servicios;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
@Stateless
@LocalBean
public class GestionEventoService implements Serializable {

	private static final long serialVersionUID = 1L;
	private String BASE_URL = "http://localhost:8080/ochoschool.apirest/";
	
	public String getBASE_URL() {
		return BASE_URL;
	}
	public void setBASE_URL(String url) {
		BASE_URL = url;
	}
	
	public GestionEventoService() {
		super();
	}
	
	
}
