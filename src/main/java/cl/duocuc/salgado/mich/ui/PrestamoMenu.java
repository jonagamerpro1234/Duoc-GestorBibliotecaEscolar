package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.controller.EstudianteController;
import cl.duocuc.salgado.mich.controller.LibroController;
import cl.duocuc.salgado.mich.controller.PrestamoController;
import cl.duocuc.salgado.mich.model.Estudiante;
import cl.duocuc.salgado.mich.model.Libro;
import cl.duocuc.salgado.mich.model.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PrestamoMenu extends JFrame {

    private final PrestamoController prestamoController;
    private final EstudianteController estudianteController;
    private final LibroController libroController;

    private JComboBox<Estudiante> cmbEstudiantes;
    private JComboBox<Libro> cmbLibros;
    private JTextField txtFechaPrestamo;
    private JTextField txtFechaDevolucion;

    private JTable tabla;
    private DefaultTableModel modelo;

    public PrestamoMenu() {
        prestamoController = new PrestamoController();
        estudianteController = new EstudianteController();
        libroController = new LibroController();

        configurarVentana();
        inicializarComponentes();
        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();
    }

    private void configurarVentana() {
        setTitle("Gestión de préstamos");
        setSize(950, 580);
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
                BorderFactory.createTitledBorder("Registrar préstamo")
        );

        cmbEstudiantes = new JComboBox<>();
        cmbLibros = new JComboBox<>();

        txtFechaPrestamo = new JTextField(LocalDate.now().toString());
        txtFechaDevolucion = new JTextField(
                LocalDate.now().plusDays(14).toString()
        );

        formulario.add(new JLabel("Estudiante:"));
        formulario.add(cmbEstudiantes);

        formulario.add(new JLabel("Libro:"));
        formulario.add(cmbLibros);

        formulario.add(new JLabel("Fecha de préstamo (AAAA-MM-DD):"));
        formulario.add(txtFechaPrestamo);

        formulario.add(new JLabel("Fecha de devolución (AAAA-MM-DD):"));
        formulario.add(txtFechaDevolucion);

        modelo = new DefaultTableModel(
                new Object[]{
                        "ID", "Estudiante", "Libro",
                        "Fecha préstamo", "Fecha devolución", "Devuelto"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnRegistrar = new JButton("Registrar préstamo");
        JButton btnDevolver = new JButton("Registrar devolución");
        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
        btnActualizar.addActionListener(e -> actualizarDatos());
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnRegistrar);
        botones.add(btnDevolver);
        botones.add(btnActualizar);
        botones.add(btnCerrar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private void cargarEstudiantes() {
        cmbEstudiantes.removeAllItems();

        try {
            List<Estudiante> estudiantes = estudianteController.listar();

            for (Estudiante estudiante : estudiantes) {
                cmbEstudiantes.addItem(estudiante);
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los estudiantes.");
        }
    }

    private void cargarLibros() {
        cmbLibros.removeAllItems();

        try {
            List<Libro> libros = libroController.listar();

            for (Libro libro : libros) {
                if (libro.getStock() > 0) {
                    cmbLibros.addItem(libro);
                }
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los libros.");
        }
    }

    private void cargarPrestamos() {
        modelo.setRowCount(0);

        try {
            List<Prestamo> prestamos = prestamoController.listar();

            for (Prestamo prestamo : prestamos) {
                modelo.addRow(new Object[]{
                        prestamo.getId(),
                        prestamo.getEstudiante().getNombre()
                                + " " + prestamo.getEstudiante().getApellido(),
                        prestamo.getLibro().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion(),
                        prestamo.isDevuelto() ? "Sí" : "No"
                });
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los préstamos.");
        }
    }

    private void registrarPrestamo() {
        Estudiante estudiante =
                (Estudiante) cmbEstudiantes.getSelectedItem();

        Libro libro = (Libro) cmbLibros.getSelectedItem();

        if (estudiante == null) {
            mostrarError("Debes seleccionar un estudiante.");
            return;
        }

        if (libro == null) {
            mostrarError("Debes seleccionar un libro con stock disponible.");
            return;
        }

        try {
            LocalDate fechaPrestamo =
                    LocalDate.parse(txtFechaPrestamo.getText().trim());

            LocalDate fechaDevolucion =
                    LocalDate.parse(txtFechaDevolucion.getText().trim());

            Prestamo prestamo = new Prestamo(
                    0,
                    estudiante,
                    libro,
                    fechaPrestamo,
                    fechaDevolucion,
                    false
            );

            prestamoController.registrar(prestamo);

            actualizarDatos();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de préstamo procesada. "
                            + "Comprueba el historial y el stock."
            );

        } catch (DateTimeParseException e) {
            mostrarError(
                    "Las fechas deben tener el formato AAAA-MM-DD."
            );

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());

        } catch (RuntimeException e) {
            mostrarError("No se pudo procesar el préstamo.");
        }
    }

    private void registrarDevolucion() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            mostrarError("Selecciona un préstamo del historial.");
            return;
        }

        String estado = String.valueOf(
                modelo.getValueAt(fila, 5)
        );

        if ("Sí".equals(estado)) {
            mostrarError("Este préstamo ya fue devuelto.");
            return;
        }

        int idPrestamo = (int) modelo.getValueAt(fila, 0);

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Confirmas la devolución del libro?",
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            prestamoController.devolver(idPrestamo);
            actualizarDatos();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de devolución procesada. "
                            + "Comprueba el estado del préstamo y el stock."
            );

        } catch (RuntimeException e) {
            mostrarError("No se pudo procesar la devolución.");
        }
    }

    private void actualizarDatos() {
        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();
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