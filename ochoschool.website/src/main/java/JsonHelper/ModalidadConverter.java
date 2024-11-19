package JsonHelper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ochoschool.website.beans.EventoBean;
import ochoschool.website.entidades.Modalidad;

@FacesConverter("modalidadConverter")
public class ModalidadConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        EventoBean bean = context.getApplication().evaluateExpressionGet(context, "#{eventoBean}", EventoBean.class);
        return bean.getListaModalidades().stream()
                .filter(m -> m.getDescripcion().equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Modalidad) {
            return ((Modalidad) value).getDescripcion();
        }
        return value.toString();
    }
}