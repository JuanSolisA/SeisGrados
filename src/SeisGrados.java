
import java.sql.*;
import java.util.*;

public class SeisGrados {

    private Connection connection;

    public SeisGrados() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        try {
            String url = "jdbc:mysql://localhost:3306/seisgrados";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para calcular el grado de separación entre dos personas usando BFS
    public int calcularGradoDeSeparacion(String persona1, String persona2) throws SQLException {
        if (persona1.equals(persona2)) {
            return 0;// misma persona = 0
        }

        // Mapa para almacenar el grado de separación de cada persona
        Map<String, Integer> grados = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        // Iniciar la búsqueda desde la primera persona
        queue.add(persona1);
        grados.put(persona1, 0);

        // Algoritmo de búsqueda en anchura (BFS)
        while (!queue.isEmpty()) {
            String actual = queue.poll();
            int gradoActual = grados.get(actual);

            // Obtener los conocidos de la persona actual
            List<String> conocidos = getConocidos(actual);

            for (String conocido : conocidos) {
                if (!grados.containsKey(conocido)) {
                    grados.put(conocido, gradoActual + 1);
                    queue.add(conocido);

                    // Si encontramos a la segunda persona, devolvemos el grado
                    if (conocido.equals(persona2)) {
                        return grados.get(conocido);
                    }
                }
            }
        }

        // Si no se encuentra una conexión, devolvemos -1
        return -1;
    }

    // Método para obtener las personas conocidas de una persona desde la base de datos
    public List<String> getConocidos(String nombre) throws SQLException {
        List<String> conocidos = new ArrayList<>();

        // Consulta SQL que busca conocidos en ambas direcciones
        String query = "SELECT conoce FROM conocidos WHERE nombre = ? UNION SELECT nombre FROM conocidos WHERE conoce = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, nombre);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                conocidos.add(rs.getString(1));
            }
        }

        return conocidos;
    }

    // Cerrar la conexión a la base de datos
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
