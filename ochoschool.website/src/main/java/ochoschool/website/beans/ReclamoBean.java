package ochoschool.website.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.Departamento;
import ochoschool.website.entidades.Estado;
import ochoschool.website.entidades.Estudiante;
import ochoschool.website.entidades.Evento;
import ochoschool.website.entidades.Genero;
import ochoschool.website.entidades.Itr;
import ochoschool.website.entidades.Localidad;
import ochoschool.website.entidades.Reclamo;
import ochoschool.website.entidades.RegistroReclamo;
import ochoschool.website.entidades.TipoReclamo;
import ochoschool.website.entidades.Tutor;
import ochoschool.website.entidades.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named("reclamoBean")
@SessionScoped
public class ReclamoBean implements Serializable {
	
    
	private static final long serialVersionUID = 1L;
	
	private static final String BASE_URL = "http://localhost:8080/ochoschool.apirest/";
	private Reclamo reclamo;
    private String tipoReclamoSeleccionado;
    private List<TipoReclamo> listaTiposReclamo;
    private boolean mostrarCamposAdicionales;
    private List<Tutor> listaTutores;
    private Long idTutorSeleccionado;
    private ApiClientAux apiClient;
    private String nombreActividad;
    private Date fechaRealizacion; 
    private Integer semestre;
    private Integer creditos;
    private Long tipoReclamoIdSeleccionado;
    @Inject
    private UsuarioBean usuarioBean;
    private List<Reclamo> reclamosPropios;
    private List<Reclamo> todosReclamos;
    private Long estadoSeleccionado;
    private List<Estado> estados;
    private List<Reclamo> reclamosFiltrados;
    private Reclamo reclamoSeleccionado;
    private Long idReclamo;
    private List<Reclamo> reclamosAnalista;
    private List<Estudiante> estudiantes;
    private String estudianteSeleccionado;
    private RegistroReclamo registro;


    @PostConstruct
    public void init() {
        reclamo = new Reclamo();
        registro = new RegistroReclamo();
        cargarTiposReclamo();
        mostrarCamposAdicionales = false;
        cargarListaTutores();
        cargarReclamosPropios();
        cargarEstados();
        reclamosFiltrados = new ArrayList<>(reclamosPropios);
        filtrarAnalista();
        cargarEstudiantes();
    }
    
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
    
    public void cargarTiposReclamo() {
        String endpointUrl = BASE_URL + "reclamo/tipo";
        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
                listaTiposReclamo = new ArrayList<>();

                for (JsonElement element : jsonArray) {
                    JsonObject tipoReclamoJson = element.getAsJsonObject();
                    TipoReclamo tipoReclamo = new TipoReclamo();
                    tipoReclamo.setIdTipoReclamo(tipoReclamoJson.get("idTipoReclamo").getAsLong());
                    tipoReclamo.setDescripcion(tipoReclamoJson.get("descripcion").getAsString());
                    listaTiposReclamo.add(tipoReclamo);
                }
            } else {
                // Manejar errores si la respuesta no es exitosa
                System.err.println("Error al obtener los tipos de reclamo. Código de respuesta: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    
    public void cargarListaTutores() {
        String endpointUrl = BASE_URL + "tutor"; 
        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
                listaTutores = new ArrayList<>();

                for (JsonElement element : jsonArray) {
                    JsonObject tutorJson = element.getAsJsonObject();
                    Tutor tutor = new Tutor();
                    tutor.setIdUsuario(tutorJson.get("idUsuario").getAsLong());
                    tutor.setPrimer_nombre(tutorJson.get("primer_nombre").getAsString());
                    tutor.setPrimer_apellido(tutorJson.get("primer_apellido").getAsString());
                    listaTutores.add(tutor);
                }
            } else {
                System.err.println("Error al obtener la lista de tutores. Código de respuesta: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String crearReclamo() {
        try {
            // Definir los tipos de reclamo que requieren tutor y campos adicionales
            List<String> tiposReclamoRequierenTutor = List.of("VME", "APE", "OPTATIVA");

            // Validar que se haya seleccionado un tipo de reclamo
            if (tipoReclamoIdSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de reclamo es obligatorio", null));
                return null;
            }

            // Obtener la descripción del tipo de reclamo seleccionado
            TipoReclamo tipoReclamoSeleccionadoObj = listaTiposReclamo.stream()
                .filter(tr -> tr.getIdTipoReclamo().equals(tipoReclamoIdSeleccionado))
                .findFirst()
                .orElse(null);

            if (tipoReclamoSeleccionadoObj == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de reclamo inválido", null));
                return null;
            }

            String tipoReclamoUpper = tipoReclamoSeleccionadoObj.getDescripcion().trim().toUpperCase();

            boolean requiereTutor = tiposReclamoRequierenTutor.contains(tipoReclamoUpper);
            boolean requiereCamposAdicionales = requiereTutor; // Según descripción

            // Validar que si se requiere tutor, esté seleccionado
            if (requiereTutor) {
                if (idTutorSeleccionado == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un tutor para este tipo de reclamo", null));
                    return null;
                }
            }

            // Validar campos adicionales si es requerido
            if (requiereCamposAdicionales) {
                if (nombreActividad == null || nombreActividad.trim().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre de la actividad es obligatorio para este tipo de reclamo", null));
                    return null;
                }
                if (fechaRealizacion == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha de realización es obligatoria para este tipo de reclamo", null));
                    return null;
                }
                if (semestre == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Semestre es obligatorio para este tipo de reclamo", null));
                    return null;
                }
                if (creditos == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Créditos son obligatorios para este tipo de reclamo", null));
                    return null;
                }
            }

            // Si se requiere tutor, buscar en la lista de tutores
            Tutor tutorSeleccionado = null;
            if (requiereTutor) {
                for (Tutor tutor : listaTutores) {
                    if (tutor.getIdUsuario().equals(idTutorSeleccionado)) {
                        tutorSeleccionado = tutor;
                        break;
                    }
                }
                if (tutorSeleccionado == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tutor seleccionado no encontrado", null));
                    return null;
                }
            }

            // Construir el objeto JSON del reclamo
            JsonObject reclamoJson = new JsonObject();

            apiClient = new ApiClientAux();

            JsonObject tipoReclamoJson = apiClient.obtenerJsonDesdeEndpointTipoReclamo(tipoReclamoIdSeleccionado);
            reclamoJson.add("tipoReclamo", tipoReclamoJson);
            reclamoJson.addProperty("descripcion", reclamo.getDescripcion());
            reclamoJson.addProperty("titulo", reclamo.getTitulo());
            
            JsonObject estadoReclamoJson = apiClient.obtenerJsonDesdeEndpointGeneral("mantenimiento/estado", 1);
            reclamoJson.add("estado", estadoReclamoJson);
            
			Usuario usuario = usuarioBean.getUsuarioLogueado();
            JsonObject estudianteJson = apiClient.obtenerJsonDesdeEndpointGeneral("estudiante", usuario.getIdUsuario());
            reclamoJson.add("estudiante", estudianteJson);
            
            Long fechaRealizacionActual = System.currentTimeMillis();
            reclamoJson.addProperty("fechaHora", fechaRealizacionActual);
            
            

            // Agregar campos adicionales si es requerido
            if (requiereCamposAdicionales) {
            	Long fechaRealizacionTimestamp = fechaRealizacion != null ? fechaRealizacion.getTime() : null;
                reclamoJson.addProperty("nombre", nombreActividad.trim());
                reclamoJson.addProperty("fechaRealizacion", fechaRealizacionTimestamp); // Timestamp en milisegundos
                reclamoJson.addProperty("semestre", semestre);
                reclamoJson.addProperty("creditos", creditos);
            }

            // Agregar tutor al reclamo si es requerido
            if (requiereTutor) {
                JsonObject tutorJson = new JsonObject();
                tutorJson.addProperty("idUsuario", tutorSeleccionado.getIdUsuario());
                // Agrega otros campos del tutor si es necesario
                reclamoJson.add("tutor", tutorJson);
            }

            System.out.println("JSON del reclamo a enviar: " + reclamoJson.toString());

            // Enviar el reclamo al backend
            String endpointUrl = BASE_URL + "reclamo"; // Ajusta el endpoint según tu API
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Configurar conexión para POST
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Escribir el JSON en el cuerpo de la solicitud
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = reclamoJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta del servidor: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                // Reclamo creado exitosamente
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Reclamo creado exitosamente", null));

                // Opcional: Resetear los campos después de crear el reclamo
                resetCampos();
                cargarReclamosPropios();

                return "paginaExito?faces-redirect=true"; // Navegar a la página de éxito
            } else {
                // Leer el mensaje de error del servidor
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
                StringBuilder errorResponse = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                System.err.println("Error al crear el reclamo: " + errorResponse.toString());
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear el reclamo", errorResponse.toString()));
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores generales
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear el reclamo", e.getMessage()));
            return null;
        }
    }

    
    private void resetCampos() {
        reclamo = new Reclamo();
        idTutorSeleccionado = null;
        tipoReclamoSeleccionado = null;
        nombreActividad = null;
        fechaRealizacion = null;
        semestre = null;
        creditos = null;
        reclamoSeleccionado = new Reclamo();
    }
    
    public void cargarTodosLosReclamos() {
        String endpointUrl = BASE_URL + "reclamo"; // Ajusta este endpoint según tu API

        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(con.getInputStream(), "utf-8");
                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
                todosReclamos = new ArrayList<>();

                for (JsonElement element : jsonArray) {
                    JsonObject reclamoJson = element.getAsJsonObject();
                    Reclamo reclamo = new Reclamo();
                    reclamo.setIdReclamo(reclamoJson.get("idReclamo").getAsLong());
                    reclamo.setTitulo(reclamoJson.get("titulo").getAsString());
                    reclamo.setDescripcion(reclamoJson.get("descripcion").getAsString());
                    reclamo.setFechaHora(new Date(reclamoJson.get("fechaHora").getAsLong()));
                    
                    // Parsear evento si existe
                    JsonElement eventoElement = reclamoJson.get("evento");
                    if (eventoElement != null && !eventoElement.isJsonNull()) {
                        JsonObject eventoJson = eventoElement.getAsJsonObject();
                        Evento evento = new Evento();                    
                        reclamo.setEvento(evento);
                    }

                    // Parsear estudiante
                    JsonElement estudianteElement = reclamoJson.get("estudiante");
                    if (estudianteElement != null && !estudianteElement.isJsonNull()) {
                        JsonObject estudianteJson = estudianteElement.getAsJsonObject();
                        Estudiante estudiante = new Estudiante();
                        estudiante.setIdUsuario(estudianteJson.get("idUsuario").getAsLong());
                        estudiante.setPrimer_nombre(estudianteJson.get("primer_nombre").getAsString());
                        estudiante.setPrimer_apellido(estudianteJson.get("primer_apellido").getAsString());
                        reclamo.setEstudiante(estudiante);
                    }

                    // Parsear estado correctamente
                    JsonElement estadoElement = reclamoJson.get("estado");
                    if (estadoElement != null && !estadoElement.isJsonNull()) {
                        JsonObject estadoJson = estadoElement.getAsJsonObject();
                        Estado estado = new Estado();
                        if (estadoJson.has("idEstado") && !estadoJson.get("idEstado").isJsonNull()) {
                            estado.setIdEstado(estadoJson.get("idEstado").getAsLong());
                        }
                        if (estadoJson.has("descripcion") && !estadoJson.get("descripcion").isJsonNull()) {
                            estado.setDescripcion(estadoJson.get("descripcion").getAsString());
                        }
                        reclamo.setEstado(estado);
                    } else {
                        reclamo.setEstado(null); 
                    }

                    // Parsear tipoReclamo
                    JsonElement tipoReclamoElement = reclamoJson.get("tipoReclamo");
                    if (tipoReclamoElement != null && !tipoReclamoElement.isJsonNull()) {
                        JsonObject tipoReclamoJson = tipoReclamoElement.getAsJsonObject();
                        TipoReclamo tipoReclamo = new TipoReclamo();
                        tipoReclamo.setIdTipoReclamo(tipoReclamoJson.get("idTipoReclamo").getAsLong());
                        tipoReclamo.setDescripcion(tipoReclamoJson.get("descripcion").getAsString());
                        reclamo.setTipoReclamo(tipoReclamo);
                    }

                    // Parsear tutor si existe
                    JsonElement tutorElement = reclamoJson.get("tutor");
                    if (tutorElement != null && !tutorElement.isJsonNull()) {
                        JsonObject tutorJson = tutorElement.getAsJsonObject();
                        Tutor tutor = new Tutor();
                        tutor.setIdUsuario(tutorJson.get("idUsuario").getAsLong());
                        tutor.setPrimer_nombre(tutorJson.get("primer_nombre").getAsString());
                        tutor.setPrimer_apellido(tutorJson.get("primer_apellido").getAsString());
                        // Asigna otros campos según sea necesario
                        reclamo.setTutor(tutor);
                    }

                    // Asignar campos adicionales si existen
                    if (reclamoJson.has("nombre") && !reclamoJson.get("nombre").isJsonNull()) {
                        reclamo.setNombre(reclamoJson.get("nombre").getAsString());
                    }
                    if (reclamoJson.has("semestre") && !reclamoJson.get("semestre").isJsonNull()) {
                        reclamo.setSemestre(reclamoJson.get("semestre").getAsInt());
                    }
                    if (reclamoJson.has("fechaRealizacion") && !reclamoJson.get("fechaRealizacion").isJsonNull()) {
                        reclamo.setFechaRealizacion(new Date(reclamoJson.get("fechaRealizacion").getAsLong()));
                    }
                    if (reclamoJson.has("creditos") && !reclamoJson.get("creditos").isJsonNull()) {
                        reclamo.setCreditos(reclamoJson.get("creditos").getAsInt());
                    }

                    todosReclamos.add(reclamo);
                }
            } else {
                System.err.println("Error al obtener todos los reclamos. Código de respuesta: " + responseCode);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar reclamos", null));
            }
        } catch (IOException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar reclamos", e.getMessage()));
        }
    }


    
    public void cargarReclamosPropios() {
        // Cargar todos los reclamos primero
        cargarTodosLosReclamos();

        if (todosReclamos == null) {
            // Manejar error si no se pudieron cargar los reclamos
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudieron cargar los reclamos.", null));
            return;
        }

        // Obtener el usuario logueado
        Usuario usuario = usuarioBean.getUsuarioLogueado();
        if (usuario == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no logueado.", null));
            return;
        }
        Long idUsuario = usuario.getIdUsuario();

        // Filtrar los reclamos propios por usuario
        reclamosPropios = new ArrayList<>();
        for (Reclamo reclamo : todosReclamos) {
            if (reclamo.getEstudiante() != null && idUsuario.equals(reclamo.getEstudiante().getIdUsuario())) {
                reclamosPropios.add(reclamo);
            }
        }

        // Inicializar reclamosFiltrados con todos los reclamos propios
        reclamosFiltrados = new ArrayList<>(reclamosPropios);
    }

    
    public void filtrarPorEstado() {
        // Reiniciar la lista filtrada como una copia de todos los reclamos propios
        reclamosFiltrados = new ArrayList<>(reclamosPropios);
        
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
            
            // Filtrar la lista removiendo reclamos que no coinciden con el estado seleccionado
            reclamosFiltrados.removeIf(reclamo -> 
                reclamo.getEstado() == null || !reclamo.getEstado().getIdEstado().equals(idEstado)
            );
        }
    }
    
    public void filtrarAnalista() { 
    	
        reclamosAnalista = new ArrayList<>(todosReclamos);
        
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
            
            // Filtrar la lista removiendo reclamos que no coinciden con el estado seleccionado
            reclamosAnalista.removeIf(reclamo -> 
                reclamo.getEstado() == null || !reclamo.getEstado().getIdEstado().equals(idEstado)
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
            
            // Filtrar la lista removiendo reclamos que no coinciden con el estudiante seleccionado
            reclamosAnalista.removeIf(reclamo -> 
                reclamo.getEstudiante() == null || !reclamo.getEstudiante().getIdUsuario().equals(idUsuario)
            );
        }
    }
    
    public String irAModificarReclamo(Long idReclamo) {
		if (idReclamo != null) {
			cargarReclamoSeleccionadoPorId(idReclamo);
			return "/pages/ModificarReclamo.xhtml?faces-redirect=true&includeViewParams=true&idReclamo=" + idReclamo;
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un reclamo."));
			return null;
		}
	}
    
    public String irAccionReclamo(Long idReclamo) {
		if (idReclamo != null) {
			cargarReclamoSeleccionadoPorId(idReclamo);
			return "/pages/AccionReclamo.xhtml?faces-redirect=true&includeViewParams=true&idReclamo=" + idReclamo;
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un reclamo."));
			return null;
		}
	}
    
    public String irAModificarEstadoReclamo(Long idReclamo) {
		if (idReclamo != null) {
			cargarReclamoSeleccionadoPorId(idReclamo);
			return "/pages/ModificarEstadoReclamo.xhtml?faces-redirect=true&includeViewParams=true&idReclamo=" + idReclamo;
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un reclamo."));
			return null;
		}
	}
    
    public void cargarReclamoSeleccionadoPorId(Long idReclamo) {
        if (idReclamo != null) {
            try {
                // Definir el endpoint para obtener el reclamo por ID
                String endpointUrl = BASE_URL + "reclamo/" + idReclamo;
                URL url = new URL(endpointUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leer la respuesta del servidor
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Utilizar Gson para deserializar la respuesta JSON en un objeto Reclamo
                    reclamoSeleccionado = gson.fromJson(response.toString(), Reclamo.class);

                    // Procesar objetos anidados si es necesario
                    if (reclamoSeleccionado.getEstado() == null) {
                        reclamoSeleccionado.setEstado(new Estado());
                    }
                    if (reclamoSeleccionado.getEstado() != null) {
                        estadoSeleccionado = reclamoSeleccionado.getEstado().getIdEstado();
                    } else {
                        estadoSeleccionado = null;
                    }
                    if (reclamoSeleccionado.getEstudiante() == null) {
                        reclamoSeleccionado.setEstudiante(new Estudiante());
                    }
                    if (reclamoSeleccionado.getTipoReclamo() == null) {
                        reclamoSeleccionado.setTipoReclamo(new TipoReclamo());
                    }
                    if (reclamoSeleccionado.getTutor() != null) {
                        idTutorSeleccionado = reclamoSeleccionado.getTutor().getIdUsuario();
                    } else {
                        idTutorSeleccionado = null;
                    }
                    if (reclamoSeleccionado.getEvento() == null) {
                        reclamoSeleccionado.setEvento(new Evento());
                    }

                    System.out.println("Reclamo seleccionado: " + reclamoSeleccionado.toString());

                } else {
                    System.err.println("Error al obtener Reclamo: Código HTTP " + responseCode);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo cargar la información del reclamo."));
                }

            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "Ocurrió un error al cargar el reclamo."));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ID de reclamo no válido."));
        }
    }
    
    public String modificarReclamo() {
        try {
            // Validar que se haya seleccionado un tipo de reclamo
            if (reclamoSeleccionado.getTipoReclamo() == null || reclamoSeleccionado.getTipoReclamo().getIdTipoReclamo() == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de reclamo es obligatorio", null));
                return null;
            }

            // Obtener la descripción del tipo de reclamo seleccionado
            TipoReclamo tipoReclamoSeleccionadoObj = listaTiposReclamo.stream()
                .filter(tr -> tr.getIdTipoReclamo().equals(reclamoSeleccionado.getTipoReclamo().getIdTipoReclamo()))
                .findFirst()
                .orElse(null);

            if (tipoReclamoSeleccionadoObj == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tipo de reclamo inválido", null));
                return null;
            }

            String tipoReclamoUpper = tipoReclamoSeleccionadoObj.getDescripcion().trim().toUpperCase();

            List<String> tiposReclamoRequierenTutor = List.of("VME", "APE", "OPTATIVA");
            boolean requiereTutor = tiposReclamoRequierenTutor.contains(tipoReclamoUpper);
            boolean requiereCamposAdicionales = requiereTutor; // Según descripción

            // Validar que si se requiere tutor, esté seleccionado
            if (requiereTutor) {
                if (idTutorSeleccionado == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un tutor para este tipo de reclamo", null));
                    return null;
                }
            }

            // Validar campos adicionales si es requerido
            if (requiereCamposAdicionales) {
                if (reclamoSeleccionado.getNombre() == null || reclamoSeleccionado.getNombre().trim().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre de la actividad es obligatorio para este tipo de reclamo", null));
                    return null;
                }
                if (reclamoSeleccionado.getFechaRealizacion() == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fecha de realización es obligatoria para este tipo de reclamo", null));
                    return null;
                }
                if (reclamoSeleccionado.getSemestre() == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Semestre es obligatorio para este tipo de reclamo", null));
                    return null;
                }
                if (reclamoSeleccionado.getCreditos() == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Créditos son obligatorios para este tipo de reclamo", null));
                    return null;
                }
            }

            // Asignar el objeto Tutor basado en idTutorSeleccionado
            if (requiereTutor) {
                Tutor tutorSeleccionado = listaTutores.stream()
                    .filter(tutor -> tutor.getIdUsuario().equals(idTutorSeleccionado))
                    .findFirst()
                    .orElse(null);

                if (tutorSeleccionado != null) {
                    reclamoSeleccionado.setTutor(tutorSeleccionado);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tutor seleccionado no válido", null));
                    return null;
                }
            } else {
                reclamoSeleccionado.setTutor(null); // Si no se requiere tutor
            }

            // Construir el objeto JSON del reclamo seleccionado
            JsonObject reclamoJson = new JsonObject();

            Date date = reclamoSeleccionado.getFechaHora();
            Long timestamp;
            if (date != null) {
                timestamp = date.getTime();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fecha de creación del reclamo es obligatoria."));
                return null;
            }

            apiClient = new ApiClientAux();

            JsonObject tipoReclamoJson = apiClient.obtenerJsonDesdeEndpointTipoReclamo(reclamoSeleccionado.getTipoReclamo().getIdTipoReclamo());
            reclamoJson.addProperty("idReclamo", reclamoSeleccionado.getIdReclamo());
            reclamoJson.add("tipoReclamo", tipoReclamoJson);
            reclamoJson.addProperty("descripcion", reclamoSeleccionado.getDescripcion());
            reclamoJson.addProperty("titulo", reclamoSeleccionado.getTitulo());
            reclamoJson.addProperty("fechaHora", timestamp);

            JsonObject estadoReclamoJson = apiClient.obtenerJsonDesdeEndpointGeneral("mantenimiento/estado", reclamoSeleccionado.getEstado().getIdEstado());
            reclamoJson.add("estado", estadoReclamoJson);

            Usuario usuario = usuarioBean.getUsuarioLogueado();
            JsonObject estudianteJson = apiClient.obtenerJsonDesdeEndpointGeneral("estudiante", usuario.getIdUsuario());
            reclamoJson.add("estudiante", estudianteJson);

            // Agregar campos adicionales si es requerido
            if (requiereCamposAdicionales) {
                Long fechaRealizacionTimestamp = reclamoSeleccionado.getFechaRealizacion() != null ? reclamoSeleccionado.getFechaRealizacion().getTime() : null;
                reclamoJson.addProperty("nombre", reclamoSeleccionado.getNombre().trim());
                reclamoJson.addProperty("fechaRealizacion", fechaRealizacionTimestamp); // Timestamp en milisegundos
                reclamoJson.addProperty("semestre", reclamoSeleccionado.getSemestre());
                reclamoJson.addProperty("creditos", reclamoSeleccionado.getCreditos());
            }

            // Agregar tutor al reclamo si es requerido
            if (requiereTutor) {
                JsonObject tutorJson = new JsonObject();
                tutorJson.addProperty("idUsuario", reclamoSeleccionado.getTutor().getIdUsuario());
                // Agrega otros campos del tutor si es necesario
                reclamoJson.add("tutor", tutorJson);
            }

            System.out.println("JSON del reclamo a modificar: " + reclamoJson.toString());

            // Enviar el reclamo al backend para su modificación
            String endpointUrl = BASE_URL + "reclamo";
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Configurar conexión para PUT
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Escribir el JSON en el cuerpo de la solicitud
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = reclamoJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta del servidor: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Reclamo modificado exitosamente
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Reclamo modificado exitosamente", null));

                // Opcional: Actualizar la lista de reclamos
                cargarReclamosPropios();

                return "paginaExito?faces-redirect=true"; // Navegar a la página de éxito
            } else {
                // Leer el mensaje de error del servidor si está disponible
                InputStream errorStream = con.getErrorStream();
                StringBuilder errorResponse = new StringBuilder();

                if (errorStream != null) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream, "utf-8"))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            errorResponse.append(responseLine.trim());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    errorResponse.append("No se proporcionó mensaje de error por el servidor.");
                }

                System.err.println("Error al modificar el reclamo: " + errorResponse.toString());
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar el reclamo", errorResponse.toString()));
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores generales
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar el reclamo", e.getMessage()));
            return null;
        }
    }
    
    public String registrarAccion() {
        try {       

            // Construir el objeto JSON del registro
            JsonObject registroJson = new JsonObject();

            apiClient = new ApiClientAux();

            JsonObject reclamoJson = apiClient.obtenerJsonDesdeEndpointGeneral("reclamo", reclamoSeleccionado.getIdReclamo());
            registroJson.add("reclamo", reclamoJson);
            registroJson.addProperty("detalle", registro.getDetalle());
            
			Usuario usuario = usuarioBean.getUsuarioLogueado();
            JsonObject analistaJson = apiClient.obtenerJsonDesdeEndpointGeneral("analista", usuario.getIdUsuario());
            registroJson.add("analista", analistaJson);
            
            Long fechaRealizacionActual = System.currentTimeMillis();
            registroJson.addProperty("fechaHora", fechaRealizacionActual);

            // Enviar el registro al backend
            String endpointUrl = BASE_URL + "reclamo/registro"; 
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
                // Reclamo creado exitosamente
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro creado exitosamente", null));

                // Opcional: Resetear los campos después de crear el reclamo
                resetCampos();

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
    
    public String cambiarEstado() {
        try {
          
            // Construir el objeto JSON del reclamo seleccionado
            JsonObject reclamoJson = new JsonObject();

            Date date = reclamoSeleccionado.getFechaHora();
            Long timestamp;
            timestamp = date.getTime();
        
            apiClient = new ApiClientAux();

            JsonObject tipoReclamoJson = apiClient.obtenerJsonDesdeEndpointTipoReclamo(reclamoSeleccionado.getTipoReclamo().getIdTipoReclamo());
            reclamoJson.addProperty("idReclamo", reclamoSeleccionado.getIdReclamo());
            reclamoJson.add("tipoReclamo", tipoReclamoJson);
            reclamoJson.addProperty("descripcion", reclamoSeleccionado.getDescripcion());
            reclamoJson.addProperty("titulo", reclamoSeleccionado.getTitulo());
            reclamoJson.addProperty("fechaHora", timestamp);

            JsonObject estadoReclamoJson = apiClient.obtenerJsonDesdeEndpointGeneral("mantenimiento/estado", estadoSeleccionado);
            reclamoJson.add("estado", estadoReclamoJson);

            JsonObject estudianteJson = apiClient.obtenerJsonDesdeEndpointGeneral("estudiante", reclamoSeleccionado.getEstudiante().getIdUsuario());
            reclamoJson.add("estudiante", estudianteJson);
           
            
            try {
            	JsonObject tutorJson = new JsonObject();
                tutorJson.addProperty("idUsuario", reclamoSeleccionado.getTutor().getIdUsuario());
                reclamoJson.add("tutor", tutorJson);
                Long fechaRealizacionTimestamp = reclamoSeleccionado.getFechaRealizacion() != null ? reclamoSeleccionado.getFechaRealizacion().getTime() : null;
                reclamoJson.addProperty("nombre", reclamoSeleccionado.getNombre());
                reclamoJson.addProperty("fechaRealizacion", fechaRealizacionTimestamp); // Timestamp en milisegundos
                reclamoJson.addProperty("semestre", reclamoSeleccionado.getSemestre());
                reclamoJson.addProperty("creditos", reclamoSeleccionado.getCreditos());
            } catch (Exception e) {

            }
            
            System.out.println("JSON del reclamo a modificar: " + reclamoJson.toString());

            // Enviar el reclamo al backend para su modificación
            String endpointUrl = BASE_URL + "reclamo";
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Configurar conexión para PUT
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Escribir el JSON en el cuerpo de la solicitud
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = reclamoJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta del servidor: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Reclamo modificado exitosamente
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Reclamo modificado exitosamente", null));

                // Opcional: Actualizar la lista de reclamos
                estadoSeleccionado = null;
                cargarReclamosPropios();
                filtrarAnalista();

                return "paginaExito?faces-redirect=true"; // Navegar a la página de éxito
            } else {
                // Leer el mensaje de error del servidor si está disponible
                InputStream errorStream = con.getErrorStream();
                StringBuilder errorResponse = new StringBuilder();

                if (errorStream != null) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream, "utf-8"))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            errorResponse.append(responseLine.trim());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    errorResponse.append("No se proporcionó mensaje de error por el servidor.");
                }

                System.err.println("Error al modificar el reclamo: " + errorResponse.toString());
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar el reclamo", errorResponse.toString()));
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores generales
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar el reclamo", e.getMessage()));
            return null;
        }
    }
    
    public String eliminarReclamo(Long idReclamo) {
        try {
            System.out.println("Eliminar reclamo con id: " + idReclamo);
            
            if (idReclamo == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha proporcionado un ID de reclamo válido."));
                return null;
            }

            String endpointUrl = BASE_URL + "reclamo/" + idReclamo; 
            URL url = new URL(endpointUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Configurar conexión
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Leer la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta del servidor: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Reclamo eliminado exitosamente
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Reclamo eliminado exitosamente", null));

                // Opcional: Actualizar la lista de reclamos después de eliminar
                cargarReclamosPropios();

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
                System.err.println("Error al eliminar el reclamo: " + errorMsg);

                // Analizar el mensaje de error para detectar violación de restricción
                if (errorMsg.contains("ORA-02292") || errorMsg.contains("ConstraintViolationException")) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede eliminar el reclamo porque tiene registros asociados.", "No se puede eliminar el reclamo porque tiene registros asociados."));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar el reclamo", "Ocurrió un error al eliminar el reclamo."));
                    
                }
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores generales
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar el reclamo", e.getMessage()));
            return null;
        }
    }

    
    public List<TipoReclamo> getListaTiposReclamo() {
        return listaTiposReclamo;
    }

    public String getTipoReclamoSeleccionado() {
        return tipoReclamoSeleccionado;
    }

    public void setTipoReclamoSeleccionado(String tipoReclamoSeleccionado) {
        this.tipoReclamoSeleccionado = tipoReclamoSeleccionado;
        //onTipoReclamoChange(); // Llamar al método para actualizar la visibilidad de los campos adicionales
    }
    
    public List<Tutor> getListaTutores() {
        return listaTutores;
    }

    public void setListaTutores(List<Tutor> listaTutores) {
        this.listaTutores = listaTutores;
    }

    public Long getIdTutorSeleccionado() {
        return idTutorSeleccionado;
    }

    public void setIdTutorSeleccionado(Long idTutorSeleccionado) {
        this.idTutorSeleccionado = idTutorSeleccionado;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }

	public String getNombreActividad() {
		return nombreActividad;
	}

	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}

	public Date getFechaRealizacion() {
		return fechaRealizacion;
	}

	public void setFechaRealizacion(Date fechaRealizacion) {
		this.fechaRealizacion = fechaRealizacion;
	}

	public Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
	}

	public Integer getCreditos() {
		return creditos;
	}

	public void setCreditos(Integer creditos) {
		this.creditos = creditos;
	}

	public Long getTipoReclamoIdSeleccionado() {
		return tipoReclamoIdSeleccionado;
	}

	public void setTipoReclamoIdSeleccionado(Long tipoReclamoIdSeleccionado) {
		this.tipoReclamoIdSeleccionado = tipoReclamoIdSeleccionado;
	}

	public void setListaTiposReclamo(List<TipoReclamo> listaTiposReclamo) {
		this.listaTiposReclamo = listaTiposReclamo;
	}
	
	public List<Reclamo> getReclamosPropios() {
	    return reclamosPropios;
	}
	
	public void setReclamosPropios(List<Reclamo> reclamosPropios) {
	    this.reclamosPropios = reclamosPropios;
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
	
	public List<Reclamo> getReclamosFiltrados() {
	    return reclamosFiltrados;
	}

	public void setReclamosFiltrados(List<Reclamo> reclamosFiltrados) {
	    this.reclamosFiltrados = reclamosFiltrados;
	}
	
	public List<Reclamo> getReclamosAnalista() {
	    return reclamosAnalista;
	}

	public void setReclamosAnalista(List<Reclamo> reclamosAnalista) {
	    this.reclamosAnalista = reclamosAnalista;
	}
	
	
	
	public Reclamo getReclamoSeleccionado() {
	    return reclamoSeleccionado;
	}

	public void setReclamoSeleccionado(Reclamo reclamoSeleccionado) {
	    this.reclamoSeleccionado = reclamoSeleccionado;
	}
	
	public Long getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(Long idReclamo) {
        this.idReclamo = idReclamo;
    }

	public List<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(List<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public String getEstudianteSeleccionado() {
		return estudianteSeleccionado;
	}

	public void setEstudianteSeleccionado(String estudianteSeleccionado) {
		this.estudianteSeleccionado = estudianteSeleccionado;
	}
	
	public RegistroReclamo getRegistro() {
	    return registro;
	}

	public void setRegistro(RegistroReclamo registro) {
	    this.registro = registro;
	}
    	
}
