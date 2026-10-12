package cl.duocuc.salgado.mich.controller;

import cl.duocuc.salgado.mich.enums.Roles;
import cl.duocuc.salgado.mich.model.Usuario;
import cl.duocuc.salgado.mich.service.UsuarioService;

public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    public Usuario iniciarSesion(String correo, String clave) {
        return usuarioService.iniciarSesion(correo, clave);
    }

    public boolean tieneAccesoCompleto(Usuario usuario) {
        return usuario != null && usuario.getRol() == Roles.ADMINISTRADOR;
    }

    public boolean puedeGestionarLibros(Usuario usuario) {
        return usuario != null
                && (usuario.getRol() == Roles.ADMINISTRADOR
                || usuario.getRol() == Roles.DOCENTE);
    }

    public boolean puedeSolicitarLibros(Usuario usuario) {
        return usuario != null
                && (usuario.getRol() == Roles.ESTUDIANTE
                || usuario.getRol() == Roles.DOCENTE);
    }

    public boolean puedeSolicitarPrestamos(Usuario usuario) {
        return usuario != null
                && (usuario.getRol() == Roles.ADMINISTRADOR
                || usuario.getRol() == Roles.ESTUDIANTE
                || usuario.getRol() == Roles.DOCENTE);
    }

    public boolean puedeVerLibros(Usuario usuario) {
        return usuario != null;
    }

    public boolean esVisita(Usuario usuario) {
        return usuario != null && usuario.getRol() == Roles.VISITA;
    }
}