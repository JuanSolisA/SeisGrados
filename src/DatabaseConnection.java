import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/seisgrados";
    private static final String USER = "root"; // Cambia esto si usas otro usuario
    private static final String PASSWORD = ""; // Cambia la contraseña si es necesario

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
