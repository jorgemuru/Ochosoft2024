package ochoschool.website.beans;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.google.gson.Gson;

import ochoschool.website.entidades.Estudiante;

@Named("estudianteBean")
@SessionScoped
public class EstudianteBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idUsuario;
    private String generacion;
    private Estudiante estudianteNuevo;
    private String BASE_URL = "http://localhost:8080/ochoschool.apirest/estudiante";

    // Constructor
    public EstudianteBean() {
        estudianteNuevo = new Estudiante(); // Inicializar el estudiante nuevo
    }

    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    // Método para crear un estudiante
    public String crearEstudiante() {
    	
    	 // Imprimir detalles de los datos recibidos
	    System.out.println("IDUSUARIO: " + (idUsuario != null ? idUsuario : "null"));
	    System.out.println("Generación: " + generacion);
	    
        if (idUsuario == null || generacion == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ID de usuario y año de ingreso son obligatorios."));
            return null;
        }

        // Setear los valores de estudianteNuevo
        estudianteNuevo.setIdUsuario(idUsuario);
        estudianteNuevo.setGeneracion(generacion);

        Gson gson = new Gson();
        String eventoJson = gson.toJson(estudianteNuevo);
        System.out.println("json evento: " + eventoJson);

        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream())) {
                out.write(eventoJson);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Estudiante creado exitosamente", ""));
                return "pages/principal.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear el evento. Código de respuesta: " + responseCode));
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al intentar crear el evento: " + e.getMessage()));
            return null;
        }
    }
    
    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("idUsuario")) {
            idUsuario = Long.parseLong(params.get("idUsuario"));
        }
    }
}
