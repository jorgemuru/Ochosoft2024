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
import ochoschool.apirest.ejb.AnalistaEJB;
import ochoschool.apirest.entidades.Analista;

@Path("/analista")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalistaControlador {
	 @Inject
	    private AnalistaEJB analistaEJB;

	    @GET
	    public Response obtenerTodosLosAnalistas() {
	        List<Analista> analistas = analistaEJB.obtenerTodosLosAnalistas();
	        return Response.ok(analistas).build();
	    }

	    @POST
	    public Response crearAnalista(Analista analista) {
	        Analista analistaCreado = analistaEJB.crearAnalista(analista);
	        return Response.ok(analistaCreado).build();
	    }

	    @GET
	    @Path("/{id}")
	    public Response obtenerAnalista(@PathParam("id") Long id) {
	        Analista analista = analistaEJB.obtenerAnalistaPorId(id);
	        if (analista != null) {
	            return Response.ok(analista).build();
	        } else {
	            return Response.status(Response.Status.NOT_FOUND).build();
	        }
	    }

	    @PUT
	    public Response actualizarAnalista(Analista analista) {
	        Analista analistaActualizado = analistaEJB.actualizarAnalista(analista);
	        return Response.ok(analistaActualizado).build();
	    }

	    @DELETE
	    @Path("/{id}")
	    public Response eliminarAnalista(@PathParam("id") Long id) {
			if (analistaEJB.eliminarAnalista(id)) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
	    }
	    
		@PUT
		@Path("/{id}")
		public Response deshabilitarAnalista(@PathParam("id") Long id) {
			if (analistaEJB.deshabilitarAnalista(id)) {
				return Response.ok().build();
			} else {
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
		}
	
}//fin clase
