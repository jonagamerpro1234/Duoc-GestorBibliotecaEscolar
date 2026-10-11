package cl.duocuc.salgado.mich.model;

public class Estudiante extends Persona {

    private int id;
    private String curso;
    private String correo;

    public Estudiante(int id, String nombre, String apellido, Rut rut, String curso, String correo) {
        super(nombre, apellido, rut);
        this.id = id;
        this.curso = curso;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String getTipoPersona() {
        return "Estudiante";
    }

    @Override
    public String toString() {
        return super.toString()
                + " - Curso: " + curso
                + " - Correo: " + correo;
    }
}