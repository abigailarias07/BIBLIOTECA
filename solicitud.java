import java.sql.*;
import java.util.ArrayList;

public class solicitud {

    public static void registrar(
            Connection conexion,
            String titulo,
            estudiante usuario,
            String codigo,
            String fecha
    ) throws SQLException {

        String sql =
                "INSERT INTO solicitudes "
                + "(titulo, estudiante, correo, codigo, fecha) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, titulo);
            ps.setString(2, usuario.nombre);
            ps.setString(3, usuario.correo);
            ps.setString(4, codigo);
            ps.setString(5, fecha);

            ps.executeUpdate();
        }
    }

    public static ArrayList<String> obtenerHistorial(String correo)
            throws SQLException {

        ArrayList<String> historial = new ArrayList<>();

        String sql =
                "SELECT titulo, estudiante, codigo, fecha "
                + "FROM solicitudes "
                + "WHERE correo = ? "
                + "ORDER BY id DESC";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String accion = rs.getString("codigo") == null
                        ? "[DEVUELTO]"
                        : "[SEPARADO]";

                historial.add(
                        accion + "  "
                        + rs.getString("titulo")
                        + "  •  Usuario: "
                        + rs.getString("estudiante")
                        + "  •  "
                        + rs.getString("fecha")
                );
            }
        }

        return historial;
    }

    public static boolean perteneceA(
            Connection conexion,
            String titulo,
            String correo
    ) throws SQLException {

        String sql =
                "SELECT correo FROM solicitudes "
                + "WHERE titulo = ? AND codigo IS NOT NULL "
                + "ORDER BY id DESC LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, titulo);

            ResultSet rs = ps.executeQuery();

            return rs.next()
                    && rs.getString("correo").equals(correo);
        }
    }
}