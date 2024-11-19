package ochoschool.website.beans;

import java.io.BufferedReader;
import java.io.IOException;
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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped; // Importa la anotación correcta
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.Departamento;
import ochoschool.website.entidades.Estudiante;
import ochoschool.website.entidades.Genero;
import ochoschool.website.entidades.Itr;
import ochoschool.website.entidades.Localidad;
import ochoschool.website.entidades.Tutor;
import ochoschool.website.entidades.Usuario;

@Named("itrBean")
@ViewScoped
public class ItrBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Itr> listaItr;
    private List<Itr> listaItrsFiltrados;
    private String estadoSeleccionado; // 'S' o 'N'

    private final String BASE_URL = "http://localhost:8080/ochoschool.apirest/mantenimiento/";

    private Long idItrSeleccionado;
    private Itr itrSeleccionado;
    private Departamento departamento;
    private List<Departamento> departamentos;
    private Long idDepartamentoSeleccionado;

	private ApiClientAux apiClient;
	@Inject
    private UsuarioBean usuarioBean;

    @PostConstruct
    public void init() {
        // Inicializar la lista de ITRs
        cargarListaItr();
        // Establecer el estado seleccionado por defecto a 'S'
        estadoSeleccionado = "S";
        // Filtrar la lista inicialmente para mostrar solo activos
        filtrarItrs();
        cargarListaDepartamentos();
        apiClient = new ApiClientAux();
        itrSeleccionado = new Itr();
    }

    /**
     * Carga la lista completa de ITRs desde el backend.
     */
    private void cargarListaItr() {
        String apiUrl = BASE_URL + "itr";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(apiUrl))
                                    .GET()
                                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                Itr[] itrsArray = objectMapper.readValue(response.body(), Itr[].class);
                listaItr = new ArrayList<>(List.of(itrsArray));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar ITRs",
                        "Código de respuesta: " + response.statusCode()));
                listaItr = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar ITRs", e.getMessage()));
            listaItr = new ArrayList<>();
        }
    }
    
    private void cargarListaDepartamentos() {
        String apiUrl = BASE_URL + "departamento";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(apiUrl))
                                    .GET()
                                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                Departamento[] dptosArray = objectMapper.readValue(response.body(), Departamento[].class);
                departamentos = new ArrayList<>(List.of(dptosArray));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar Departamentos",
                        "Código de respuesta: " + response.statusCode()));
                departamentos = new ArrayList<>(); // Corrección aquí
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar Departamentos", e.getMessage()));
            departamentos = new ArrayList<>(); // Corrección aquí
        }
    }


    /**
     * Filtra la lista de ITRs según el estado seleccionado.
     * Siempre muestra los activos ('S') a menos que se seleccione específicamente 'N'.
     */
    public void filtrarItrs() {
        if (estadoSeleccionado == null || estadoSeleccionado.equals("")) {
            // Si no se selecciona ningún estado, mostrar activos por defecto
            estadoSeleccionado = "S";
        }

        listaItrsFiltrados = new ArrayList<>();

        for (Itr itr : listaItr) {
            if ("S".equals(estadoSeleccionado)) {
                if (itr.getActivo() == 'S') { // Comparación directa de caracteres
                    listaItrsFiltrados.add(itr);
                }
            } else if ("N".equals(estadoSeleccionado)) {
                if (itr.getActivo() == 'N') { // Comparación directa de caracteres
                    listaItrsFiltrados.add(itr);
                }
            }
        }
    }

    /**
     * Navega a la página de modificación del ITR seleccionado.
     * @param idItr ID del ITR a modificar.
     * @return Redirección a la página de modificación.
     */
    public String irAModificarItr(Long idItr) {
        if (idItr != null) {
            // Redireccionar con el parámetro en la URL
            return "/pages/ModificarItr.xhtml?idItrSeleccionado=" + idItr + "&faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un ITR."));
            return null;
        }
    }
    
    public String irACrearItr() {
            return "/pages/AltaItr.xhtml&faces-redirect=true";
    }


    /**
     * Carga el ITR seleccionado para su modificación.
     */
    public void cargarItrSeleccionado() {
        if (idItrSeleccionado != null) {
            System.out.println("Cargando ITR con idItr: " + idItrSeleccionado);
            String apiUrl = BASE_URL + "itr/" + idItrSeleccionado;

            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(apiUrl))
                                        .GET()
                                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    itrSeleccionado = objectMapper.readValue(response.body(), Itr.class);
                    System.out.println("ITR cargado: " + itrSeleccionado.getNombre());

                    // Establecer el ID del departamento seleccionado
                    if (itrSeleccionado.getDepartamento() != null) {
                        idDepartamentoSeleccionado = itrSeleccionado.getDepartamento().getIdDepartamento();
                    } else {
                    	idDepartamentoSeleccionado = null;
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar ITR",
                            "Código de respuesta: " + response.statusCode()));
                    itrSeleccionado = new Itr(); // Inicializar con un objeto vacío
                    idDepartamentoSeleccionado = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cargar ITR", e.getMessage()));
                itrSeleccionado = new Itr(); // Inicializar con un objeto vacío
                idDepartamentoSeleccionado = null;
            }
        }
    }


    /**
     * Modifica el ITR en el backend.
     */
    public void modificarItr() {
        if (itrSeleccionado == null || itrSeleccionado.getIdItr() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ITR no válido."));
            return;
        }

        // Asignar el departamento seleccionado al ITR
        if (idDepartamentoSeleccionado != null) {
            Departamento selectedDepartamento = null;
            for (Departamento dept : departamentos) {
                if (dept.getIdDepartamento().equals(idDepartamentoSeleccionado)) {
                    selectedDepartamento = dept;
                    break;
                }
            }
            itrSeleccionado.setDepartamento(selectedDepartamento);
        } else {
            // Manejar el caso donde no se selecciona un departamento
            itrSeleccionado.setDepartamento(null);
        }

        String apiUrl = BASE_URL + "itr";

		try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonItr = objectMapper.writeValueAsString(itrSeleccionado);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(apiUrl))
                                    .header("Content-Type", "application/json")
                                    .PUT(HttpRequest.BodyPublishers.ofString(jsonItr))
                                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 204) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "ITR modificado exitosamente."));
                // Recargar la lista y aplicar el filtro nuevamente
                cargarListaItr();
                filtrarItrs();
                usuarioBean.cargarListaItr();
            } else if (response.statusCode() == 500) {
				// Verificar si el error es por restricción única
				if (response.body().contains("ORA-00001")) { // ORA-00001 es el error de restricción única en Oracle
					String descripcionDuplicada = itrSeleccionado.getNombre();
					FacesContext.getCurrentInstance().addMessage(null,
		                    new FacesMessage(FacesMessage.SEVERITY_ERROR,  "Error",
									"El ITR con el nombre '" + descripcionDuplicada + "' ya existe."));
				} else {
					// Manejar otros errores 500
					String errorMessage = "Error al crear ITR: Código de respuesta " + response.statusCode();
					String responseBody = response.body();
					if (responseBody != null && !responseBody.isEmpty()) {
						errorMessage += " - " + responseBody;
					}
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
				}
			} else {
				// Manejar otros códigos de error
				String errorMessage = "Error al crear ITR: Código de respuesta " + response.statusCode();
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
					"No se pudo crear el ITR: " + e.getMessage()));
		}
	}
    
    public String cambiarEstadoItr(Long idItr) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpURLConnection conn = null;
        BufferedReader in = null;

        try {
            System.out.println("Iniciando cambio de estado del ITR con ID: " + idItr);

            // Paso 1: Obtener el ITR usando el ID a través del endpoint
            URL url = new URL(BASE_URL + "itr/" + idItr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Leer la respuesta y convertirla a un objeto
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println("Respuesta del servidor al obtener ITR: " + content.toString());

            // Convertir la respuesta JSON a un objeto ITR
            Gson gson = new Gson(); 
            Itr itr = gson.fromJson(content.toString(), Itr.class);
            System.out.println("Itr obtenido: " + itr);

            // Cerrar conexiones y streams
            in.close();
            conn.disconnect();
            in = null;
            conn = null;

            // Modificar el campo 'activo' del ITR
            char nuevoEstado = (itr.getActivo() == 'S') ? 'N' : 'S';
            itr.setActivo(nuevoEstado);
            System.out.println("El campo 'activo' del ITR ha sido establecido a '" + nuevoEstado + "'.");

            // Construir el objeto JSON para enviar
            JsonObject itrJson = new JsonObject();
            itrJson.addProperty("idItr", itr.getIdItr());
            itrJson.addProperty("nombre", itr.getNombre());

            Long idDepartamento = itr.getDepartamento().getIdDepartamento();
            JsonObject dptoJson = apiClient.obtenerJsonDesdeEndpoint("departamento", idDepartamento);

            itrJson.add("departamento", dptoJson);
            itrJson.addProperty("activo", String.valueOf(itr.getActivo())); // Convertir char a String
            System.out.println("JSON construido para enviar al servidor: " + itrJson.toString());
            
            url = new URL(BASE_URL + "itr");

            // Configurar conexión para PUT
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Escribir el JSON en el cuerpo de la solicitud
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = itrJson.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta del servidor: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado modificado exitosamente", null));

                // Actualizar las listas para reflejar el cambio
                cargarListaItr();
                filtrarItrs();
                usuarioBean.cargarListaItr();

                return "ITRs?faces-redirect=true";

            } else {
                // Leer el mensaje de error del servidor
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
                StringBuilder errorResponse = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                System.err.println("Error al modificar estado: " + errorResponse.toString());
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar estado", errorResponse.toString()));
                return null;
            }
        } catch (Exception e) {
            // Mostrar mensaje de error si ocurre una excepción
            System.err.println("Error al cambiar estado del ITR: " + e.getMessage());
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "No se pudo cambiar el estado del ITR: " + e.getMessage()));
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
    
    public void crearItr() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {
            // Paso 1: Validar los datos de entrada
            if (itrSeleccionado == null) {
                itrSeleccionado = new Itr();
            }

            // Asignar el departamento seleccionado al ITR
            if (idDepartamentoSeleccionado != null) {
                Departamento selectedDepartamento = null;
                for (Departamento dept : departamentos) {
                    if (dept.getIdDepartamento().equals(idDepartamentoSeleccionado)) {
                        selectedDepartamento = dept;
                        break;
                    }
                }
                itrSeleccionado.setDepartamento(selectedDepartamento);
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un departamento."));
                return; // Salir del método si no se selecciona un departamento
            }

            // Establecer el estado inicial del ITR a 'S' (Activo)
            itrSeleccionado.setActivo('S');

            // Paso 2: Serializar el objeto ITR a JSON usando ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonItr = objectMapper.writeValueAsString(itrSeleccionado);
            System.out.println("JSON construido para enviar al servidor: " + jsonItr);

            // Paso 3: Enviar la solicitud POST al backend
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(BASE_URL + "itr"))
                                    .header("Content-Type", "application/json")
                                    .POST(HttpRequest.BodyPublishers.ofString(jsonItr))
                                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Paso 4: Manejar la respuesta del servidor
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "ITR creado exitosamente."));
                // Resetear el formulario
                itrSeleccionado = new Itr();
                idDepartamentoSeleccionado = null;
                // Actualizar las listas para reflejar el cambio
                cargarListaItr();
                filtrarItrs();
                usuarioBean.cargarListaItr();
            } else if (response.statusCode() == 500) {
				// Verificar si el error es por restricción única
				if (response.body().contains("ORA-00001")) { // ORA-00001 es el error de restricción única en Oracle
					String descripcionDuplicada = itrSeleccionado.getNombre();
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"El ITR con el nombre '" + descripcionDuplicada + "' ya existe."));
				} else {
					// Manejar otros errores 500
					String errorMessage = "Error al crear ITR: Código de respuesta " + response.statusCode();
					String responseBody = response.body();
					if (responseBody != null && !responseBody.isEmpty()) {
						errorMessage += " - " + responseBody;
					}
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
				}
			} else {
				// Manejar otros códigos de error
				String errorMessage = "Error al crear ITR: Código de respuesta " + response.statusCode();
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
					"No se pudo crear el ITR: " + e.getMessage()));
		}
	}



    
    public boolean isItrActivo(Itr itr) {
        return itr.getActivo() == 'S';
    }

    public String getActionLabel(Itr itr) {
        return isItrActivo(itr) ? "Desactivar" : "Activar";
    }

    public String getConfirmMessage(Itr itr) {
        return isItrActivo(itr) ? "¿Está seguro de que desea desactivar al ITR?" : "¿Está seguro de que desea activar al ITR?";
    }
    

    // Getters y Setters

    public List<Itr> getListaItr() {
        return listaItr;
    }

    public void setListaItr(List<Itr> listaItr) {
        this.listaItr = listaItr;
    }

    public List<Itr> getListaItrsFiltrados() {
        return listaItrsFiltrados;
    }

    public void setListaItrsFiltrados(List<Itr> listaItrsFiltrados) {
        this.listaItrsFiltrados = listaItrsFiltrados;
    }

    public String getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(String estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public Long getIdItrSeleccionado() {
        return idItrSeleccionado;
    }

    public void setIdItrSeleccionado(Long idItrSeleccionado) {
        this.idItrSeleccionado = idItrSeleccionado;
    }

    public Itr getItrSeleccionado() {
        return itrSeleccionado;
    }

    public void setItrSeleccionado(Itr itrSeleccionado) {
        this.itrSeleccionado = itrSeleccionado;
    }

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public Long getIdDepartamentoSeleccionado() {
		return idDepartamentoSeleccionado;
	}

	public void setIdDepartamentoSeleccionado(Long idDepartamentoSeleccionado) {
		this.idDepartamentoSeleccionado = idDepartamentoSeleccionado;
	}
	
	
    
   
}
