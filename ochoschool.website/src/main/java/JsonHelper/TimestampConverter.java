package JsonHelper;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@FacesConverter("timestampConverter")
public class TimestampConverter implements Converter {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Date date = sdf.parse(value);
            return date.getTime();
        } catch (ParseException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de conversión", "Formato de fecha inválido"));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        Date date = new Date((Long) value);
        return sdf.format(date);
    }
}