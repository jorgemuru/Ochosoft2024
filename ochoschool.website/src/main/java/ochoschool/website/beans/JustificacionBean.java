package ochoschool.website.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.ConvocadoEvento;
import ochoschool.website.entidades.ConvocatoriaEvento;
import ochoschool.website.entidades.Estado;
import ochoschool.website.entidades.Estudiante;
import ochoschool.website.entidades.Evento;
import ochoschool.website.entidades.Justificacion;
import ochoschool.website.entidades.Reclamo;
import ochoschool.website.entidades.RegistroJustificacion;
import ochoschool.website.entidades.TipoReclamo;
import ochoschool.website.entidades.Tutor;
import ochoschool.website.entidades.Usuario;

@ManagedBean(name = "justificacionBean")
@SessionScoped
public class JustificacionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String BASE_URL = "http://localhost:8080/ochoschool.apirest/";
	private ApiClientAux apiClient;
	private Long eventoIdSeleccionado;
	private Justificacion justificacion;
	@Inject
    private UsuarioBean usuarioBean;
	private List<ConvocadoEvento> eventosConvocados;
	private Long convocadoEventoIdSeleccionado;
	private UploadedFile archivoAdjunto;
	private Long estadoSeleccionado;
	private List<Estado> estados;
	private List<Justificacion> justificacionesFiltradas;
	private List<Justificacion> justificacionesPropias;
	private List<Justificacion> justificacionesAnalista;
	private List<Justificacion> justificacionesAnalistaFiltradas;
	private String estudianteSeleccionado;
	private List<Estudiante> estudiantes;
	private Long idJustificacion;
	private Justificacion justificacionSeleccionada;
	private RegistroJustificacion registro;
	
	
	private static final Gson gson = new GsonBuilder()
	        .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
	            @Override
	            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	                    throws JsonParseException {
	                if (json == null || json.isJsonNull()) {
	                    return null;
	                }
	                try {
	                    long timestamp = json.getAsLong();
	                    return new Date(timestamp);
	                } catch (NumberFormatException e) {
	                    throw new JsonParseException("Failed to parse timestamp: " + json.getAsString(), e);
	                }
	            }
	        })
	        .create();
	
	@PostConstruct
	public void init() {
	    justificacion = new Justificacion();
	    cargarJustificacionesPropias();
	    cargarJustificacionesAnalista();
	    cargarEstados();
	    filtrarAnalista();
	    cargarEstudiantes();
	    justificacionesAnalistaFiltradas = new ArrayList<>(justificacionesAnalista);
	    apiClient = new ApiClientAux();
	}
	
	public String crearJustificacion() {
	    try {  
	        // Construir el objeto JSON
	        JsonObject justificacionJson = new JsonObject();

	        apiClient = new ApiClientAux();

	        // Obtener el ConvocadoEvento
	        JsonObject convocadoEventoJson = apiClient.obtenerJsonDesdeEndpointGeneral("evento/convocado", convocadoEventoIdSeleccionado);
	        justificacionJson.add("convocadoEvento", convocadoEventoJson);
	        justificacionJson.addProperty("detalle", justificacion.getDetalle());

	        Long fechaHora = System.currentTimeMillis();
	        justificacionJson.addProperty("fechaHora", fechaHora);
	        
	        JsonObject estadoJson = new JsonObject();
	        estadoJson.addProperty("idEstado", 1);
	        justificacionJson.add("estado", estadoJson);
	        
	     // **Procesar el archivo adjunto**
	        if (archivoAdjunto != null && archivoAdjunto.getContent() != null && archivoAdjunto.getSize() > 0) {
	            // Codificar el archivo en Base64
	            String archivoBase64 = Base64.getEncoder().encodeToString(archivoAdjunto.getContent());
	            justificacionJson.addProperty("archivoAdjunto", archivoBase64);

	            // También puedes agregar el nombre y tipo del archivo si es necesario
	            justificacionJson.addProperty("nombreArchivo", archivoAdjunto.getFileName());
	            justificacionJson.addProperty("tipoArchivo", archivoAdjunto.getContentType());
	        } else {
	            // Si no se adjuntó un archivo, puedes manejarlo según tu lógica de negocio
	            justificacionJson.add("archivoAdjunto", JsonNull.INSTANCE);
	        }

	        System.out.println("JSON de la Justificación a enviar: " + justificacionJson.toString());

	        // Enviar la justificación al backend
	        String endpointUrl = BASE_URL + "justificacion";
	        URL url = new URL(endpointUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();

	        // Configurar conexión para POST
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        con.setDoOutput(true);

	        // Escribir el JSON en el cuerpo de la solicitud
	        try (OutputStream os = con.getOutputStream()) {
	            byte[] input = justificacionJson.toString().getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        // Leer la respuesta del servidor
	        int responseCode = con.getResponseCode();
	        System.out.println("Código de respuesta del servidor: " + responseCode);

	        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
	            // Justificación creada exitosamente
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Justificación creada exitosamente", null));
	            
	            cargarJustificacionesPropias();

	            return "paginaExito?faces-redirect=true"; // Navegar a la página de éxito
	        } else {
	            // Leer el mensaje de error del servidor
	            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
	            StringBuilder errorResponse = new StringBuilder();
	            String responseLine = null;
	            while ((responseLine = br.readLine()) != null) {
	                errorResponse.append(responseLine.trim());
	            }
	            System.err.println("Error al crear justificación: " + errorResponse.toString());
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear justificación", errorResponse.toString()));
	            return null;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Manejo de errores generales
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear justificación", e.getMessage()));
	        return null;
	    }
	}

	
	public void cargarTodosLosConvocados() {
	    String endpointUrl = BASE_URL + "evento/convocado"; 

	    try {
	        URL url = new URL(endpointUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Accept", "application/json");

	        int responseCode = con.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
	            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
	            List<ConvocadoEvento> todosConvocados = new ArrayList<>();

	            for (JsonElement element : jsonArray) {
	                JsonObject convocadoJson = element.getAsJsonObject();
	                ConvocadoEvento convocado = new ConvocadoEvento();

	                // Parsear idConvocadoEvento
	                if (convocadoJson.has("idConvocadoEvento")) {
	                    convocado.setIdConvocadoEvento(convocadoJson.get("idConvocadoEvento").getAsLong());
	                }

	                // Parsear estudiante
	                if (convocadoJson.has("estudiante") && !convocadoJson.get("estudiante").isJsonNull()) {
	                    JsonObject estudianteJson = convocadoJson.get("estudiante").getAsJsonObject();
	                    Estudiante estudiante = new Estudiante();
	                    estudiante.setIdUsuario(estudianteJson.get("idUsuario").getAsLong());
	                    estudiante.setPrimer_nombre(estudianteJson.get("primer_nombre").getAsString());
	                    estudiante.setPrimer_apellido(estudianteJson.get("primer_apellido").getAsString());
	                    convocado.setEstudiante(estudiante);
	                }

	                // Parsear convocatoriaEvento
	                if (convocadoJson.has("convocatoriaEvento") && !convocadoJson.get("convocatoriaEvento").isJsonNull()) {
	                    JsonObject convocatoriaJson = convocadoJson.get("convocatoriaEvento").getAsJsonObject();
	                    ConvocatoriaEvento convocatoriaEvento = new ConvocatoriaEvento();
	                    convocatoriaEvento.setIdConvocatoriaEvento(convocatoriaJson.get("idConvocatoriaEvento").getAsLong());

	                    // Parsear evento dentro de convocatoriaEvento
	                    if (convocatoriaJson.has("evento") && !convocatoriaJson.get("evento").isJsonNull()) {
	                        JsonObject eventoJson = convocatoriaJson.get("evento").getAsJsonObject();
	                        Evento evento = new Evento();
	                        evento.setIdEvento(eventoJson.get("idEvento").getAsLong());
	                        evento.setTitulo(eventoJson.get("titulo").getAsString());
	                        convocatoriaEvento.setEvento(evento);
	                    }
	                    convocado.setConvocatoriaEvento(convocatoriaEvento);
	                }

	                // Agregar a la lista
	                todosConvocados.add(convocado);
	            }

	            // Asignar la lista obtenida a tu variable de clase
	            this.eventosConvocados = todosConvocados;
	        } else {
	            System.err.println("Error al obtener la lista de convocados. Código de respuesta: " + responseCode);
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar convocados", null));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar convocados", e.getMessage()));
	    }
	}
	
	public void cargarEventosDeEstudiante() {

	    // Obtener el usuario logueado
	    Usuario usuario = usuarioBean.getUsuarioLogueado();
	    if (usuario == null) {
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no logueado.", null));
	        return;
	    }
	    Long idUsuario = usuario.getIdUsuario();

	    // Cargar todos los convocados
	    cargarTodosLosConvocados();

	    if (eventosConvocados == null) {
	        // Manejar error si no se pudieron cargar los convocados
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudieron cargar los eventos convocados.", null));
	        return;
	    }

	    // Filtrar los eventos propios por usuario
	    List<ConvocadoEvento> eventosDelEstudiante = new ArrayList<>();
	    for (ConvocadoEvento convocadoEvento : eventosConvocados) {
	        if (convocadoEvento.getEstudiante() != null && idUsuario.equals(convocadoEvento.getEstudiante().getIdUsuario())) {
	            eventosDelEstudiante.add(convocadoEvento);
	        }
	    }
	    // Asignar la lista filtrada a eventosConvocados
	    eventosConvocados = eventosDelEstudiante;
	    
	    System.out.println("Eventos convocados del estudiante:");
	    for (ConvocadoEvento ce : eventosConvocados) {
	        System.out.println("ID ConvocadoEvento: " + ce.getIdConvocadoEvento());
	    }
	}
	
	public void filtrarPorEstado() {
        // Reiniciar la lista filtrada como una copia de todos las justificaciones propias
        justificacionesFiltradas = new ArrayList<>(justificacionesPropias);
        
        // Verificar si se ha seleccionado un estado
        if (estadoSeleccionado != null) {
            Long idEstado;
            try {
                idEstado = estadoSeleccionado;
            } catch (NumberFormatException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Estado seleccionado inválido.", null));
                return;
            }
            
            // Filtrar la lista removiendo justificaciones que no coinciden con el estado seleccionado
            justificacionesFiltradas.removeIf(justificacion -> 
            justificacion.getEstado() == null || !justificacion.getEstado().getIdEstado().equals(idEstado)
            );
        }
    }
	
	public void cargarJustificacionesPropias() {
	    // Obtener el usuario logueado
	    Usuario usuario = usuarioBean.getUsuarioLogueado();
	    if (usuario == null) {
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no logueado.", null));
	        return;
	    }

	    // Obtener los ConvocadoEvento del usuario logueado
	    cargarEventosDeEstudiante(); 
	    List<Long> idsConvocadoEventoUsuario = new ArrayList<>();
	    for (ConvocadoEvento ce : eventosConvocados) {
	        idsConvocadoEventoUsuario.add(ce.getIdConvocadoEvento());
	        
	    }

	    // Obtener todas las Justificaciones
	    String endpointUrl = BASE_URL + "justificacion";
	    try {
	        URL url = new URL(endpointUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Accept", "application/json");

	        int responseCode = con.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
	            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
	            List<Justificacion> justificaciones = new ArrayList<>();

	            for (JsonElement element : jsonArray) {
	                JsonObject justificacionJson = element.getAsJsonObject();
	                Justificacion justificacion = new Justificacion();

	                // Parsear idJustificacion
	                if (justificacionJson.has("idJustificacion") && !justificacionJson.get("idJustificacion").isJsonNull()) {
	                    justificacion.setIdJustificacion(justificacionJson.get("idJustificacion").getAsLong());
	                }

	                // Parsear detalle
	                if (justificacionJson.has("detalle") && !justificacionJson.get("detalle").isJsonNull()) {
	                    justificacion.setDetalle(justificacionJson.get("detalle").getAsString());
	                }

	                // Parsear fechaHora
	                if (justificacionJson.has("fechaHora") && !justificacionJson.get("fechaHora").isJsonNull()) {
	                    long fechaHoraMillis = justificacionJson.get("fechaHora").getAsLong();
	                    justificacion.setFechaHora(new Date(fechaHoraMillis));
	                }

	                // Parsear estado
	                if (justificacionJson.has("estado") && !justificacionJson.get("estado").isJsonNull()) {
	                    JsonObject estadoJson = justificacionJson.get("estado").getAsJsonObject();
	                    Estado estado = new Estado();
	                    if (estadoJson.has("idEstado") && !estadoJson.get("idEstado").isJsonNull()) {
	                        estado.setIdEstado(estadoJson.get("idEstado").getAsLong());
	                    }
	                    if (estadoJson.has("descripcion") && !estadoJson.get("descripcion").isJsonNull()) {
	                        estado.setDescripcion(estadoJson.get("descripcion").getAsString());
	                    }
	                    justificacion.setEstado(estado);
	                }

	                // Parsear archivoAdjunto
	                if (justificacionJson.has("archivoAdjunto") && !justificacionJson.get("archivoAdjunto").isJsonNull()) {
	                    String archivoBase64 = justificacionJson.get("archivoAdjunto").getAsString();
	                    byte[] archivoAdjunto = Base64.getDecoder().decode(archivoBase64);
	                    justificacion.setArchivoAdjunto(archivoAdjunto);
	                }

	             // Parsear convocadoEvento y sus objetos anidados
	                if (justificacionJson.has("convocadoEvento") && !justificacionJson.get("convocadoEvento").isJsonNull()) {
	                    JsonObject convocadoEventoJson = justificacionJson.get("convocadoEvento").getAsJsonObject();
	                    ConvocadoEvento convocadoEvento = new ConvocadoEvento();

	                    // Parsear idConvocadoEvento
	                    if (convocadoEventoJson.has("idConvocadoEvento") && !convocadoEventoJson.get("idConvocadoEvento").isJsonNull()) {
	                        convocadoEvento.setIdConvocadoEvento(convocadoEventoJson.get("idConvocadoEvento").getAsLong());
	                    }

	                    // Parsear convocatoriaEvento
	                    if (convocadoEventoJson.has("convocatoriaEvento") && !convocadoEventoJson.get("convocatoriaEvento").isJsonNull()) {
	                        JsonObject convocatoriaEventoJson = convocadoEventoJson.get("convocatoriaEvento").getAsJsonObject();
	                        ConvocatoriaEvento convocatoriaEvento = new ConvocatoriaEvento();

	                        // Parsear idConvocatoriaEvento
	                        if (convocatoriaEventoJson.has("idConvocatoriaEvento") && !convocatoriaEventoJson.get("idConvocatoriaEvento").isJsonNull()) {
	                            convocatoriaEvento.setIdConvocatoriaEvento(convocatoriaEventoJson.get("idConvocatoriaEvento").getAsLong());
	                        }

	                        // Parsear evento
	                        if (convocatoriaEventoJson.has("evento") && !convocatoriaEventoJson.get("evento").isJsonNull()) {
	                            JsonObject eventoJson = convocatoriaEventoJson.get("evento").getAsJsonObject();
	                            Evento evento = new Evento();

	                            // Parsear idEvento
	                            if (eventoJson.has("idEvento") && !eventoJson.get("idEvento").isJsonNull()) {
	                                evento.setIdEvento(eventoJson.get("idEvento").getAsLong());
	                            }

	                            // Parsear titulo
	                            if (eventoJson.has("titulo") && !eventoJson.get("titulo").isJsonNull()) {
	                                evento.setTitulo(eventoJson.get("titulo").getAsString());
	                            }

	                            // Asignar evento a convocatoriaEvento
	                            convocatoriaEvento.setEvento(evento);
	                        }

	                        // Asignar convocatoriaEvento a convocadoEvento
	                        convocadoEvento.setConvocatoriaEvento(convocatoriaEvento);
	                    }

	                    // Asignar convocadoEvento a justificacion
	                    justificacion.setConvocadoEvento(convocadoEvento);
	                }

	                // Verificar si la justificación pertenece a uno de los ConvocadoEvento del usuario
	                System.out.println("ID Justificación: " + justificacion.getIdJustificacion());

	                if (justificacion.getConvocadoEvento() != null) {
	                    Long idJustificacionConvocado = justificacion.getConvocadoEvento().getIdConvocadoEvento();
	                    System.out.println("ID ConvocadoEvento en Justificación: " + idJustificacionConvocado);

	                    if (idsConvocadoEventoUsuario.contains(idJustificacionConvocado)) {
	                        System.out.println("La justificación pertenece al usuario.");
	                        justificaciones.add(justificacion);
	                    } else {
	                        System.out.println("La justificación NO pertenece al usuario.");
	                    }
	                } else {
	                    System.out.println("La justificación no tiene ConvocadoEvento asociado.");
	                }
	            }

	            // Asignar la lista obtenida a tus variables de clase
	            this.justificacionesPropias = justificaciones;
	            this.justificacionesFiltradas = new ArrayList<>(justificacionesPropias);
	            
	            System.out.println("Total de justificaciones del usuario: " + justificacionesPropias.size());

	        } else {
	            System.err.println("Error al obtener las justificaciones. Código de respuesta: " + responseCode);
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar justificaciones", null));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar justificaciones", e.getMessage()));
	    }
	}

	
	 public void cargarEstados() {
	        String endpointUrl = BASE_URL + "mantenimiento/estado";
	        try {
	            URL url = new URL(endpointUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setRequestMethod("GET");
	            con.setRequestProperty("Accept", "application/json");

	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
	                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
	                estados = new ArrayList<>();

	                for (JsonElement element : jsonArray) {
	                    JsonObject estadoJSON = element.getAsJsonObject();
	                    Estado estado = new Estado();
	                    estado.setIdEstado(estadoJSON.get("idEstado").getAsLong());
	                    estado.setDescripcion(estadoJSON.get("descripcion").getAsString());
	                    estado.setActivo(estadoJSON.get("activo").getAsCharacter());
	                    if(estado.getActivo() =='S') {
	                    	estados.add(estado);
	                    }                 	
	                }
	            } else {
	                // Manejar errores si la respuesta no es exitosa
	                System.err.println("Error al obtener los tipos de reclamo. Código de respuesta: " + responseCode);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public void filtrarAnalista() {
		 
		    // Reiniciar la lista filtrada como una copia de la lista completa
		    justificacionesAnalistaFiltradas = new ArrayList<>(justificacionesAnalista);

		    // Aplicar filtros
		    if (estadoSeleccionado != null) {
		        Long idEstado = estadoSeleccionado;

		        // Filtrar la lista removiendo justificaciones que no coinciden con el estado seleccionado
		        justificacionesAnalistaFiltradas.removeIf(justificacion ->
		            justificacion.getEstado() == null || !justificacion.getEstado().getIdEstado().equals(idEstado)
		        );
		    }

		    if (estudianteSeleccionado != null && !estudianteSeleccionado.isEmpty()) {
		        Long idUsuario;
		        try {
		            idUsuario = Long.parseLong(estudianteSeleccionado);
		        } catch (NumberFormatException e) {
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Estudiante seleccionado inválido.", null));
		            return;
		        }

		        // Filtrar la lista removiendo justificaciones que no coinciden con el estudiante seleccionado
		        justificacionesAnalistaFiltradas.removeIf(justificacion ->
		            justificacion.getConvocadoEvento().getEstudiante() == null ||
		            !justificacion.getConvocadoEvento().getEstudiante().getIdUsuario().equals(idUsuario)
		        );
		    }
		}

	 
	 public void cargarEstudiantes() {
	        String endpointUrl = BASE_URL + "estudiante";
	        try {
	            URL url = new URL(endpointUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setRequestMethod("GET");
	            con.setRequestProperty("Accept", "application/json");

	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
	                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
	                estudiantes = new ArrayList<>();

	                for (JsonElement element : jsonArray) {
	                    JsonObject estudianteJSON = element.getAsJsonObject();
	                    Estudiante estudiante = new Estudiante();
	                    estudiante.setIdUsuario(estudianteJSON.get("idUsuario").getAsLong());
	                    estudiante.setPrimer_nombre(estudianteJSON.get("primer_nombre").getAsString());
	                    estudiante.setPrimer_apellido(estudianteJSON.get("primer_apellido").getAsString());
	                    estudiantes.add(estudiante);
	                }
	            } else {
	                // Manejar errores si la respuesta no es exitosa
	                System.err.println("Error al obtener los Estudiantes. Código de respuesta: " + responseCode);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public void cargarJustificacionesAnalista() {
		    cargarTodosLosConvocados();
		    String endpointUrl = BASE_URL + "justificacion";
		    try {
		        URL url = new URL(endpointUrl);
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		        con.setRequestMethod("GET");
		        con.setRequestProperty("Accept", "application/json");

		        int responseCode = con.getResponseCode();
		        if (responseCode == HttpURLConnection.HTTP_OK) {
		            InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
		            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
		            List<Justificacion> justificaciones = new ArrayList<>();

		            for (JsonElement element : jsonArray) {
		                JsonObject justificacionJson = element.getAsJsonObject();
		                Justificacion justificacion = new Justificacion();

		                // Parsear idJustificacion
		                if (justificacionJson.has("idJustificacion") && !justificacionJson.get("idJustificacion").isJsonNull()) {
		                    justificacion.setIdJustificacion(justificacionJson.get("idJustificacion").getAsLong());
		                }

		                // Parsear detalle
		                if (justificacionJson.has("detalle") && !justificacionJson.get("detalle").isJsonNull()) {
		                    justificacion.setDetalle(justificacionJson.get("detalle").getAsString());
		                }

		                // Parsear fechaHora
		                if (justificacionJson.has("fechaHora") && !justificacionJson.get("fechaHora").isJsonNull()) {
		                    long fechaHoraMillis = justificacionJson.get("fechaHora").getAsLong();
		                    justificacion.setFechaHora(new Date(fechaHoraMillis));
		                }

		                // Parsear estado
		                if (justificacionJson.has("estado") && !justificacionJson.get("estado").isJsonNull()) {
		                    JsonObject estadoJson = justificacionJson.get("estado").getAsJsonObject();
		                    Estado estado = new Estado();
		                    if (estadoJson.has("idEstado") && !estadoJson.get("idEstado").isJsonNull()) {
		                        estado.setIdEstado(estadoJson.get("idEstado").getAsLong());
		                    }
		                    if (estadoJson.has("descripcion") && !estadoJson.get("descripcion").isJsonNull()) {
		                        estado.setDescripcion(estadoJson.get("descripcion").getAsString());
		                    }
		                    justificacion.setEstado(estado);
		                }

		                // Parsear archivoAdjunto
		                if (justificacionJson.has("archivoAdjunto") && !justificacionJson.get("archivoAdjunto").isJsonNull()) {
		                    String archivoBase64 = justificacionJson.get("archivoAdjunto").getAsString();
		                    byte[] archivoAdjunto = Base64.getDecoder().decode(archivoBase64);
		                    justificacion.setArchivoAdjunto(archivoAdjunto);
		                }

		             // Parsear convocadoEvento y sus objetos anidados
		                if (justificacionJson.has("convocadoEvento") && !justificacionJson.get("convocadoEvento").isJsonNull()) {
		                    JsonObject convocadoEventoJson = justificacionJson.get("convocadoEvento").getAsJsonObject();
		                    ConvocadoEvento convocadoEvento = new ConvocadoEvento();

		                    // Parsear idConvocadoEvento
		                    if (convocadoEventoJson.has("idConvocadoEvento") && !convocadoEventoJson.get("idConvocadoEvento").isJsonNull()) {
		                        convocadoEvento.setIdConvocadoEvento(convocadoEventoJson.get("idConvocadoEvento").getAsLong());
		                    }

		                    // Parsear convocatoriaEvento
		                    if (convocadoEventoJson.has("convocatoriaEvento") && !convocadoEventoJson.get("convocatoriaEvento").isJsonNull()) {
		                        JsonObject convocatoriaEventoJson = convocadoEventoJson.get("convocatoriaEvento").getAsJsonObject();
		                        ConvocatoriaEvento convocatoriaEvento = new ConvocatoriaEvento();

		                        // Parsear idConvocatoriaEvento
		                        if (convocatoriaEventoJson.has("idConvocatoriaEvento") && !convocatoriaEventoJson.get("idConvocatoriaEvento").isJsonNull()) {
		                            convocatoriaEvento.setIdConvocatoriaEvento(convocatoriaEventoJson.get("idConvocatoriaEvento").getAsLong());
		                        }

		                        // Parsear evento
		                        if (convocatoriaEventoJson.has("evento") && !convocatoriaEventoJson.get("evento").isJsonNull()) {
		                            JsonObject eventoJson = convocatoriaEventoJson.get("evento").getAsJsonObject();
		                            Evento evento = new Evento();

		                            // Parsear idEvento
		                            if (eventoJson.has("idEvento") && !eventoJson.get("idEvento").isJsonNull()) {
		                                evento.setIdEvento(eventoJson.get("idEvento").getAsLong());
		                            }

		                            // Parsear titulo
		                            if (eventoJson.has("titulo") && !eventoJson.get("titulo").isJsonNull()) {
		                                evento.setTitulo(eventoJson.get("titulo").getAsString());
		                            }

		                            // Asignar evento a convocatoriaEvento
		                            convocatoriaEvento.setEvento(evento);
		                        }

		                        // Asignar convocatoriaEvento a convocadoEvento
		                        convocadoEvento.setConvocatoriaEvento(convocatoriaEvento);
		                    }

		                    // Asignar convocadoEvento a justificacion
		                    justificacion.setConvocadoEvento(convocadoEvento);
		                }


		                // Agregar la justificación a la lista
		                justificaciones.add(justificacion);
		            }

		            // Asignar la lista obtenida a la variable de clase
		            this.justificacionesAnalista = justificaciones;
		            // Inicializar la lista filtrada como una copia de la lista completa
		            this.justificacionesAnalistaFiltradas = new ArrayList<>(justificacionesAnalista);

		        } else {
		            System.err.println("Error al obtener las justificaciones. Código de respuesta: " + responseCode);
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar justificaciones", null));
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        FacesContext.getCurrentInstance().addMessage(null,
		            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar justificaciones", e.getMessage()));
		    }
		}
	 
	 public String irAModificarJustificacion(Long idJustificacion) {
			if (idJustificacion != null) {
				cargarJustificacionSeleccionadaPorId(idJustificacion);
				return "/pages/ModificarJustificacion.xhtml?faces-redirect=true&includeViewParams=true&idJustificacion=" + idJustificacion;
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado una justificación."));
				return null;
			}
		}
	 
	 public void cargarJustificacionSeleccionadaPorId(Long idJustificacion) {
		    String endpointUrl = BASE_URL + "justificacion/" + idJustificacion;
		    try {
		        URL url = new URL(endpointUrl);
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		        con.setRequestMethod("GET");
		        con.setRequestProperty("Accept", "application/json");

		        int responseCode = con.getResponseCode();
		        if (responseCode == HttpURLConnection.HTTP_OK) {
		            InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
		            JsonObject justificacionJson = JsonParser.parseReader(reader).getAsJsonObject();

		            Justificacion justificacion = new Justificacion();

		            // Parsear idJustificacion
		            if (justificacionJson.has("idJustificacion") && !justificacionJson.get("idJustificacion").isJsonNull()) {
		                justificacion.setIdJustificacion(justificacionJson.get("idJustificacion").getAsLong());
		            }

		            // Parsear detalle
		            if (justificacionJson.has("detalle") && !justificacionJson.get("detalle").isJsonNull()) {
		                justificacion.setDetalle(justificacionJson.get("detalle").getAsString());
		            }

		            // Parsear fechaHora
		            if (justificacionJson.has("fechaHora") && !justificacionJson.get("fechaHora").isJsonNull()) {
		                long fechaHoraMillis = justificacionJson.get("fechaHora").getAsLong();
		                justificacion.setFechaHora(new Date(fechaHoraMillis));
		            }

		            // Parsear estado
		            if (justificacionJson.has("estado") && !justificacionJson.get("estado").isJsonNull()) {
		                JsonObject estadoJson = justificacionJson.get("estado").getAsJsonObject();
		                Estado estado = new Estado();
		                if (estadoJson.has("idEstado") && !estadoJson.get("idEstado").isJsonNull()) {
		                    estado.setIdEstado(estadoJson.get("idEstado").getAsLong());
		                }
		                if (estadoJson.has("descripcion") && !estadoJson.get("descripcion").isJsonNull()) {
		                    estado.setDescripcion(estadoJson.get("descripcion").getAsString());
		                }
		                justificacion.setEstado(estado);
		            }

		            // Parsear archivoAdjunto
		            if (justificacionJson.has("archivoAdjunto") && !justificacionJson.get("archivoAdjunto").isJsonNull()) {
		                String archivoBase64 = justificacionJson.get("archivoAdjunto").getAsString();
		                byte[] archivoAdjunto = Base64.getDecoder().decode(archivoBase64);
		                justificacion.setArchivoAdjunto(archivoAdjunto);
		            }

		            // Parsear convocadoEvento y sus objetos anidados
		            if (justificacionJson.has("convocadoEvento") && !justificacionJson.get("convocadoEvento").isJsonNull()) {
		                JsonObject convocadoEventoJson = justificacionJson.get("convocadoEvento").getAsJsonObject();
		                ConvocadoEvento convocadoEvento = new ConvocadoEvento();

		                // Parsear idConvocadoEvento
		                if (convocadoEventoJson.has("idConvocadoEvento") && !convocadoEventoJson.get("idConvocadoEvento").isJsonNull()) {
		                    convocadoEvento.setIdConvocadoEvento(convocadoEventoJson.get("idConvocadoEvento").getAsLong());
		                }

		                // Parsear estudiante
		                if (convocadoEventoJson.has("estudiante") && !convocadoEventoJson.get("estudiante").isJsonNull()) {
		                    JsonObject estudianteJson = convocadoEventoJson.get("estudiante").getAsJsonObject();
		                    Estudiante estudiante = new Estudiante();
		                    estudiante.setIdUsuario(estudianteJson.get("idUsuario").getAsLong());
		                    estudiante.setPrimer_nombre(estudianteJson.get("primer_nombre").getAsString());
		                    estudiante.setPrimer_apellido(estudianteJson.get("primer_apellido").getAsString());
		                    convocadoEvento.setEstudiante(estudiante);
		                }

		                // Parsear convocatoriaEvento
		                if (convocadoEventoJson.has("convocatoriaEvento") && !convocadoEventoJson.get("convocatoriaEvento").isJsonNull()) {
		                    JsonObject convocatoriaJson = convocadoEventoJson.get("convocatoriaEvento").getAsJsonObject();
		                    ConvocatoriaEvento convocatoriaEvento = new ConvocatoriaEvento();
		                    convocatoriaEvento.setIdConvocatoriaEvento(convocatoriaJson.get("idConvocatoriaEvento").getAsLong());

		                    // Parsear evento dentro de convocatoriaEvento
		                    if (convocatoriaJson.has("evento") && !convocatoriaJson.get("evento").isJsonNull()) {
		                        JsonObject eventoJson = convocatoriaJson.get("evento").getAsJsonObject();
		                        Evento evento = new Evento();
		                        evento.setIdEvento(eventoJson.get("idEvento").getAsLong());
		                        evento.setTitulo(eventoJson.get("titulo").getAsString());
		                        convocatoriaEvento.setEvento(evento);
		                    }
		                    convocadoEvento.setConvocatoriaEvento(convocatoriaEvento);
		                }

		                justificacion.setConvocadoEvento(convocadoEvento);

		                // **Asignar el ID del ConvocadoEvento Seleccionado**
		                if (convocadoEvento.getIdConvocadoEvento() != null) {
		                    this.convocadoEventoIdSeleccionado = convocadoEvento.getIdConvocadoEvento();
		                } else {
		                    this.convocadoEventoIdSeleccionado = null;
		                }

		                // **Log para Verificar el Título del Evento**
		                if (convocadoEvento.getConvocatoriaEvento() != null 
		                    && convocadoEvento.getConvocatoriaEvento().getEvento() != null) {
		                    System.out.println("Justificación ID: " + justificacion.getIdJustificacion() +
		                                       " - Evento Título: " + convocadoEvento.getConvocatoriaEvento().getEvento().getTitulo());
		                } else {
		                    System.out.println("Justificación ID: " + justificacion.getIdJustificacion() +
		                                       " - Evento Título: NULL");
		                }
		            }

		            // Asignar la justificación cargada a la variable de clase
		            this.justificacion = justificacion;

		        } else {
		            System.err.println("Error al obtener la justificación. Código de respuesta: " + responseCode);
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar la justificación", null));
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        FacesContext.getCurrentInstance().addMessage(null,
		            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar la justificación", e.getMessage()));
		    }
		}

	 
	 public String modificarJustificacion() {
		    try {
		        // Construir el objeto JSON con los datos actualizados
		        JsonObject justificacionJson = new JsonObject();

		        // Agregar el ID de la justificación
		        justificacionJson.addProperty("idJustificacion", justificacion.getIdJustificacion());

		        // Agregar el detalle actualizado
		        justificacionJson.addProperty("detalle", justificacion.getDetalle());

		        // Agregar la fecha y hora
		        if (justificacion.getFechaHora() != null) {
		            justificacionJson.addProperty("fechaHora", justificacion.getFechaHora().getTime());
		        } else {
		            justificacionJson.addProperty("fechaHora", System.currentTimeMillis());
		        }

		        // Agregar el estado
		        JsonObject estadoJson = new JsonObject();
		        estadoJson.addProperty("idEstado", justificacion.getEstado().getIdEstado());
		        justificacionJson.add("estado", estadoJson);

		        // Agregar el convocadoEvento
		        JsonObject convocadoEventoJson = new JsonObject();
		        convocadoEventoJson.addProperty("idConvocadoEvento", convocadoEventoIdSeleccionado);
		        justificacionJson.add("convocadoEvento", convocadoEventoJson);

		        // Procesar el archivo adjunto
		        if (archivoAdjunto != null && archivoAdjunto.getContent() != null && archivoAdjunto.getSize() > 0) {
		            // Codificar el archivo en Base64
		            String archivoBase64 = Base64.getEncoder().encodeToString(archivoAdjunto.getContent());
		            justificacionJson.addProperty("archivoAdjunto", archivoBase64);
		            justificacionJson.addProperty("nombreArchivo", archivoAdjunto.getFileName());
		            justificacionJson.addProperty("tipoArchivo", archivoAdjunto.getContentType());
		        } else {
		            // Si no se adjuntó un nuevo archivo, mantener el existente o manejar según la lógica
		            // Si deseas eliminar el archivo existente, puedes establecer "archivoAdjunto" en null
		            // justificacionJson.add("archivoAdjunto", JsonNull.INSTANCE);
		            // En este ejemplo, no se modifica el archivo adjunto si no se sube uno nuevo
		        }

		        System.out.println("JSON de la Justificación a enviar: " + justificacionJson.toString());

		        // Enviar la justificación actualizada al backend
		        String endpointUrl = BASE_URL + "justificacion";
		        URL url = new URL(endpointUrl);
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();

		        // Configurar conexión para PUT
		        con.setRequestMethod("PUT");
		        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		        con.setDoOutput(true);

		        // Escribir el JSON en el cuerpo de la solicitud
		        try (OutputStream os = con.getOutputStream()) {
		            byte[] input = justificacionJson.toString().getBytes("utf-8");
		            os.write(input, 0, input.length);
		        }

		        // Leer la respuesta del servidor
		        int responseCode = con.getResponseCode();
		        System.out.println("Código de respuesta del servidor: " + responseCode);

		        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
		            // Justificación modificada exitosamente
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_INFO, "Justificación modificada exitosamente", null));

		            // Recargar las justificaciones propias
		            cargarJustificacionesPropias();

		            return "ListadoJustificacionesPropias?faces-redirect=true"; // Navegar a la lista de justificaciones
		        } else {
		            // Leer el mensaje de error del servidor
		            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
		            StringBuilder errorResponse = new StringBuilder();
		            String responseLine = null;
		            while ((responseLine = br.readLine()) != null) {
		                errorResponse.append(responseLine.trim());
		            }
		            System.err.println("Error al modificar justificación: " + errorResponse.toString());
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar justificación", errorResponse.toString()));
		            return null;
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        // Manejo de errores generales
		        FacesContext.getCurrentInstance().addMessage(null,
		            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar justificación", e.getMessage()));
		        return null;
		    }
		}
	 
	 public String irAccionJustificacion(Long idJustificacion) {
			if (idJustificacion != null) {
				cargarJustificacionSeleccionadaPorId(idJustificacion);
				return "/pages/AccionJustificacion.xhtml?faces-redirect=true&includeViewParams=true&idJustificacion=" + idJustificacion;
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado una justifiación."));
				return null;
			}
		}
	 
	 public String registrarAccion() {
	        try {       

	            // Construir el objeto JSON del registro
	            JsonObject registroJson = new JsonObject();

	            apiClient = new ApiClientAux();

	            JsonObject justificacionJson = apiClient.obtenerJsonDesdeEndpointGeneral("justificacion", justificacion.getIdJustificacion());
	            registroJson.add("justificacion", justificacionJson);
	            registroJson.addProperty("detalle", registro.getDetalle());
	            
				Usuario usuario = usuarioBean.getUsuarioLogueado();
	            JsonObject analistaJson = apiClient.obtenerJsonDesdeEndpointGeneral("analista", usuario.getIdUsuario());
	            registroJson.add("analista", analistaJson);
	            
	            Long fechaRealizacionActual = System.currentTimeMillis();
	            registroJson.addProperty("fechaHora", fechaRealizacionActual);

	            // Enviar el registro al backend
	            String endpointUrl = BASE_URL + "justificacion/registro"; 
	            URL url = new URL(endpointUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();

	            // Configurar conexión para POST
	            con.setRequestMethod("POST");
	            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            con.setDoOutput(true);

	            // Escribir el JSON en el cuerpo de la solicitud
	            try (OutputStream os = con.getOutputStream()) {
	                byte[] input = registroJson.toString().getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }

	            // Leer la respuesta del servidor
	            int responseCode = con.getResponseCode();
	            System.out.println("Código de respuesta del servidor: " + responseCode);

	            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
	                // Registro creado exitosamente
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado exitosamente", null));



	                return "paginaExito?faces-redirect=true"; // Navegar a la página de éxito
	            } else {
	                // Leer el mensaje de error del servidor
	                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
	                StringBuilder errorResponse = new StringBuilder();
	                String responseLine = null;
	                while ((responseLine = br.readLine()) != null) {
	                    errorResponse.append(responseLine.trim());
	                }
	                System.err.println("Error al crear el registro: " + errorResponse.toString());
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear el registro", errorResponse.toString()));
	                return null;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            // Manejo de errores generales
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear el registro", e.getMessage()));
	            return null;
	        }
	    }
	 
	 public String irAModificarEstadoJustificacion(Long idJustificacion) {
			if (idJustificacion != null) {
				cargarJustificacionSeleccionadaPorId(idJustificacion);
				return "/pages/ModificarEstadoJustificacion.xhtml?faces-redirect=true&includeViewParams=true&idJustificacion=" + idJustificacion;
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado una justificación."));
				return null;
			}
		}
	 
	 public String cambiarEstado() {
		    try {
		        // Construir el objeto JSON con los datos actualizados
		        JsonObject justificacionJson = new JsonObject();

		        // Agregar el ID de la justificación
		        justificacionJson.addProperty("idJustificacion", justificacion.getIdJustificacion());

		        // Agregar el detalle actualizado
		        justificacionJson.addProperty("detalle", justificacion.getDetalle());

		        // Agregar la fecha y hora
		        if (justificacion.getFechaHora() != null) {
		            justificacionJson.addProperty("fechaHora", justificacion.getFechaHora().getTime());
		        } else {
		            justificacionJson.addProperty("fechaHora", System.currentTimeMillis());
		        }

		        // Agregar el estado
		        JsonObject estadoReclamoJson = apiClient.obtenerJsonDesdeEndpointGeneral("mantenimiento/estado", estadoSeleccionado);
		        justificacionJson.add("estado", estadoReclamoJson);

		        // Agregar el convocadoEvento
		        JsonObject convocadoEventoJson = new JsonObject();
		        convocadoEventoJson.addProperty("idConvocadoEvento", justificacion.getConvocadoEvento().getIdConvocadoEvento());
		        justificacionJson.add("convocadoEvento", convocadoEventoJson);

		        // Procesar el archivo adjunto
		        if (archivoAdjunto != null && archivoAdjunto.getContent() != null && archivoAdjunto.getSize() > 0) {
		            // Codificar el archivo en Base64
		            String archivoBase64 = Base64.getEncoder().encodeToString(archivoAdjunto.getContent());
		            justificacionJson.addProperty("archivoAdjunto", archivoBase64);
		            justificacionJson.addProperty("nombreArchivo", archivoAdjunto.getFileName());
		            justificacionJson.addProperty("tipoArchivo", archivoAdjunto.getContentType());
		        } else {
		            // Si no se adjuntó un nuevo archivo, mantener el existente o manejar según la lógica
		            // Si deseas eliminar el archivo existente, puedes establecer "archivoAdjunto" en null
		            // justificacionJson.add("archivoAdjunto", JsonNull.INSTANCE);
		            // En este ejemplo, no se modifica el archivo adjunto si no se sube uno nuevo
		        }

		        System.out.println("JSON de la Justificación a enviar: " + justificacionJson.toString());

		        // Enviar la justificación actualizada al backend
		        String endpointUrl = BASE_URL + "justificacion";
		        URL url = new URL(endpointUrl);
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();

		        // Configurar conexión para PUT
		        con.setRequestMethod("PUT");
		        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		        con.setDoOutput(true);

		        // Escribir el JSON en el cuerpo de la solicitud
		        try (OutputStream os = con.getOutputStream()) {
		            byte[] input = justificacionJson.toString().getBytes("utf-8");
		            os.write(input, 0, input.length);
		        }

		        // Leer la respuesta del servidor
		        int responseCode = con.getResponseCode();
		        System.out.println("Código de respuesta del servidor: " + responseCode);

		        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
		            // Justificación modificada exitosamente
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_INFO, "Justificación modificada exitosamente", null));

		            // Recargar las justificaciones
		            cargarJustificacionesAnalista();
		            filtrarAnalista();

		            return "ListadoJustificaciones?faces-redirect=true"; // Navegar a la lista de justificaciones
		        } else {
		            // Leer el mensaje de error del servidor
		            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
		            StringBuilder errorResponse = new StringBuilder();
		            String responseLine = null;
		            while ((responseLine = br.readLine()) != null) {
		                errorResponse.append(responseLine.trim());
		            }
		            System.err.println("Error al modificar justificación: " + errorResponse.toString());
		            FacesContext.getCurrentInstance().addMessage(null,
		                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar justificación", errorResponse.toString()));
		            return null;
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		        // Manejo de errores generales
		        FacesContext.getCurrentInstance().addMessage(null,
		            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar justificación", e.getMessage()));
		        return null;
		    }
		}
	 
	 public String eliminarJustificacion(Long idJustificacion) {
	        try {
	            System.out.println("Eliminar justificación con id: " + idJustificacion);
	            
	            if (idJustificacion == null) {
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha proporcionado un ID de justificación válido."));
	                return null;
	            }

	            String endpointUrl = BASE_URL + "justificacion/" + idJustificacion; 
	            URL url = new URL(endpointUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();

	            // Configurar conexión
	            con.setRequestMethod("DELETE");
	            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

	            // Leer la respuesta del servidor
	            int responseCode = con.getResponseCode();
	            System.out.println("Código de respuesta del servidor: " + responseCode);

	            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
	                //Justificación eliminada exitosamente
	                FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Justificación eliminada exitosamente", null));

	                // Opcional: Actualizar la lista después de eliminar
	                cargarJustificacionesPropias();

	                return "paginaExito?faces-redirect=true"; // Navegar a la página de éxito
	            } else {
	                // Leer el mensaje de error del servidor
	                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
	                StringBuilder errorResponse = new StringBuilder();
	                String responseLine = null;
	                while ((responseLine = br.readLine()) != null) {
	                    errorResponse.append(responseLine.trim());
	                }
	                String errorMsg = errorResponse.toString();
	                System.err.println("Error al eliminar justificación: " + errorMsg);

	                // Analizar el mensaje de error para detectar violación de restricción
	                if (errorMsg.contains("ORA-02292") || errorMsg.contains("ConstraintViolationException")) {
	                    FacesContext.getCurrentInstance().addMessage(null,
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede eliminar la justificación porque tiene registros asociados.", "No se puede eliminar la justificación porque tiene registros asociados."));
	                } else {
	                    FacesContext.getCurrentInstance().addMessage(null,
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar justificación", "Ocurrió un error al eliminar la justificación."));
	                    
	                }
	                return null;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            // Manejo de errores generales
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar justificación", e.getMessage()));
	            return null;
	        }
	    }

	
	public Long getConvocadoEventoIdSeleccionado() {
	    return convocadoEventoIdSeleccionado;
	}

	public void setConvocadoEventoIdSeleccionado(Long convocadoEventoIdSeleccionado) {
	    this.convocadoEventoIdSeleccionado = convocadoEventoIdSeleccionado;
	}
	
	public List<ConvocadoEvento> getEventosConvocados() {
	    return eventosConvocados;
	}

	public void setEventosConvocados(List<ConvocadoEvento> eventosConvocados) {
	    this.eventosConvocados = eventosConvocados;
	}
	
	public Justificacion getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(Justificacion justificacion) {
        this.justificacion = justificacion;
    }
    
    public UploadedFile getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(UploadedFile archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

	public Long getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(Long estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Justificacion> getJustificacionesFiltradas() {
		return justificacionesFiltradas;
	}

	public void setJustificacionesFiltradas(List<Justificacion> justificacionesFiltradas) {
		this.justificacionesFiltradas = justificacionesFiltradas;
	}

	public List<Justificacion> getJustificacionesPropias() {
		return justificacionesPropias;
	}

	public void setJustificacionesPropias(List<Justificacion> justificacionesPropias) {
		this.justificacionesPropias = justificacionesPropias;
	}

	public List<Justificacion> getJustificacionesAnalista() {
		return justificacionesAnalista;
	}

	public void setJustificacionesAnalista(List<Justificacion> justificacionesAnalista) {
		this.justificacionesAnalista = justificacionesAnalista;
	}

	public String getEstudianteSeleccionado() {
		return estudianteSeleccionado;
	}

	public void setEstudianteSeleccionado(String estudianteSeleccionado) {
		this.estudianteSeleccionado = estudianteSeleccionado;
	}

	public List<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(List<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public List<Justificacion> getJustificacionesAnalistaFiltradas() {
		return justificacionesAnalistaFiltradas;
	}

	public void setJustificacionesAnalistaFiltradas(List<Justificacion> justificacionesAnalistaFiltradas) {
		this.justificacionesAnalistaFiltradas = justificacionesAnalistaFiltradas;
	}

	public Long getIdJustificacion() {
		return idJustificacion;
	}

	public void setIdJustificacion(Long idJustificacion) {
		this.idJustificacion = idJustificacion;
	}

	public Justificacion getJustificacionSeleccionada() {
		return justificacionSeleccionada;
	}

	public void setJustificacionSeleccionada(Justificacion justificacionSeleccionada) {
		this.justificacionSeleccionada = justificacionSeleccionada;
	}
	
	public RegistroJustificacion getRegistroJustficacion() {
	    if (registro == null) {
	        registro = new RegistroJustificacion();
	    }
	    return registro;
	}

	public void setRegistroJustficacion(RegistroJustificacion registro) {
	    this.registro = registro;
	}
	
	
}
