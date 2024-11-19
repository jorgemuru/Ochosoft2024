package JsonHelper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ochoschool.website.beans.EventoBean;
import ochoschool.website.entidades.TipoEvento;

@FacesConverter("tipoEventoConverter")
public class TipoEventoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        EventoBean bean = context.getApplication().evaluateExpressionGet(context, "#{eventoBean}", EventoBean.class);
        return bean.getListaTiposEvento().stream()
                .filter(t -> t.getDescripcion().equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof TipoEvento) {
            return ((TipoEvento) value).getDescripcion();
        }
        return value.toString();
    }
}