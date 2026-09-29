import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class principalGUI extends JFrame {

    // ====== PALETA ======
    private static final Color COLOR_PRINCIPAL = new Color(30, 60, 114);   // azul oscuro
    private static final Color COLOR_HOVER     = new Color(52, 100, 180);  // azul claro
    private static final Color COLOR_FONDO     = new Color(245, 247, 250); // gris muy claro
    private static final Color COLOR_TEXTO     = new Color(40, 40, 40);

    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);

    private JTextField campoCorreo, campoNombre, campoCarrera;
    private JLabel mensaje;

    public principalGUI() {
        setTitle("Biblioteca Certus");
        setSize(500, 410);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    // ---------- Encabezado ----------
    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setBackground(COLOR_PRINCIPAL);
        panel.setBorder(new EmptyBorder(24, 10, 24, 10));

        JLabel titulo = new JLabel("BIBLIOTECA CERTUS", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);

        panel.add(titulo);
        return panel;
    }

    // ---------- Formulario ----------
    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 15));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(5, 40, 5, 40));

        campoCorreo  = new JTextField();
        campoNombre  = new JTextField();
        campoCarrera = new JTextField();

        panel.add(crearCampo("Correo institucional", campoCorreo));
        panel.add(crearCampo("Nombre", campoNombre));
        panel.add(crearCampo("Carrera", campoCarrera));
        return panel;
    }

    private JPanel crearCampo(String etiqueta, JTextField campo) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(COLOR_FONDO);

        JLabel label = new JLabel(etiqueta);
        label.setFont(FUENTE_NORMAL);
        label.setForeground(COLOR_TEXTO);

        campo.setFont(FUENTE_NORMAL);
        campo.setForeground(COLOR_TEXTO);
        campo.setBackground(Color.WHITE);
        campo.setCaretColor(COLOR_TEXTO);
        campo.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 205, 215), 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        campo.setPreferredSize(new Dimension(350, 36));

        p.add(label, BorderLayout.NORTH);
        p.add(campo, BorderLayout.SOUTH);
        return p;
    }

    // ---------- Botón + mensaje ----------
    private JPanel crearPie() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 8));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 40, 18, 40));

        JButton boton = new JButton("Ingresar  →");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(COLOR_PRINCIPAL);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { boton.setBackground(COLOR_HOVER); }
            public void mouseExited(MouseEvent e)  { boton.setBackground(COLOR_PRINCIPAL); }
        });
        boton.addActionListener(e -> ingresar());

        mensaje = new JLabel(" ", SwingConstants.CENTER);
        mensaje.setFont(FUENTE_NORMAL);

        panel.add(boton);
        panel.add(mensaje);
        return panel;
    }

    private void ingresar() {
        String correo  = campoCorreo.getText().trim();
        String nombre  = campoNombre.getText().trim();
        String carrera = campoCarrera.getText().trim();

        if (!correo.endsWith("@certus.edu.pe")) {
            mostrarMensaje("Correo no permitido. Inténtalo de nuevo.", Color.RED);
            return;
        }
        if (nombre.isEmpty() || carrera.isEmpty()) {
            mostrarMensaje("Completa todos los campos.", Color.RED);
            return;
        }

        estudiante usuario = new estudiante();
        usuario.correo = correo;
        usuario.nombre = nombre;
        usuario.carrera = carrera;

        // Abre la interfaz de búsqueda y préstamo de libros
        miVentana ventanaLibros = new miVentana(usuario);
        ventanaLibros.setVisible(true);
        
        // Cierra la ventana del registro actual
        this.dispose();
    }

    private void mostrarMensaje(String texto, Color color) {
        mensaje.setForeground(color);
        mensaje.setText(texto);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new principalGUI().setVisible(true));
    }
}