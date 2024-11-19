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

import ochoschool.apirest.ejb.UsuarioEJB;
import ochoschool.apirest.entidades.Usuario;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioControlador {
	@Inject
	private UsuarioEJB usuarioEJB;

	@GET
	public Response obtenerTodosLosUsuarios() {
		List<Usuario> usuarios = usuarioEJB.obtenerTodosLosUsuarios();
		return Response.ok(usuarios).build();
	}

	@POST
	public Response crearUsuario(Usuario usuario) {
		Usuario usuarioCreado = usuarioEJB.crearUsuario(usuario);
		return Response.ok(usuarioCreado).build();
	}

	@GET
	@Path("/{id}")
	public Response obtenerUsuario(@PathParam("id") Long id) {
		Usuario usuario = usuarioEJB.obtenerUsuarioPorId(id);
		if (usuario != null) {
			return Response.ok(usuario).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	public Response actualizarUsuario(Usuario usuario) {
		Usuario usuarioActualizado = usuarioEJB.actualizarUsuario(usuario);
		return Response.ok(usuarioActualizado).build();
	}

	@DELETE
	@Path("/{id}")
	public Response eliminarUsuario(@PathParam("id") Long id) {
		if (usuarioEJB.eliminarUsuario(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}

	@PUT
	@Path("/{id}")
	public Response deshabilitarUsuario(@PathParam("id") Long id) {
		if (usuarioEJB.deshabilitarUsuario(id)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}

}// fin clase
