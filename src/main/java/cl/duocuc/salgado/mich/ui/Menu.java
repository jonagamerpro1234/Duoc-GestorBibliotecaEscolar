package cl.duocuc.salgado.mich.ui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Menu extends JFrame {

    public Menu(){
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400,400);
        setLocationRelativeTo(null);

        setupListeners();
    }

    public void init(){
        setVisible(true);
    }

    private void setupPanels(){

    }

    private void setupButtons(){

    }


    private void setupListeners(){

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Se ha cerrado");
            }

            @Override
            public void windowClosed(WindowEvent e){
                System.out.println("Se ha cerrado 1");
            }

        });
    }

}
