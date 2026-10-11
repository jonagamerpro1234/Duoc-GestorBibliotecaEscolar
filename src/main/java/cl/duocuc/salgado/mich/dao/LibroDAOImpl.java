package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Categoria;
import cl.duocuc.salgado.mich.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    @Override
    public void guardar(Libro libro) {
        String sql = """
                INSERT INTO libros
                (titulo, autor, editorial, isbn, stock, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setString(4, libro.getIsbn());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getCategoria().getId());

            ps.executeUpdate();

            System.out.println("[MYSQL]: Libro registrado correctamente.");

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al registrar el libro.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Libro> listarTodos() {
        List<Libro> libros = new ArrayList<>();

        String sql = """
                SELECT l.id, l.titulo, l.autor, l.editorial,
                       l.isbn, l.stock,
                       c.id AS categoria_id,
                       c.nombre_categoria
                FROM libros l
                INNER JOIN categorias c ON l.id_categoria = c.id
                ORDER BY l.id
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("categoria_id"),
                        rs.getString("nombre_categoria")
                );

                Libro libro = new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getString("isbn"),
                        rs.getInt("stock"),
                        categoria
                );

                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al listar los libros.");
            System.out.println(e.getMessage());
        }

        return libros;
    }

    @Override
    public void actualizar(Libro libro) {
        String sql = """
                UPDATE libros
                SET titulo = ?,
                    autor = ?,
                    editorial = ?,
                    isbn = ?,
                    stock = ?,
                    id_categoria = ?
                WHERE id = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getEditorial());
            ps.setString(4, libro.getIsbn());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getCategoria().getId());
            ps.setInt(7, libro.getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Libro actualizado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el libro.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al actualizar el libro.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = """
                DELETE FROM libros WHERE id = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Libro eliminado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el libro.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "[MYSQL]: No se pudo eliminar el libro. "
                            + "Puede tener préstamos asociados."
            );
            System.out.println(e.getMessage());
        }
    }
}