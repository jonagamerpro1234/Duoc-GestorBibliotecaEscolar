package cl.duocuc.salgado.mich.service;

import cl.duocuc.salgado.mich.dao.PrestamoDAO;
import cl.duocuc.salgado.mich.dao.PrestamoDAOImpl;
import cl.duocuc.salgado.mich.model.Prestamo;

import java.util.List;

public class PrestamoService {

    private final PrestamoDAO prestamoDAO;

    public PrestamoService() {
        this.prestamoDAO = new PrestamoDAOImpl();
    }

    public void registrar(Prestamo prestamo) {
        validar(prestamo);
        prestamoDAO.guardar(prestamo);
    }

    public List<Prestamo> listar() {
        return prestamoDAO.listarTodos();
    }

    public void devolver(int idPrestamo) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del préstamo no es válido."
            );
        }

        prestamoDAO.devolver(idPrestamo);
    }

    public void eliminar(int idPrestamo) {
        if (idPrestamo <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del préstamo no es válido."
            );
        }

        prestamoDAO.eliminar(idPrestamo);
    }

    private void validar(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo.");
        }

        if (prestamo.getEstudiante() == null || prestamo.getEstudiante().getId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un estudiante registrado.");
        }

        if (prestamo.getLibro() == null || prestamo.getLibro().getId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un libro registrado.");
        }

        if (prestamo.getFechaPrestamo() == null || prestamo.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("Las fechas del préstamo son obligatorias.");
        }

        if (prestamo.getFechaDevolucion().isBefore(prestamo.getFechaPrestamo())) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior " + "a la fecha del préstamo.");
        }
    }
}