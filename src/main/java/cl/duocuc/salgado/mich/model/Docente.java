package cl.duocuc.salgado.mich.model;

public class Docente extends Persona {

    private int id;

    public Docente(int id, String nombre, String apellido, Rut rut) {
        super(nombre, apellido, rut);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTipoPersona() {
        return "Docente";
    }

}