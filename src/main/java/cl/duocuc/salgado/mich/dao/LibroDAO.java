package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Libro;
import java.util.List;

public interface LibroDAO {

    void guardar(Libro libro);

    List<Libro> listarTodos();

    void actualizar(Libro libro);

    void eliminar(int id);
}