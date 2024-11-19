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
import ochoschool.apirest.ejb.TutorEJB;
import ochoschool.apirest.entidades.Tutor;

@Path("/tutor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TutorControlador {
	 @Inject
	    private TutorEJB tutorEJB;

	    @GET
	    public Response obtenerTodosLosTutores() {
	        List<Tutor> tutores = tutorEJB.obtenerTodosLosTutores();
	        return Response.ok(tutores).build();
	    }

	    @POST
	    public Response crearTutor(Tutor tutor) {
	        Tutor tutorCreado = tutorEJB.crearTutor(tutor);
	        return Response.ok(tutorCreado).build();
	    }

	    @GET
	    @Path("/{id}")
	    public Response obtenerTutor(@PathParam("id") Long id) {
	        Tutor tutor = tutorEJB.obtenerTutorPorId(id);
	        if (tutor != null) {
	            return Response.ok(tutor).build();
	        } else {
	            return Response.status(Response.Status.NOT_FOUND).build();
	        }
	    }

	    @PUT
	    public Response actualizarTutor(Tutor tutor) {
	        Tutor tutorActualizado = tutorEJB.actualizarTutor(tutor);
	        return Response.ok(tutorActualizado).build();
	    }

	    @DELETE
	    @Path("/{id}")
	    public Response eliminarTutor(@PathParam("id") Long id) {
			if (tutorEJB.eliminarTutor(id)) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
	    }
	    
		@PUT
		@Path("/{id}")
		public Response deshabilitarTutor(@PathParam("id") Long id) {
			if (tutorEJB.deshabilitarTutor(id)) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
		}
	
}//fin clase
