package cl.duocuc.salgado.mich.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión con la base de datos Gestion de  Biblioteca Escolar.
 */
public class ConexionDB {

    private static ConexionDB instancia;

    private final String URL = "jdbc:mysql://localhost:3306/biblioteca_escolar_db?createDatabaseIfNotExist=true";
    private final String USER = "root";
    private final String PASS = "admin";
    private Connection conexion;

    private ConexionDB() {}

    public static ConexionDB getInstance() {

        if (instancia == null) {
            instancia = new ConexionDB();
        }

        return instancia;
    }

    // Conectar a la base de datos
    public boolean conectar() throws SQLException {

        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("[MYSQL]: Se ha conectado correctamente a la base de datos.");
            return true;
        }

        System.out.println("[MYSQL]: La base de datos ya se encuentra conectada.");
        return false;
    }

    // Entregar conexión de base de datos
    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conectar();
        }
        return conexion;
    }

    // Desconectar de la base de datos
    public void desconectar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("[MYSQL]: Se ha desconectado correctamente de la base de datos.");
        }
    }
}