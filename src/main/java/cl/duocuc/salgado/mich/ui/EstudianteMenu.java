package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.controller.EstudianteController;
import cl.duocuc.salgado.mich.util.InvalidRutException;
import cl.duocuc.salgado.mich.model.Estudiante;
import cl.duocuc.salgado.mich.model.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstudianteMenu extends JFrame {

    private final EstudianteController controller;

    private JTextField txtRut;
    private JTextField txtDv;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JTextField txtCurso;
    private JTextField txtCorreo;

    private JTable tabla;
    private DefaultTableModel modelo;

    public EstudianteMenu() {
        controller = new EstudianteController();

        configurarVentana();
        inicializarComponentes();
        cargarEstudiantes();
    }

    private void configurarVentana() {
        setTitle("Gestión de estudiantes");
        setSize(950, 600);
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
                BorderFactory.createTitledBorder("Datos del estudiante")
        );

        txtRut = new JTextField();
        txtDv = new JTextField();
        txtNombre = new JTextField();
        txtApellidoPaterno = new JTextField();
        txtApellidoMaterno = new JTextField();
        txtCurso = new JTextField();
        txtCorreo = new JTextField();

        formulario.add(new JLabel("RUT (sin dígito verificador):"));
        formulario.add(txtRut);

        formulario.add(new JLabel("Dígito verificador:"));
        formulario.add(txtDv);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Apellido paterno:"));
        formulario.add(txtApellidoPaterno);

        formulario.add(new JLabel("Apellido materno:"));
        formulario.add(txtApellidoMaterno);

        formulario.add(new JLabel("Curso:"));
        formulario.add(txtCurso);

        formulario.add(new JLabel("Correo electrónico:"));
        formulario.add(txtCorreo);

        modelo = new DefaultTableModel(
                new Object[]{
                        "ID", "RUT", "Nombre",
                        "Apellidos", "Curso", "Correo"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnActualizar.addActionListener(e -> cargarEstudiantes());
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnRegistrar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnCerrar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(scrollTabla, BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private void cargarEstudiantes() {
        modelo.setRowCount(0);

        try {
            List<Estudiante> estudiantes = controller.listar();

            for (Estudiante estudiante : estudiantes) {
                modelo.addRow(new Object[]{
                        estudiante.getId(),
                        estudiante.getRut(),
                        estudiante.getNombre(),
                        estudiante.getApellido(),
                        estudiante.getCurso(),
                        estudiante.getCorreo()
                });
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los estudiantes.");
        }
    }

    private void registrarEstudiante() {
        try {
            String textoRut = txtRut.getText().trim();
            String textoDv = txtDv.getText().trim();
            String nombre = txtNombre.getText().trim();
            String paterno = txtApellidoPaterno.getText().trim();
            String materno = txtApellidoMaterno.getText().trim();
            String curso = txtCurso.getText().trim();
            String correo = txtCorreo.getText().trim();

            if (textoRut.isEmpty() || textoDv.isEmpty() || nombre.isEmpty() || paterno.isEmpty() || materno.isEmpty() || curso.isEmpty() || correo.isEmpty()) {
                mostrarError("Completa todos los campos.");
                return;
            }

            if (!textoRut.matches("\\d+") || !textoDv.matches("(?i)[0-9k]")) {
                mostrarError("Ingresa un RUT y dígito verificador válidos.");
                return;
            }

            int numeroRut = Integer.parseInt(textoRut);
            char dv = textoDv.toUpperCase().charAt(0);

            Rut rut = new Rut(numeroRut, dv);

            Estudiante estudiante = new Estudiante(
                    0,
                    nombre,
                    paterno + " " + materno,
                    rut,
                    curso,
                    correo
            );

            controller.registrar(estudiante);

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de registro enviada."
            );

            limpiarFormulario();
            cargarEstudiantes();

        } catch (NumberFormatException e) {
            mostrarError("El número del RUT no es válido.");

        } catch (InvalidRutException e) {
            mostrarError(e.getMessage());

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());

        } catch (RuntimeException e) {
            mostrarError("No se pudo registrar el estudiante.");
        }
    }

    private void eliminarEstudiante() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            mostrarError("Selecciona un estudiante de la tabla.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar al estudiante seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.eliminar(id);
            cargarEstudiantes();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de eliminación enviada."
            );

        } catch (RuntimeException e) {
            mostrarError(
                    "No se pudo eliminar. Comprueba si tiene préstamos asociados."
            );
        }
    }

    private void limpiarFormulario() {
        txtRut.setText("");
        txtDv.setText("");
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
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