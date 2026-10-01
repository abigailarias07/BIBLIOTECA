import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class libro {

    public static ArrayList<String> buscar(int criterio, String texto) throws SQLException {
        ArrayList<String> resultados = new ArrayList<>();

        String columna;
        if (criterio == 0) {
            columna = "titulo";
        } else if (criterio == 1) {
            columna = "autor";
        } else {
            columna = "categoria";
        }

        String sql = "SELECT titulo, autor, categoria, estado FROM libros WHERE "
                + columna + " LIKE ?";

        try (
            Connection conexion = ConexionSQLite.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(formatear(rs));
                }
            }
        }

        return resultados;
    }

    public static ArrayList<String> obtenerTodos() throws SQLException {
        ArrayList<String> resultados = new ArrayList<>();
        String sql = "SELECT titulo, autor, categoria, estado FROM libros";

        try (
            Connection conexion = ConexionSQLite.conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                resultados.add(formatear(rs));
            }
        }

        return resultados;
    }

    public static String obtenerEstado(Connection conexion, String titulo) throws SQLException {
        String sql = "SELECT estado FROM libros WHERE titulo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, titulo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("estado");
                }
            }
        }

        return null;
    }

    public static void actualizarEstado(Connection conexion, String titulo, String estado)
            throws SQLException {

        String sql = "UPDATE libros SET estado = ? WHERE titulo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, titulo);
            ps.executeUpdate();
        }
    }

    private static String formatear(ResultSet rs) throws SQLException {
        return rs.getString("titulo") + " | "
                + rs.getString("autor") + " | "
                + rs.getString("categoria") + " | "
                + rs.getString("estado");
    }
}
