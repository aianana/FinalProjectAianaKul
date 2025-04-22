package managers;

import models.Customer;

import java.sql.*;
import java.util.List;
import java.util.Scanner;
import javax.swing.JTextArea;
import java.util.ArrayList;

public class CustomerManager {
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = FileManager.loadCustomers();
    }

    public void addCustomer(Scanner scanner) {
        System.out.print("Enter customer ID (only digits, max 10 characters): ");
        String id = scanner.nextLine();

        if (!id.matches("\\d{1,10}")) {
            System.out.println("Error: ID must contain only digits and be up to 10 characters.");
            return;
        }

        if (findCustomerById(id) != null) {
            System.out.println("Error: Customer with this ID already exists.");
            return;
        }

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("Error: Invalid email format.");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO customers (id, name, email) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.setString(2, name);
                stmt.setString(3, email);
                stmt.executeUpdate();
                System.out.println("✅ Customer successfully added to DB!");
            }
        } catch (SQLException e) {
            System.err.println("❌ DB error: " + e.getMessage());
        }
    }



    public void listCustomers() {
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean found = false;

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                Customer c = new Customer(id, name, email);
                System.out.println(c);
                found = true;
            }

            if (!found) {
                System.out.println("Customer list is empty.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error reading customers: " + e.getMessage());
        }
    }


    public void deleteCustomer(Scanner scanner) {
        System.out.print("Enter customer ID to delete (max 10 characters): ");
        String id = scanner.nextLine();

        if (id.length() > 10) {
            System.out.println("❌ Error: ID is too long.");
            return;
        }

        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("🗑️ Customer deleted.");
            } else {
                System.out.println("⚠️ No customer found with this ID.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error deleting customer: " + e.getMessage());
        }
    }


    public Customer findCustomerById(String id) {
        if (id.length() > 10) return null;

        for (Customer c : customers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }



    public void addCustomerFromGUI(Customer customer, JTextArea output) {
        if (findCustomerByIdInDB(customer.getId()) != null) {
            output.setText("❌ Customer with this ID already exists.");
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO customers (id, name, email) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customer.getId());
                stmt.setString(2, customer.getName());
                stmt.setString(3, customer.getEmail());
                stmt.executeUpdate();
                output.setText("✅ Customer added to DB!");
            }
        } catch (SQLException e) {
            output.setText("❌ Error: " + e.getMessage());
        }
    }

    public Customer findCustomerByIdInDB(String id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ DB Error: " + e.getMessage());
        }
        return null;
    }

    public List<Customer> getAllCustomersFromDB() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ DB Error: " + e.getMessage());
        }
        return customers;
    }
    public void deleteCustomerFromDB(String id, JTextArea output) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                output.setText("🗑️ Customer deleted from DB.");
            } else {
                output.setText("⚠️ Customer not found.");
            }
        } catch (SQLException e) {
            output.setText("❌ Error deleting customer: " + e.getMessage());
        }
    }


}
