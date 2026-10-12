package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.controller.CategoriaController;
import cl.duocuc.salgado.mich.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaMenu extends JFrame {

    private final CategoriaController controller;

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modelo;

    public CategoriaMenu() {
        controller = new CategoriaController();

        configurarVentana();
        inicializarComponentes();
        cargarCategorias();
    }

    private void configurarVentana() {
        setTitle("Gestión de categorías");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel formulario = new JPanel(new BorderLayout(10, 10));
        formulario.setBorder(
                BorderFactory.createTitledBorder("Nueva categoría")
        );

        txtNombre = new JTextField();
        JButton btnRegistrar = new JButton("Registrar");

        formulario.add(new JLabel("Nombre:"), BorderLayout.WEST);
        formulario.add(txtNombre, BorderLayout.CENTER);
        formulario.add(btnRegistrar, BorderLayout.EAST);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre de categoría"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargarCategorias());
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnCerrar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private void cargarCategorias() {
        modelo.setRowCount(0);

        try {
            List<Categoria> categorias = controller.listar();

            for (Categoria categoria : categorias) {
                modelo.addRow(new Object[]{
                        categoria.getId(),
                        categoria.getNombre()
                });
            }
        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar las categorías.");
        }
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("Ingresa el nombre de la categoría.");
            return;
        }

        if (nombre.length() > 20) {
            mostrarError(
                    "El nombre no puede superar los 20 caracteres."
            );
            return;
        }

        try {
            controller.registrar(new Categoria(0, nombre));

            txtNombre.setText("");
            cargarCategorias();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de registro enviada."
            );

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (RuntimeException e) {
            mostrarError("No se pudo registrar la categoría.");
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            mostrarError("Selecciona una categoría.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar la categoría seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.eliminar(id);
            cargarCategorias();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de eliminación enviada."
            );

        } catch (RuntimeException e) {
            mostrarError(
                    "No se pudo eliminar. Comprueba si tiene libros asociados."
            );
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }
}