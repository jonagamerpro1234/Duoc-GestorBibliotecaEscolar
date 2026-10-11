package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Estudiante;
import java.util.List;

public interface EstudianteDAO {

    void guardar(Estudiante estudiante);

    List<Estudiante> listarTodos();

    void actualizar(Estudiante estudiante);

    void eliminar(int id);
}