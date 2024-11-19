package ochoschool.apirest.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TUTORES")
public class Tutor extends Usuario{
	private static final long serialVersionUID = 1L;
	@ManyToOne
    @JoinColumn(name = "idRol", nullable = false)
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "idArea", nullable = false)
    private Area area;

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Tutor(Rol rol, Area area) {
        this.rol = rol;
        this.area = area;
    }

    public Tutor(Long idUsuario, int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
                 String segundo_apellido, String nombre_usuario, String clave, Date fecha_nacimiento, Genero genero,
                 Departamento departamento, Localidad localidad, Itr itr, String telefono, String mail_institucional,
                 String mail_personal, char activo, char confirmado, String tipo_usuario, Rol rol, Area area) {
        super(idUsuario, documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nombre_usuario,
                clave, fecha_nacimiento, genero, departamento, localidad, itr, telefono, mail_institucional, mail_personal,
                activo, confirmado, tipo_usuario);
        this.rol = rol;
        this.area = area;
    }

    public Tutor(int documento, String primer_nombre, String segundo_nombre, String primer_apellido, String segundo_apellido,
                 String nombre_usuario, String clave, Date fecha_nacimiento, Genero genero, Departamento departamento,
                 Localidad localidad, Itr itr, String telefono, String mail_institucional, String mail_personal, char activo,
                 char confirmado, String tipo_usuario, Rol rol, Area area) {
        super(documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nombre_usuario, clave,
                fecha_nacimiento, genero, departamento, localidad, itr, telefono, mail_institucional, mail_personal, activo,
                confirmado, tipo_usuario);
        this.rol = rol;
        this.area = area;
    }

    public Tutor() {
        super();
    }

}//fin clase
