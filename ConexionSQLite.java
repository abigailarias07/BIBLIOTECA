import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConexionSQLite {

    private static final String URL = "jdbc:sqlite:biblioteca.db";

    // Conectarse a la base de datos
    public static Connection conectar() {

        try {

            Connection conexion =
                    DriverManager.getConnection(URL);

            System.out.println("Conexión con SQLite exitosa.");

            return conexion;

        } catch (SQLException e) {

            System.out.println(
                    "Error al conectar: " + e.getMessage()
            );

            return null;
        }
    }

    // Crear las tablas
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
                + "usuario TEXT NOT NULL,"
                + "libro TEXT NOT NULL,"
                + "accion TEXT NOT NULL,"
                + "fecha TEXT NOT NULL"
                + ");";

        try (
            Connection conexion = conectar();
            Statement stmt = conexion.createStatement()
        ) {

            stmt.execute(tablaLibros);
            stmt.execute(tablaSolicitudes);

            System.out.println(
                    "Tablas creadas correctamente."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear tablas: "
                    + e.getMessage()
            );
        }
    }

    // Insertar los 6 libros iniciales
    public static void insertarLibrosIniciales() {

        String verificar = "SELECT COUNT(*) FROM libros";

        String insertar =
                "INSERT INTO libros "
                + "(titulo, autor, categoria, estado) "
                + "VALUES (?, ?, ?, ?)";

        String[][] libros = {

            {"Logica de programación",
             "Luis Joyanes",
             "programacion",
             "DISPONIBLE"},

            {"El cuerpo humano",
             "Alejandra Alvarez",
             "medicina",
             "DISPONIBLE"},

            {"Contabilidad financiera",
             "Ana Linares",
             "contabilidad",
             "DISPONIBLE"},

            {"Algoritmos para la solucion",
             "Bernal Quiones",
             "programacion",
             "DISPONIBLE"},

            {"Los organos",
             "Alejandra Alvarez",
             "medicina",
             "DISPONIBLE"},

            {"Hojas de calculo",
             "Silvia Villasante",
             "contabilidad",
             "DISPONIBLE"}
        };

        try (
            Connection conexion = conectar();
            PreparedStatement psVerificar =
                    conexion.prepareStatement(verificar);
            ResultSet rs = psVerificar.executeQuery()
        ) {

            // Verificar si ya existen libros
            if (rs.next() && rs.getInt(1) > 0) {

                System.out.println(
                        "Los libros ya están registrados."
                );

                return;
            }

            // Insertar los 6 libros
            try (PreparedStatement ps =
                    conexion.prepareStatement(insertar)) {

                for (String[] libro : libros) {

                    ps.setString(1, libro[0]);
                    ps.setString(2, libro[1]);
                    ps.setString(3, libro[2]);
                    ps.setString(4, libro[3]);

                    ps.executeUpdate();
                }
            }

            System.out.println(
                    "Los 6 libros fueron registrados correctamente."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error al insertar libros: "
                    + e.getMessage()
            );
        }
    }
}