package cl.duocuc.salgado.mich.dao;

import cl.duocuc.salgado.mich.model.Usuario;
import java.util.List;

public interface UsuarioDAO {

    void guardar(Usuario usuario);

    List<Usuario> listarTodos();

    Usuario autenticar(String correo, String clave);

    void actualizar(Usuario usuario);

    void eliminar(int id);
}