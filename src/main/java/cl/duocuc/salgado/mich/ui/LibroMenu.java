package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.controller.CategoriaController;
import cl.duocuc.salgado.mich.controller.LibroController;
import cl.duocuc.salgado.mich.model.Categoria;
import cl.duocuc.salgado.mich.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibroMenu extends JFrame {

    private final LibroController libroController;
    private final CategoriaController categoriaController;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtEditorial;
    private JTextField txtIsbn;
    private JTextField txtStock;

    private JComboBox<Categoria> cmbCategoria;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    public LibroMenu() {
        libroController = new LibroController();
        categoriaController = new CategoriaController();

        configurarVentana();
        inicializarComponentes();
        cargarCategorias();
        cargarLibros();
    }

    private void configurarVentana() {
        setTitle("Gestión de libros");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del libro")
        );

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtEditorial = new JTextField();
        txtIsbn = new JTextField();
        txtStock = new JTextField();

        cmbCategoria = new JComboBox<>();

        formulario.add(new JLabel("Título:"));
        formulario.add(txtTitulo);

        formulario.add(new JLabel("Autor:"));
        formulario.add(txtAutor);

        formulario.add(new JLabel("Editorial:"));
        formulario.add(txtEditorial);

        formulario.add(new JLabel("ISBN:"));
        formulario.add(txtIsbn);

        formulario.add(new JLabel("Stock:"));
        formulario.add(txtStock);

        formulario.add(new JLabel("Categoría:"));
        formulario.add(cmbCategoria);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID", "Título", "Autor",
                        "Editorial", "ISBN", "Stock", "Categoría"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaLibros);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizarLista = new JButton("Actualizar lista");
        JButton btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnActualizarLista.addActionListener(e -> cargarLibros());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizarLista);
        panelBotones.add(btnCerrar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(scrollTabla, BorderLayout.CENTER);
        principal.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();

        try {
            List<Categoria> categorias = categoriaController.listar();

            for (Categoria categoria : categorias) {
                cmbCategoria.addItem(categoria);
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar las categorías.");
        }
    }

    private void cargarLibros() {
        modeloTabla.setRowCount(0);

        try {
            List<Libro> libros = libroController.listar();

            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getEditorial(),
                        libro.getIsbn(),
                        libro.getStock(),
                        libro.getCategoria().getNombre()
                });
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los libros.");
        }
    }

    private void registrarLibro() {
        try {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String editorial = txtEditorial.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String textoStock = txtStock.getText().trim();

            if (titulo.isEmpty() || autor.isEmpty() || textoStock.isEmpty()) {
                mostrarError("Completa el título, autor y stock.");
                return;
            }

            int stock = Integer.parseInt(textoStock);

            if (stock < 0) {
                mostrarError("El stock no puede ser negativo.");
                return;
            }

            Categoria categoria =
                    (Categoria) cmbCategoria.getSelectedItem();

            if (categoria == null) {
                mostrarError("Primero debes registrar una categoría.");
                return;
            }

            Libro libro = new Libro(
                    0,
                    titulo,
                    autor,
                    editorial,
                    isbn,
                    stock,
                    categoria
            );

            libroController.registrar(libro);

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de registro enviada."
            );

            limpiarFormulario();
            cargarLibros();

        } catch (NumberFormatException e) {
            mostrarError("El stock debe ser un número entero.");

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());

        } catch (RuntimeException e) {
            mostrarError("No se pudo registrar el libro.");
        }
    }

    private void eliminarLibro() {
        int fila = tablaLibros.getSelectedRow();

        if (fila == -1) {
            mostrarError("Selecciona un libro de la tabla.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar el libro seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            libroController.eliminar(id);
            cargarLibros();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de eliminación enviada."
            );

        } catch (RuntimeException e) {
            mostrarError("No se pudo eliminar el libro.");
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtIsbn.setText("");
        txtStock.setText("");
        cmbCategoria.setSelectedIndex(-1);
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