package ochoschool.website.beans;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ochoschool.website.entidades.Usuario;

import com.fasterxml.jackson.databind.JsonNode;

@FacesConverter(value = "usuarioConverter")
public class UsuarioConverter implements Converter {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            // Leer el JSON en un JsonNode
            JsonNode jsonNode = mapper.readTree(value);

            // Si el JsonNode es un ObjectNode, eliminar la propiedad "firmaDigital"
            if (jsonNode instanceof ObjectNode) {
                ((ObjectNode) jsonNode).remove("firmaDigital");
            }

            // Convertir el JsonNode a una cadena JSON
            String jsonString = mapper.writeValueAsString(jsonNode);

            // Convertir la cadena JSON a un objeto Usuario
            return mapper.readValue(jsonString, Usuario.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert JSON to Usuario object", e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            // Convertir el objeto Usuario a JSON
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert Usuario object to JSON", e);
        }
    }
}
