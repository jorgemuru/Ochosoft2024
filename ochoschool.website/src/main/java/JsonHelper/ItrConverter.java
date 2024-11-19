package JsonHelper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ochoschool.website.beans.EventoBean;
import ochoschool.website.entidades.Itr;

@FacesConverter("itrConverter")
public class ItrConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        EventoBean bean = context.getApplication().evaluateExpressionGet(context, "#{eventoBean}", EventoBean.class);
        return bean.getListaItrs().stream()
                .filter(i -> i.getNombre().equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Itr) {
            return ((Itr) value).getNombre();
        }
        return value.toString();
    }
}