package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Categoria;
import java.util.List;

public interface CategoriaDAO {

    void guardar(Categoria categoria);

    List<Categoria> listarTodos();

    void actualizar(Categoria categoria);

    void eliminar(int id);
}