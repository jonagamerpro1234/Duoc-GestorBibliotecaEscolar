package cl.duocuc.salgado.mich.service;

import cl.duocuc.salgado.mich.dao.LibroDAO;
import cl.duocuc.salgado.mich.dao.LibroDAOImpl;
import cl.duocuc.salgado.mich.model.Libro;

import java.util.List;

public class LibroService {

    private final LibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAOImpl();
    }

    public void registrar(Libro libro) {
        validar(libro);
        libroDAO.guardar(libro);
    }

    public List<Libro> listar() {
        return libroDAO.listarTodos();
    }

    public void actualizar(Libro libro) {
        if (libro == null || libro.getId() <= 0) {
            throw new IllegalArgumentException("El libro no es válido para actualizar.");
        }

        validar(libro);
        libroDAO.actualizar(libro);
    }

    public void eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador del libro no es válido.");
        }

        libroDAO.eliminar(id);
    }

    private void validar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }

        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor es obligatorio.");
        }

        if (libro.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        if (libro.getCategoria() == null || libro.getCategoria().getId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una categoría válida.");
        }
    }
}