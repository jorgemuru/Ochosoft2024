package ochoschool.website.entidades;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApiClientAux {

    private static final String API_URL = "http://localhost:8080/ochoschool.apirest/";

    public JsonNode getDataFromApi() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
    }

    public JsonObject obtenerJsonDesdeEndpoint(String endpoint, long id) {
        try {
            // Construir la URL del endpoint con el ID proporcionado
            String endpointUrl = API_URL + "mantenimiento/" + endpoint + "/" + id;

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
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                return jsonResponse;
            }
        } catch (IOException e) {
            // Manejar la excepción de entrada/salida (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        } catch (Exception e) {
            // Manejar otras excepciones (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        }
    }
    
    // hacer metodo generico prueba
     
     public JsonObject obtenerJsonDesdeEndpointGeneral(String endpoint, long id) {
        try {
            // Construir la URL del endpoint con el ID proporcionado
            String endpointUrl = API_URL  + endpoint + "/" + id;

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
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                return jsonResponse;
            }
        } catch (IOException e) {
            // Manejar la excepción de entrada/salida (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        } catch (Exception e) {
            // Manejar otras excepciones (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        }
    }
     // fin hacer metodo generico prueba
    
    
    
    public JsonArray obtenerJson(String endpoint) {
        try {
            // Construir la URL del endpoint con el ID proporcionado
            String endpointUrl = API_URL + "mantenimiento/" + endpoint ;

            // Crear la conexión HTTP
            HttpURLConnection con = (HttpURLConnection) new URL(endpointUrl).openConnection();
            con.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parsear el JSON completo y devolverlo como un objeto JsonArray
                JsonParser parser = new JsonParser();
                JsonArray jsonResponse = parser.parse(response.toString()).getAsJsonArray();

                return jsonResponse;
            }
        } catch (IOException e) {
            // Manejar la excepción de entrada/salida (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        } catch (Exception e) {
            // Manejar otras excepciones (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        }
    }
    
    
    public JsonObject obtenerJsonDesdeEndpointTutor(String endpoint, long id) {
        try {
            // Construir la URL del endpoint con el ID proporcionado
            String endpointUrl = API_URL + endpoint + "/" + id;

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
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                return jsonResponse;
            }
        } catch (IOException e) {
            // Manejar la excepción de entrada/salida (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        } catch (Exception e) {
            // Manejar otras excepciones (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        }
    }
    
    public JsonObject obtenerJsonDesdeEndpointTipoReclamo(long id) {
        try {
            // Construir la URL del endpoint con el ID proporcionado
            String endpointUrl = API_URL + "reclamo/tipo/" + id;

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
                JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

                return jsonResponse;
            }
        } catch (IOException e) {
            // Manejar la excepción de entrada/salida (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        } catch (Exception e) {
            // Manejar otras excepciones (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        }
    }
    
    
    public JsonArray obtenerTodosJson(String endpoint) {
        try {
            // Construir la URL del endpoint con el ID proporcionado
            String endpointUrl = API_URL + endpoint ;

            // Crear la conexión HTTP
            HttpURLConnection con = (HttpURLConnection) new URL(endpointUrl).openConnection();
            con.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parsear el JSON completo y devolverlo como un objeto JsonArray
                JsonParser parser = new JsonParser();
                JsonArray jsonResponse = parser.parse(response.toString()).getAsJsonArray();

                return jsonResponse;
            }
        } catch (IOException e) {
            // Manejar la excepción de entrada/salida (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        } catch (Exception e) {
            // Manejar otras excepciones (puedes personalizar este bloque según tus necesidades)
            e.printStackTrace();
            return null; // O lanzar una excepción personalizada si es más adecuado para tu aplicación
        }
    }
    
    
    

}
