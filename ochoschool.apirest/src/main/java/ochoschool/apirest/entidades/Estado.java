package ochoschool.apirest.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ESTADOS")
public class Estado implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SEQ_ID_ESTADO", initialValue = 500, sequenceName = "SEQ_ID_ESTADO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_ESTADO")
    @Column(unique = true, nullable = false, precision = 38)
    private Long idEstado;
    @Column(unique = true, nullable = false)
    private String descripcion;
    private char activo;

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado(Long idEstado, String descripcion) {
        this.idEstado = idEstado;
        this.descripcion = descripcion;
    }

    public Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado() {
    }

	public char getActivo() {
		return activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
	}
    
    

}//fin clase
