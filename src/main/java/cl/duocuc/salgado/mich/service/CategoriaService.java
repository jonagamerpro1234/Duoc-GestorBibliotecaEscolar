package cl.duocuc.salgado.mich.service;

import cl.duocuc.salgado.mich.dao.CategoriaDAO;
import cl.duocuc.salgado.mich.dao.CategoriaDAOImpl;
import cl.duocuc.salgado.mich.model.Categoria;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAOImpl();
    }

    public void registrar(Categoria categoria) {
        if (categoria == null || categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }

        categoriaDAO.guardar(categoria);
    }

    public List<Categoria> listar() {
        return categoriaDAO.listarTodos();
    }

    public void actualizar(Categoria categoria) {
        if (categoria == null || categoria.getId() <= 0) {
            throw new IllegalArgumentException("La categoría no es válida.");
        }

        registrarValidacionNombre(categoria);
        categoriaDAO.actualizar(categoria);
    }

    public void eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser válido.");
        }

        categoriaDAO.eliminar(id);
    }

    private void registrarValidacionNombre(@NotNull Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
    }
}