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
import ochoschool.apirest.ejb.ReclamoEJB;
import ochoschool.apirest.ejb.TipoReclamoEJB;
import ochoschool.apirest.ejb.RegistroReclamoEJB;
import ochoschool.apirest.entidades.Reclamo;
import ochoschool.apirest.entidades.RegistroReclamo;
import ochoschool.apirest.entidades.TipoReclamo;

@Path("/reclamo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReclamoControlador {

	@Inject
	private ReclamoEJB reclamoEJB;
	@Inject
	private RegistroReclamoEJB registroReclamoEJB;
	@Inject
	private TipoReclamoEJB tipoReclamoEJB;

	@GET
	public Response obtenerTodosLosReclamos() {
		List<Reclamo> reclamos = reclamoEJB.obtenerTodosLosReclamos();
		return Response.ok(reclamos).build();
	}

	@POST
	public Response crearReclamo(Reclamo reclamo) {
		Reclamo reclamoCreado = reclamoEJB.crearReclamo(reclamo);
		return Response.ok(reclamoCreado).build();
	}

	@GET
	@Path("/{id}")
	public Response obtenerReclamo(@PathParam("id") Long id) {
		Reclamo reclamo = reclamoEJB.obtenerReclamoPorId(id);
		if (reclamo != null) {
			return Response.ok(reclamo).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	public Response actualizarReclamo(Reclamo reclamo) {
		Reclamo reclamoActualizado = reclamoEJB.actualizarReclamo(reclamo);
		return Response.ok(reclamoActualizado).build();
	}

	@DELETE
	@Path("/{id}")
	public Response eliminarReclamo(@PathParam("id") Long id) {
		if (reclamoEJB.eliminarReclamo(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints para Registro de Reclamo
	@GET
	@Path("/registro")
	public Response obtenerTodosLosRegistroReclamos() {
		List<RegistroReclamo> registroReclamos = registroReclamoEJB.obtenerTodosLosRegistroReclamos();
		return Response.ok(registroReclamos).build();
	}

	@POST
	@Path("/registro")
	public Response crearRegistroReclamo(RegistroReclamo registroReclamo) {
		RegistroReclamo registroReclamoCreado = registroReclamoEJB.crearRegistroReclamo(registroReclamo);
		return Response.ok(registroReclamoCreado).build();
	}

	@GET
	@Path("/registro/{id}")
	public Response obtenerRegistroReclamo(@PathParam("id") Long id) {
		RegistroReclamo registroReclamo = registroReclamoEJB.obtenerRegistroReclamoPorId(id);
		if (registroReclamo != null) {
			return Response.ok(registroReclamo).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/registro")
	public Response actualizarRegistroReclamo(RegistroReclamo registroReclamo) {
		RegistroReclamo registroReclamoActualizado = registroReclamoEJB.actualizarRegistroReclamo(registroReclamo);
		return Response.ok(registroReclamoActualizado).build();
	}

	@DELETE
	@Path("/registro/{id}")
	public Response eliminarRegistroReclamo(@PathParam("id") Long id) {
		if (registroReclamoEJB.eliminarRegistroReclamo(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	// Endpoints para Tipos Reclamo
	@GET
	@Path("/tipo")
	public Response obtenerTodosLosTiposReclamo() {
		List<TipoReclamo> tipos = tipoReclamoEJB.obtenerTodosLosTiposReclamo();
		return Response.ok(tipos).build();
	}

	@POST
	@Path("/tipo")
	public Response crearTipoReclamo(TipoReclamo tipo) {
		TipoReclamo tipoReclamoCreado = tipoReclamoEJB.crearTipoReclamo(tipo);
		return Response.ok(tipoReclamoCreado).build();
	}

	@GET
	@Path("/tipo/{id}")
	public Response obtenerTipoReclamo(@PathParam("id") Long id) {
		TipoReclamo tipo = tipoReclamoEJB.obtenerTipoReclamoPorId(id);
		if (tipo != null) {
			return Response.ok(tipo).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/tipo")
	public Response actualizarTipoReclamo(TipoReclamo tipo) {
		TipoReclamo tipoReclamoActualizado = tipoReclamoEJB.actualizarTipoReclamo(tipo);
		return Response.ok(tipoReclamoActualizado).build();
	}

	@DELETE
	@Path("/tipo/{id}")
	public Response eliminarTipoReclamo(@PathParam("id") Long id) {
		if (tipoReclamoEJB.eliminarTipoReclamo(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
}//fin clase
