import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionSQLite {

    private static final String URL = "jdbc:sqlite:biblioteca.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public static void crearTablas() {

        String tablaLibros =
                "CREATE TABLE IF NOT EXISTS libros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT NOT NULL,"
                + "autor TEXT NOT NULL,"
                + "categoria TEXT NOT NULL,"
                + "estado TEXT NOT NULL"
                + ");";

        String tablaSolicitudes =
                "CREATE TABLE IF NOT EXISTS solicitudes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT NOT NULL,"
                + "estudiante TEXT NOT NULL,"
                + "correo TEXT NOT NULL,"
                + "codigo TEXT,"
                + "fecha TEXT NOT NULL"
                + ");";

        try (
            Connection conexion = conectar();
            Statement stmt = conexion.createStatement()
        ) {
            stmt.execute(tablaLibros);
            stmt.execute(tablaSolicitudes);
            System.out.println("Tablas creadas correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }

    public static void insertarLibrosIniciales() {

        String verificar = "SELECT COUNT(*) FROM libros";
        String insertar =
                "INSERT INTO libros (titulo, autor, categoria, estado) "
                + "VALUES (?, ?, ?, ?)";

        String[][] libros = {
            {"Logica de programación", "Luis Joyanes", "programacion", "DISPONIBLE"},
            {"El cuerpo humano", "Alejandra Alvarez", "medicina", "DISPONIBLE"},
            {"Contabilidad financiera", "Ana Linares", "contabilidad", "DISPONIBLE"},
            {"Algoritmos para la solucion", "Bernal Quiones", "programacion", "DISPONIBLE"},
            {"Los organos", "Alejandra Alvarez", "medicina", "DISPONIBLE"},
            {"Hojas de calculo", "Silvia Villasante", "contabilidad", "DISPONIBLE"}
        };

        try (
            Connection conexion = conectar();
            PreparedStatement psVerificar = conexion.prepareStatement(verificar);
            ResultSet rs = psVerificar.executeQuery()
        ) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }

            try (PreparedStatement ps = conexion.prepareStatement(insertar)) {
                for (String[] datos : libros) {
                    ps.setString(1, datos[0]);
                    ps.setString(2, datos[1]);
                    ps.setString(3, datos[2]);
                    ps.setString(4, datos[3]);
                    ps.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar libros: " + e.getMessage());
        }
    }
}
