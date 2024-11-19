package ochoschool.website.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

public class Itr implements Serializable{
	 private static final long serialVersionUID = 1L;

	    private Long idItr;
	    private Departamento departamento;
	    private String nombre;
	    private char activo;

	    public Long getIdItr() {
	        return idItr;
	    }

	    public void setIdItr(Long idItr) {
	        this.idItr = idItr;
	    }

	    public Departamento getDepartamento() {
	        return departamento;
	    }

	    public void setDepartamento(Departamento departamento) {
	        this.departamento = departamento;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public char getActivo() {
	        return activo;
	    }

	    public void setActivo(char activo) {
	        this.activo = activo;
	    }

	    public Itr(Long idItr, Departamento departamento, String nombre, char activo) {
	        this.idItr = idItr;
	        this.departamento = departamento;
	        this.nombre = nombre;
	        this.activo = activo;
	    }

	    public Itr(Departamento departamento, String nombre, char activo) {
	        this.departamento = departamento;
	        this.nombre = nombre;
	        this.activo = activo;
	    }

	    public Itr() {
	    }

		@Override
		public String toString() {
			return nombre;
		}

		@Override
		public int hashCode() {
			return Objects.hash(activo, departamento, idItr, nombre);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Itr other = (Itr) obj;
			return activo == other.activo && Objects.equals(departamento, other.departamento)
					&& Objects.equals(idItr, other.idItr) && Objects.equals(nombre, other.nombre);
		}
	    
		
		
	    

}//fin clase
