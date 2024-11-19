package ochoschool.website.entidades;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ApiClient {

    private static final String API_URL = "http://localhost:8080/ochoschool.apirest/";

    public JsonNode getDatosUsuarios() throws IOException, InterruptedException {
    	 System.out.println("Iniciando getDatosUsuarios()");
    	    HttpClient httpClient = HttpClient.newHttpClient();
    	    HttpRequest request = HttpRequest.newBuilder()
    	            .uri(URI.create(API_URL + "usuario"))
    	            .build();
    	    System.out.println("URL de la solicitud: " + API_URL + "usuario");
    	    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    	    System.out.println("Código de respuesta: " + response.statusCode());
    	    System.out.println("Cuerpo de la respuesta: " + response.body());
    	    ObjectMapper objectMapper = new ObjectMapper();
    	    JsonNode jsonNode = objectMapper.readTree(response.body());
    	    System.out.println("JsonNode creado: " + jsonNode);
    	    return jsonNode;
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



    public JsonObject obtenerUsuarioPorId(String idUsuario) throws IOException {
        // Construir la URL del endpoint para obtener un usuario por su ID
        String endpointUrl = API_URL + "usuario/" + idUsuario;

        // Crear la conexión HTTP
        HttpURLConnection con = (HttpURLConnection) new URL(endpointUrl).openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Parsear el JSON completo y devolverlo como un objeto JsonObject
            JsonParser parser = new JsonParser();
            return parser.parse(response.toString()).getAsJsonObject();
        }
    }
    
    public JsonNode getDatosEventos() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "evento"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
    }

    public JsonNode getDatosReclamos()  throws IOException, InterruptedException{
    	 HttpClient httpClient = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(API_URL + "reclamo"))
                 .build();

         HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

         ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.readTree(response.body());
	}
    
    public JsonNode getDatosAreas()  throws IOException, InterruptedException{
   	 HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "mantenimiento/area"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
	}
    
    public JsonNode getDatosRoles()  throws IOException, InterruptedException{
      	 HttpClient httpClient = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create(API_URL + "mantenimiento/rol"))
                   .build();

           HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

           ObjectMapper objectMapper = new ObjectMapper();
           return objectMapper.readTree(response.body());
   	}

	
/*
    public void putRequest(String url, String jsonInputString) {
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

            // Leer la respuesta
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



*/

	
}
