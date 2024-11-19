package JsonHelper;

import com.fasterxml.jackson.databind.ObjectMapper;

import ochoschool.website.entidades.Genero;

public class GeneroJsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String obtenerJsonDesdeGenero(Genero genero) {
        try {
            // Serializar el objeto Genero a JSON
            return objectMapper.writeValueAsString(genero);
        } catch (Exception e) {
            // Manejar excepciones en caso de error de serialización
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Genero genero = new Genero(/* Parámetros del género */);

        GeneroJsonHelper jsonHelper = new GeneroJsonHelper();
        String jsonGenero = jsonHelper.obtenerJsonDesdeGenero(genero);

        // Imprimir el JSON obtenido
        System.out.println("JSON del objeto Genero: " + jsonGenero);
    }
}
