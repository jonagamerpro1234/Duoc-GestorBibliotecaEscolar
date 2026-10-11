package cl.duocuc.salgado.mich.model;

public class PersonaGenerica extends Persona {

    public PersonaGenerica(String nombre, String apellido, Rut rut) {
        super(nombre, apellido, rut);
    }

    @Override
    public String getTipoPersona() {
        return "Persona";
    }
}