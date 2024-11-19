package ochoschool.apirest.controladores;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ochoschool.apirest.ejb.EstudianteEJB;
import ochoschool.apirest.entidades.Estudiante;

@Path("/estudiante")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstudianteControlador {
	 @Inject
	    private EstudianteEJB estudianteEJB;

	    @GET
	    public Response obtenerTodosLosEstudiantes() {
	        List<Estudiante> estudiantes = estudianteEJB.obtenerTodosLosEstudiantes();
	        return Response.ok(estudiantes).build();
	    }

	    @POST
	    public Response crearEstudiante(Estudiante estudiante) {
	        Estudiante estudianteCreado = estudianteEJB.crearEstudiante(estudiante);
	        return Response.ok(estudianteCreado).build();
	    }

	    @GET
	    @Path("/{id}")
	    public Response obtenerEstudiante(@PathParam("id") Long id) {
	        Estudiante estudiante = estudianteEJB.obtenerEstudiantePorId(id);
	        if (estudiante != null) {
	            return Response.ok(estudiante).build();
	        } else {
	            return Response.status(Response.Status.NOT_FOUND).build();
	        }
	    }

	    @PUT
	    public Response actualizarEstudiante(Estudiante estudiante) {
	        Estudiante estudianteActualizado = estudianteEJB.actualizarEstudiante(estudiante);
	        return Response.ok(estudianteActualizado).build();
	    }

	    @DELETE
	    @Path("/{id}")
	    public Response eliminarEstudiante(@PathParam("id") Long id) {
			if (estudianteEJB.eliminarEstudiante(id)) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
	    }
	    
		@PUT
		@Path("/{id}")
		public Response deshabilitarEstudiante(@PathParam("id") Long id) {
			if (estudianteEJB.deshabilitarEstudiante(id)) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
		}
	
	
}//fin clase
