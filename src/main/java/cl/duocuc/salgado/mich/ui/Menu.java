package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.enums.Roles;
import cl.duocuc.salgado.mich.model.Usuario;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Menu extends JFrame {

    private Usuario usuario;
    private JPanel panelPrincipal;
    private JLabel lblBienvenida;

    public Menu() {
        configurarVentana();
        setupPanels();
        setupButtons();
        setupListeners();
    }

    public Menu(Usuario usuario) {
        this.usuario = usuario;

        configurarVentana();
        setupPanels();
        setupButtons();
        setupListeners();
    }

    private void configurarVentana() {
        setTitle("Gestor de Biblioteca Escolar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(450, 430);
        setLocationRelativeTo(null);
    }

    public void init() {
        setVisible(true);
    }

    private void setupPanels() {
        panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        String nombre = usuario != null
                ? usuario.getPersona().getNombre()
                : "Usuario";

        lblBienvenida = new JLabel(
                "Biblioteca Escolar - Bienvenido/a " + nombre,
                SwingConstants.CENTER
        );

        lblBienvenida.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        panelPrincipal.add(lblBienvenida, BorderLayout.NORTH);

        setContentPane(panelPrincipal);
    }

    private void setupButtons() {
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));

        agregarBoton(panelBotones, "Libros", true);
        agregarBoton(panelBotones, "Estudiantes", esAdministrador());
        agregarBoton(panelBotones, "Categorías", esAdministrador());
        agregarBoton(panelBotones, "Préstamos y devoluciones", puedeUsarPrestamos());
        agregarBoton(panelBotones, "Usuarios", esAdministrador());

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(btnCerrarSesion, BorderLayout.CENTER);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    private void agregarBoton(JPanel panel, String texto, boolean visible) {
        if (!visible) {
            return;
        }

        JButton boton = new JButton(texto);

        boton.addActionListener(e ->
                mostrarModulo(texto)
        );

        panel.add(boton);
    }

    private boolean esAdministrador() {
        return usuario != null && usuario.getRol() == Roles.ADMINISTRADOR;
    }

    private boolean puedeUsarPrestamos() {
        if (usuario == null) {
            return false;
        }

        Roles rol = usuario.getRol();

        return rol == Roles.ADMINISTRADOR || rol == Roles.ESTUDIANTE || rol == Roles.DOCENTE;
    }

    private void mostrarModulo(@NotNull String modulo) {
        switch (modulo) {
            case "Libros":
                new LibroMenu().setVisible(true);
                break;
            case "Categorías":
                new CategoriaMenu().setVisible(true);
                break;
            case "Estudiantes":
                new EstudianteMenu().setVisible(true);
                break;
            case "Préstamos y devoluciones":
                new PrestamoMenu().setVisible(true);
                break;
            case "Usuarios":
                new UsuarioMenu().setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(
                        this,
                        "El módulo de " + modulo + " se implementará próximamente.",
                        "Gestor de Biblioteca Escolar",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
        }
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar la sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();

            // La ventana de acceso se abrirá nuevamente.
            new LoginMenu().setVisible(true);
        }
    }

    private void setupListeners() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Se ha cerrado el menú.");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Ventana del menú cerrada.");
            }
        });
    }
}