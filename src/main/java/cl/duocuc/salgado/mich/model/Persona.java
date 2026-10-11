package cl.duocuc.salgado.mich.model;

public abstract class Persona {

    private String nombre;
    private String apellido;
    private Rut rut;

    public Persona(String nombre, String apellido, Rut rut) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rut getRut() {
        return rut;
    }

    public void setRut(Rut rut) {
        this.rut = rut;
    }

    public abstract String getTipoPersona();

    @Override
    public String toString() {
        return nombre + " " + apellido + " - RUT: " + rut;
    }
}