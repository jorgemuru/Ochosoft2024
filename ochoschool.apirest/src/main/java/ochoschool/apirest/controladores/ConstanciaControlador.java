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
import ochoschool.apirest.ejb.ConstanciaEJB;
import ochoschool.apirest.entidades.Constancia;
import ochoschool.apirest.ejb.RegistroConstanciaEJB;
import ochoschool.apirest.ejb.TipoEJB;
import ochoschool.apirest.entidades.RegistroConstancia;
import ochoschool.apirest.entidades.Tipo;
import ochoschool.apirest.ejb.EmisionConstanciaEJB;
import ochoschool.apirest.entidades.EmisionConstancia;
import ochoschool.apirest.ejb.PlantillaConstanciaEJB;
import ochoschool.apirest.entidades.PlantillaConstancia;

@Path("/constancia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConstanciaControlador {
	@Inject
	private ConstanciaEJB constanciaEJB;
	@Inject 
	private RegistroConstanciaEJB registroConstanciaEJB;
	@Inject
	private EmisionConstanciaEJB emisionConstanciaEJB;
	@Inject
	private PlantillaConstanciaEJB plantillaConstanciaEJB;
	@Inject
	private TipoEJB tipoEJB;

	//Endpoints Constancias
	@GET
	public Response obtenerTodasLasConstancias() {
		List<Constancia> constancias = constanciaEJB.obtenerTodasLasConstancias();
		return Response.ok(constancias).build();
	}

	@POST
	public Response crearConstancia(Constancia constancia) {
		Constancia constanciaCreada = constanciaEJB.crearConstancia(constancia);
		return Response.ok(constanciaCreada).build();
	}

	@GET
	@Path("/{id}")
	public Response obtenerConstancia(@PathParam("id") Long id) {
		Constancia constancia = constanciaEJB.obtenerConstanciaPorId(id);
		if (constancia != null) {
			return Response.ok(constancia).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	public Response actualizarConstancia(Constancia constancia) {
		Constancia constanciaActualizada = constanciaEJB.actualizarConstancia(constancia);
		return Response.ok(constanciaActualizada).build();
	}

	@DELETE
	@Path("/{id}")
	public Response eliminarConstancia(@PathParam("id") Long id) {
		if (constanciaEJB.eliminarConstancia(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints Registro Constancias
	@GET
	@Path("/registro")
	public Response obtenerTodosLosRegistrosConstancia() {
		List<RegistroConstancia> registros = registroConstanciaEJB.obtenerTodosLosRegistrosConstancia();
		return Response.ok(registros).build();
	}

	@POST
	@Path("/registro")
	public Response crearRegistroConstancia(RegistroConstancia registro) {
		RegistroConstancia registroCreado = registroConstanciaEJB.crearRegistroConstancia(registro);
		return Response.ok(registroCreado).build();
	}

	@GET
	@Path("/registro/{id}")
	public Response obtenerRegistroConstancia(@PathParam("id") Long id) {
		RegistroConstancia registro = registroConstanciaEJB.obtenerRegistroConstanciaPorId(id);
		if (registro != null) {
			return Response.ok(registro).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/registro")
	public Response actualizarRegistroConstancia(RegistroConstancia registro) {
		RegistroConstancia registroActualizado = registroConstanciaEJB.actualizarRegistroConstancia(registro);
		return Response.ok(registroActualizado).build();
	}

	@DELETE
	@Path("/registro/{id}")
	public Response eliminarRegistroConstancia(@PathParam("id") Long id) {
		if (registroConstanciaEJB.eliminarRegistroConstancia(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints Emision Constancias
	@GET
	@Path("/emision")
	public Response obtenerTodosLasEmisionesConstancia() {
		List<EmisionConstancia> emisiones = emisionConstanciaEJB.obtenerTodasLasEmisionesConstancia();
		return Response.ok(emisiones).build();
	}

	@POST
	@Path("/emision")
	public Response crearEmisionConstancia(EmisionConstancia emision) {
		EmisionConstancia emisionCreada = emisionConstanciaEJB.crearEmisionConstancia(emision);
		return Response.ok(emisionCreada).build();
	}

	@GET
	@Path("/emision/{id}")
	public Response obtenerEmisionConstancia(@PathParam("id") Long id) {
		EmisionConstancia emision = emisionConstanciaEJB.obtenerEmisionConstanciaPorId(id);
		if (emision != null) {
			return Response.ok(emision).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/emision")
	public Response actualizarEmisionConstancia(EmisionConstancia emision) {
		EmisionConstancia emisionActualizada = emisionConstanciaEJB.actualizarEmisionConstancia(emision);
		return Response.ok(emisionActualizada).build();
	}

	@DELETE
	@Path("/emision/{id}")
	public Response eliminarEmisionConstancia(@PathParam("id") Long id) {
		if (emisionConstanciaEJB.eliminarEmisionConstancia(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints Plantillas Constancias
	@GET
	@Path("/plantilla")
	public Response obtenerTodasLasPlantillasConstancia() {
		List<PlantillaConstancia> plantillas = plantillaConstanciaEJB.obtenerTodasLasPlantillasConstancia();
		return Response.ok(plantillas).build();
	}

	@POST
	@Path("/plantilla")
	public Response crearPlantillaConstancia(PlantillaConstancia plantilla) {
		PlantillaConstancia plantillaCreada = plantillaConstanciaEJB.crearPlantillaConstancia(plantilla);
		return Response.ok(plantillaCreada).build();
	}

	@GET
	@Path("/plantilla/{id}")
	public Response obtenerPlantillaConstancia(@PathParam("id") Long id) {
		PlantillaConstancia plantilla = plantillaConstanciaEJB.obtenerPlantillaConstanciaPorId(id);
		if (plantilla != null) {
			return Response.ok(plantilla).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/plantilla")
	public Response actualizarPlantillaConstancia(PlantillaConstancia plantilla) {
		PlantillaConstancia plantillaActualizada = plantillaConstanciaEJB.actualizarPlantillaConstancia(plantilla);
		return Response.ok(plantillaActualizada).build();
	}

	@DELETE
	@Path("/plantilla/{id}")
	public Response eliminarPlantillaConstancia(@PathParam("id") Long id) {
		if (plantillaConstanciaEJB.eliminarPlantillaConstancia(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	// Endpoints Tipo de Constancias
	@GET
	@Path("/tipo")
	public Response obtenerTodosLosTipos() {
		List<Tipo> tipos = tipoEJB.obtenerTodosLosTipos();
		return Response.ok(tipos).build();
	}

	@POST
	@Path("/tipo")
	public Response crearTipo(Tipo tipo) {
		Tipo tipoCreado = tipoEJB.crearTipo(tipo);
		return Response.ok(tipoCreado).build();
	}

	@GET
	@Path("/tipo/{id}")
	public Response obtenerTipo(@PathParam("id") Long id) {
		Tipo tipo = tipoEJB.obtenerTipoPorId(id);
		if (tipo != null) {
			return Response.ok(tipo).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/tipo")
	public Response actualizarTipo(Tipo tipo) {
		Tipo tipoActualizado = tipoEJB.actualizarTipo(tipo);
		return Response.ok(tipoActualizado).build();
	}

	@DELETE
	@Path("/tipo/{id}")
	public Response eliminarTipo(@PathParam("id") Long id) {
		if (tipoEJB.eliminarTipo(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	
}//fin clase
