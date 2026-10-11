package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Prestamo;
import java.util.List;

public interface PrestamoDAO {

    void guardar(Prestamo prestamo);

    List<Prestamo> listarTodos();

    void devolver(int idPrestamo);

    void eliminar(int id);
}