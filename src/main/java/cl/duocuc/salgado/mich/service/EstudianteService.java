package cl.duocuc.salgado.mich.service;

import cl.duocuc.salgado.mich.dao.EstudianteDAO;
import cl.duocuc.salgado.mich.dao.EstudianteDAOImpl;
import cl.duocuc.salgado.mich.model.Estudiante;

import java.util.List;

public class EstudianteService {

    private final EstudianteDAO estudianteDAO;

    public EstudianteService() {
        this.estudianteDAO = new EstudianteDAOImpl();
    }

    public void registrar(Estudiante estudiante) {
        validar(estudiante);
        estudianteDAO.guardar(estudiante);
    }

    public List<Estudiante> listar() {
        return estudianteDAO.listarTodos();
    }

    public void actualizar(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() <= 0) {
            throw new IllegalArgumentException("El estudiante no es válido para actualizar.");
        }

        validar(estudiante);
        estudianteDAO.actualizar(estudiante);
    }

    public void eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador del estudiante no es válido.");
        }

        estudianteDAO.eliminar(id);
    }

    private void validar(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (estudiante.getApellido() == null || estudiante.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (estudiante.getRut() == null) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }

        if (estudiante.getCurso() == null || estudiante.getCurso().trim().isEmpty()) {
            throw new IllegalArgumentException("El curso es obligatorio.");
        }

        if (estudiante.getCorreo() == null || !estudiante.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Debe ingresar un correo válido.");
        }
    }
}