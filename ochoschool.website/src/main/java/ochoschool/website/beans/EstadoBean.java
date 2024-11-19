package ochoschool.website.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.Departamento;
import ochoschool.website.entidades.Estado;
import ochoschool.website.entidades.Itr;

@Named("estadoBean")
@ViewScoped
public class EstadoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Estado> listaEstados;
	private List<Estado> listaEstadosFiltrados;
	private String estadoActivoSeleccionado; // 'S' o 'N'

	private final String BASE_URL = "http://localhost:8080/ochoschool.apirest/mantenimiento/";

	private Long idEstadoSeleccionado;
	private Estado estadoSeleccionado;
	@Inject
    private ReclamoBean reclamoBean;
	@Inject
    private JustificacionBean justificacionBean;

	private ApiClientAux apiClient;

	@PostConstruct
	public void init() {
		// Inicializar la lista de Estados
		cargarListaEstados();
		// Establecer el estado seleccionado por defecto a 'S'
		estadoActivoSeleccionado = "S";
		// Filtrar la lista inicialmente para mostrar solo activos
		filtrarEstados();
		apiClient = new ApiClientAux();
		estadoSeleccionado = new Estado();
	}

	/**
	 * Carga la lista completa de Estados desde el backend.
	 */
	private void cargarListaEstados() {
		String apiUrl = BASE_URL + "estado";

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				Estado[] estadosArray = objectMapper.readValue(response.body(), Estado[].class);
				listaEstados = new ArrayList<>(List.of(estadosArray));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error al cargar Estados", "Código de respuesta: " + response.statusCode()));
				listaEstados = new ArrayList<>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar Estados", e.getMessage()));
			listaEstados = new ArrayList<>();
		}
	}

	public void filtrarEstados() {
		if (estadoActivoSeleccionado == null || estadoActivoSeleccionado.equals("")) {
			// Si no se selecciona ningún estado, mostrar activos por defecto
			estadoActivoSeleccionado = "S";
		}

		listaEstadosFiltrados = new ArrayList<>();

		for (Estado estado : listaEstados) {
			if ("S".equals(estadoActivoSeleccionado)) {
				if (estado.getActivo() == 'S') { // Comparación directa de caracteres
					listaEstadosFiltrados.add(estado);
				}
			} else if ("N".equals(estadoActivoSeleccionado)) {
				if (estado.getActivo() == 'N') { // Comparación directa de caracteres
					listaEstadosFiltrados.add(estado);
				}
			}
		}
	}

	public String irAModificarEstado(Long idEstado) {
		if (idEstado != null) {
			// Redireccionar con el parámetro en la URL
			return "/pages/ModificarEstado.xhtml?idEstadoSeleccionado=" + idEstado + "&faces-redirect=true";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un estado."));
			return null;
		}
	}

	public String irACrearEstado() {
		return "/pages/AltaEstadoReclamo.xhtml?faces-redirect=true";
	}

	/**
	 * Carga el estado seleccionado para su modificación.
	 */
	public void cargarEstadoSeleccionado() {
		if (idEstadoSeleccionado != null) {
			System.out.println("Cargando Estado con id: " + idEstadoSeleccionado);
			String apiUrl = BASE_URL + "estado/" + idEstadoSeleccionado;

			try {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();

				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				if (response.statusCode() == 200) {
					ObjectMapper objectMapper = new ObjectMapper();
					estadoSeleccionado = objectMapper.readValue(response.body(), Estado.class);
					System.out.println("Estado cargado: " + estadoSeleccionado.getDescripcion());

				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error al cargar Estado", "Código de respuesta: " + response.statusCode()));
					estadoSeleccionado = new Estado(); // Inicializar con un objeto vacío

				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar Estado", e.getMessage()));
				estadoSeleccionado = new Estado(); // Inicializar con un objeto vacío

			}
		}
	}

	/**
	 * Modifica el Estado en el backend.
	 */
	public void modificarEstado() {
		if (estadoSeleccionado == null || estadoSeleccionado.getIdEstado() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Estado no válido."));
			return;
		}

		String apiUrl = BASE_URL + "estado";

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonItr = objectMapper.writeValueAsString(estadoSeleccionado);

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
					.header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(jsonItr))
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200 || response.statusCode() == 204) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Estado modificado exitosamente."));
				// Recargar la lista y aplicar el filtro nuevamente
				cargarListaEstados();
				filtrarEstados();
				reclamoBean.cargarEstados();
				justificacionBean.cargarEstados();
				
			} else if (response.statusCode() == 500) {
				// Verificar si el error es por restricción única
				if (response.body().contains("ORA-00001")) { // ORA-00001 es el error de restricción única en Oracle
					String descripcionDuplicada = estadoSeleccionado.getDescripcion();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"El Estado con la descripción '" + descripcionDuplicada + "' ya existe."));
				} else {
					// Manejar otros errores 500
					String errorMessage = "Error al crear Estado: Código de respuesta " + response.statusCode();
					String responseBody = response.body();
					if (responseBody != null && !responseBody.isEmpty()) {
						errorMessage += " - " + responseBody;
					}
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
				}
			} else {
				// Manejar otros códigos de error
				String errorMessage = "Error al crear Estado: Código de respuesta " + response.statusCode();
				String responseBody = response.body();
				if (responseBody != null && !responseBody.isEmpty()) {
					errorMessage += " - " + responseBody;
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
			}
		} catch (Exception e) {
			// Manejar excepciones
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se pudo crear el Estado: " + e.getMessage()));
		}
	}

	public String cambiarEstadoActivo(Long idEstado) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpURLConnection conn = null;
		BufferedReader in = null;

		try {
			System.out.println("Iniciando cambio de estado activo del Estado con ID: " + idEstado);

			URL url = new URL(BASE_URL + "estado/" + idEstado);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// Leer la respuesta y convertirla a un objeto
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			StringBuilder content = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			System.out.println("Respuesta del servidor al obtener Estado: " + content.toString());

			// Convertir la respuesta JSON a un objeto Estado
			Gson gson = new Gson();
			Estado estado = gson.fromJson(content.toString(), Estado.class);
			System.out.println("Estado obtenido: " + estado);

			// Cerrar conexiones y streams
			in.close();
			conn.disconnect();
			in = null;
			conn = null;

			// Modificar el campo 'activo' del Estado
			char nuevoEstado = (estado.getActivo() == 'S') ? 'N' : 'S';
			estado.setActivo(nuevoEstado);
			System.out.println("El campo 'activo' del Estado ha sido establecido a '" + nuevoEstado + "'.");

			// Construir el objeto JSON para enviar
			JsonObject estadoJson = new JsonObject();
			estadoJson.addProperty("idEstado", estado.getIdEstado());
			estadoJson.addProperty("descripcion", estado.getDescripcion());

			estadoJson.addProperty("activo", String.valueOf(estado.getActivo())); // Convertir char a String
			System.out.println("JSON construido para enviar al servidor: " + estadoJson.toString());

			url = new URL(BASE_URL + "estado");

			// Configurar conexión para PUT
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setDoOutput(true);

			// Escribir el JSON en el cuerpo de la solicitud
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = estadoJson.toString().getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			// Leer la respuesta del servidor
			int responseCode = con.getResponseCode();
			System.out.println("Código de respuesta del servidor: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado modificado exitosamente", null));

				// Actualizar las listas para reflejar el cambio
				cargarListaEstados();
				filtrarEstados();
				reclamoBean.cargarEstados();
				justificacionBean.cargarEstados();
				
				return "ListaEstadosReclamos?faces-redirect=true";

			} else {
				// Leer el mensaje de error del servidor
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
				StringBuilder errorResponse = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					errorResponse.append(responseLine.trim());
				}
				System.err.println("Error al modificar estado: " + errorResponse.toString());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error al modificar estado", errorResponse.toString()));
				return null;
			}
		} catch (Exception e) {
			// Mostrar mensaje de error si ocurre una excepción
			System.err.println("Error al cambiar estado: " + e.getMessage());
			e.printStackTrace();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se pudo cambiar el estado: " + e.getMessage()));
			return null;
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

	public void crearEstado() {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		try {
			// Paso 1: Validar los datos de entrada
			if (estadoSeleccionado == null) {
				estadoSeleccionado = new Estado();
			}

			// Establecer el estado inicial del ITR a 'S' (Activo)
			estadoSeleccionado.setActivo('S');

			// Paso 2: Serializar el objeto Estado a JSON usando ObjectMapper
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonEstado = objectMapper.writeValueAsString(estadoSeleccionado);
			System.out.println("JSON construido para enviar al servidor: " + jsonEstado);

			// Paso 3: Enviar la solicitud POST al backend
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "estado"))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonEstado))
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// Paso 4: Manejar la respuesta del servidor
			if (response.statusCode() == 201 || response.statusCode() == 200) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Estado creado exitosamente."));
				// Resetear el formulario
				estadoSeleccionado = new Estado();
				// Actualizar las listas para reflejar el cambio
				cargarListaEstados();
				filtrarEstados();
				reclamoBean.cargarEstados();
				justificacionBean.cargarEstados();
				
			} else if (response.statusCode() == 500) {
				// Verificar si el error es por restricción única
				if (response.body().contains("ORA-00001")) { // ORA-00001 es el error de restricción única en Oracle
					String descripcionDuplicada = estadoSeleccionado.getDescripcion();
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"El Estado con la descripción '" + descripcionDuplicada + "' ya existe."));
				} else {
					// Manejar otros errores 500
					String errorMessage = "Error al crear Estado: Código de respuesta " + response.statusCode();
					String responseBody = response.body();
					if (responseBody != null && !responseBody.isEmpty()) {
						errorMessage += " - " + responseBody;
					}
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
				}
			} else {
				// Manejar otros códigos de error
				String errorMessage = "Error al crear Estado: Código de respuesta " + response.statusCode();
				String responseBody = response.body();
				if (responseBody != null && !responseBody.isEmpty()) {
					errorMessage += " - " + responseBody;
				}
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
			}
		} catch (Exception e) {
			// Manejar excepciones
			e.printStackTrace();
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"No se pudo crear el Estado: " + e.getMessage()));
		}
	}

	public boolean isEstadoActivo(Estado estado) {
		return estado.getActivo() == 'S';
	}

	public String getActionLabel(Estado estado) {
		return isEstadoActivo(estado) ? "Desactivar" : "Activar";
	}

	public String getConfirmMessage(Estado estado) {
		return isEstadoActivo(estado) ? "¿Está seguro de que desea desactivar al Estado?"
				: "¿Está seguro de que desea activar al Estado?";
	}

	public List<Estado> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public List<Estado> getListaEstadosFiltrados() {
		return listaEstadosFiltrados;
	}

	public void setListaEstadosFiltrados(List<Estado> listaEstadosFiltrados) {
		this.listaEstadosFiltrados = listaEstadosFiltrados;
	}

	public String getEstadoActivoSeleccionado() {
		return estadoActivoSeleccionado;
	}

	public void setEstadoActivoSeleccionado(String estadoActivoSeleccionado) {
		this.estadoActivoSeleccionado = estadoActivoSeleccionado;
	}

	public Long getIdEstadoSeleccionado() {
		return idEstadoSeleccionado;
	}

	public void setIdEstadoSeleccionado(Long idEstadoSeleccionado) {
		this.idEstadoSeleccionado = idEstadoSeleccionado;
	}

	public Estado getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(Estado estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

}
