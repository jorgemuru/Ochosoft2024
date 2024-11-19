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

import ochoschool.apirest.ejb.ConvocadoEventoEJB;
import ochoschool.apirest.ejb.ConvocatoriaEventoEJB;
import ochoschool.apirest.ejb.EstadoEventoEJB;
import ochoschool.apirest.ejb.EventoEJB;
import ochoschool.apirest.ejb.ModalidadEJB;
import ochoschool.apirest.ejb.TipoEventoEJB;
import ochoschool.apirest.entidades.ConvocadoEvento;
import ochoschool.apirest.entidades.ConvocatoriaEvento;
import ochoschool.apirest.entidades.EstadoEvento;
import ochoschool.apirest.entidades.Evento;
import ochoschool.apirest.entidades.Modalidad;
import ochoschool.apirest.entidades.TipoEvento;

@Path("/evento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoControlador {
	@Inject
	private EventoEJB eventoEJB;
	@Inject
	private EstadoEventoEJB estadoEventoEJB;
	@Inject
	private TipoEventoEJB tipoEventoEJB;
	@Inject
	private ModalidadEJB modalidadEJB;
	@Inject
	private ConvocatoriaEventoEJB convocatoriaEventoEJB;
	@Inject
	private ConvocadoEventoEJB convocadoEventoEJB;

	@GET
	public Response obtenerTodosLosEventos() {
		List<Evento> eventos = eventoEJB.obtenerTodosLosEventos();
		return Response.ok(eventos).build();
	}

	@POST
	public Response crearEvento(Evento evento) {
		Evento eventoCreado = eventoEJB.crearEvento(evento);
		return Response.ok(eventoCreado).build();
	}

	@GET
	@Path("/{id}")
	public Response obtenerEvento(@PathParam("id") Long id) {
		Evento evento = eventoEJB.obtenerEventoPorId(id);
		if (evento != null) {
			return Response.ok(evento).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	public Response actualizarEvento(Evento evento) {
		Evento eventoActualizado = eventoEJB.actualizarEvento(evento);
		return Response.ok(eventoActualizado).build();
	}

	@DELETE
	@Path("/{id}")
	public Response eliminarEvento(@PathParam("id") Long id) {
		if (eventoEJB.eliminarEvento(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	// Endpoints para Estados_Evento
	@GET
	@Path("/estado")
	public Response obtenerTodosLosEstadosEvento() {
		List<EstadoEvento> estados = estadoEventoEJB.obtenerTodosLosEstadosEvento();
		return Response.ok(estados).build();
	}

	@POST
	@Path("/estado")
	public Response crearEstadoEvento(EstadoEvento estado) {
		EstadoEvento estadoEventoCreado = estadoEventoEJB.crearEstadoEvento(estado);
		return Response.ok(estadoEventoCreado).build();
	}

	@GET
	@Path("/estado/{id}")
	public Response obtenerEstadoEvento(@PathParam("id") Long id) {
		EstadoEvento estado = estadoEventoEJB.obtenerEstadoEventoPorId(id);
		if (estado != null) {
			return Response.ok(estado).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/estado")
	public Response actualizarEstadoEvento(EstadoEvento estado) {
		EstadoEvento estadoEventoActualizado = estadoEventoEJB.actualizarEstadoEvento(estado);
		return Response.ok(estadoEventoActualizado).build();
	}

	@DELETE
	@Path("/estado/{id}")
	public Response eliminarEstadoEvento(@PathParam("id") Long id) {
		if (estadoEventoEJB.eliminarEstadoEvento(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}

	// Endpoints para Tipos_Evento
	@GET
	@Path("/tipo")
	public Response obtenerTodosLosTiposEvento() {
		List<TipoEvento> tipos = tipoEventoEJB.obtenerTodosLosTiposEvento();
		return Response.ok(tipos).build();
	}

	@POST
	@Path("/tipo")
	public Response crearTipoEvento(TipoEvento tipo) {
		TipoEvento tipoEventoCreado = tipoEventoEJB.crearTipoEvento(tipo);
		return Response.ok(tipoEventoCreado).build();
	}

	@GET
	@Path("/tipo/{id}")
	public Response obtenerTipoEvento(@PathParam("id") Long id) {
		TipoEvento tipo = tipoEventoEJB.obtenerTipoEventoPorId(id);
		if (tipo != null) {
			return Response.ok(tipo).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/tipo")
	public Response actualizarTipoEvento(TipoEvento tipo) {
		TipoEvento tipoEventoActualizado = tipoEventoEJB.actualizarTipoEvento(tipo);
		return Response.ok(tipoEventoActualizado).build();
	}

	@DELETE
	@Path("/tipo/{id}")
	public Response eliminarTipoEvento(@PathParam("id") Long id) {
		if (tipoEventoEJB.eliminarTipoEvento(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}

	// Endpoints para Modalidad
	@GET
	@Path("/modalidad")
	public Response obtenerTodasLasModalidades() {
		List<Modalidad> lista = modalidadEJB.obtenerTodasLasModalidades();
		return Response.ok(lista).build();
	}

	@POST
	@Path("/modalidad")
	public Response crearModalidad(Modalidad modalidad) {
		Modalidad modalidadCreado = modalidadEJB.crearModalidad(modalidad);
		return Response.ok(modalidadCreado).build();
	}

	@GET
	@Path("/modalidad/{id}")
	public Response obtenerModalidad(@PathParam("id") Long id) {
		Modalidad modalidad = modalidadEJB.obtenerModalidadPorId(id);
		if (modalidad != null) {
			return Response.ok(modalidad).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/modalidad")
	public Response actualizarModalidad(Modalidad modalidad) {
		Modalidad modalidadActualizada = modalidadEJB.actualizarModalidad(modalidad);
		return Response.ok(modalidadActualizada).build();
	}

	@DELETE
	@Path("/modalidad/{id}")
	public Response eliminarModalidad(@PathParam("id") Long id) {
		if (modalidadEJB.eliminarModalidad(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints para Convocatoria
	@GET
	@Path("/convocatoria")
	public Response obtenerTodasLasConvocatorias() {
		List<ConvocatoriaEvento> convocatorias = convocatoriaEventoEJB.obtenerTodasLasConvocatorias();
		return Response.ok(convocatorias).build();
	}

	@POST
	@Path("/convocatoria")
	public Response crearConvocatoriaEvento(ConvocatoriaEvento convocatoria) {
		ConvocatoriaEvento convocatoriaEventoCreado = convocatoriaEventoEJB.crearConvocatoriaEvento(convocatoria);
		return Response.ok(convocatoriaEventoCreado).build();
	}

	@GET
	@Path("/convocatoria/{id}")
	public Response obtenerConvocatoriaEvento(@PathParam("id") Long id) {
		ConvocatoriaEvento convocatoria = convocatoriaEventoEJB.obtenerConvocatoriaEventoPorId(id);
		if (convocatoria != null) {
			return Response.ok(convocatoria).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/convocatoria")
	public Response actualizarConvocatoriaEvento(ConvocatoriaEvento convocatoria) {
		ConvocatoriaEvento convocatoriaEventoActualizado = convocatoriaEventoEJB.actualizarConvocatoriaEvento(convocatoria);
		return Response.ok(convocatoriaEventoActualizado).build();
	}

	@DELETE
	@Path("/convocatoria/{id}")
	public Response eliminarConvocatoriaEvento(@PathParam("id") Long id) {
		if (convocatoriaEventoEJB.eliminarConvocatoriaEvento(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints para Convocado
	@GET
	@Path("/convocado")
	public Response obtenerTodosLosConvocados() {
		List<ConvocadoEvento> convocados = convocadoEventoEJB.obtenerTodosLosConvocados();
		return Response.ok(convocados).build();
	}

	@POST
	@Path("/convocado")
	public Response crearConvocadoEvento(ConvocadoEvento convocado) {
		ConvocadoEvento convocadoEventoCreado = convocadoEventoEJB.crearConvocadoEvento(convocado);
		return Response.ok(convocadoEventoCreado).build();
	}

	@GET
	@Path("/convocado/{id}")
	public Response obtenerConvocadoEvento(@PathParam("id") Long id) {
		ConvocadoEvento convocado = convocadoEventoEJB.obtenerConvocadoEventoPorId(id);
		if (convocado != null) {
			return Response.ok(convocado).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/convocado")
	public Response actualizarConvocadoEvento(ConvocadoEvento convocado) {
		ConvocadoEvento convocadoEventoActualizado = convocadoEventoEJB.actualizarConvocadoEvento(convocado);
		return Response.ok(convocadoEventoActualizado).build();
	}

	@DELETE
	@Path("/convocado/{id}")
	public Response eliminarConvocadoEvento(@PathParam("id") Long id) {
		if (convocadoEventoEJB.eliminarConvocadoEvento(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
}//fin clase
