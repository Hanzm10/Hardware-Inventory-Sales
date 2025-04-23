import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // --- Database Credentials ---
    // WARNING: Hardcoding credentials is not recommended for production environments.
    // Consider using environment variables, configuration files, or secrets management.
    private static final String DB_HOST = "127.0.0.1";
    private static final String DB_PORT = "3306";
    // IMPORTANT: Replace "your_database_name" with the actual name of your database
    private static final String DB_NAME = "murico";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "H@nzmapua12";

    // --- JDBC Connection URL ---
    // Includes parameters for SSL (as indicated in your info) and timezone handling
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                                         "?useSSL=true" + // Enforce SSL as mentioned
                                         "&serverTimezone=UTC"; // Recommended to avoid timezone issues

    // --- Connection Method ---
    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object or null if connection fails.
     */
    public static Connection connect() {
        Connection connection = null;
        try {
            // Optional: Explicitly load the driver (often not needed with JDBC 4.0+)
            // Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Attempting to connect to database: " + DB_URL);
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful!");
                // You could print more connection metadata if needed
                // System.out.println("SSL Cipher: " + connection.getClientInfo("SSL_CIPHER")); // Might require specific setup
            } else {
                System.err.println("Failed to establish database connection.");
            }

        } catch (SQLException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
            // Print the stack trace for detailed debugging
            e.printStackTrace();
        }
        // catch (ClassNotFoundException e) {
        //     System.err.println("MySQL JDBC Driver not found. Make sure it's in the classpath.");
        //     e.printStackTrace();
        // }

        return connection;
    }

    // --- Close Connection Method ---
    /**
     * Closes the given database connection.
     *
     * @param connection The Connection object to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Example Usage (Optional) ---
    public static void main(String[] args) {
        Connection conn = DatabaseManager.connect();

        // --- You can perform database operations here using the 'conn' object ---
        // Example: Create a statement, execute a query, process results...
        // Remember to handle potential SQLExceptions for all DB operations.

        // Ensure the connection is closed when done
        DatabaseManager.closeConnection(conn);
    }
}