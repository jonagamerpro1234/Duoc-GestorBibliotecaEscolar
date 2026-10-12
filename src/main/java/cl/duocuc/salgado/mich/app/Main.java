package cl.duocuc.salgado.mich.app;

import cl.duocuc.salgado.mich.ui.LoginMenu;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("No se pudo aplicar el estilo del sistema.");
            }

            LoginMenu login = new LoginMenu();
            login.setVisible(true);
        });
    }
}