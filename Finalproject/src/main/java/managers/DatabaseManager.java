package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:data/app.db"; // SQLite файл

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        String createCustomerTable = "CREATE TABLE IF NOT EXISTS customers (" +
                "id TEXT PRIMARY KEY," +
                "name TEXT," +
                "email TEXT)";
        String createOrderTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "orderId TEXT PRIMARY KEY," +
                "customerId TEXT," +
                "product TEXT," +
                "quantity INTEGER," +
                "status TEXT," +
                "FOREIGN KEY(customerId) REFERENCES customers(id))";
        String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL CHECK(role IN ('Admin', 'User')))";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createCustomerTable);
            stmt.execute(createOrderTable);
            stmt.execute(createUserTable);
            System.out.println("✅ Database initialized.");
        } catch (SQLException e) {
            System.err.println("❌ Error initializing DB: " + e.getMessage());
        }
    }
}
