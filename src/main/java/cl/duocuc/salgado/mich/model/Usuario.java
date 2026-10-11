package cl.duocuc.salgado.mich.model;

import cl.duocuc.salgado.mich.enums.Roles;

public class Usuario {

    private int id;
    private Persona persona;
    private String correo;
    private String clave;
    private Roles rol;

    public Usuario(int id, Persona persona, String correo,String clave, Roles rol) {
        this.id = id;
        this.persona = persona;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario #" + id
                + " - " + persona.getNombre()
                + " - Rol: " + rol;
    }
}