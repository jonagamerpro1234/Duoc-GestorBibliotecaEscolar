package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.util.InvalidRutException;
import cl.duocuc.salgado.mich.model.Categoria;
import cl.duocuc.salgado.mich.model.Estudiante;
import cl.duocuc.salgado.mich.model.Libro;
import cl.duocuc.salgado.mich.model.Prestamo;
import cl.duocuc.salgado.mich.model.Rut;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOImpl implements PrestamoDAO {

    @Override
    public synchronized void guardar(@NotNull Prestamo prestamo) {
        String actualizarStock = """
                UPDATE libros
                SET stock = stock - 1
                WHERE id = ? AND stock > 0
                """;

        String insertarPrestamo = """
                INSERT INTO prestamos
                (id_estudiante, id_libro, f_prestamo, f_devolucion, devuelto)
                VALUES (?, ?, ?, ?, FALSE)
                """;

        try (Connection con = ConexionDB.getInstance().getConexion()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps = con.prepareStatement(actualizarStock)) {
                    ps.setInt(1, prestamo.getLibro().getId());

                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("El libro no existe o no tiene stock disponible.");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(insertarPrestamo)) {
                    ps.setInt(1, prestamo.getEstudiante().getId());
                    ps.setInt(2, prestamo.getLibro().getId());
                    ps.setDate(3, java.sql.Date.valueOf(prestamo.getFechaPrestamo()));
                    ps.setDate(4, java.sql.Date.valueOf(prestamo.getFechaDevolucion()));

                    ps.executeUpdate();
                }

                con.commit();
                System.out.println("[MYSQL]: Préstamo registrado correctamente.");

            } catch (SQLException e) {
                con.rollback();
                System.out.println("[MYSQL]: No se pudo registrar el préstamo.");
                System.out.println(e.getMessage());

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error de conexión al registrar préstamo.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Prestamo> listarTodos() {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT
                    p.id AS prestamo_id,
                    p.f_prestamo,
                    p.f_devolucion,
                    p.devuelto,
                    e.id AS estudiante_id,
                    e.rut,
                    e.dv,
                    e.nombre AS estudiante_nombre,
                    e.apaterno,
                    e.amaterno,
                    e.curso,
                    e.correo,
                    l.id AS libro_id,
                    l.titulo,
                    l.autor,
                    l.editorial,
                    l.isbn,
                    l.stock,
                    c.id AS categoria_id,
                    c.nombre_categoria
                FROM prestamos p
                INNER JOIN estudiantes e ON p.id_estudiante = e.id
                INNER JOIN libros l ON p.id_libro = l.id
                INNER JOIN categorias c ON l.id_categoria = c.id
                ORDER BY p.id DESC
                """;

        try (Connection con = ConexionDB.getInstance().getConexion(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                try {
                    Rut rut = new Rut(rs.getInt("rut"), rs.getString("dv").charAt(0));
                    String apellido = rs.getString("apaterno") + " " + rs.getString("amaterno");

                    Estudiante estudiante = new Estudiante(
                            rs.getInt("estudiante_id"),
                            rs.getString("estudiante_nombre"),
                            apellido,
                            rut,
                            rs.getString("curso"),
                            rs.getString("correo")
                    );

                    Categoria categoria = new Categoria(
                            rs.getInt("categoria_id"),
                            rs.getString("nombre_categoria")
                    );

                    Libro libro = new Libro(
                            rs.getInt("libro_id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("editorial"),
                            rs.getString("isbn"),
                            rs.getInt("stock"),
                            categoria
                    );

                    Prestamo prestamo = new Prestamo(
                            rs.getInt("prestamo_id"),
                            estudiante,
                            libro,
                            rs.getDate("f_prestamo").toLocalDate(),
                            rs.getDate("f_devolucion").toLocalDate(),
                            rs.getBoolean("devuelto")
                    );

                    prestamos.add(prestamo);
                } catch (InvalidRutException e) {
                    System.out.println("[MYSQL]: RUT inválido en el préstamo #" + rs.getInt("prestamo_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al listar préstamos.");
            System.out.println(e.getMessage());
        }

        return prestamos;
    }

    @Override
    public synchronized void devolver(int idPrestamo) {
        String buscarPrestamo = """
                SELECT id_libro
                FROM prestamos
                WHERE id = ? AND devuelto = FALSE
                FOR UPDATE
                """;

        String actualizarStock = """
                UPDATE libros
                SET stock = stock + 1
                WHERE id = ?
                """;

        String actualizarPrestamo = """
                UPDATE prestamos
                SET devuelto = TRUE
                WHERE id = ? AND devuelto = FALSE
                """;

        try (Connection con = ConexionDB.getInstance().getConexion()) {
            con.setAutoCommit(false);

            try {
                int idLibro;

                try (PreparedStatement ps = con.prepareStatement(buscarPrestamo)) {
                    ps.setInt(1, idPrestamo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El préstamo no existe o ya fue devuelto.");
                        }

                        idLibro = rs.getInt("id_libro");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(actualizarStock)) {
                    ps.setInt(1, idLibro);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(actualizarPrestamo)) {
                    ps.setInt(1, idPrestamo);

                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("No se pudo actualizar el estado del préstamo.");
                    }
                }

                con.commit();
                System.out.println("[MYSQL]: Devolución registrada correctamente.");

            } catch (SQLException e) {
                con.rollback();
                System.out.println("[MYSQL]: No se pudo registrar la devolución.");
                System.out.println(e.getMessage());

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error de conexión al devolver libro.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public synchronized void eliminar(int id) {
        String sql = """
                DELETE FROM prestamos
                WHERE id = ? AND devuelto = TRUE
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Préstamo eliminado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el préstamo " + "o todavía no ha sido devuelto.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al eliminar el préstamo.");
            System.out.println(e.getMessage());
        }
    }
}