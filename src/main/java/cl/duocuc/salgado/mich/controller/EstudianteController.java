package cl.duocuc.salgado.mich.controller;

import cl.duocuc.salgado.mich.model.Estudiante;
import cl.duocuc.salgado.mich.service.EstudianteService;

import java.util.List;

public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController() {
        this.estudianteService = new EstudianteService();
    }

    public void registrar(Estudiante estudiante) {
        estudianteService.registrar(estudiante);
    }

    public List<Estudiante> listar() {
        return estudianteService.listar();
    }

    public void actualizar(Estudiante estudiante) {
        estudianteService.actualizar(estudiante);
    }

    public void eliminar(int id) {
        estudianteService.eliminar(id);
    }
}