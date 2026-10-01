import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class miVentana extends JFrame {

    private static final Color AZUL = new Color(30, 60, 114);
    private static final Color FONDO = new Color(245, 247, 250);

    private estudiante usuario;

    private JComboBox<String> cbCriterio;
    private JTextField txtBuscar;
    private DefaultListModel<String> modeloResultados;
    private JList<String> listaResultados;

    private DefaultListModel<String> modeloHistorial = new DefaultListModel<>();

    public miVentana(estudiante usuario) {
        this.usuario = usuario;

        setTitle("Sistema de Biblioteca - Búsqueda y Préstamos");
        setSize(700, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(FONDO);

        crearEncabezado();
        crearCentro();
        crearBotones();
    }

    private void crearEncabezado() {
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(AZUL);
        encabezado.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("BIBLIOTECA CERTUS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        encabezado.add(titulo);

        if (usuario != null) {
            JLabel datos = new JLabel(
                    usuario.nombre + "  |  " + usuario.carrera + "  |  " + usuario.correo
            );
            datos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            datos.setForeground(new Color(225, 232, 245));
            datos.setAlignmentX(Component.CENTER_ALIGNMENT);
            encabezado.add(Box.createVerticalStrut(5));
            encabezado.add(datos);
        }

        add(encabezado, BorderLayout.NORTH);
    }

    private void crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.setBackground(FONDO);
        centro.setBorder(new EmptyBorder(0, 18, 0, 18));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        busqueda.setBackground(FONDO);
        busqueda.setBorder(BorderFactory.createTitledBorder("Buscar libros"));

        cbCriterio = new JComboBox<>(new String[]{"Título", "Autor", "Categoría"});
        txtBuscar = new JTextField(18);
        txtBuscar.setPreferredSize(new Dimension(210, 32));

        JButton btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar);

        busqueda.add(new JLabel("Buscar por:"));
        busqueda.add(cbCriterio);
        busqueda.add(txtBuscar);
        busqueda.add(btnBuscar);

        modeloResultados = new DefaultListModel<>();
        listaResultados = new JList<>(modeloResultados);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaResultados.setFixedCellHeight(30);

        JScrollPane scroll = new JScrollPane(listaResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        centro.add(busqueda, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> buscarLibros());
        txtBuscar.addActionListener(e -> buscarLibros());
    }

    private void crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(FONDO);
        panel.setBorder(new EmptyBorder(0, 10, 12, 10));

        JButton btnSeparar = new JButton("Separar Libro");
        JButton btnDevolver = new JButton("Devolver Libro");
        JButton btnCatalogo = new JButton("Ver Catálogo");
        JButton btnHistorial = new JButton("Historial");

        estilizarBoton(btnSeparar);
        estilizarBoton(btnDevolver);
        estilizarBoton(btnCatalogo);
        estilizarBoton(btnHistorial);

        panel.add(btnHistorial);
        panel.add(btnSeparar);
        panel.add(btnDevolver);
        panel.add(btnCatalogo);
        add(panel, BorderLayout.SOUTH);

        btnSeparar.addActionListener(e -> cambiarEstado("SEPARADO"));
        btnDevolver.addActionListener(e -> cambiarEstado("DISPONIBLE"));
        btnCatalogo.addActionListener(e -> mostrarCatalogo());
        btnHistorial.addActionListener(e -> mostrarHistorial());
    }

    private void buscarLibros() {

        String texto = txtBuscar.getText().trim();
        modeloResultados.clear();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un término para buscar.");
            return;
        }

        try {
            ArrayList<String> resultados =
                    libro.buscar(cbCriterio.getSelectedIndex(), texto);

            for (String resultado : resultados) {
                modeloResultados.addElement(resultado);
            }

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron libros.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
        }
    }

    private void mostrarCatalogo() {

        modeloResultados.clear();

        try {
            for (String resultado : libro.obtenerTodos()) {
                modeloResultados.addElement(resultado);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al mostrar catálogo: " + e.getMessage()
            );
        }
    }

    private String obtenerTituloSeleccionado() {
        String seleccionado = listaResultados.getSelectedValue();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la lista.");
            return null;
        }

        return seleccionado.substring(0, seleccionado.indexOf(" | "));
    }

    private void cambiarEstado(String nuevoEstado) {
        String titulo = obtenerTituloSeleccionado();
        if (titulo == null) return;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a");
        String fechaFormateada = LocalDateTime.now().format(formato);

        try (Connection conexion = ConexionSQLite.conectar()) {

            conexion.setAutoCommit(false);

            String estadoActual = libro.obtenerEstado(conexion, titulo);

            if (estadoActual == null) {
                JOptionPane.showMessageDialog(this, "El libro no existe en la base de datos.");
                return;
            }

            if (estadoActual.equals(nuevoEstado)) {
                JOptionPane.showMessageDialog(
                        this,
                        nuevoEstado.equals("SEPARADO")
                                ? "El libro ya se encuentra SEPARADO."
                                : "No puedes devolver un libro que no has separado."
                );
                return;
            }
            if (nuevoEstado.equals("DISPONIBLE")
                    && !solicitud.perteneceA(conexion, titulo, usuario.correo)){
                JOptionPane.showMessageDialog(this, "No puedes devolver un libro que otro lo ha separado"

                );    
                return;
            }

            libro.actualizarEstado(conexion, titulo, nuevoEstado);

            String codigo = null;

            if (nuevoEstado.equals("SEPARADO"))
                codigo = "CE-" + ((int) (Math.random() * 9000) + 1000);

            solicitud.registrar(conexion, titulo, usuario, codigo, fechaFormateada);

            conexion.commit();

            if (nuevoEstado.equals("SEPARADO")) {
                JOptionPane.showMessageDialog(
                        this,
                        "¡Libro separado con éxito!\nCódigo: " + codigo + "\nFecha/Hora: " + fechaFormateada
                );
            } else {
                JOptionPane.showMessageDialog(this, "Libro devuelto correctamente.");
            }

            actualizarResultados();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void actualizarResultados() {
        if (txtBuscar.getText().trim().isEmpty()) {
            mostrarCatalogo();
        } else {
            buscarLibros();
        }
    }


    private void mostrarHistorial() {
        
        modeloHistorial.clear();
        try {
            for (String movimiento : solicitud.obtenerHistorial(usuario.correo))
                modeloHistorial.addElement(movimiento);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        
        if (modeloHistorial.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aún no hay movimientos registrados en esta sesión.",
                    "Historial Vacío",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JPanel panelUsuario = new JPanel(new GridLayout(2, 1, 2, 2));
        panelUsuario.setBackground(AZUL);
        panelUsuario.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblUsuario = new JLabel("Usuario: " + usuario.nombre + " (" + usuario.carrera + ")", SwingConstants.LEFT);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);

        JLabel lblCorreo = new JLabel("Correo: " + usuario.correo, SwingConstants.LEFT);
        lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCorreo.setForeground(new Color(225, 232, 245));

        panelUsuario.add(lblUsuario);
        panelUsuario.add(lblCorreo);

                //muestra la lista
        JList<String> listaHistorial = new JList<>(modeloHistorial);
        listaHistorial.setFont(new Font("Segoe UI", Font.BOLD, 12));
        listaHistorial.setBackground(Color.WHITE);
        listaHistorial.setForeground(AZUL);
        listaHistorial.setSelectionBackground(AZUL);
        listaHistorial.setSelectionForeground(Color.WHITE);
        listaHistorial.setFixedCellHeight(32);

        JScrollPane scroll = new JScrollPane(listaHistorial);
        scroll.setPreferredSize(new Dimension(620, 240));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                " Movimientos Recientes ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                Color.WHITE
        ));

        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBackground(AZUL);
        panelHistorial.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelHistorial.add(panelUsuario, BorderLayout.NORTH);
        panelHistorial.add(scroll, BorderLayout.CENTER);

        UIManager.put("OptionPane.background", AZUL);
        UIManager.put("Panel.background", AZUL);
                ///panel de opciones
        JOptionPane optionPane = new JOptionPane(
                panelHistorial,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},
                null
        );

        JDialog dialog = optionPane.createDialog(this, "Historial de Transacciones");
        dialog.getContentPane().setBackground(AZUL);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setForeground(AZUL);
        btnCerrar.setBackground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(100, 32));
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(AZUL);
        panelBoton.setBorder(new EmptyBorder(12, 0, 0, 0));
        panelBoton.add(btnCerrar);

        panelHistorial.add(panelBoton, BorderLayout.SOUTH);

        dialog.setVisible(true);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(145, 34));
    }
}