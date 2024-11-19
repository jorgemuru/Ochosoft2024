package ochoschool.website.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ochoschool.website.entidades.ApiClient;
import ochoschool.website.entidades.Area;
import ochoschool.website.entidades.Rol;

@ManagedBean(name = "tuBeanJSF")
@SessionScoped
public class TuBeanJSF implements Serializable {
    private static final long serialVersionUID = 1L;

    private JsonNode datos;
    private List<Map<String, Object>> datosList;
    private List<Map<String, Object>> datosFiltrados;
    private String tipoUsuarioSeleccionado;
    private List<SelectItem> tiposUsuarios;
    private String itrSeleccionado;
    private List<SelectItem> itrs;
    private boolean filtrandoEstudiantes;
    private String generacionSeleccionada; // Para almacenar la generación seleccionada
    private List<SelectItem> generaciones; // Lista de generaciones disponibles
    private String estadoActivoSeleccionado;
    private List<SelectItem> estadosActivos;
    private Map<String, Object> usuarioSeleccionado; 
    private Long idUsuario;
    private List<Area> listaAreas;  // Lista de áreas precargadas
    private List<Rol> listaRoles;   // Lista de roles precargados

    @PostConstruct
    public void init() {
        cargarDatos("usuarios");
        listaAreas = cargarAreas();
        listaRoles = cargarRoles();
    }

    public void cargarDatos(String tipoDatos) {
        ApiClient apiClient = new ApiClient();

        try {
            if ("usuarios".equals(tipoDatos)) {
                datos = apiClient.getDatosUsuarios();
            } else if ("eventos".equals(tipoDatos)) {
                datos = apiClient.getDatosEventos();
            } else if ("reclamos".equals(tipoDatos)) {
                datos = apiClient.getDatosReclamos();
            }

            if (datos == null || datos.isEmpty()) {
                System.out.println("No se obtuvieron datos de " + tipoDatos);
            } else {
                System.out.println("Número de elementos obtenidos: " + datos.size());
                convertirDatosJsonALista();
                obtenerTiposUsuarios(); // Llama al método para obtener los tipos de usuarios
                obtenerItr();
                obtenerGeneraciones();
                obtenerEstadosActivos();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar datos de " + tipoDatos + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void convertirDatosJsonALista() {
        ObjectMapper mapper = new ObjectMapper();
        datosList = new ArrayList<>();
        for (JsonNode objeto : datos) {
            datosList.add(mapper.convertValue(objeto, Map.class));
        }
        System.out.println("Lista creada, tamaño: " + datosList.size());
    }

    private void obtenerTiposUsuarios() {
        Set<String> tiposSet = new HashSet<>();
        for (Map<String, Object> usuario : datosList) {
            String tipo = (String) usuario.get("tipo_usuario");
            if (tipo != null) {
                tiposSet.add(tipo);
            }
        }

        tiposUsuarios = new ArrayList<>();
        for (String tipo : tiposSet) {
            tiposUsuarios.add(new SelectItem(tipo, tipo));
        }
    }
    
    private void obtenerItr() {
        Set<String> itrSet = new HashSet<>();
        
        // Recorre cada usuario en la lista de datos
        for (Map<String, Object> usuario : datosList) {
            // Verifica si existe un objeto 'itr' dentro del usuario
            Map<String, Object> itr = (Map<String, Object>) usuario.get("itr");
            if (itr != null) {
                // Extrae el nombre del ITR desde el campo 'nombre'
                String nombreItr = (String) itr.get("nombre");
                if (nombreItr != null) {
                    itrSet.add(nombreItr);  // Agrega el nombre del ITR al conjunto
                }
            }
        }

        // Transforma el conjunto de ITRs únicos en una lista de SelectItem
        itrs = new ArrayList<>();
        for (String itr : itrSet) {
            itrs.add(new SelectItem(itr, itr));
        }
    }

    // Método para filtrar la lista de usuarios según el tipo seleccionado
    public void filtrarUsuarios() {
        if ("ESTUDIANTE".equals(tipoUsuarioSeleccionado)) {
            filtrandoEstudiantes = true;
        } else {
            filtrandoEstudiantes = false;
            generacionSeleccionada = null; // Reiniciar generación si no se está filtrando por estudiantes
        }
        filtrarDatosAcumulados(); // Llamamos al método que aplica todos los filtros
    }
    
    public void filtrarUsuariosItr() {
        filtrarDatosAcumulados(); // Llama al método general de filtrado
    }
    
    private void obtenerGeneraciones() {
        Set<String> generacionesSet = new HashSet<>();
        
        // Recorre la lista de usuarios para obtener las generaciones
        for (Map<String, Object> usuario : datosList) {
            if ("ESTUDIANTE".equals(usuario.get("tipo_usuario"))) {
                String generacion = (String) usuario.get("generacion");
                if (generacion != null) {
                    generacionesSet.add(generacion);  // Añadir generación al set
                }
            }
        }

        // Convertimos el conjunto a una lista de SelectItems
        generaciones = new ArrayList<>();
        for (String gen : generacionesSet) {
            generaciones.add(new SelectItem(gen, gen));
        }

        // Verificación de contenido
        System.out.println("Generaciones cargadas: " + generaciones.size());
    }
    
    private void obtenerEstadosActivos() {
        estadosActivos = new ArrayList<>();
        estadosActivos.add(new SelectItem("", "Todos")); // Opción para todos los usuarios
        estadosActivos.add(new SelectItem("S", "Activo")); // Usuarios activos
        estadosActivos.add(new SelectItem("N", "Inactivo")); // Usuarios inactivos
    }
    
    public void filtrarUsuariosPorActivo() {
        filtrarDatosAcumulados(); // Llama al método general de filtrado
    }
    
    public String irAModificarUsuario(Long idUsuario) {
        if (idUsuario != null) {
            cargarUsuarioSeleccionadoPorId(idUsuario);
            return "/pages/ModificarUsuario.xhtml?faces-redirect=true&includeViewParams=true&idUsuario=" + idUsuario;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado un usuario."));
            return null;
        }
    }
    
    public void cargarUsuarioSeleccionadoPorId(Long idUsuario) {
        if (idUsuario != null) {
            for (Map<String, Object> usuario : datosFiltrados != null ? datosFiltrados : datosList) {
                Long id = ((Number) usuario.get("idUsuario")).longValue();
                if (id.equals(idUsuario)) {
                    usuarioSeleccionado = usuario;

                    // Verifica si 'fecha_nacimiento' es un String o un Long
                    Object fechaNacimientoObj = usuario.get("fecha_nacimiento");

                    // Si es un String (posible caso)
                    if (fechaNacimientoObj instanceof String) {
                        String fechaNacimientoStr = (String) fechaNacimientoObj;
                        usuarioSeleccionado.put("fecha_nacimiento", fechaNacimientoStr);

                    // Si es un Long (timestamp en milisegundos)
                    } else if (fechaNacimientoObj instanceof Long) {
                        Long timestamp = (Long) fechaNacimientoObj;
                        String fechaNacimientoFormateada = convertirTimestampALaFecha(timestamp);
                        usuarioSeleccionado.put("fecha_nacimiento", fechaNacimientoFormateada);
                    }

                    System.out.println("Usuario seleccionado: " + usuarioSeleccionado.toString());
                    break;
                }
            }
            if (usuarioSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no encontrado."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "ID de usuario no válido."));
        }
    }
    
    public void filtrarDatosAcumulados() {
        datosFiltrados = new ArrayList<>(datosList); // Copia la lista completa de datos

        // Filtro por tipo de usuario
        if (tipoUsuarioSeleccionado != null && !tipoUsuarioSeleccionado.isEmpty()) {
            datosFiltrados.removeIf(usuario -> 
                !tipoUsuarioSeleccionado.equals(usuario.get("tipo_usuario"))
            );
        }

        // Filtro por ITR
        if (itrSeleccionado != null && !itrSeleccionado.isEmpty()) {
            datosFiltrados.removeIf(usuario -> {
                Map<String, Object> itr = (Map<String, Object>) usuario.get("itr");
                return itr == null || !itrSeleccionado.equals(itr.get("nombre"));
            });
        }

        // Filtro por generación (solo si estamos filtrando estudiantes)
        if (filtrandoEstudiantes && generacionSeleccionada != null && !generacionSeleccionada.isEmpty()) {
            datosFiltrados.removeIf(usuario -> 
                !"ESTUDIANTE".equals(usuario.get("tipo_usuario")) ||
                !generacionSeleccionada.equals(usuario.get("generacion"))
            );
        }

        // Filtro por estado (Activo/Inactivo)
        if (estadoActivoSeleccionado != null && !estadoActivoSeleccionado.isEmpty()) {
            datosFiltrados.removeIf(usuario -> 
                !estadoActivoSeleccionado.equals(usuario.get("activo"))
            );
        }
    }
    
    private List<Area> cargarAreas() {
        List<Area> listaAreas = new ArrayList<>();
        ApiClient apiClient = new ApiClient();
        
        try {
            JsonNode areasData = apiClient.getDatosAreas();  // Llama al servicio API que devuelve las áreas
            
            if (areasData != null && !areasData.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                for (JsonNode areaNode : areasData) {
                    Area area = mapper.convertValue(areaNode, Area.class);
                    listaAreas.add(area);  // Añade cada área a la lista
                }
            } else {
                System.out.println("No se encontraron áreas.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar las áreas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaAreas;
    }

    private List<Rol> cargarRoles() {
        List<Rol> listaRoles = new ArrayList<>();
        ApiClient apiClient = new ApiClient();
        
        try {
            JsonNode rolesData = apiClient.getDatosRoles();  // Llama al servicio API que devuelve los roles
            
            if (rolesData != null && !rolesData.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                for (JsonNode rolNode : rolesData) {
                    Rol rol = mapper.convertValue(rolNode, Rol.class);
                    listaRoles.add(rol);  // Añade cada rol a la lista
                }
            } else {
                System.out.println("No se encontraron roles.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar los roles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaRoles;
    }
    
    public String convertirTimestampALaFecha(Long timestamp) {
        if (timestamp != null) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // El formato que prefieras
            return sdf.format(date);
        }
        return null;
    }


    // Getters y Setters

    public List<Map<String, Object>> getUsuarios() {
        if (datosFiltrados != null) {
            return datosFiltrados;
        } else {
            return datosList;
        }
    }

    public List<Map<String, Object>> getDatosFiltrados() {
        return datosFiltrados;
    }

    public void setDatosFiltrados(List<Map<String, Object>> datosFiltrados) {
        this.datosFiltrados = datosFiltrados;
    }

    public String getTipoUsuarioSeleccionado() {
        return tipoUsuarioSeleccionado;
    }

    public void setTipoUsuarioSeleccionado(String tipoUsuarioSeleccionado) {
        this.tipoUsuarioSeleccionado = tipoUsuarioSeleccionado;
        filtrarUsuarios(); // Filtrar la lista cuando cambia el valor seleccionado
    }

    public List<SelectItem> getTiposUsuarios() {
        return tiposUsuarios;
    }

    public void setUsuarioSeleccionado(Map<String, Object> usuario) {
        this.usuarioSeleccionado = usuario;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("usuarioSeleccionado", usuario);
    }
    
    public Map<String, Object> getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

	public String getItrSeleccionado() {
		return itrSeleccionado;
	}

	public void setItrSeleccionado(String itrSeleccionado) {
		this.itrSeleccionado = itrSeleccionado;
		filtrarUsuariosItr();
	}

	public List<SelectItem> getItrs() {
		return itrs;
	}

	public void setItrs(List<SelectItem> itrs) {
		this.itrs = itrs;
	}
	
	public boolean isFiltrandoEstudiantes() {
	    return filtrandoEstudiantes;
	}
	
	public String getGeneracionSeleccionada() {
	    return generacionSeleccionada;
	}

	public void setGeneracionSeleccionada(String generacionSeleccionada) {
	    this.generacionSeleccionada = generacionSeleccionada;
	    filtrarUsuarios(); // Llamar al filtro cuando cambia la generación
	}

	public List<SelectItem> getGeneraciones() {
	    return generaciones;
	}

	public String getEstadoActivoSeleccionado() {
		return estadoActivoSeleccionado;
	}

	public void setEstadoActivoSeleccionado(String estadoActivoSeleccionado) {
		this.estadoActivoSeleccionado = estadoActivoSeleccionado;
	}

	public List<SelectItem> getEstadosActivos() {
		return estadosActivos;
	}

	public void setEstadosActivos(List<SelectItem> estadosActivos) {
		this.estadosActivos = estadosActivos;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public List<Area> getListaAreas() {
        return listaAreas;
    }

    public List<Rol> getListaRoles() {
        return listaRoles;
    }

}
