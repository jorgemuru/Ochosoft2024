package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ITRS")
public class Itr implements Serializable{
	 private static final long serialVersionUID = 1L;

	    @Id
	    @SequenceGenerator(name = "SEQ_ID_ITR", initialValue = 500, sequenceName = "SEQ_ID_ITR")
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_ITR")
	    @Column(unique = true, nullable = false, precision = 38)
	    private Long idItr;
	    @ManyToOne
	    @JoinColumn(name = "idDepartamento", nullable = false)
	    private Departamento departamento;
	    @Column(unique = true, nullable = false)
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

}//fin clase
