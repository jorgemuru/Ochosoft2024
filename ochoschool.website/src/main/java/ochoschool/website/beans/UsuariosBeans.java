package ochoschool.website.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.fasterxml.jackson.databind.JsonNode;

import ochoschool.website.entidades.ApiClient;

@ManagedBean
@SessionScoped
public class UsuariosBeans implements Serializable{

	private JsonNode datos;

	public String cargarDatos() {
		ApiClient apiClient = new ApiClient();

		try {
			datos = apiClient.getDatosUsuarios();
			// Procesa los datos según tus necesidades
		} catch (Exception e) {
			e.printStackTrace();
			// Maneja errores apropiadamente
		}
		return "Usuarios.xhtml"; // Página de resultados en JSF
	}

	public JsonNode getDatos() {
		return datos;
	}

	@PostConstruct
	public void init() {
		cargarDatos();
	}
}
