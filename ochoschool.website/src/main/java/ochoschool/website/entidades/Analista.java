package ochoschool.website.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "ANALISTAS")
public class Analista extends Usuario{

	private static final long serialVersionUID = 1L;
	private String firmaDigital;

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Analista(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Analista(Long idUsuario, int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
                    String segundo_apellido, String nombre_usuario, String clave, Date fecha_nacimiento, Genero genero,
                    Departamento departamento, Localidad localidad, Itr itr, String telefono, String mail_institucional,
                    String mail_personal, char activo, char confirmado, String tipo_usuario, String firmaDigital) {
        super(idUsuario, documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nombre_usuario,
                clave, fecha_nacimiento, genero, departamento, localidad, itr, telefono, mail_institucional, mail_personal,
                activo, confirmado, tipo_usuario);
        this.firmaDigital = firmaDigital;
    }

    public Analista(int documento, String primer_nombre, String segundo_nombre, String primer_apellido,
                    String segundo_apellido, String nombre_usuario, String clave, Date fecha_nacimiento, Genero genero,
                    Departamento departamento, Localidad localidad, Itr itr, String telefono, String mail_institucional,
                    String mail_personal, char activo, char confirmado, String tipo_usuario, String firmaDigital) {
        super(documento, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nombre_usuario, clave,
                fecha_nacimiento, genero, departamento, localidad, itr, telefono, mail_institucional, mail_personal,
                activo, confirmado, tipo_usuario);
        this.firmaDigital = firmaDigital;
    }

    public Analista() {
        super();
    }

}//fin clase

