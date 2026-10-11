package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Categoria;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public void guardar(@NotNull Categoria categoria) {
        String sql = """
                INSERT INTO categorias (nombre_categoria)
                VALUES (?)
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.executeUpdate();

            System.out.println("[MYSQL]: Categoría registrada correctamente.");
        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al registrar la categoría.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Categoria> listarTodos() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                SELECT id, nombre_categoria
                FROM categorias
                ORDER BY id
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre_categoria")
                );

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al listar las categorías.");
            System.out.println(e.getMessage());
        }

        return categorias;
    }

    @Override
    public void actualizar(@NotNull Categoria categoria) {
        String sql = """
                UPDATE categorias
                SET nombre_categoria = ?
                WHERE id = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Categoría actualizada correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró la categoría.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al actualizar la categoría.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = """
                DELETE FROM categorias
                WHERE id = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Categoría eliminada correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró la categoría.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: No se pudo eliminar la categoría. " + "Puede estar asociada a libros.");
            System.out.println(e.getMessage());
        }
    }
}