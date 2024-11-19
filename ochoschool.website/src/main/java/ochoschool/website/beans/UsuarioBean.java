package ochoschool.website.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.primefaces.context.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ochoschool.website.entidades.ApiClient;
import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.Area;
import ochoschool.website.entidades.Credenciales;
import ochoschool.website.entidades.Departamento;
import ochoschool.website.entidades.Estudiante;
import ochoschool.website.entidades.Genero;
import ochoschool.website.entidades.Itr;
import ochoschool.website.entidades.Localidad;
import ochoschool.website.entidades.Rol;
import ochoschool.website.entidades.Tutor;
import ochoschool.website.entidades.Usuario;
import ochoschool.website.servicios.GestionUsuarioService;
import ochoschool.website.servicios.ServicioException;

@Named("usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	GestionUsuarioService gestionUsuarioService = new GestionUsuarioService();

	private String usuario;
	private String clave;
	private String tipoUsuario;
	private String BASE_URL = gestionUsuarioService.getBASE_URL();
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private ApiClientAux apiClient;
	private List<Usuario> listaUsuarios = null;
	private List<Genero> listaGeneros = null;

	private List<Itr> listaItr = null;
	private List<Departamento> listaDepartamentos = null;
	private List<Localidad> listaLocalidades = null;
	private String localidadesJSON;

	private Long generoSeleccionado;
	private Long itrSeleccionado;
	private Long departamentoSeleccionado;
	private Long localidadSeleccionada;
	protected Map<String, Object> usuarioSeleccionadoo;
	private Usuario usuarioSeleccionado;
	protected Usuario usuarioNuevo = new Usuario();
	private boolean mostrarAnioIngreso;
	private Long areaSeleccionada;
	private Long rolSeleccionado;
	private List<Area> listaAreas = null;
	private List<Rol> listaRoles = null;
	private boolean mostrarArea;
	private boolean mostrarRol;
	private JsonNode datos;
	private List<Map<String, Object>> datosList;
	private List<SelectItem> tiposUsuarios;
	private List<SelectItem> itrs;
	private List<SelectItem> generaciones;
	private List<SelectItem> estadosActivos;
	private List<SelectItem> estadosConfirmados;
	private String tipoUsuarioSeleccionado;
	private boolean filtrandoEstudiantes;
	private String generacionSeleccionada;
	private List<Map<String, Object>> datosFiltrados;
	private String estadoActivoSeleccionado;
	private String estadoConfirmadoSeleccionado;
	private Long idUsuario;

	private boolean estaLogueado = false;
	private String generacion;
	String endpointEstudiante = BASE_URL + "estudiante";
	String endpointTutor = BASE_URL + "tutor";
	String endpointAnalista = BASE_URL + "analista";
	private Estudiante estudianteSeleccionado;
	private Tutor tutorSeleccionado;
	private Usuario usuarioLogueado;
	private String nuevaClave;
	private String confirmarClave;
	 private Credenciales credenciales = new Credenciales();

	public String getGeneracion() {
		return generacion;
	}

	public void setGeneracion(String generacion) {
		this.generacion = generacion;
	}

	public Long getAreaSeleccionada() {
		return areaSeleccionada;
	}

	public void setAreaSeleccionada(Long areaSeleccionada) {
		this.areaSeleccionada = areaSeleccionada;
	}

	public Long getRolSeleccionado() {
		return rolSeleccionado;
	}

	public void setRolSeleccionado(Long rolSeleccionado) {
		this.rolSeleccionado = rolSeleccionado;
	}

	public void setListaAreas(List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	}

	public void setListaRoles(List<Rol> listaRoles) {
		this.listaRoles = listaRoles;
	}

	public UsuarioBean() {
		super();
	}

	@PostConstruct
	public void init() {
		// Inicializar las listas al cargar la página
		cargarDatos("usuarios");
		cargarListaGeneros();
		cargarListaItr();
		cargarListaDepartamentos();
		cargarListaLocalidades();
		cargarListaAreas();
		cargarListaRoles();
		estadoActivoSeleccionado = "S";

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
		

	public void cargarDatos(String tipoDatos) {
		ApiClient apiClient = new ApiClient();

		try {
			if ("usuarios".equals(tipoDatos)) {
				datos = apiClient.getDatosUsuarios();
			} else if ("eventos".equals(tipoDatos)) {
				datos = apiClient.getDatosEventos();
			} else if ("reclamos".equals(tipoDatos)) {
				datos = apiClient.getDatosReclamos();
			}

			if (datos == null || datos.isEmpty()) {
				System.out.println("No se obtuvieron datos de " + tipoDatos);
			} else {
				System.out.println("Número de elementos obtenidos: " + datos.size());
				convertirDatosJsonALista();
				obtenerTiposUsuarios(); // Llama al método para obtener los tipos de usuarios
				obtenerItr();
				obtenerGeneraciones();
				obtenerEstadosActivos();
				obtenerEstadosConfirmados();

				datosList = new ArrayList<>();
				for (JsonNode objeto : datos) {
					datosList.add(objectMapper.convertValue(objeto, Map.class));
				}

				// Reset filtered data
				datosFiltrados = null;
			}
		} catch (Exception e) {
			System.out.println("Error al cargar datos de " + tipoDatos + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void convertirDatosJsonALista() {
		ObjectMapper mapper = new ObjectMapper();
		datosList = new ArrayList<>();
		for (JsonNode objeto : datos) {
			datosList.add(mapper.convertValue(objeto, Map.class));
		}
		System.out.println("Lista creada, tamaño: " + datosList.size());
	}

	private void obtenerTiposUsuarios() {
		Set<String> tiposSet = new HashSet<>();
		for (Map<String, Object> usuario : datosList) {
			String tipo = (String) usuario.get("tipo_usuario");
			if (tipo != null) {
				tiposSet.add(tipo);
			}
		}

		tiposUsuarios = new ArrayList<>();
		for (String tipo : tiposSet) {
			tiposUsuarios.add(new SelectItem(tipo, tipo));
		}
	}

	private void obtenerItr() {
		Set<Map<String, Object>> itrSet = new HashSet<>();

		for (Map<String, Object> usuario : datosList) {
			Map<String, Object> itr = (Map<String, Object>) usuario.get("itr");
			if (itr != null) {
				itrSet.add(itr);
			}
		}

		itrs = new ArrayList<>();
		for (Map<String, Object> itr : itrSet) {
			Long idItr = ((Number) itr.get("idItr")).longValue();
			String nombreItr = (String) itr.get("nombre");
			itrs.add(new SelectItem(idItr, nombreItr));
		}
	}

	private void obtenerGeneraciones() {
		Set<String> generacionesSet = new HashSet<>();

		// Recorre la lista de usuarios para obtener las generaciones
		for (Map<String, Object> usuario : datosList) {
			if ("ESTUDIANTE".equals(usuario.get("tipo_usuario"))) {
				String generacion = (String) usuario.get("generacion");
				if (generacion != null) {
					generacionesSet.add(generacion); // Añadir generación al set
				}
			}
		}

		// Convertimos el conjunto a una lista de SelectItems
		generaciones = new ArrayList<>();
		for (String gen : generacionesSet) {
			generaciones.add(new SelectItem(gen, gen));
		}

		// Verificación de contenido
		System.out.println("Generaciones cargadas: " + generaciones.size());
	}

	private void obtenerEstadosActivos() {
		estadosActivos = new ArrayList<>();
		estadosActivos.add(new SelectItem("S", "Activo")); // Usuarios activos
		estadosActivos.add(new SelectItem("N", "Inactivo")); // Usuarios inactivos
	}
	
	private void obtenerEstadosConfirmados() {
		estadosConfirmados = new ArrayList<>();
		estadosConfirmados.add(new SelectItem("", "Todos"));
		estadosConfirmados.add(new SelectItem("S", "Confirmado")); 
		estadosConfirmados.add(new SelectItem("N", "Sin Confirmar"));
	}

	public void filtrarUsuarios() {
		if ("ESTUDIANTE".equals(tipoUsuarioSeleccionado)) {
			filtrandoEstudiantes = true;
		} else {
			filtrandoEstudiantes = false;
			generacionSeleccionada = null; // Reiniciar generación si no se está filtrando por estudiantes
		}
		filtrarDatosAcumulados(); // Llamamos al método que aplica todos los filtros
	}

	public void filtrarDatosAcumulados() {

		System.out.println("Ejecutando filtrarDatosAcumulados...");
		datosFiltrados = new ArrayList<>(datosList); // Copia la lista completa de datos

		// Filtro por tipo de usuario
		if (tipoUsuarioSeleccionado != null && !tipoUsuarioSeleccionado.isEmpty()) {
			datosFiltrados.removeIf(usuario -> !tipoUsuarioSeleccionado.equals(usuario.get("tipo_usuario")));
		}

		// Filtro por ITR
		if (itrSeleccionado != null) {
			datosFiltrados.removeIf(usuario -> {
				Map<String, Object> itr = (Map<String, Object>) usuario.get("itr");
				if (itr == null)
					return true; // Exclude users without an ITR
				Number itrId = (Number) itr.get("idItr");
				return !itrSeleccionado.equals(itrId.longValue());
			});
		}

		// Filtro por generación (solo si estamos filtrando estudiantes)
		if (filtrandoEstudiantes && generacionSeleccionada != null && !generacionSeleccionada.isEmpty()) {
			datosFiltrados.removeIf(usuario -> !"ESTUDIANTE".equals(usuario.get("tipo_usuario"))
					|| !generacionSeleccionada.equals(usuario.get("generacion")));
		}

		// Filtro por estado (Confirmado)
		if (estadoConfirmadoSeleccionado != null && !estadoConfirmadoSeleccionado.isEmpty()) {
			datosFiltrados.removeIf(usuario -> !estadoConfirmadoSeleccionado.equals(usuario.get("confirmado")));
		}
		
		 if ("N".equals(estadoActivoSeleccionado)) {
		        // Si se selecciona "N", mostrar solo usuarios inactivos
		        datosFiltrados.removeIf(usuario -> !"N".equals(usuario.get("activo")));
		    } else {
		        // En cualquier otro caso (incluyendo "S" y "Todos"), mostrar solo usuarios activos
		        datosFiltrados.removeIf(usuario -> !"S".equals(usuario.get("activo")));
		    }
	}

	private String getItrNombrePorId(Long itrId) {
		// Encuentra el nombre del ITR a partir del ID en `listaItr`
		for (Itr itr : listaItr) {
			if (itr.getIdItr().equals(itrId)) {
				return itr.getNombre();
			}
		}
		return null;
	}

	public String irAModificarUsuario(Long idUsuario) {
		if (idUsuario != null) {
			cargarUsuarioSeleccionadoPorId(idUsuario);
			return "/pages/ModificarUsuario.xhtml?faces-redirect=true&includeViewParams=true&idUsuario=" + idUsuario;
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un usuario."));
			return null;
		}
	}

	public void cargarUsuarioSeleccionadoPorId(Long idUsuario) {
	    if (idUsuario != null) {
	        try {
	            // Fetch the Usuario data from the API
	            String endpointUrl = BASE_URL + "usuario/" + idUsuario;
	            URL url = new URL(endpointUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setRequestMethod("GET");

	            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
	                StringBuilder response = new StringBuilder();
	                String inputLine;
	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                in.close();

	                // Utilizar la instancia personalizada de Gson
	                usuarioSeleccionado = gson.fromJson(response.toString(), Usuario.class);

	                // Procesar según el tipo de usuario
	                String tipoUsuario = usuarioSeleccionado.getTipo_usuario();

	                if ("ESTUDIANTE".equalsIgnoreCase(tipoUsuario)) {
	                    estudianteSeleccionado = obtenerEstudiantePorUsuario(usuarioSeleccionado.getIdUsuario());
	                    if (estudianteSeleccionado != null) {
	                        generacionSeleccionada = estudianteSeleccionado.getGeneracion();
	                        System.out.println("Generación cargada para el estudiante: " + generacionSeleccionada);
	                    } else {
	                        FacesContext.getCurrentInstance().addMessage(null,
	                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
	                                "No se pudo cargar la información del estudiante."));
	                    }
	                } else if ("TUTOR".equalsIgnoreCase(tipoUsuario)) {
	                    tutorSeleccionado = obtenerTutorPorUsuario(usuarioSeleccionado.getIdUsuario());
	                    if (tutorSeleccionado != null) {
	                        areaSeleccionada = tutorSeleccionado.getArea().getIdArea();
	                        rolSeleccionado = tutorSeleccionado.getRol().getIdRol();
	                    } else {
	                        FacesContext.getCurrentInstance().addMessage(null,
	                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
	                                "No se pudo cargar la información del tutor."));
	                    }
	                }

	                // Inicializar objetos anidados si es necesario
	                if (usuarioSeleccionado.getGenero() == null) {
	                    usuarioSeleccionado.setGenero(new Genero());
	                }
	                if (usuarioSeleccionado.getItrAsItr() == null) {
	                    usuarioSeleccionado.setItr(new Itr());
	                }
	                if (usuarioSeleccionado.getDepartamentoAsDpto() == null) {
	                    usuarioSeleccionado.setDepartamento(new Departamento());
	                }
	                if (usuarioSeleccionado.getLocalidadAsLoc() == null) {
	                    usuarioSeleccionado.setLocalidad(new Localidad());
	                }

	                System.out.println("Usuario seleccionado: " + usuarioSeleccionado.toString());

	            } else {
	                System.err.println("Error al obtener Usuario: Código HTTP " + con.getResponseCode());
	                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                        "Error", "No se pudo cargar la información del usuario."));
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "Error", "Ocurrió un error al cargar el usuario."));
	        }

	    } else {
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ID de usuario no válido."));
	    }
	}


	private List<Area> cargarAreas() {
		List<Area> listaAreas = new ArrayList<>();
		ApiClient apiClient = new ApiClient();

		try {
			JsonNode areasData = apiClient.getDatosAreas(); // Llama al servicio API que devuelve las áreas

			if (areasData != null && !areasData.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				for (JsonNode areaNode : areasData) {
					Area area = mapper.convertValue(areaNode, Area.class);
					listaAreas.add(area); // Añade cada área a la lista
				}
			} else {
				System.out.println("No se encontraron áreas.");
			}
		} catch (Exception e) {
			System.err.println("Error al cargar las áreas: " + e.getMessage());
			e.printStackTrace();
		}

		return listaAreas;
	}

	private List<Rol> cargarRoles() {
		List<Rol> listaRoles = new ArrayList<>();
		ApiClient apiClient = new ApiClient();

		try {
			JsonNode rolesData = apiClient.getDatosRoles(); // Llama al servicio API que devuelve los roles

			if (rolesData != null && !rolesData.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				for (JsonNode rolNode : rolesData) {
					Rol rol = mapper.convertValue(rolNode, Rol.class);
					listaRoles.add(rol); // Añade cada rol a la lista
				}
			} else {
				System.out.println("No se encontraron roles.");
			}
		} catch (Exception e) {
			System.err.println("Error al cargar los roles: " + e.getMessage());
			e.printStackTrace();
		}

		return listaRoles;
	}

	public String convertirTimestampALaFecha(Long timestamp) {
		if (timestamp != null) {
			Date date = new Date(timestamp);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // El formato que prefieras
			return sdf.format(date);
		}
		return null;
	}

	public String login() throws ServicioException {
	    try {
	        // 1. Validar que 'usuario' y 'clave' no sean null o vacíos
	        if (usuario == null || clave == null || usuario.trim().isEmpty() || clave.trim().isEmpty()) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario y contraseña son requeridos."));
	            return null;
	        }

	        // 2. Autenticación LDAP
	        String ldapUrl = "ldap://ServidorAD8:389"; // Reemplaza con tu URL LDAP
	        String ldapUser = usuario + "@8soft.utec.uy"; // Formato UPN
	        String ldapPassword = clave;

	        Hashtable<String, String> env = new Hashtable<>();
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	        env.put(Context.PROVIDER_URL, ldapUrl);
	        env.put(Context.SECURITY_AUTHENTICATION, "simple");
	        env.put(Context.SECURITY_PRINCIPAL, ldapUser);
	        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);

	        try {
	            // Intentar conectarse al LDAP
	            InitialDirContext ctx = new InitialDirContext(env);
	            ctx.close(); // Cerrar el contexto LDAP
	            // Si no se lanza una excepción, la autenticación LDAP fue exitosa
	        } catch (javax.naming.AuthenticationException authEx) {
	            // Autenticación LDAP fallida
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña inválidos.", "Usuario o contraseña inválidos."));
	            return null;
	        } catch (NamingException ne) {
	            // Otros errores relacionados con LDAP
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de conexión con LDAP.", "Error de conexión con LDAP."));
	            return null;
	        }

	        // 3. URL del endpoint de la API REST para obtener la lista de usuarios
	        String endpointUrl = BASE_URL + "usuario";
	        // Realiza la conexión HTTP para obtener la lista de usuarios
	        URL url = new URL(endpointUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Accept", "application/json");

	        int responseCode = con.getResponseCode();
	        if (responseCode != 200) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo obtener información del usuario."));
	            return null;
	        }

	        // Lee la respuesta de la API REST
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuilder response = new StringBuilder();
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        // Procesa la respuesta JSON y convierte en una lista de usuarios
	        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
	            @Override
	            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	                    throws JsonParseException {
	                if (json == null || json.isJsonNull()) {
	                    return null;
	                }
	                try {
	                    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
	                        long timestamp = json.getAsLong();
	                        return new Date(timestamp);
	                    } else {
	                        String dateStr = json.getAsString();
	                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	                        return sdf.parse(dateStr);
	                    }
	                } catch (Exception e) {
	                    throw new JsonParseException("Failed to parse Date: " + e.getMessage(), e);
	                }
	            }
	        }).create();

	        List<Usuario> listaUsuarios = gson.fromJson(response.toString(), new TypeToken<List<Usuario>>() {
	        }.getType());

	        // Verificar si el usuario está en la lista y está activo y confirmado
	        for (Usuario u : listaUsuarios) {
	            if (u.getUsuario().equals(usuario) && u.getActivo() == 'S' && u.getConfirmado() == 'S') {
	                tipoUsuario = u.getTipo_usuario();
	                usuarioLogueado = u; // Asignamos el usuario logueado
	                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u); // Corregido de 'user' a 'u'
	                estaLogueado = true;
	                return "pages/principal.xhtml?faces-redirect=true";
	            }
	        }

	        // Si llegamos aquí, el inicio de sesión ha fallado
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña inválidos.", "Usuario o contraseña inválidos."));
	        return null; // O puedes redirigir a una página de error, por ejemplo
	    } catch (Exception e) {
	        // Manejo de errores generales
	        System.err.println("Error del Login:");
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al intentar iniciar sesión"));
	        return null;
	    }
	}
	
	
	public String validateUserLogin() {
		if (estaLogueado)
			return "";
		else {
			// Usuario no logueado, muestra el mensaje y redirige a la página de inicio de
			// sesión

			return "Login.xhtml?faces-redirect=true";
		}
	}

	// Getters & Setters
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public boolean isEstaLogueado() {
		return estaLogueado;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public void setEstaLogueado(boolean estaLogueado) {
		this.estaLogueado = estaLogueado;
	}

	/**
	 * @return
	 * @throws ServicioException
	 * @throws ParseException
	 */
	/**
	 * @return
	 * @throws ServicioException
	 * @throws ParseException
	 */

	public String crearUsuario() throws ServicioException {
		// URL del endpoint para crear un nuevo usuario
		String endpointUrl = BASE_URL + "usuario";

		try {
			System.out.println("Inicio del método crearUsuario");

			// Validación de campos
			if (campoVacio(usuarioNuevo.getPrimer_nombre()) || campoVacio(usuarioNuevo.getPrimer_apellido())
					|| campoVacio(usuarioNuevo.getDocumentoAString()) || campoVacio(usuarioNuevo.getMail_personal())
					|| campoVacio(usuarioNuevo.getTelefono()) || campoVacio(usuarioNuevo.getMail_institucional())
					|| campoVacio(usuarioNuevo.getClave()) || campoVacio(usuarioNuevo.getTipo_usuario())
					|| usuarioNuevo.getFecha_nacimiento() == null || campoVacio(usuarioNuevo.getLocalidad())
					|| campoVacio(usuarioNuevo.getItr())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Todos los campos son obligatorios"));
				System.out.println("Error: Todos los campos son obligatorios");
				return null;
			}

			// Validación de formato de correo electrónico
			if (!isValidEmail(usuarioNuevo.getMail_personal())) {
				FacesContext.getCurrentInstance().addMessage("emailPersonal",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Formato de correo personal no válido"));
				System.out.println("Error: Formato de correo personal no válido");
				return null;
			}

			if (!isValidUtecEmail(usuarioNuevo.getMail_institucional())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Formato de correo institucional no válido"));
				System.out.println("Error: Formato de correo institucional no válido");
				return null;
			}

			if (!isValidPassword(usuarioNuevo.getClave())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "La contraseña debe contener al menos 8 caracteres, una letra y un número."));
				System.out.println("Error: Formato de contraseña no válida");
				return null;
			}

			// Preparación de datos
			Date date = usuarioNuevo.getFecha_nacimiento();
			Long timestamp;
			if (date != null) {
				timestamp = date.getTime();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fecha de nacimiento es obligatoria."));
				return null;
			}

			apiClient = new ApiClientAux();

			long idGeneroSeleccionado = this.getGeneroSeleccionado();
			JsonObject generoJson = apiClient.obtenerJsonDesdeEndpoint("genero", idGeneroSeleccionado);
			String nombreGenero = generoJson.get("nombre").getAsString();

			long idItrSeleccionado = this.getItrSeleccionado();
			long idDepartamentoSeleccionado = this.getDepartamentoSeleccionado();
			long idLocalidadSeleccionado = this.getLocalidadSeleccionada();

			JsonObject DepartamentoJson = apiClient.obtenerJsonDesdeEndpoint("departamento",
					idDepartamentoSeleccionado);
			JsonObject LocalidadJson = apiClient.obtenerJsonDesdeEndpoint("localidad", idLocalidadSeleccionado);
			JsonObject itrJson = apiClient.obtenerJsonDesdeEndpoint("itr", idItrSeleccionado);

			// Construir objetos JSON para enviar
			JsonObject itrJsonObject = new JsonObject();
			itrJsonObject.addProperty("idItr", idItrSeleccionado);
			itrJsonObject.addProperty("nombre", itrJson.get("nombre").getAsString());
			itrJsonObject.addProperty("activo", itrJson.get("activo").getAsString());
			itrJsonObject.add("departamento", DepartamentoJson);

			JsonObject generoJsonObject = new JsonObject();
			generoJsonObject.addProperty("idGenero", idGeneroSeleccionado);
			generoJsonObject.addProperty("nombre", nombreGenero);

			// Construye los datos del nuevo usuario
			JsonObject usuarioJson = new JsonObject();
			usuarioJson.addProperty("documento", usuarioNuevo.getDocumento());
			usuarioJson.addProperty("primer_nombre", usuarioNuevo.getPrimer_nombre());
			usuarioJson.addProperty("segundo_nombre", usuarioNuevo.getSegundo_nombre());
			usuarioJson.addProperty("primer_apellido", usuarioNuevo.getPrimer_apellido());
			usuarioJson.addProperty("segundo_apellido", usuarioNuevo.getSegundo_apellido());
			usuarioJson.addProperty("usuario", usuarioNuevo.getPrimer_nombre().toLowerCase() + "."
					+ usuarioNuevo.getPrimer_apellido().toLowerCase());
			usuarioJson.addProperty("clave", usuarioNuevo.getClave() != null ? usuarioNuevo.getClave() : "Sin clave");
			usuarioJson.addProperty("fecha_nacimiento", timestamp);
			usuarioJson.add("genero", generoJsonObject);
			usuarioJson.add("itr", itrJsonObject);
			usuarioJson.add("departamento", DepartamentoJson);
			usuarioJson.add("localidad", LocalidadJson);
			usuarioJson.addProperty("telefono", usuarioNuevo.getTelefono());
			usuarioJson.addProperty("mail_institucional", usuarioNuevo.getMail_institucional());
			usuarioJson.addProperty("mail_personal", usuarioNuevo.getMail_personal());
			usuarioJson.addProperty("activo", "N");
			usuarioJson.addProperty("confirmado", "N");
			usuarioJson.addProperty("tipo_usuario",
					usuarioNuevo.getTipo_usuario() != null ? usuarioNuevo.getTipo_usuario() : "ANALISTA");

			System.out.println("JSON enviado al servidor:");
			System.out.println(usuarioJson.toString());
			HttpURLConnection con;

			if ("ESTUDIANTE".equalsIgnoreCase(usuarioNuevo.getTipo_usuario())) {
				usuarioJson.addProperty("generacion", generacion);
				con = (HttpURLConnection) new URL(endpointEstudiante).openConnection();

			} else if ("TUTOR".equalsIgnoreCase(usuarioNuevo.getTipo_usuario())) {

				long idAreaSeleccionada = this.getAreaSeleccionada();
				JsonObject areaJson = apiClient.obtenerJsonDesdeEndpoint("area", idAreaSeleccionada);
				String nombreArea = areaJson.get("descripcion").getAsString();

				long idRolSeleccionado = this.getRolSeleccionado();
				JsonObject rolJson = apiClient.obtenerJsonDesdeEndpoint("rol", idRolSeleccionado);
				String nombreRol = rolJson.get("descripcion").getAsString();

				JsonObject rolJsonObject = new JsonObject();
				rolJsonObject.addProperty("idRol", idRolSeleccionado);
				rolJsonObject.addProperty("descripcion", nombreRol);

				usuarioJson.add("area", areaJson);
				usuarioJson.add("rol", rolJson);

				con = (HttpURLConnection) new URL(endpointTutor).openConnection();

			} else {
				// Enviar el objeto JSON al servidor
				con = (HttpURLConnection) new URL(endpointAnalista).openConnection();
			}

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setDoOutput(true);

			try (OutputStream os = con.getOutputStream()) {
				byte[] input = usuarioJson.toString().getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("Usuario creado exitosamente");

				// Lee la respuesta del servidor para obtener el usuario creado
				StringBuilder response = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
				}

				// Parsear la respuesta JSON para obtener el ID del usuario
				JsonObject usuarioCreadoJson = JsonParser.parseString(response.toString()).getAsJsonObject();
				long idUsuarioCreado = usuarioCreadoJson.get("idUsuario").getAsLong();

				System.out.println("ID del usuario creado: " + idUsuarioCreado);
				
				cargarDatos("usuarios");

				// Agrega un mensaje de éxito para mostrar en el front-end
				FacesContext.getCurrentInstance().getExternalContext().getFlash().put("mensajeExito",
						"Usuario creado exitosamente");

				// Verifica si el tipo de usuario es "ESTUDIANTE"
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				try {

					externalContext.redirect(externalContext.getRequestContextPath() + "/pages/PostRegistro.xhtml");
					FacesContext.getCurrentInstance().responseComplete(); // Indica que la respuesta está completa
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null; // Retorna null para evitar que JSF procese otra navegación
			} else {
				// Manejo de errores si la respuesta no es exitosa
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
				StringBuilder errorResponse = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					errorResponse.append(responseLine.trim());
				}
				System.err.println("Error al crear el usuario: " + errorResponse.toString());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error al crear el usuario", errorResponse.toString()));
				return null;
			}

		} catch (IOException e) {
			System.out.println("Error de conexión: " + e.getMessage());
			throw new ServicioException("Error de conexión: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new ServicioException("Error: " + e.getMessage());
		}
	}

// crearUsuario

	////////// ################# ################# #################
	////////// ################# ################# #################
	////////// ################# ################# #################

	//// ################### GEMINI #######################

	public List<Usuario> obtenerListaUsuarios() {
		try {
			// URL del endpoint de la API REST para obtener la lista de usuarios
			String endpointUrl = BASE_URL + "usuario";

			// Realiza la conexión HTTP para obtener la lista de usuarios
			URL url = new URL(endpointUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// Lee la respuesta de la API REST
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				// Procesa la respuesta JSON y convierte en una lista de usuarios
				Gson gson = new Gson();
				TypeToken<List<Usuario>> token = new TypeToken<List<Usuario>>() {
				};
				List<Usuario> listaUsuarios = gson.fromJson(response.toString(), token.getType());

				System.out.println("Lista de usuarios obtenida con éxito.");
				System.out.println();
				System.out.println();

				for (Usuario usuario : listaUsuarios) {
					System.out.println(usuario.toString());
				}
				return listaUsuarios;
			} catch (IOException e) {
				// Manejo de errores
				System.err.println("Error al leer la respuesta de la API REST: " + e.getMessage());
				// Puedes lanzar una excepción personalizada o devolver una lista vacía según tu
				// lógica
				return Collections.emptyList();
			}
		} catch (Exception e) {
			// Manejo de errores
			System.err.println("Error al obtener la lista de usuarios: " + e.getMessage());
			// Puedes lanzar una excepción personalizada o devolver una lista vacía según tu
			// lógica
			return Collections.emptyList();
		}
	}

	public String modificarUsuario() throws ServicioException {
	    try {
	        System.out.println("Inicio del método modificarUsuario");

	        // Validación de campos obligatorios
	        if (campoVacio(usuarioSeleccionado.getPrimer_nombre())
	                || campoVacio(usuarioSeleccionado.getPrimer_apellido())
	                || campoVacio(usuarioSeleccionado.getDocumentoAString())
	                || campoVacio(usuarioSeleccionado.getMail_personal())
	                || campoVacio(usuarioSeleccionado.getTelefono())
	                || campoVacio(usuarioSeleccionado.getMail_institucional())
	                || campoVacio(usuarioSeleccionado.getTipo_usuario())
	                || usuarioSeleccionado.getFecha_nacimiento() == null
	                || usuarioSeleccionado.getLocalidadAsLoc() == null || usuarioSeleccionado.getItrAsItr() == null
	                || usuarioSeleccionado.getGenero() == null || usuarioSeleccionado.getDepartamentoAsDpto() == null) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Todos los campos son obligatorios"));
	            System.out.println("Error: Todos los campos son obligatorios");
	            return null;
	        }

	        // Validación de formato de correo electrónico
	        if (!isValidEmail(usuarioSeleccionado.getMail_personal())) {
	            FacesContext.getCurrentInstance().addMessage("emailPersonal",
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Formato de correo personal no válido"));
	            System.out.println("Error: Formato de correo personal no válido");
	            return null;
	        }

	        if (!isValidUtecEmail(usuarioSeleccionado.getMail_institucional())) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "Error", "Formato de correo institucional no válido"));
	            System.out.println("Error: Formato de correo institucional no válido");
	            return null;
	        }

	        // Preparación de datos
	        Date date = usuarioSeleccionado.getFecha_nacimiento();
	        Long timestamp;
	        if (date != null) {
	            timestamp = date.getTime();
	        } else {
	            FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Fecha de nacimiento es obligatoria."));
	            return null;
	        }

	        apiClient = new ApiClientAux();

	        // Obtener IDs seleccionados de genero, itr, departamento y localidad
	        long idGeneroSeleccionado = usuarioSeleccionado.getGenero().getIdGenero();
	        long idItrSeleccionado = usuarioSeleccionado.getItrAsItr().getIdItr();
	        long idDepartamentoSeleccionado = usuarioSeleccionado.getDepartamentoAsDpto().getIdDepartamento();
	        long idLocalidadSeleccionado = usuarioSeleccionado.getLocalidadAsLoc().getIdLocalidad();

	        // Obtener objetos JSON desde los endpoints
	        JsonObject generoJson = apiClient.obtenerJsonDesdeEndpoint("genero", idGeneroSeleccionado);
	        JsonObject itrJson = apiClient.obtenerJsonDesdeEndpoint("itr", idItrSeleccionado);
	        JsonObject departamentoJson = apiClient.obtenerJsonDesdeEndpoint("departamento",
	                idDepartamentoSeleccionado);
	        JsonObject localidadJson = apiClient.obtenerJsonDesdeEndpoint("localidad", idLocalidadSeleccionado);

	        // Construir JSON para el usuario modificado
	        JsonObject usuarioJson = new JsonObject();
	        usuarioJson.addProperty("idUsuario", usuarioSeleccionado.getIdUsuario());
	        usuarioJson.addProperty("documento", usuarioSeleccionado.getDocumento());
	        usuarioJson.addProperty("primer_nombre", usuarioSeleccionado.getPrimer_nombre());
	        usuarioJson.addProperty("segundo_nombre", usuarioSeleccionado.getSegundo_nombre());
	        usuarioJson.addProperty("primer_apellido", usuarioSeleccionado.getPrimer_apellido());
	        usuarioJson.addProperty("segundo_apellido", usuarioSeleccionado.getSegundo_apellido());
	        usuarioJson.addProperty("usuario", usuarioSeleccionado.getPrimer_nombre().toLowerCase() + "."
	                + usuarioSeleccionado.getPrimer_apellido().toLowerCase());
	        usuarioJson.addProperty("clave", usuarioSeleccionado.getClave());
	        
	     // Validación y actualización de contraseña
	        if (nuevaClave != null && !nuevaClave.isEmpty()) {
	            if (!nuevaClave.equals(confirmarClave)) {
	                FacesContext.getCurrentInstance().addMessage(null,
	                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden"));
	                nuevaClave = null;
	                confirmarClave = null;
	                return null;
	            }

	            if (!isValidPassword(nuevaClave)) {
	                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                        "Error", "La contraseña debe contener al menos 8 caracteres, una letra y un número."));
	                return null;
	            }

	            usuarioJson.addProperty("clave", nuevaClave);
	        }

	        usuarioJson.addProperty("fecha_nacimiento", timestamp);
	        usuarioJson.add("genero", generoJson);
	        usuarioJson.add("itr", itrJson);
	        usuarioJson.add("departamento", departamentoJson);
	        usuarioJson.add("localidad", localidadJson);
	        usuarioJson.addProperty("telefono", usuarioSeleccionado.getTelefono());
	        usuarioJson.addProperty("mail_institucional", usuarioSeleccionado.getMail_institucional());
	        usuarioJson.addProperty("mail_personal", usuarioSeleccionado.getMail_personal());
	        usuarioJson.addProperty("activo", String.valueOf(usuarioSeleccionado.getActivo()));
	        usuarioJson.addProperty("confirmado", String.valueOf(usuarioSeleccionado.getConfirmado()));
	        usuarioJson.addProperty("tipo_usuario", usuarioSeleccionado.getTipo_usuario());

	        String tipoUsuario = usuarioSeleccionado.getTipo_usuario();

	        HttpURLConnection con;

	        if ("ESTUDIANTE".equalsIgnoreCase(usuarioSeleccionado.getTipo_usuario())) {
	        	System.out.println("Generación Seleccionada antes de agregar al JSON: " + generacionSeleccionada);
				usuarioJson.addProperty("generacion", generacionSeleccionada);
				con = (HttpURLConnection) new URL(endpointEstudiante).openConnection();

			} else if ("TUTOR".equalsIgnoreCase(usuarioSeleccionado.getTipo_usuario())) {

				long idAreaSeleccionada = this.getAreaSeleccionada();
				JsonObject areaJson = apiClient.obtenerJsonDesdeEndpoint("area", idAreaSeleccionada);
				String nombreArea = areaJson.get("descripcion").getAsString();

				long idRolSeleccionado = this.getRolSeleccionado();
				JsonObject rolJson = apiClient.obtenerJsonDesdeEndpoint("rol", idRolSeleccionado);
				String nombreRol = rolJson.get("descripcion").getAsString();

				JsonObject rolJsonObject = new JsonObject();
				rolJsonObject.addProperty("idRol", idRolSeleccionado);
				rolJsonObject.addProperty("descripcion", nombreRol);

				usuarioJson.add("area", areaJson);
				usuarioJson.add("rol", rolJson);

				con = (HttpURLConnection) new URL(endpointTutor).openConnection();

	        } else {
	            // Si es otro tipo de usuario, enviar al endpoint de Analista
	            con = (HttpURLConnection) new URL(endpointAnalista).openConnection();
	        }

	        // Enviar la solicitud al servidor
	        con.setRequestMethod("PUT");
	        con.setRequestProperty("Content-Type", "application/json; utf-8");
	        con.setDoOutput(true);

	        try (OutputStream os = con.getOutputStream()) {
	            byte[] input = usuarioJson.toString().getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        int responseCode = con.getResponseCode();
	        System.out.println("Código de respuesta: " + responseCode);

	        System.out.println("JSON a enviar: " + usuarioJson.toString());

	        try (BufferedReader br = new BufferedReader(new InputStreamReader(
	                (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED)
	                        ? con.getInputStream()
	                        : con.getErrorStream(),
	                "utf-8"))) {
	            StringBuilder response = new StringBuilder();
	            String responseLine;
	            while ((responseLine = br.readLine()) != null) {
	                response.append(responseLine.trim());
	            }
	            System.out.println("Respuesta del servidor: " + response.toString());

	            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
	                System.out.println("Usuario modificado exitosamente");
	                cargarDatos("usuarios");
	                nuevaClave = null;
	                confirmarClave = null;
	                FacesContext.getCurrentInstance().addMessage(null,
	                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario modificado exitosamente"));
	                return "principal.xhtml?faces-redirect=true";
	            } else {
	                System.err.println("Error al modificar el usuario: " + response.toString());
	                nuevaClave = null;
	                confirmarClave = null;
	                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                        "Error al modificar el usuario", response.toString()));
	                return null;
	            }
	            
	        }

	    } catch (IOException e) {
	        System.out.println("Error de conexión: " + e.getMessage());
	        throw new ServicioException("Error de conexión: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Error: " + e.getMessage());
	        throw new ServicioException("Error: " + e.getMessage());
	    }
	}
 // Fin modificar

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Usuario getUsuarioNuevo() {
		return usuarioNuevo;
	}

	public void setUsuarioNuevo(Usuario usuarioNuevo) {
		this.usuarioNuevo = usuarioNuevo;
	}

	/*
	 * ##################### MICODIGO ###################### public List<Usuario>
	 * obtenerListaUsuarios() { try { // URL del endpoint de la API REST para
	 * obtener la lista de usuarios String endpointUrl = BASE_URL + "usuario";
	 * 
	 * // Realiza la conexión HTTP para obtener la lista de usuarios URL url = new
	 * URL(endpointUrl); HttpURLConnection con = (HttpURLConnection)
	 * url.openConnection(); con.setRequestMethod("GET");
	 * 
	 * // Lee la respuesta de la API REST BufferedReader in = new BufferedReader(new
	 * InputStreamReader(con.getInputStream())); String inputLine; StringBuilder
	 * response = new StringBuilder(); while ((inputLine = in.readLine()) != null) {
	 * response.append(inputLine); } in.close();
	 * 
	 * // Procesa la respuesta JSON y convierte en una lista de usuarios Gson gson =
	 * new Gson(); TypeToken<List<Usuario>> token = new TypeToken<List<Usuario>>()
	 * {}; System.out.println(); System.out.println(); System.out.println();
	 * System.out.println("DEVUELVE"); System.out.println(token);
	 * //System.out.println(gson.fromJson(response.toString(), token.getType());
	 * System.out.println(); System.out.println(); System.out.println(); return
	 * gson.fromJson(response.toString(), token.getType()); } catch (Exception e) {
	 * // Manejo de errores e.printStackTrace(); // Puedes lanzar una excepción
	 * personalizada o devolver una lista vacía según tu lógica return null; }
	 * 
	 * }
	 * 
	 */ // ##################### MICODIGO ######################

	private void cargarListaGeneros() {
		String apiUrl = "http://localhost:8080/ochoschool.apirest/mantenimiento/genero"; // Ajusta la URL según tu
																							// configuración
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				// La variable 'response.body()' contiene la respuesta en formato JSON

				// Convierte la respuesta JSON a una lista de objetos Genero
				ObjectMapper objectMapper = new ObjectMapper();
				listaGeneros = Arrays.asList(objectMapper.readValue(response.body(), Genero[].class));
				// System.out.println("Se cargo la lista de géneros: " + response.body());
				setListaGeneros(listaGeneros);
				// Devuelve la lista de géneros
				return;
			} else {
				// Manejar el caso en que la solicitud no fue exitosa
				System.out
						.println("Error al cargar la lista de géneros. Código de respuesta: " + response.statusCode());
			}
		} catch (Exception e) {
			// Manejar excepciones
			e.printStackTrace();
		}

		// En caso de error, devuelve una lista vacía o maneja el flujo según tus
		// necesidades
		return;
	}

	public void cargarListaItr() {
	    String apiUrl = "http://localhost:8080/ochoschool.apirest/mantenimiento/itr"; 

	    try {
	        HttpClient client = HttpClient.newHttpClient();
	        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();

	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	        if (response.statusCode() == 200) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            // Deserializar la respuesta JSON en una lista de Itr
	            List<Itr> allItrs = Arrays.asList(objectMapper.readValue(response.body(), Itr[].class));
	            
	            // Filtrar solo los Itrs con activo 'S'
	            listaItr = allItrs.stream()
	                              .filter(itr -> itr.getActivo() == 'S')
	                              .collect(Collectors.toList());
	            
	            // Asignar la lista filtrada
	            setListaItr(listaItr);
	            
	            System.out.println("Lista de Itrs cargada y filtrada: " + listaItr.size() + " elementos.");
	            return;
	        } else {
	            System.out.println("Error al cargar la lista de Itrs. Código de respuesta: " + response.statusCode());
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "Error al cargar Itrs", "Código de respuesta: " + response.statusCode()));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar Itrs", e.getMessage()));
	    }
	}

	private void cargarListaDepartamentos() {

		String apiUrl = "http://localhost:8080/ochoschool.apirest/mantenimiento/departamento"; // Ajusta la URL según tu
		// configuración
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
// La variable 'response.body()' contiene la respuesta en formato JSON

// Convierte la respuesta JSON a una lista de objetos departamento
				ObjectMapper objectMapper = new ObjectMapper();
				listaDepartamentos = Arrays.asList(objectMapper.readValue(response.body(), Departamento[].class));
				System.out.println("Se cargo la lista de departamentos: " + response.body());
				setListaDepartamentos(listaDepartamentos);
// Devuelve la lista de departamento
				return;
			} else {
// Manejar el caso en que la solicitud no fue exitosa
				System.out.println(
						"Error al cargar la lista de departamentos. Código de respuesta: " + response.statusCode());
			}
		} catch (Exception e) {
// Manejar excepciones
			e.printStackTrace();
		}

// En caso de error, devuelve una lista vacía o maneja el flujo según tus
// necesidades
		return;
	}

	private void cargarListaLocalidades() {

		String apiUrl = "http://localhost:8080/ochoschool.apirest/mantenimiento/localidad"; // Ajusta la URL según tu
		// configuración
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
// La variable 'response.body()' contiene la respuesta en formato JSON

// Convierte la respuesta JSON a una lista de objetos localidad
				ObjectMapper objectMapper = new ObjectMapper();
				listaLocalidades = Arrays.asList(objectMapper.readValue(response.body(), Localidad[].class));
				System.out.println("Se cargo la lista de localidades: " + response.body());
				setListaLocalidades(listaLocalidades);
// Devuelve la lista de localidad
				return;
			} else {
// Manejar el caso en que la solicitud no fue exitosa
				System.out.println(
						"Error al cargar la lista de localidades. Código de respuesta: " + response.statusCode());
			}
		} catch (Exception e) {
// Manejar excepciones
			e.printStackTrace();
		}

// En caso de error, devuelve una lista vacía o maneja el flujo según tus
// necesidades
		return;
	}

	private void cargarListaAreas() {
		String apiUrl = "http://localhost:8080/ochoschool.apirest/mantenimiento/area";

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				// La variable 'response.body()' contiene la respuesta en formato JSON

				ObjectMapper objectMapper = new ObjectMapper();
				listaAreas = Arrays.asList(objectMapper.readValue(response.body(), Area[].class));
				setListaAreas(listaAreas);
				return;
			} else {
				// Manejar el caso en que la solicitud no fue exitosa
				System.out.println("Error al cargar la lista de áreas. Código de respuesta: " + response.statusCode());
			}
		} catch (Exception e) {
			// Manejar excepciones
			e.printStackTrace();
		}

		// En caso de error, devuelve una lista vacía o maneja el flujo según tus
		// necesidades
		return;
	}

	private void cargarListaRoles() {
		String apiUrl = "http://localhost:8080/ochoschool.apirest/mantenimiento/rol";

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				// La variable 'response.body()' contiene la respuesta en formato JSON

				ObjectMapper objectMapper = new ObjectMapper();
				listaRoles = Arrays.asList(objectMapper.readValue(response.body(), Rol[].class));
				setListaRoles(listaRoles);
				return;
			} else {
				// Manejar el caso en que la solicitud no fue exitosa
				System.out.println("Error al cargar la lista de áreas. Código de respuesta: " + response.statusCode());
			}
		} catch (Exception e) {
			// Manejar excepciones
			e.printStackTrace();
		}

		// En caso de error, devuelve una lista vacía o maneja el flujo según tus
		// necesidades
		return;
	}
	
	public void cargarDatosUsuarioLogueado() {
	    if (usuarioLogueado != null) {
	        cargarUsuarioSeleccionadoPorId(usuarioLogueado.getIdUsuario());
	    } else {
	        // Si el usuario no está logueado, redirigir al login o manejar el caso
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay un usuario logueado."));
	    }
	}

	public Long getGeneroSeleccionado() {
		return generoSeleccionado;
	}

	public void setGeneroSeleccionado(Long generoSeleccionado) {
		this.generoSeleccionado = generoSeleccionado;
	}

	public List<Genero> getListaGeneros() {
		return listaGeneros;
	}

	public void setListaGeneros(List<Genero> listaGeneros) {
		this.listaGeneros = listaGeneros;
	}

	public List<Itr> getListaItr() {
		return listaItr;
	}

	public void setListaItr(List<Itr> listaItr) {
		this.listaItr = listaItr;
	}

	public List<Departamento> getListaDepartamentos() {
		return listaDepartamentos;
	}

	public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
		this.listaDepartamentos = listaDepartamentos;
	}

	public List<Localidad> getListaLocalidades() {
		return listaLocalidades;
	}

	public void setListaLocalidades(List<Localidad> listaLocalidades) {
		this.listaLocalidades = listaLocalidades;
	}

	public Long getItrSeleccionado() {
		return itrSeleccionado;
	}

	public void setItrSeleccionado(Long itrSeleccionado) {
		this.itrSeleccionado = itrSeleccionado;
		filtrarDatosAcumulados();
	}

	public Long getDepartamentoSeleccionado() {
		return departamentoSeleccionado;
	}

	public void setDepartamentoSeleccionado(Long departamentoSeleccionado) {
		this.departamentoSeleccionado = departamentoSeleccionado;
	}

	public Long getLocalidadSeleccionada() {
		return localidadSeleccionada;
	}

	public void setLocalidadSeleccionada(Long localidadSeleccionada) {
		this.localidadSeleccionada = localidadSeleccionada;
	}

	public String getLocalidadesJSON() {
		return localidadesJSON;
	}

	// Método para actualizar dinámicamente las localidades según el departamento
	// seleccionado
	public void actualizarLocalidades() {
		// Obtener el ID del departamento seleccionado desde la vista
		String idDepartamentoStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("idDepartamento");
		System.out.println("id partamento seleccionado: " + idDepartamentoStr);
		if (idDepartamentoStr != null) {
			try {
				Long idDepartamento = Long.parseLong(idDepartamentoStr);
				// Filtrar las localidades por el ID del departamento seleccionado
				List<Localidad> localidadesFiltradas = listLocalidadesDepto(idDepartamento);
				for (Localidad localidad : localidadesFiltradas) {
					System.out.println(localidadesFiltradas.toString());
				}
				// Convertir la lista de localidades a JSON
				Gson gson = new Gson();
				String jsonLocalidades = gson.toJson(localidadesFiltradas);
				// Establecer el JSON de las localidades en el atributo localidadesJSON
				setLocalidadesJSON(jsonLocalidades);
			} catch (NumberFormatException e) {
				// Manejar la excepción si no se puede convertir la cadena a Long
				System.err.println("Error al convertir el ID del departamento a Long: " + e.getMessage());
			}
		} else {
			// Manejar el caso en que idDepartamentoStr sea nulo
			System.err.println("El ID del departamento es nulo");
		}
	}

	private void setLocalidadesJSON(String jsonLocalidades) {
	    this.localidadesJSON = jsonLocalidades;
	}

	// Función para verificar si un campo está vacío
	private boolean campoVacio(String valor) {
		return valor == null || valor.trim().isEmpty();
	}

	// Función para verificar el formato de un correo electrónico
	private boolean isValidEmail(String email) {
		// Puedes utilizar una expresión regular para validar el formato del correo
		// electrónico
		// Aquí hay un ejemplo básico, pero puedes ajustarlo según tus necesidades
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	// Para verificar específicamente correo de UTEC
	private boolean isValidUtecEmail(String email) {
		if (email == null || !email.endsWith("@8soft.edu.uy")) {
			return false;
		}

		// Expresión regular para el formato correcto de correos @utec.edu.uy
		String utecEmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@utec\\.edu\\.uy$";
		return email.matches(utecEmailRegex);
	}

	private boolean isValidPassword(String password) {
		// Expresión regular para una contraseña que contenga al menos una letra, al
		// menos un número,
		// y al menos 8 caracteres, permitiendo caracteres especiales opcionales
		String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
		return password.matches(passwordRegex);
	}

	private String obtenerJsonDesdeEndpoint(String endpointUrl) throws IOException {
		URL url = new URL(endpointUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			StringBuilder response = new StringBuilder();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			return response.toString();
		}
	}

	public void setUsuarioSeleccionado(Usuario ususeleccionado) {

		this.usuarioSeleccionado = ususeleccionado;
	}

	public String getGeneroString() {
		return BASE_URL;

	}

	public List<Localidad> listLocalidadesDepto(Long idDpto) {

		apiClient = new ApiClientAux();
		List<Localidad> listaLocalidades = new ArrayList<Localidad>();

		// Suponiendo que apiClient.obtenerJsonDesdeEndpoint("localidad") devuelve un
		// JSONArray
		JsonArray localidadesJson = apiClient.obtenerJson("localidad");

		System.out.println("::::   " + localidadesJson.toString());
		// Itera sobre el JSONArray para encontrar las localidades del departamento dado
		for (JsonElement elemento : localidadesJson) {
			JsonObject localidadJson = elemento.getAsJsonObject();
			Long idDepartamento = localidadJson.get("idDepartamento").getAsLong();
			System.out.println("Id departamento Sele: " + idDepartamento);
			// Si la localidad pertenece al departamento dado, añádela a la lista
			if (idDepartamento.equals(idDpto)) {
				// Aquí necesitas crear un objeto Localidad a partir de localidadJson
				// Suponiendo que tengas un método para convertir un objeto JSON en un objeto
				// Localidad
				Localidad localidad = convertirJsonALocalidad(localidadJson);
				System.out.println("Localidad convertida: " + localidad.toString());
				listaLocalidades.add(localidad);
				System.out.println("Lista result: " + listaLocalidades);
			}
		}

		return listaLocalidades;
	}

	public Localidad convertirJsonALocalidad(JsonObject localidadJson) {
		Long id = localidadJson.get("idLocalidad").getAsLong();
		String nombre = localidadJson.get("nombre").getAsString();
		// Long idDepartamento = localidadJson.get("idDepartanmento").getAsLong();
		// Asume que los atributos de Localidad se llaman id y nombre, ajusta según sea
		// necesario
		return new Localidad(id, null, nombre);
	}

	public Usuario convertirAUsuario(ObjectNode usuarioObjectNode) {
		try {
			return objectMapper.readValue(usuarioObjectNode.toString(), Usuario.class);

		} catch (IOException e) {
			// Manejo de excepciones
			e.printStackTrace();
			return null;
		}
	}

	public void cambiarEstadoUsuario(Long idUsuario) {
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    HttpURLConnection conn = null;
	    BufferedReader in = null;

	    try {
	        System.out.println("Iniciando cambio de estado del usuario con ID: " + idUsuario);

	        // Paso 1: Obtener el usuario usando el ID a través del endpoint
	        URL url = new URL(BASE_URL + "usuario/" + idUsuario);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        // Leer la respuesta y convertirla a un objeto Usuario
	        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	        StringBuilder content = new StringBuilder();
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            content.append(inputLine);
	        }
	        System.out.println("Respuesta del servidor al obtener usuario: " + content.toString());

	        // Convertir la respuesta JSON a un objeto Usuario
	        Gson gson = new GsonBuilder()
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

	        Usuario usuario = gson.fromJson(content.toString(), Usuario.class);
	        System.out.println("Usuario obtenido: " + usuario);

	        // Cerrar conexiones y streams
	        in.close();
	        conn.disconnect();
	        in = null;
	        conn = null;

	        // Paso 2: Determinar el tipo de usuario
	        String tipoUsuario = usuario.getTipo_usuario();
	        System.out.println("Tipo de usuario: " + tipoUsuario);

	        // Paso 3: Establecer el endpoint adecuado según el tipo de usuario
	        String endpointUrl;
	        if ("ESTUDIANTE".equalsIgnoreCase(tipoUsuario)) {
	            endpointUrl = endpointEstudiante;
	            System.out.println("Endpoint seleccionado: " + endpointUrl);

	            // Obtener información adicional del estudiante
	            Estudiante estudiante = obtenerEstudiantePorUsuario(usuario.getIdUsuario());
	            if (estudiante != null) {
	                usuario = estudiante;
	                System.out.println("Información adicional del estudiante obtenida.");
	            } else {
	                System.err.println("No se pudo obtener información adicional del estudiante.");
	            }
	        } else if ("TUTOR".equalsIgnoreCase(tipoUsuario)) {
	            endpointUrl = endpointTutor;
	            System.out.println("Endpoint seleccionado: " + endpointUrl);

	            // Obtener información adicional del tutor
	            Tutor tutor = obtenerTutorPorUsuario(usuario.getIdUsuario());
	            if (tutor != null) {
	                usuario = tutor;
	                System.out.println("Información adicional del tutor obtenida.");
	            } else {
	                System.err.println("No se pudo obtener información adicional del tutor.");
	            }
	        } else {
	            // Suponiendo que cualquier otro tipo es analista
	            endpointUrl = endpointAnalista;
	            System.out.println("Endpoint seleccionado: " + endpointUrl);
	        }

	        // Paso 4: Modificar el campo 'activo' del usuario
	        char nuevoEstado = (usuario.getActivo() == 'S') ? 'N' : 'S';
	        usuario.setActivo(nuevoEstado);
	        System.out.println("El campo 'activo' del usuario ha sido establecido a '" + nuevoEstado + "'.");

	        // Paso 5: Construir el objeto JSON para enviar
	        JsonObject usuarioJson = construirJsonUsuario(usuario);
	        System.out.println("JSON construido para enviar al servidor: " + usuarioJson.toString());

	        // Paso 6: Enviar la solicitud PUT al endpoint correspondiente
	        boolean success = putRequest(endpointUrl, usuarioJson.toString());
	        System.out.println("Resultado de la solicitud PUT: " + (success ? "Éxito" : "Error"));

	        // Mostrar mensaje de éxito si la actualización fue exitosa
	        if (success) {
	            String mensajeExito = (nuevoEstado == 'S') ? "El usuario ha sido activado exitosamente." : "El usuario ha sido desactivado exitosamente.";
	            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensajeExito));
	            // Recargar los datos si es necesario
	            cargarDatos("usuarios");
	            filtrarDatosAcumulados();
	        } else {
	            String mensajeError = (nuevoEstado == 'S') ? "No se pudo activar el usuario." : "No se pudo desactivar el usuario.";
	            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensajeError));
	        }

	    } catch (Exception e) {
	        // Mostrar mensaje de error si ocurre una excepción
	        System.err.println("Error al cambiar estado del usuario: " + e.getMessage());
	        e.printStackTrace();
	        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
	                "No se pudo cambiar el estado del usuario: " + e.getMessage()));
	    } finally {
	        // Asegurarse de que las conexiones y streams se cierren en caso de error
	        try {
	            if (in != null) {
	                in.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	}

	
	private JsonObject construirJsonUsuario(Usuario usuario) throws Exception {
	    apiClient = new ApiClientAux();

	    // Obtener IDs seleccionados de genero, itr, departamento y localidad
	    long idGeneroSeleccionado = usuario.getGenero().getIdGenero();
	    long idItrSeleccionado = usuario.getItrAsItr().getIdItr();
	    long idDepartamentoSeleccionado = usuario.getDepartamentoAsDpto().getIdDepartamento();
	    long idLocalidadSeleccionado = usuario.getLocalidadAsLoc().getIdLocalidad();

	    // Obtener objetos JSON desde los endpoints
	    JsonObject generoJson = apiClient.obtenerJsonDesdeEndpoint("genero", idGeneroSeleccionado);
	    JsonObject itrJson = apiClient.obtenerJsonDesdeEndpoint("itr", idItrSeleccionado);
	    JsonObject departamentoJson = apiClient.obtenerJsonDesdeEndpoint("departamento", idDepartamentoSeleccionado);
	    JsonObject localidadJson = apiClient.obtenerJsonDesdeEndpoint("localidad", idLocalidadSeleccionado);

	    // Construir JSON para el usuario
	    JsonObject usuarioJson = new JsonObject();
	    usuarioJson.addProperty("idUsuario", usuario.getIdUsuario());
	    usuarioJson.addProperty("documento", usuario.getDocumento());
	    usuarioJson.addProperty("primer_nombre", usuario.getPrimer_nombre());
	    usuarioJson.addProperty("segundo_nombre", usuario.getSegundo_nombre());
	    usuarioJson.addProperty("primer_apellido", usuario.getPrimer_apellido());
	    usuarioJson.addProperty("segundo_apellido", usuario.getSegundo_apellido());
	    usuarioJson.addProperty("usuario", usuario.getUsuario());
	    usuarioJson.addProperty("clave", usuario.getClave());
	    usuarioJson.addProperty("fecha_nacimiento", usuario.getFecha_nacimiento().getTime());
	    usuarioJson.add("genero", generoJson);
	    usuarioJson.add("itr", itrJson);
	    usuarioJson.add("departamento", departamentoJson);
	    usuarioJson.add("localidad", localidadJson);
	    usuarioJson.addProperty("telefono", usuario.getTelefono());
	    usuarioJson.addProperty("mail_institucional", usuario.getMail_institucional());
	    usuarioJson.addProperty("mail_personal", usuario.getMail_personal());
	    usuarioJson.addProperty("activo", String.valueOf(usuario.getActivo()));
	    usuarioJson.addProperty("confirmado", String.valueOf(usuario.getConfirmado()));
	    usuarioJson.addProperty("tipo_usuario", usuario.getTipo_usuario());

	    // Si es estudiante, añadir 'generacion'
	    if ("ESTUDIANTE".equalsIgnoreCase(usuario.getTipo_usuario())) {
	        usuarioJson.addProperty("generacion", ((Estudiante) usuario).getGeneracion());
	    }

	    // Si es tutor, añadir 'area' y 'rol'
	    if ("TUTOR".equalsIgnoreCase(usuario.getTipo_usuario())) {
	        long idAreaSeleccionada = ((Tutor) usuario).getArea().getIdArea();
	        JsonObject areaJson = apiClient.obtenerJsonDesdeEndpoint("area", idAreaSeleccionada);

	        long idRolSeleccionado = ((Tutor) usuario).getRol().getIdRol();
	        JsonObject rolJson = apiClient.obtenerJsonDesdeEndpoint("rol", idRolSeleccionado);

	        usuarioJson.add("area", areaJson);
	        usuarioJson.add("rol", rolJson);
	    }

	    return usuarioJson;
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

	private Estudiante obtenerEstudiantePorUsuario(Long idUsuario) {
	    try {
	        String endpointUrl = BASE_URL + "estudiante/" + idUsuario;
	        URL url = new URL(endpointUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");

	        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
	            StringBuilder response = new StringBuilder();
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

	            // Utilizar la instancia personalizada de Gson
	            Estudiante estudiante = gson.fromJson(response.toString(), Estudiante.class);
	            return estudiante;

	        } else {
	            System.err.println("Error al obtener Estudiante: Código HTTP " + con.getResponseCode());
	            return null;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	private Tutor obtenerTutorPorUsuario(Long idUsuario) {
	    try {
	        String endpointUrl = BASE_URL + "tutor/" + idUsuario;
	        URL url = new URL(endpointUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");

	        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
	            StringBuilder response = new StringBuilder();
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

	            // Utilizar la instancia personalizada de Gson
	            Tutor tutor = gson.fromJson(response.toString(), Tutor.class);
	            return tutor;

	        } else {
	            System.err.println("Error al obtener Tutor: Código HTTP " + con.getResponseCode());
	            return null;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public String logout() {
	    // Obtener el contexto actual de Faces
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    ExternalContext externalContext = facesContext.getExternalContext();

	    // Invalidar la sesión actual para cerrar el login del usuario
	    externalContext.invalidateSession();

	    // Opcional: Resetear variables de estado en el bean
	    this.usuarioLogueado = null;
	    this.estaLogueado = false;

	    try {
	        // Redirigir al usuario a la página principal de la aplicación
	        externalContext.redirect(externalContext.getRequestContextPath() + "/");
	    } catch (IOException e) {
	        // Manejar posibles excepciones durante la redirección
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo redirigir al inicio."));
	    }

	    // Indicar que la respuesta está completa y JSF no debe procesar más
	    facesContext.responseComplete();

	    // Retornar null para indicar que la navegación ya se ha manejado
	    return null;
	}

	// Getter y Setter para mostrarAnioIngreso
	public boolean isMostrarAnioIngreso() {
		return mostrarAnioIngreso;
	}

	public void setMostrarAnioIngreso(boolean mostrarAnioIngreso) {
		this.mostrarAnioIngreso = mostrarAnioIngreso;
	}

	public boolean isMostrarArea() {
		return mostrarArea;
	}

	public void setMostrarArea(boolean mostrarArea) {
		this.mostrarArea = mostrarArea;
	}

	public boolean isMostrarRol() {
		return mostrarRol;
	}

	public void setMostrarRol(boolean mostrarRol) {
		this.mostrarRol = mostrarRol;
	}

	// Método que se ejecuta cuando se selecciona un tipo de usuario
	public void onTipoUsuarioChange() {
		// Si se selecciona ESTUDIANTE, mostramos el campo para el año de ingreso
		mostrarAnioIngreso = "ESTUDIANTE".equals(usuarioNuevo.getTipo_usuario());
	}

	public List<Map<String, Object>> getUsuarios() {
		if (datosFiltrados != null) {
			return datosFiltrados;
		} else {
			return datosList;
		}
	}

	public List<Map<String, Object>> getDatosFiltrados() {
		return datosFiltrados;
	}

	public void setDatosFiltrados(List<Map<String, Object>> datosFiltrados) {
		this.datosFiltrados = datosFiltrados;
	}

	public String getTipoUsuarioSeleccionado() {
		return tipoUsuarioSeleccionado;
	}

	public void setTipoUsuarioSeleccionado(String tipoUsuarioSeleccionado) {
		this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
		filtrarUsuarios(); // Filtrar la lista cuando cambia el valor seleccionado
	}

	public List<SelectItem> getTiposUsuarios() {
		return tiposUsuarios;
	}

	public List<SelectItem> getItrs() {
		return itrs;
	}

	public void setItrs(List<SelectItem> itrs) {
		this.itrs = itrs;
	}

	public boolean isFiltrandoEstudiantes() {
		return filtrandoEstudiantes;
	}

	public String getGeneracionSeleccionada() {
		return generacionSeleccionada;
	}

	public void setGeneracionSeleccionada(String generacionSeleccionada) {
		this.generacionSeleccionada = generacionSeleccionada;
		//filtrarUsuarios(); // Llamar al filtro cuando cambia la generación
	}

	public List<SelectItem> getGeneraciones() {
		return generaciones;
	}

	public String getEstadoActivoSeleccionado() {
		return estadoActivoSeleccionado;
	}

	public void setEstadoActivoSeleccionado(String estadoActivoSeleccionado) {
		this.estadoActivoSeleccionado = estadoActivoSeleccionado;
	}
	
	public String getEstadoConfirmadoSeleccionado() {
		return estadoConfirmadoSeleccionado;
	}

	public void setEstadoConfirmadoSeleccionado(String estadoConfirmadoSeleccionado) {
		this.estadoConfirmadoSeleccionado = estadoConfirmadoSeleccionado;
	}

	public List<SelectItem> getEstadosActivos() {
		return estadosActivos;
	}

	public void setEstadosActivos(List<SelectItem> estadosActivos) {
		this.estadosActivos = estadosActivos;
	}
	
	public List<SelectItem> getEstadosConfirmados() {
		return estadosConfirmados;
	}

	public void setEstadosConfirmados(List<SelectItem> estadosConfirmados) {
		this.estadosConfirmados = estadosConfirmados;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public List<Area> getListaAreas() {
		return listaAreas;
	}

	public List<Rol> getListaRoles() {
		return listaRoles;
	}

	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public Estudiante getEstudianteSeleccionado() {
		return estudianteSeleccionado;
	}

	public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
		this.estudianteSeleccionado = estudianteSeleccionado;
	}

	public Tutor getTutorSeleccionado() {
		return tutorSeleccionado;
	}

	public void setTutorSeleccionado(Tutor tutorSeleccionado) {
		this.tutorSeleccionado = tutorSeleccionado;
	}

	public Usuario getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public void setUsuarioLogueado(Usuario usuarioLogueado) {
		this.usuarioLogueado = usuarioLogueado;
	}

	public String getNuevaClave() {
		return nuevaClave;
	}

	public void setNuevaClave(String nuevaClave) {
		this.nuevaClave = nuevaClave;
	}

	public String getConfirmarClave() {
		return confirmarClave;
	}

	public void setConfirmarClave(String confirmarClave) {
		this.confirmarClave = confirmarClave;
	}

	public Credenciales getCredenciales() {
		return credenciales;
	}

	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
	

}// fin clase
