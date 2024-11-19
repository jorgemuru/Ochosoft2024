package ochoschool.apirest.controladores;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ochoschool.apirest.ejb.*;
import ochoschool.apirest.entidades.*;

@Path("/reporte")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportesControlador {
	@Inject
	private ConvocadoEventoEJB convocadoEJB;
	@Inject
	private ReclamoEJB reclamoEJB;

	//Asistencias y Convocatorias
	@GET
	@Path("/asistencia/estudiante/{id}")
	public Response obtenerAsistenciaEstudiante(@PathParam("id") long id) {
		
		List<ConvocadoEvento> convocados = convocadoEJB.obtenerAsistenciaEstudiante(id);
		if (!convocados.isEmpty()) {
			return Response.ok(convocados).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/asistencia/evento/{id}")
	public Response obtenerAsistenciaEvento(@PathParam("id") long id) {
		
		List<ConvocadoEvento> convocados = convocadoEJB.obtenerAsistenciaEvento(id);
		if (!convocados.isEmpty()) {
			return Response.ok(convocados).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/convocatoria/estudiante/{id}")
	public Response obtenerConvocatoriasEstudiante(@PathParam("id") long id) {
		
		List<ConvocadoEvento> convocados = convocadoEJB.obtenerConvocatoriasEstudiante(id);
		if (!convocados.isEmpty()) {
			return Response.ok(convocados).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/convocatoria/evento/{id}")
	public Response obtenerConvocatoriasEvento(@PathParam("id") long id) {
		
		List<ConvocadoEvento> convocados = convocadoEJB.obtenerConvocatoriasEvento(id);
		if (!convocados.isEmpty()) {
			return Response.ok(convocados).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	//Reclamos
	@GET
	@Path("/reclamo/itr/{id}")
	public Response obtenerReclamosXItr(@PathParam("id") long id) {
		
		List<Reclamo> reclamos = reclamoEJB.obtenerReclamosXItr(id);
		if (!reclamos.isEmpty()) {
			return Response.ok(reclamos).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	@GET
	@Path("/reclamo/evento/{id}")
	public Response obtenerReclamosXEvento(@PathParam("id") long id) {
		
		List<Reclamo> reclamos = reclamoEJB.obtenerReclamosXEvento(id);
		if (!reclamos.isEmpty()) {
			return Response.ok(reclamos).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	@GET
	@Path("/reclamo/generacion/{gen}")
	public Response obtenerReclamosXItr(@PathParam("gen") String generacion) {
		
		List<Reclamo> reclamos = reclamoEJB.obtenerReclamosXGen(generacion);
		if (!reclamos.isEmpty()) {
			return Response.ok(reclamos).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	@GET
	@Path("/reclamo/anio/{anio}")
	public Response obtenerReclamosXAnio(@PathParam("anio") int anio) {
		int anioActual = LocalDate.now().getYear();
		if(anio > 2000 && anio <= anioActual) {
		List<Reclamo> reclamos = reclamoEJB.obtenerReclamosXAnio(anio);
		if (!reclamos.isEmpty()) {
			return Response.ok(reclamos).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		}else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}
	@GET
	@Path("/reclamo/tipo/{tipo}")
	public Response obtenerReclamosXTipo(@PathParam("tipo") String tipo) {
		
		List<Reclamo> reclamos = reclamoEJB.obtenerReclamosXTipo(tipo);
		if (!reclamos.isEmpty()) {
			return Response.ok(reclamos).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
}// fin clase
