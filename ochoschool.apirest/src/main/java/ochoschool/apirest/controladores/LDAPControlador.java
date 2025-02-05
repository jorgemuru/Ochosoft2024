package ochoschool.apirest.controladores;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import ochoschool.apirest.entidades.Usuario;

@Path("/auth")
public class LDAPControlador {
	
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response authenticateUser(Usuario usuario) {
        // Obtener credenciales de usuario del cuerpo de la solicitud
        String username = usuario.getUsuario();
        String password = usuario.getClave();
        String upn  = username + "@8soft.utec.uy";// UPN del usuario
        //String LDAP_URL = "ldap://192.168.1.10:5389";
        String LDAP_URL = "ldap://ServidorAD8:389";

        // Configurar las propiedades de la conexión LDAP
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_URL ); // URL del servidor LDAP
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        //env.put(Context.SECURITY_PRINCIPAL, "CN=" + username + ",CN=Users,DC=8soft,DC=utec,DC=uy"); // DN del usuario
        env.put(Context.SECURITY_PRINCIPAL, upn);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            // Intentar autenticar al usuario
            InitialDirContext ctx = new InitialDirContext(env);
            ctx.close();
            
        	System.out.println("**************************************************************************************");
        	System.out.println("ENTRE AL TRY DEL CONTROLADOR LDAP");
        	System.out.println("**************************************************************************************");
        	
            // La autenticación fue exitosa, devolver un código de estado HTTP 200
            return Response.ok(usuario).build();
        } catch (Exception e) {
        	System.out.println("**************************************************************************************");
        	System.out.println("Usuario: " + username);
        	System.out.println("Password: " + password);
        	System.out.println("Error durante la autenticación: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace completo en la consola (opcional)
            System.out.println("**************************************************************************************");
        	
            // La autenticación falló, devolver un código de estado HTTP 403
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
	
}//fin clase
