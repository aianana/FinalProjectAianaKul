package managers;

import models.Order;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class OrderManager {
    private List<Order> orders;

    public OrderManager() {
        this.orders = FileManager.loadOrders(); // Загружаем из файла
    }


    public void addOrder(Scanner scanner, CustomerManager customerManager) {
        System.out.print("Enter customer ID (max 10 digits): ");
        String customerId = scanner.nextLine();


        if (!customerId.matches("\\d{1,10}")) {
            System.out.println("Error: Customer ID must contain only digits (max 10).");
            return;
        }


        if (!customerExistsInDatabase(customerId)) {
            System.out.println("Error: Customer with this ID does not exist.");
            return;
        }

        System.out.print("Enter product name: ");
        String product = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Error: Quantity must be a positive integer.");
            return;
        }

        System.out.print("Enter status (e.g., Processing, Shipped, Delivered): ");
        String status = scanner.nextLine();

        String orderId = UUID.randomUUID().toString();

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO orders (orderId, customerId, product, quantity, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, orderId);
                stmt.setString(2, customerId);
                stmt.setString(3, product);
                stmt.setInt(4, quantity);
                stmt.setString(5, status);
                stmt.executeUpdate();
                System.out.println("✅ Order successfully added to DB!");
            }
        } catch (SQLException e) {
            System.err.println("❌ DB error: " + e.getMessage());
        }
    }

     public void listOrders() {
        String sql = "SELECT * FROM orders";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean found = false;

            while (rs.next()) {
                String orderId = rs.getString("orderId");
                String customerId = rs.getString("customerId");
                String product = rs.getString("product");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                Order order = new Order(orderId, customerId, product, quantity, status);
                System.out.println(order);
                found = true;
            }

            if (!found) {
                System.out.println("Order list is empty.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error reading orders: " + e.getMessage());
        }
    }

    public void deleteOrder(Scanner scanner) {
        System.out.print("Enter order ID to delete: ");
        String orderId = scanner.nextLine();

        String sql = "DELETE FROM orders WHERE orderId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orderId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("🗑️ Order deleted successfully.");
            } else {
                System.out.println("⚠️ No order found with this ID.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error deleting order: " + e.getMessage());
        }
    }

    public void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();

        System.out.print("Enter new status: ");
        String newStatus = scanner.nextLine();

        String sql = "UPDATE orders SET status = ? WHERE orderId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, orderId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Status updated successfully.");
            } else {
                System.out.println("⚠️ No order found with this ID.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
        }
    }

   private boolean customerExistsInDatabase(String customerId) {
        String sql = "SELECT 1 FROM customers WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Возвращает true, если клиент найден
            }

        } catch (SQLException e) {
            System.err.println("❌ Error checking customer existence: " + e.getMessage());
            return false;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String orderId = rs.getString("orderId");
                String customerId = rs.getString("customerId");
                String product = rs.getString("product");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                orders.add(new Order(orderId, customerId, product, quantity, status));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching orders: " + e.getMessage());
        }

        return orders;
    }

    public void addOrderFromGUI(Order order, JTextArea output) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO orders (orderId, customerId, product, quantity, status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, order.getOrderId());
                stmt.setString(2, order.getCustomerId());
                stmt.setString(3, order.getProduct());
                stmt.setInt(4, order.getQuantity());
                stmt.setString(5, order.getStatus());
                stmt.executeUpdate();
                output.setText("✅ Order added to DB!");
            }
        } catch (SQLException e) {
            output.setText("❌ DB Error: " + e.getMessage());
        }
    }

    public List<Order> getAllOrdersFromDB() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("orderId"),
                        rs.getString("customerId"),
                        rs.getString("product"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ DB Error: " + e.getMessage());
        }
        return orders;
    }

    public void deleteOrderFromDB(String orderId, JTextArea output) {
        String sql = "DELETE FROM orders WHERE orderId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                output.setText("🗑️ Order deleted from DB.");
            } else {
                output.setText("⚠️ Order not found.");
            }
        } catch (SQLException e) {
            output.setText("❌ Error deleting order: " + e.getMessage());
        }
    }

}