import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
}