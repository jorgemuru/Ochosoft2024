package ochoschool.website.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="AREAS")
public class Area implements Serializable{

	 private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_AREA")
	    @SequenceGenerator(name = "SEQ_ID_AREA", initialValue = 500, sequenceName = "SEQ_ID_AREA")
	    @Column(unique = true, nullable = false, precision = 38)
	    private Long idArea;
	    @Column(unique = true, nullable = false)
	    private String descripcion;

	    public Long getIdArea() {
	        return idArea;
	    }

	    public void setIdArea(Long idArea) {
	        this.idArea = idArea;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }

	    public Area(Long idArea, String descripcion) {
	        this.idArea = idArea;
	        this.descripcion = descripcion;
	    }

	    public Area(String descripcion) {
	        this.descripcion = descripcion;
	    }

	    public Area() {
	    }

}//fin clase
