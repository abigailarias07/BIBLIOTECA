import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class miVentana extends JFrame {
    
    public miVentana() {
        setTitle("Ventana Separada");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel etiqueta = new JLabel("¡Hola desde otro archivo!");
        JButton boton = new JButton("Click aquí");

        add(etiqueta);
        add(boton);
    }
}
