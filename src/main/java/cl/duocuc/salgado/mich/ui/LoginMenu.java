package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.controller.LoginController;
import cl.duocuc.salgado.mich.model.Usuario;
import cl.duocuc.salgado.mich.ui.Menu;

import javax.swing.*;
import java.awt.*;

public class LoginMenu extends JFrame {

    private final LoginController loginController;

    private JTextField txtCorreo;
    private JPasswordField txtClave;
    private JButton btnIngresar;

    public LoginMenu() {
        loginController = new LoginController();

        configurarVentana();
        inicializarComponentes();
    }

    public void  init(){
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Gestor de Biblioteca Escolar - Inicio de sesión");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblTitulo = new JLabel(
                "Biblioteca Escolar",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 15, 15));
        JLabel lblCorreo = new JLabel("Correo electrónico:");
        txtCorreo = new JTextField();

        JLabel lblClave = new JLabel("Contraseña:");
        txtClave = new JPasswordField();

        panelFormulario.add(lblCorreo);
        panelFormulario.add(txtCorreo);
        panelFormulario.add(lblClave);
        panelFormulario.add(txtClave);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> iniciarSesion());

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(btnIngresar, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String clave = new String(txtClave.getPassword());

        try {
            Usuario usuario = loginController.iniciarSesion(correo, clave);

            if (usuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos.",
                        "Acceso denegado",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido/a, " + usuario.getPersona().getNombre(),
                    "Inicio de sesión",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new Menu(usuario).setVisible(true);
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}