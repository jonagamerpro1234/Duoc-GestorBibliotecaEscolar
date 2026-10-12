package cl.duocuc.salgado.mich.app;

import cl.duocuc.salgado.mich.dao.ConexionDB;
import cl.duocuc.salgado.mich.ui.LoginMenu;
import cl.duocuc.salgado.mich.ui.Menu;

public class Main {

    public static void main(String[] args) {
        //Conectar a DB, !Cancelar si no se logra conectar y finalizar!



        //Inicio interfaz login antes que menu
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.init();
    }

}
