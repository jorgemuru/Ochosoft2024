package ochoschool.website.servicios;

import ochoschool.apirest.entidades.Analista;
import ochoschool.apirest.entidades.Estudiante;
import ochoschool.apirest.entidades.Tutor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServicios {

    // Definir un método para registrar un Analista
    @POST("ruta/analista")  // Especifica la ruta del endpoint en tu API
    Call<Analista> guardarAnalista(@Body Analista analista);

    // Definir un método para registrar un Estudiante
    @POST("ruta/estudiante")  // Especifica la ruta del endpoint en tu API
    Call<Estudiante> guardarEstudiante(@Body Estudiante estudiante);

    // Definir un método para registrar un Tutor
    @POST("ruta/docente")  // Especifica la ruta del endpoint en tu API
    Call<Tutor> guardarTutor(@Body Tutor tutor);
}

