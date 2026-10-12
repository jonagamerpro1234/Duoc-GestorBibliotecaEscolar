package cl.duocuc.salgado.mich.controller;

import cl.duocuc.salgado.mich.model.Categoria;
import cl.duocuc.salgado.mich.service.CategoriaService;

import java.util.List;

public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController() {
        this.categoriaService = new CategoriaService();
    }

    public void registrar(Categoria categoria) {
        categoriaService.registrar(categoria);
    }

    public List<Categoria> listar() {
        return categoriaService.listar();
    }

    public void actualizar(Categoria categoria) {
        categoriaService.actualizar(categoria);
    }

    public void eliminar(int id) {
        categoriaService.eliminar(id);
    }
}