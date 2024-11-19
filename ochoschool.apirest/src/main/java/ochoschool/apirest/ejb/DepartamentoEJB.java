package ochoschool.apirest.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import ochoschool.apirest.dao.DepartamentoDAO;
import ochoschool.apirest.entidades.Departamento;

@Stateless
public class DepartamentoEJB {
	@Inject
	private DepartamentoDAO departamentoDAO;

    public List<Departamento> obtenerTodosLosDepartamentos() {
        return departamentoDAO.listarDepartamentos();
    }

	public Departamento crearDepartamento(Departamento departamento) {
		return departamentoDAO.crearDepartamento(departamento);
	}

	public Departamento obtenerDepartamentoPorId(Long id) {
		return departamentoDAO.buscarDepartamentoId(id);
	}

	public Departamento actualizarDepartamento(Departamento departamento) {
		if(departamentoDAO.modificarDepartamento(departamento)) {
			return departamento;
		}else{
			return null;
		}
	}

	public Boolean eliminarDepartamento(Long id) {
		return departamentoDAO.eliminarDepartamento(id);
	}
	
}//fin clase
