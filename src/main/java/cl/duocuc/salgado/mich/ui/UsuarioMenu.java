package cl.duocuc.salgado.mich.ui;

import cl.duocuc.salgado.mich.enums.Roles;
import cl.duocuc.salgado.mich.model.PersonaGenerica;
import cl.duocuc.salgado.mich.model.Rut;
import cl.duocuc.salgado.mich.model.Usuario;
import cl.duocuc.salgado.mich.service.UsuarioService;
import cl.duocuc.salgado.mich.util.InvalidRutException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioMenu extends JFrame {

    private final UsuarioService usuarioService;

    private JTextField txtRut;
    private JTextField txtDv;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtClave;
    private JComboBox<Roles> cmbRol;

    private JTable tabla;
    private DefaultTableModel modelo;

    public UsuarioMenu() {
        usuarioService = new UsuarioService();

        configurarVentana();
        inicializarComponentes();
        cargarUsuarios();
    }

    private void configurarVentana() {
        setTitle("Administración de usuarios");
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
                BorderFactory.createTitledBorder("Datos del usuario")
        );

        txtRut = new JTextField();
        txtDv = new JTextField();
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtClave = new JPasswordField();
        cmbRol = new JComboBox<>(Roles.values());

        formulario.add(new JLabel("RUT (sin dígito verificador):"));
        formulario.add(txtRut);

        formulario.add(new JLabel("Dígito verificador:"));
        formulario.add(txtDv);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtClave);

        formulario.add(new JLabel("Rol:"));
        formulario.add(cmbRol);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "RUT", "Nombre", "Correo", "Rol"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar lista");
        JButton btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnActualizar.addActionListener(e -> cargarUsuarios());
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnRegistrar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnCerrar);

        principal.add(formulario, BorderLayout.NORTH);
        principal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);

        try {
            List<Usuario> usuarios = usuarioService.listar();

            for (Usuario usuario : usuarios) {
                modelo.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getPersona().getRut(),
                        usuario.getPersona().getNombre(),
                        usuario.getCorreo(),
                        usuario.getRol()
                });
            }

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los usuarios.");
        }
    }

    private void registrarUsuario() {
        try {
            String numeroRut = txtRut.getText().trim();
            String dvTexto = txtDv.getText().trim();
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String clave = new String(txtClave.getPassword());

            if (numeroRut.isEmpty() || dvTexto.isEmpty()
                    || nombre.isEmpty() || correo.isEmpty()
                    || clave.isEmpty()) {
                mostrarError("Completa todos los campos.");
                return;
            }

            if (!numeroRut.matches("\\d+")
                    || !dvTexto.matches("(?i)[0-9k]")) {
                mostrarError("El RUT o su dígito verificador no son válidos.");
                return;
            }

            int numero = Integer.parseInt(numeroRut);
            char dv = dvTexto.toUpperCase().charAt(0);
            Rut rut = new Rut(numero, dv);

            Roles rol = (Roles) cmbRol.getSelectedItem();

            PersonaGenerica persona = new PersonaGenerica(
                    nombre,
                    "",
                    rut
            );

            Usuario usuario = new Usuario(
                    0,
                    persona,
                    correo,
                    clave,
                    rol
            );

            usuarioService.registrar(usuario);

            limpiarFormulario();
            cargarUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de registro enviada."
            );

        } catch (NumberFormatException e) {
            mostrarError("El número del RUT no es válido.");

        } catch (InvalidRutException e) {
            mostrarError(e.getMessage());

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());

        } catch (RuntimeException e) {
            mostrarError("No se pudo registrar el usuario.");
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            mostrarError("Selecciona un usuario de la tabla.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar el usuario seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            usuarioService.eliminar(id);
            cargarUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Solicitud de eliminación enviada."
            );

        } catch (RuntimeException e) {
            mostrarError("No se pudo eliminar el usuario.");
        }
    }

    private void limpiarFormulario() {
        txtRut.setText("");
        txtDv.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtClave.setText("");
        cmbRol.setSelectedIndex(0);
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