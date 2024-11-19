package ochoschool.website.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ochoschool.website.entidades.ApiClient;
import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.EstadoEvento;
import ochoschool.website.entidades.Evento;
import ochoschool.website.entidades.Itr;
import ochoschool.website.entidades.Modalidad;
import ochoschool.website.entidades.TipoEvento;
import ochoschool.website.entidades.Tutor;

@Named("eventoBean")
@SessionScoped
public class EventoBean implements Serializable {

	private String titulo;
	private TipoEvento tipoEvento;
	private Long  horaInicio;
	private Long  horaFin;
	private Modalidad modalidad;
	private Itr itr;
	private Tutor tutor;
	private String locacion;
	private Evento eventoNuevo;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private Itr itrSeleccionado;
	// LISTAS
	private List<Tutor> listaTutores;
	private List<Tutor> tutoresSeleccionados;
	private List<Itr> listaItrs;
	private List<Modalidad> listaModalidades;
	private List<TipoEvento> listaTiposEvento;
	private Long idEventoSeleccionado;

	public Long getIdEventoSeleccionado() {
		return idEventoSeleccionado;
	}

	public void setIdEventoSeleccionado(Long idEventoSeleccionado) {
		this.idEventoSeleccionado = idEventoSeleccionado;
	}


	private static final long serialVersionUID = 1L;

	
	private String BASE_URL = "http://localhost:8080/ochoschool.apirest/evento";

	public EventoBean() {
		super();
	}

	@PostConstruct
	public void init() {
		eventoNuevo = new Evento();
		listaTutores = obtenerTutores();
		listaItrs = obtenerItrs();
		listaModalidades = obtenerModalidades();
		listaTiposEvento = obtenerTiposEvento();;
		tutoresSeleccionados = new ArrayList<>();
	}

	public List<Tutor> getListaTutores() {
		return listaTutores;
	}

	// Método altaEvento modificado
	public String altaEvento() {
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Método altaEvento() llamado", ""));
	    
	    // Imprimir detalles de los datos recibidos
	    System.out.println("Título: " + (titulo != null ? titulo : "null"));
	    System.out.println("Tipo de evento: " + (tipoEvento != null ? tipoEvento.getDescripcion() : "null"));
	    System.out.println("Modalidad: " + (modalidad != null ? modalidad.getDescripcion() : "null"));
	    System.out.println("ITR: " + (itrSeleccionado != null ? itrSeleccionado.getNombre() : "null"));
	    System.out.println("Hora de Inicio: " + (horaInicio != null ? horaInicio.toString() : "null"));
	    System.out.println("Hora de Fin: " + (horaFin != null ? horaFin.toString() : "null"));

	    // Validación de campos requeridos con mensajes específicos
	    if (titulo == null) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "El título es obligatorio"));
	        return null;
	    }
	    if (tipoEvento == null) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "El tipo de evento es obligatorio"));
	        return null;
	    }
	    if (horaInicio == null) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "La hora de inicio es obligatoria"));
	        return null;
	    }
	    if (horaFin == null) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "La hora de fin es obligatoria"));
	        return null;
	    }
	    if (modalidad == null) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "La modalidad es obligatoria"));
	        return null;
	    }
	    if (itrSeleccionado == null) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "El ITR seleccionado es obligatorio"));
	        return null;
	    }
	    if (tutoresSeleccionados == null || tutoresSeleccionados.isEmpty()) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Faltan datos requeridos", "Los tutores seleccionados son obligatorios"));
	        return null;
	    }

	    // Setear valores en el objeto eventoNuevo
	    eventoNuevo.setTitulo(titulo);
	    eventoNuevo.setTipoEvento(tipoEvento);
	 // Conversión de fechas
	    String fechaHoraStr = "22/06/2024 10:00";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);
	    long timestamp = fechaHora.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

	   eventoNuevo.setFechaHoraInicio(horaInicio);
	   eventoNuevo.setFechaHoraFin(horaFin);

	    eventoNuevo.setModalidad(modalidad);
	    eventoNuevo.setItr(itrSeleccionado);
	    eventoNuevo.setLocacion(locacion);
	    eventoNuevo.setTutores(tutoresSeleccionados);

	    // Establecer el estado del evento
	    EstadoEvento estadoEvento = new EstadoEvento();
	    estadoEvento.setIdEstadoEvento((long) 2); // Asumiendo el ID del estado es 2 para "CORRIENTE"
	    System.out.println("id estado evento: "+ estadoEvento.getIdEstadoEvento());
	    estadoEvento.setDescripcion("CORRIENTE");
	    eventoNuevo.setEstadoEvento(estadoEvento);
	    System.out.println("estado evento: " + estadoEvento);
	    // Establecer campos activos y habilitados
	    eventoNuevo.setActivo('S');
	    eventoNuevo.setHabilitado('S');

	    Gson gson = new Gson();
	    String eventoJson = gson.toJson(eventoNuevo);
	    System.out.println("json evento: "+ eventoJson);

	    try {
	        String apiUrl = BASE_URL + "evento";
	        URL url = new URL(apiUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setDoOutput(true);

	        try (OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream())) {
	            out.write(eventoJson);
	        }

	        int responseCode = con.getResponseCode();

	        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento creado exitosamente", ""));
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


	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Long  getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Long  horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Long  getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Long  horaFin) {
		this.horaFin = horaFin;
	}

	public Modalidad getModalidad() {
		return modalidad;
	}

	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
	}

	public Itr getItr() {
		return itr;
	}

	public void setItr(Itr itr) {
		this.itr = itr;
	}

	public void setListaTutores(List<Tutor> listaTutores) {
		this.listaTutores = listaTutores;
	}

	public String getLocacion() {
		return locacion;
	}

	public void setLocacion(String locacion) {
		this.locacion = locacion;
	}

	public Evento getEventoNuevo() {
		return eventoNuevo;
	}

	public void setEventoNuevo(Evento eventoNuevo) {
		this.eventoNuevo = eventoNuevo;
	}

	private boolean campoVacio(String valor) {
		return valor == null || valor.trim().isEmpty();
	}

	public Itr getItrSeleccionado() {
		return itrSeleccionado;
	}

	public void setItrSeleccionado(Itr itrSeleccionado) {
		this.itrSeleccionado = itrSeleccionado;
	}

	public List<Tutor> getTutoresSeleccionados() {
		return tutoresSeleccionados;
	}

	public void setTutoresSeleccionados(List<Tutor> tutoresSeleccionados) {
		this.tutoresSeleccionados = tutoresSeleccionados;
	}

	public List<Tutor> obtenerTutores() {
		String endpointUrl = "http://localhost:8080/ochoschool.apirest/tutor/";
		try {
			// Realiza la conexión HTTP para obtener la lista de tutores
			URL url = new URL(endpointUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// Lee la respuesta de la API REST
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Procesa la respuesta JSON y convierte en una lista de tutores
			Gson gson = new Gson();
			TypeToken<List<Tutor>> token = new TypeToken<List<Tutor>>() {
			};
			return gson.fromJson(response.toString(), token.getType());

		} catch (Exception e) {
			e.printStackTrace();
			// Maneja el error según tus necesidades
			return null;
		}
	}

	public Tutor getTutor() {
		return tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}
	
	// Métodos para obtener listas de opciones
    public List<Itr> obtenerItrs() {
    	String fullUrl = BASE_URL + "mantenimiento/itr";
        System.out.println("Obteniendo ITRs desde: " + fullUrl);
        return obtenerLista(fullUrl, Itr.class);
    }

    public List<Modalidad> obtenerModalidades() {
    	 String fullUrl = BASE_URL + "evento/modalidad";
    	    System.out.println("Obteniendo Modalidades desde: " + fullUrl);
    	    return obtenerLista(fullUrl, Modalidad.class);
    }

    public List<TipoEvento> obtenerTiposEvento() {
    	 String fullUrl = BASE_URL + "evento/tipo";
 	    System.out.println("Obteniendo Modalidades desde: " + fullUrl);
 	    return obtenerLista(fullUrl, TipoEvento.class);
 }

    private <T> List<T> obtenerLista(String endpointUrl, Class<T> clazz) {
        System.out.println("Iniciando obtenerLista para: " + endpointUrl);
        
        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                java.lang.reflect.Type listType = TypeToken.getParameterized(List.class, clazz).getType();
                List<T> result = gson.fromJson(response.toString(), listType);
                
                System.out.println("Respuesta recibida y parseada. Tamaño de la lista: " + (result != null ? result.size() : "null"));
                return result;
            } else {
                System.err.println("La solicitud GET no fue exitosa. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la lista desde " + endpointUrl + ": " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

	public List<Itr> getListaItrs() {
		return listaItrs;
	}

	public void setListaItrs(List<Itr> listaItrs) {
		this.listaItrs = listaItrs;
	}

	public List<Modalidad> getListaModalidades() {
		return listaModalidades;
	}

	public void setListaModalidades(List<Modalidad> listaModalidades) {
		this.listaModalidades = listaModalidades;
	}

	public List<TipoEvento> getListaTiposEvento() {
		return listaTiposEvento;
	}

	public void setListaTiposEvento(List<TipoEvento> listaTiposEvento) {
		this.listaTiposEvento = listaTiposEvento;
	}
	
	
	public void desactivarEvento(int eventoId) {
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    try {
	        // Obtener el evento usando el ID a través del endpoint REST
	        URL url = new URL("http://localhost:8080/ochoschool.apirest/evento/" + eventoId);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        // Leer la respuesta y convertirla a un objeto Evento
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine;
	        StringBuilder content = new StringBuilder();
	        while ((inputLine = in.readLine()) != null) {
	            content.append(inputLine);
	        }

	        // Cerrar las conexiones
	        in.close();
	        conn.disconnect();

	        // Convertir la respuesta JSON a un objeto Evento
	        Gson gson = new Gson();
	        Evento evento = gson.fromJson(content.toString(), Evento.class);

	        // Modificar el campo 'activo' del evento
	        evento.setActivo('N');

	        // Convertir el objeto Evento modificado de nuevo a JSON
	        String eventoJson = gson.toJson(evento);

	        // Llamar al método putRequest para enviar el evento actualizado
	        String putUrl = "http://localhost:8080/ochoschool.apirest/evento";
	        boolean success = putRequest(putUrl, eventoJson);

	        // Mostrar mensaje de éxito si la actualización fue exitosa
	        if (success) {
	            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "El evento ha sido desactivado exitosamente."));
	        } else {
	            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo desactivar el evento."));
	        }

	    } catch (Exception e) {
	        // Mostrar mensaje de error si ocurre una excepción
	        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo desactivar el evento: " + e.getMessage()));
	    }
	}
	
	
	public boolean putRequest(String url, String jsonInputString) {
	    try {
	        URL urlObj = new URL(url);
	        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setDoOutput(true);

	        try (OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        // Leer la respuesta para verificar si la operación fue exitosa
	        int responseCode = conn.getResponseCode();
	        conn.disconnect();

	        return responseCode == HttpURLConnection.HTTP_OK;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}




    
    
}
