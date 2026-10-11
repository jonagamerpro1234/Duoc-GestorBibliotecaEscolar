package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Estudiante;
import cl.duocuc.salgado.mich.model.Rut;
import cl.duocuc.salgado.mich.util.InvalidRutException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    @Override
    public void guardar(@NotNull Estudiante estudiante) {
        String sql = """
                INSERT INTO estudiantes
                (rut, dv, nombre, apaterno, amaterno, curso, correo)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estudiante.getRut().getNumero());
            ps.setString(2, String.valueOf(estudiante.getRut().getDv()));
            ps.setString(3, estudiante.getNombre());

            String[] apellidos = separarApellido(estudiante.getApellido());
            ps.setString(4, apellidos[0]);
            ps.setString(5, apellidos[1]);

            ps.setString(6, estudiante.getCurso());
            ps.setString(7, estudiante.getCorreo());

            ps.executeUpdate();

            System.out.println("[MYSQL]: Estudiante registrado correctamente.");

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al registrar el estudiante.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Estudiante> listarTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = """
                SELECT id, rut, dv, nombre, apaterno, amaterno, curso, correo
                FROM estudiantes
                ORDER BY id
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                try {
                    Rut rut = new Rut(
                            rs.getInt("rut"),
                            rs.getString("dv").charAt(0)
                    );

                    String apellido = rs.getString("apaterno")
                            + " " + rs.getString("amaterno");

                    Estudiante estudiante = new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            apellido,
                            rut,
                            rs.getString("curso"),
                            rs.getString("correo")
                    );

                    estudiantes.add(estudiante);

                } catch (InvalidRutException e) {
                    System.out.println(
                            "[MYSQL]: RUT inválido en estudiante #" + rs.getInt("id")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al listar estudiantes.");
            System.out.println(e.getMessage());
        }

        return estudiantes;
    }

    @Override
    public void actualizar(@NotNull Estudiante estudiante) {
        String sql = """
                UPDATE estudiantes
                SET rut = ?, dv = ?, nombre = ?,
                    apaterno = ?, amaterno = ?, curso = ?, correo = ?
                WHERE id = ?
                """;

        try (Connection con = ConexionDB.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estudiante.getRut().getNumero());
            ps.setString(2, String.valueOf(estudiante.getRut().getDv()));
            ps.setString(3, estudiante.getNombre());

            String[] apellidos = separarApellido(estudiante.getApellido());
            ps.setString(4, apellidos[0]);
            ps.setString(5, apellidos[1]);

            ps.setString(6, estudiante.getCurso());
            ps.setString(7, estudiante.getCorreo());
            ps.setInt(8, estudiante.getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Estudiante actualizado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el estudiante.");
            }

        } catch (SQLException e) {
            System.out.println("[MYSQL]: Error al actualizar el estudiante.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {

        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection con = ConexionDB.getInstance().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("[MYSQL]: Estudiante eliminado correctamente.");
            } else {
                System.out.println("[MYSQL]: No se encontró el estudiante.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "[MYSQL]: No se pudo eliminar el estudiante. "
                            + "Puede tener préstamos asociados."
            );
            System.out.println(e.getMessage());
        }
    }

    @Contract("_ -> new")
    private String @NotNull [] separarApellido(@NotNull String apellido) {
        String[] partes = apellido.trim().split("\\s+", 2);
        String paterno = partes[0];
        String materno = partes.length > 1 ? partes[1] : "-";

        return new String[]{paterno, materno};
    }
}