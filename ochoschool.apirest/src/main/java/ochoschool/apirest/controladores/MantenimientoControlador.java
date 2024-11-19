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
import ochoschool.apirest.ejb.*;
import ochoschool.apirest.entidades.*;

@Path("/mantenimiento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MantenimientoControlador {
	@Inject
	private AreaEJB areaEJB;
	@Inject
	private ItrEJB itrEJB;
	@Inject
	private EstadoEJb estadoEJB;
	@Inject
	private GeneroEJB generoEJB;
	@Inject
	private LocalidadEJB localidadEJB;
	@Inject
	private DepartamentoEJB departamentoEJB;
	@Inject
	private RolEJB rolEJB;
	
	// Endpoints para ITRs
	@GET
	@Path("/itr")
	public Response obtenerTodosLosItrs() {
		List<Itr> itrs = itrEJB.obtenerTodosLosItr();
		return Response.ok(itrs).build();
	}

	@POST
	@Path("/itr")
	public Response crearItr(Itr itr) {
		Itr itrCreado = itrEJB.crearItr(itr);
		return Response.ok(itrCreado).build();
	}

	@GET
	@Path("/itr/{id}")
	public Response obtenerItr(@PathParam("id") Long id) {
		Itr itr = itrEJB.obtenerItrPorId(id);
		if (itr != null) {
			return Response.ok(itr).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/itr")
	public Response actualizarItr(Itr itr) {
		Itr itrActualizado = itrEJB.actualizarItr(itr);
		return Response.ok(itrActualizado).build();
	}

	@DELETE
	@Path("/itr/{id}")
	public Response eliminarItr(@PathParam("id") Long id) {
		if (itrEJB.eliminarItr(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}

	// Endpoints para Estado de Reclamo, Constancias y Justificaciones
	@GET
	@Path("/estado")
	public Response obtenerTodosLosEstados() {
		List<Estado> estados = estadoEJB.obtenerTodosLosEstados();
		return Response.ok(estados).build();
	}

	@POST
	@Path("/estado")
	public Response crearEstado(Estado estado) {
		Estado estadoCreado = estadoEJB.crearEstado(estado);
		return Response.ok(estadoCreado).build();
	}

	@GET
	@Path("/estado/{id}")
	public Response obtenerEstado(@PathParam("id") Long id) {
		Estado estado = estadoEJB.obtenerEstadoPorId(id);
		if (estado != null) {
			return Response.ok(estado).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/estado")
	public Response actualizarEstado(Estado estado) {
		Estado estadoActualizado = estadoEJB.actualizarEstado(estado);
		return Response.ok(estadoActualizado).build();
	}

	@DELETE
	@Path("/estado/{id}")
	public Response eliminarEstado(@PathParam("id") Long id) {
		if (estadoEJB.eliminarEstado(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Endpoints para tablas auxiliares, Area, Genero, Localidad, Departamento, Rol
	//Area
	@GET
	@Path("/area")
	public Response obtenerTodasLasAreas() {
		List<Area> areas = areaEJB.obtenerTodosLasAreas();
		return Response.ok(areas).build();
	}

	@POST
	@Path("/area")
	public Response crearArea(Area area) {
		Area areaCreada = areaEJB.crearArea(area);
		return Response.ok(areaCreada).build();
	}

	@GET
	@Path("/area/{id}")
	public Response obtenerArea(@PathParam("id") Long id) {
		Area area = areaEJB.obtenerAreaPorId(id);
		if (area != null) {
			return Response.ok(area).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/area")
	public Response actualizarArea(Area area) {
		Area areaActualizada = areaEJB.actualizarArea(area);
		return Response.ok(areaActualizada).build();
	}

	@DELETE
	@Path("/area/{id}")
	public Response eliminarArea(@PathParam("id") Long id) {
		if (areaEJB.eliminarArea(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Departamento
	@GET
	@Path("/departamento")
	public Response obtenerTodosLosDeprtamentos() {
		List<Departamento> deptos = departamentoEJB.obtenerTodosLosDepartamentos();
		return Response.ok(deptos).build();
	}

	@POST
	@Path("/departamento")
	public Response crearDepartamento(Departamento departamento) {
		Departamento deptoCreado = departamentoEJB.crearDepartamento(departamento);
		return Response.ok(deptoCreado).build();
	}

	@GET
	@Path("/departamento/{id}")
	public Response obtenerDepartamento(@PathParam("id") Long id) {
		Departamento depto = departamentoEJB.obtenerDepartamentoPorId(id);
		if (depto != null) {
			return Response.ok(depto).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/departamento")
	public Response actualizarDepartamento(Departamento departamento) {
		Departamento deptoActualizado = departamentoEJB.actualizarDepartamento(departamento);
		return Response.ok(deptoActualizado).build();
	}

	@DELETE
	@Path("/departamento/{id}")
	public Response eliminarDepartamento(@PathParam("id") Long id) {
		if (departamentoEJB.eliminarDepartamento(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Genero
	@GET
	@Path("/genero")
	public Response obtenerTodosLosGeneros() {
		List<Genero> generos = generoEJB.obtenerTodosLosGeneros();
		return Response.ok(generos).build();
	}

	@POST
	@Path("/genero")
	public Response crearGenero(Genero genero) {
		Genero generoCreado = generoEJB.crearGenero(genero);
		return Response.ok(generoCreado).build();
	}

	@GET
	@Path("/genero/{id}")
	public Response obtenerGenero(@PathParam("id") Long id) {
		Genero genero = generoEJB.obtenerGeneroPorId(id);
		if (genero != null) {
			return Response.ok(genero).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/genero")
	public Response actualizarGenero(Genero genero) {
		Genero generoActualizado = generoEJB.actualizarGenero(genero);
		return Response.ok(generoActualizado).build();
	}

	@DELETE
	@Path("/genero/{id}")
	public Response eliminarGenero(@PathParam("id") Long id) {
		if (generoEJB.eliminarGenero(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Localidad
	@GET
	@Path("/localidad")
	public Response obtenerTodasLasLocalidades() {
		List<Localidad> localidades = localidadEJB.obtenerTodosLasLocalidades();
		return Response.ok(localidades).build();
	}

	@POST
	@Path("/localidad")
	public Response crearLocalidad(Localidad localidad) {
		Localidad localidadCreada = localidadEJB.crearLocalidad(localidad);
		return Response.ok(localidadCreada).build();
	}

	@GET
	@Path("/localidad/{id}")
	public Response obtenerLocalidad(@PathParam("id") Long id) {
		Localidad localidad = localidadEJB.obtenerLocalidadPorId(id);
		if (localidad != null) {
			return Response.ok(localidad).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/localidad")
	public Response actualizarLocalidad(Localidad localidad) {
		Localidad localidadActualizada = localidadEJB.actualizarLocalidad(localidad);
		return Response.ok(localidadActualizada).build();
	}

	@DELETE
	@Path("/localidad/{id}")
	public Response eliminarLocalidad(@PathParam("id") Long id) {
		if (localidadEJB.eliminarLocalidad(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	//Rol
	@GET
	@Path("/rol")
	public Response obtenerTodosLosRoles() {
		List<Rol> roles = rolEJB.obtenerTodosLosRoles();
		return Response.ok(roles).build();
	}

	@POST
	@Path("/rol")
	public Response crearRol(Rol rol) {
		Rol rolCreado = rolEJB.crearRol(rol);
		return Response.ok(rolCreado).build();
	}

	@GET
	@Path("/rol/{id}")
	public Response obtenerRol(@PathParam("id") Long id) {
		Rol rol = rolEJB.obtenerRolPorId(id);
		if (rol != null) {
			return Response.ok(rol).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("/rol")
	public Response actualizarRol(Rol rol) {
		Rol rolActualizado = rolEJB.actualizarRol(rol);
		return Response.ok(rolActualizado).build();
	}

	@DELETE
	@Path("/rol/{id}")
	public Response eliminarRol(@PathParam("id") Long id) {
		if (rolEJB.eliminarRol(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	
}// fin clase
