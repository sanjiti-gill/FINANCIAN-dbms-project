package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Manages MySQL JDBC connection.
 * Uses Singleton pattern to reuse one connection.
 */
public class DBConnection {

    // ===== CONFIGURE THESE VALUES =====
    private static final String URL      = "jdbc:mysql://localhost:3306/financian";
    private static final String USER     = "root";       // your MySQL username
    private static final String PASSWORD = "Manu@2602"; // your MySQL password
    // ==================================

    private static Connection connection = null;

    // Private constructor - Singleton pattern
    private DBConnection() {}

    /**
     * Returns a single shared Connection instance.
     * Creates a new one if it doesn't exist or was closed.
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Load the MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connected to MySQL: financian_db");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found! Add mysql-connector-java jar to classpath.\n" + e.getMessage());
        }
        return connection;
    }

    /**
     * Close the connection (call at app exit).
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error closing connection: " + e.getMessage());
        }
    }
}
