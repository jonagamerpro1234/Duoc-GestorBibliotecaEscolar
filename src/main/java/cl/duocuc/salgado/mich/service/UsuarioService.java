package cl.duocuc.salgado.mich.service;

import cl.duocuc.salgado.mich.dao.UsuarioDAO;
import cl.duocuc.salgado.mich.dao.UsuarioDAOImpl;
import cl.duocuc.salgado.mich.model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public void registrar(Usuario usuario) {
        validar(usuario);
        usuarioDAO.guardar(usuario);
    }

    public List<Usuario> listar() {
        return usuarioDAO.listarTodos();
    }

    public Usuario iniciarSesion(String correo, String clave) {
        if (correo == null || correo.trim().isEmpty() || clave == null || clave.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar el correo y la contraseña.");
        }

        return usuarioDAO.autenticar(correo.trim(), clave);
    }

    public void actualizar(Usuario usuario) {
        if (usuario == null || usuario.getId() <= 0) {
            throw new IllegalArgumentException("El usuario no es válido para actualizar.");
        }

        validar(usuario);
        usuarioDAO.actualizar(usuario);
    }

    public void eliminar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador del usuario no es válido.");
        }

        usuarioDAO.eliminar(id);
    }

    private void validar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        if (usuario.getPersona() == null || usuario.getPersona().getRut() == null) {
            throw new IllegalArgumentException("El usuario debe tener una persona y un RUT válidos.");
        }

        if (usuario.getPersona().getNombre() == null
                || usuario.getPersona().getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (usuario.getCorreo() == null || !usuario.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Debe ingresar un correo válido.");
        }

        if (usuario.getClave() == null || usuario.getClave().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("Debe seleccionar un rol.");
        }
    }
}