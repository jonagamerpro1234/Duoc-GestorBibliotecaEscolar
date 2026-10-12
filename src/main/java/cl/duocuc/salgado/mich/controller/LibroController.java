package cl.duocuc.salgado.mich.controller;

import cl.duocuc.salgado.mich.model.Libro;
import cl.duocuc.salgado.mich.service.LibroService;

import java.util.List;

public class LibroController {

    private final LibroService libroService;

    public LibroController() {
        this.libroService = new LibroService();
    }

    public void registrar(Libro libro) {
        libroService.registrar(libro);
    }

    public List<Libro> listar() {
        return libroService.listar();
    }

    public void actualizar(Libro libro) {
        libroService.actualizar(libro);
    }

    public void eliminar(int id) {
        libroService.eliminar(id);
    }
}