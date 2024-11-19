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
import ochoschool.apirest.ejb.JustificacionEJB;
import ochoschool.apirest.entidades.Justificacion;
import ochoschool.apirest.ejb.RegistroJustificacionEJB;
import ochoschool.apirest.entidades.RegistroJustificacion;

@Path("/justificacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JustificacionControlador {

	@Inject
	private JustificacionEJB justificacionEJB;
	@Inject
	private RegistroJustificacionEJB registroJustificacionEJB;

	@GET
	public Response obtenerTodasLasJustificaciones() {
		List<Justificacion> justificaciones = justificacionEJB.obtenerTodasLasJustificaciones();
		return Response.ok(justificaciones).build();
	}

	@POST
	public Response crearJustificacion(Justificacion justificacion) {
		Justificacion justificacionCreada = justificacionEJB.crearJustificacion(justificacion);
		return Response.ok(justificacionCreada).build();
	}

	@GET
	@Path("/{id}")
	public Response obtenerJustificacion(@PathParam("id") Long id) {
		Justificacion justificacion = justificacionEJB.obtenerJustificacionPorId(id);
		if (justificacion != null) {
			return Response.ok(justificacion).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	public Response actualizarJustificacion(Justificacion justificacion) {
		Justificacion justificacionActualizada = justificacionEJB.actualizarJustificacion(justificacion);
		return Response.ok(justificacionActualizada).build();
	}

	@DELETE
	@Path("/{id}")
	public Response eliminarJustificacion(@PathParam("id") Long id) {
		if (justificacionEJB.eliminarJustificacion(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints para Registro de Justificacion
	@GET
	@Path("/registro")
	public Response obtenerTodosLosRegistrosJustificacion() {
		List<RegistroJustificacion> registros = registroJustificacionEJB.obtenerTodosLosRegistrosJustificacion();
		return Response.ok(registros).build();
	}

	@POST
	@Path("/registro")
	public Response crearRegistroJustificacion(RegistroJustificacion registroJustificacion) {
		RegistroJustificacion registroCreado = registroJustificacionEJB.crearRegistroJustificacion(registroJustificacion);
		return Response.ok(registroCreado).build();
	}

	@GET
	@Path("/registro/{id}")
	public Response obtenerRegistroJustificacion(@PathParam("id") Long id) {
		RegistroJustificacion registro = registroJustificacionEJB.obtenerRegistroJustificacionPorId(id);
		if (registro != null) {
			return Response.ok(registro).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/registro")
	public Response actualizarRegistroJustificacion(RegistroJustificacion registroJustificacion) {
		RegistroJustificacion registroActualizado = registroJustificacionEJB.actualizarRegistroJustificacion(registroJustificacion);
		return Response.ok(registroActualizado).build();
	}

	@DELETE
	@Path("/registro/{id}")
	public Response eliminarRegistroJustificacion(@PathParam("id") Long id) {
		if (registroJustificacionEJB.eliminarRegistroJustificacion(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
}//fin clase
