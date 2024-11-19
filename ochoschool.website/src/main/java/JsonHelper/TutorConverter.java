package JsonHelper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ochoschool.website.entidades.ApiClient;
import ochoschool.website.entidades.ApiClientAux;
import ochoschool.website.entidades.Tutor;

import java.util.List;

@FacesConverter("tutorConverter")
public class TutorConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Aquí deberías convertir el String 'value' a un objeto Tutor
        Long tutorId = Long.valueOf(value); // Asumiendo que 'value' es el ID del tutor
        Tutor tutor = obtenerTutorPorId(tutorId); // Debes implementar este método

        return tutor;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Tutor) {
            Long id = ((Tutor) value).getIdUsuario();
            return String.valueOf(id);
        } else {
            throw new IllegalArgumentException("Object is not an instance of Tutor: " + value);
        }
    }
    
    // Método para obtener un tutor por su ID (deberías implementarlo según tu lógica de acceso a datos)
    private Tutor obtenerTutorPorId(Long tutorId) {
        ApiClientAux apiClient = new ApiClientAux(); // Crear instancia de ApiClientAux
        JsonObject tutorJson = apiClient.obtenerJsonDesdeEndpointTutor("usuario/", tutorId); // Obtener JSON del tutor por ID

        // Asumiendo que tutorJson contiene los datos del tutor en formato JSON
        Gson gson = new Gson();
        Tutor tutor = gson.fromJson(tutorJson, Tutor.class); // Convertir JSON a objeto Tutor

        return tutor;
    }

    
}
