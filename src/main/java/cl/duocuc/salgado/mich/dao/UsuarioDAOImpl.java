package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.enums.Roles;
import cl.duocuc.salgado.mich.util.RutInvalidoException;
import cl.duocuc.salgado.mich.model.PersonaGenerica;
import cl.duocuc.salgado.mich.model.Rut;
import cl.duocuc.salgado.mich.model.Usuario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void guardar(@NotNull Usuario usuario) {
        String sql = """
                INSERT INTO usuarios
                (rut, dv, nombre, correo, clave, rol)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexionDB.getInstance().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getPersona().getRut().getNumero());
            ps.setString(2, String.valueOf(usuario.getPersona().getRut().getDv()));
            ps.setString(3, usuario.getPersona().getNombre());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getClave());
            ps.setString(6, usuario.getRol().name());

            ps.executeUpdate();

            System.out.println("[MYSQL]: Usuario registrado correctamente.");
        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al registrar el usuario.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT id, rut, dv, nombre, correo, clave, rol
                FROM usuarios
                ORDER BY id
                """;

        try (Connection con = ConexionDB.getInstance().getConexion(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = convertirUsuario(rs);

                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al listar los usuarios.");
            System.out.println(e.getMessage());
        }

        return usuarios;
    }

    @Override
    public Usuario autenticar(String correo, String clave) {
        String sql = """
                SELECT id, rut, dv, nombre, correo, clave, rol
                FROM usuarios
                WHERE correo = ? AND clave = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al autenticar el usuario.");
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public void actualizar(@NotNull Usuario usuario) {
        String sql = """
                UPDATE usuarios
                SET rut = ?, dv = ?, nombre = ?,
                    correo = ?, clave = ?, rol = ?
                WHERE id = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getPersona().getRut().getNumero());
            ps.setString(2, String.valueOf(usuario.getPersona().getRut().getDv()));
            ps.setString(3, usuario.getPersona().getNombre());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getClave());
            ps.setString(6, usuario.getRol().name());
            ps.setInt(7, usuario.getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Usuario actualizado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el usuario.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al actualizar el usuario.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Usuario eliminado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el usuario.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al eliminar el usuario.");
            System.out.println(e.getMessage());
        }
    }

    private @Nullable Usuario convertirUsuario(@NotNull ResultSet rs) throws SQLException {
        try {
            Rut rut = new Rut(rs.getInt("rut"), rs.getString("dv").charAt(0));
            PersonaGenerica persona = new PersonaGenerica(rs.getString("nombre"), "", rut);
            Roles rol = Roles.valueOf(rs.getString("rol").toUpperCase());

            return new Usuario(
                    rs.getInt("id"),
                    persona,
                    rs.getString("correo"),
                    rs.getString("clave"),
                    rol
            );

        } catch (RutInvalidoException | IllegalArgumentException e) {
            System.out.println("[MYSQL]: No se pudo convertir el usuario #" + rs.getInt("id"));
            return null;
        }
    }
}