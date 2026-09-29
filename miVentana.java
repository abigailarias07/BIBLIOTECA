import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class miVentana extends JFrame {

    private static final Color AZUL = new Color(30, 60, 114);
    private static final Color FONDO = new Color(245, 247, 250);

    private String[][] catalogo = new libro().getCatalogo();
    private estudiante usuario;

    private JComboBox<String> cbCriterio;
    private JTextField txtBuscar;
    private DefaultListModel<String> modeloResultados;
    private JList<String> listaResultados;

    
    private DefaultListModel modeloHistorial = new DefaultListModel<>();


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
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Resultados"
        ));

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

        estilizarBoton(btnSeparar);
        estilizarBoton(btnDevolver);
        estilizarBoton(btnCatalogo);

        panel.add(btnSeparar);
        panel.add(btnDevolver);
        panel.add(btnCatalogo);
        add(panel, BorderLayout.SOUTH);

        btnSeparar.addActionListener(e -> cambiarEstado("SEPARADO"));
        btnDevolver.addActionListener(e -> cambiarEstado("DISPONIBLE"));
        btnCatalogo.addActionListener(e -> mostrarCatalogo());
    }

    private void buscarLibros() {
        String texto = txtBuscar.getText().trim().toLowerCase();

        modeloResultados.clear();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un término para buscar.");
            return;
        }

        int columna = cbCriterio.getSelectedIndex();

        for (int i = 0; i < catalogo.length; i++) {
            if (catalogo[i][columna].toLowerCase().contains(texto)) {
                agregarResultado(i);
            }
        }

        if (modeloResultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron libros.");
        }
    }

    private void mostrarCatalogo() {
        modeloResultados.clear();

        for (int i = 0; i < catalogo.length; i++) {
            agregarResultado(i);
        }
    }

    private void agregarResultado(int i) {
        modeloResultados.addElement(
                catalogo[i][0] + " | "
                + catalogo[i][1] + " | "
                + catalogo[i][2] + " | "
                + catalogo[i][3]
        );
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

        for (int i = 0; i < catalogo.length; i++) {
            if (catalogo[i][0].equals(titulo)) {

                if (catalogo[i][3].equals(nuevoEstado)) {
                    JOptionPane.showMessageDialog(
                            this,
                            nuevoEstado.equals("SEPARADO")
                                    ? "El libro ya se encuentra SEPARADO."
                                    : "El libro ya se encuentra DISPONIBLE."
                    );
                    return;
                }

                catalogo[i][3] = nuevoEstado;

                if (nuevoEstado.equals("SEPARADO")) {
                    int codigo = (int) (Math.random() * 9000) + 1000;
                    JOptionPane.showMessageDialog(
                            this,
                            "¡Libro separado con éxito!\nCódigo: CE-" + codigo

                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Libro devuelto correctamente."
                    );
                }

                actualizarResultados();
                String registro = "Usuario: " + usuario.nombre + " | Libro: " + titulo + " | Acción: " + nuevoEstado + " | Fecha/Hora: " + java.time.LocalDateTime.now();
                System.out.println("Historial guardado: " + registro);
                return;
            }
        }
    }

    private void actualizarResultados() {
        if (txtBuscar.getText().trim().isEmpty()) {
            mostrarCatalogo();
        } else {
            buscarLibros();
        }
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
