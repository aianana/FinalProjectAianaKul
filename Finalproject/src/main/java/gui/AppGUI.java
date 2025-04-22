package gui;

import managers.CustomerManager;
import managers.OrderManager;
import managers.UserManager;
import models.Customer;
import models.Order;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class AppGUI {
    private final CustomerManager customerManager;
    private final OrderManager orderManager;
    private final User currentUser;

    public AppGUI(User user) {
        this.currentUser = user;
        customerManager = new CustomerManager();
        orderManager = new OrderManager();
        createGUI();
    }

    private void createGUI() {
        JFrame frame = new JFrame("Customer Order Tracker (" + currentUser.getRole() + ")");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("\uD83D\uDC64 Customers", customerTab());
        tabs.add("\uD83D\uDED2 Orders", orderTab());

        frame.add(tabs);
        frame.setVisible(true);
    }

    private JPanel customerTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea output = new JTextArea(10, 40);
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField deleteIdField = new JTextField(15);

        formPanel.add(new JLabel("➕ Add New Customer"));
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JButton addButton = new JButton("Add Customer");
        formPanel.add(addButton);
        formPanel.add(Box.createVerticalStrut(10));

        if (currentUser.getRole().equals("Admin")) {
            formPanel.add(new JLabel("🗑️ Delete Customer"));
            formPanel.add(new JLabel("ID to delete:"));
            formPanel.add(deleteIdField);
            JButton deleteButton = new JButton("Delete Customer");
            formPanel.add(deleteButton);

            deleteButton.addActionListener(e -> {
                String idToDelete = deleteIdField.getText().trim();
                if (idToDelete.isEmpty()) {
                    output.setText("❌ Enter ID to delete.");
                    return;
                }
                customerManager.deleteCustomerFromDB(idToDelete, output);
            });
        }

        JButton listButton = new JButton("\uD83D\uDCC4 Show All Customers");
        formPanel.add(listButton);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                output.setText("❌ All fields are required.");
                return;
            }
            if (!id.matches("\\d{1,10}")) {
                output.setText("❌ ID must be digits only, max 10 chars.");
                return;
            }
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                output.setText("❌ Invalid email format.");
                return;
            }

            Customer customer = new Customer(id, name, email);
            customerManager.addCustomerFromGUI(customer, output);

            idField.setText(""); nameField.setText(""); emailField.setText("");
        });

        listButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Customer c : customerManager.getAllCustomersFromDB()) {
                sb.append(c).append("\n");
            }
            output.setText(sb.toString());
        });

        return mainPanel;
    }

    private JPanel orderTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea output = new JTextArea(10, 40);
        output.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(output);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField customerIdField = new JTextField(15);
        JTextField productField = new JTextField(15);
        JTextField quantityField = new JTextField(15);
        JTextField statusField = new JTextField(15);
        JTextField orderIdField = new JTextField(15);

        formPanel.add(new JLabel("➕ Add New Order"));
        formPanel.add(new JLabel("Customer ID:"));
        formPanel.add(customerIdField);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        JButton addButton = new JButton("Add Order");
        formPanel.add(addButton);
        formPanel.add(Box.createVerticalStrut(10));

        if (currentUser.getRole().equals("Admin")) {
            formPanel.add(new JLabel("🗑️ Delete Order"));
            formPanel.add(new JLabel("Order ID:"));
            formPanel.add(orderIdField);
            JButton deleteOrderBtn = new JButton("Delete Order");
            formPanel.add(deleteOrderBtn);

            deleteOrderBtn.addActionListener(e -> {
                String id = orderIdField.getText().trim();
                if (id.isEmpty()) {
                    output.setText("❌ Enter Order ID to delete.");
                    return;
                }
                orderManager.deleteOrderFromDB(id, output);
            });
        }

        JButton listButton = new JButton("\uD83D\uDCC4 Show All Orders");
        formPanel.add(listButton);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String customerId = customerIdField.getText().trim();
            String product = productField.getText().trim();
            String qtyText = quantityField.getText().trim();
            String status = statusField.getText().trim();

            if (customerId.isEmpty() || product.isEmpty() || qtyText.isEmpty() || status.isEmpty()) {
                output.setText("❌ All fields are required.");
                return;
            }

            if (customerManager.findCustomerByIdInDB(customerId) == null) {
                output.setText("❌ Customer not found in DB.");
                return;
            }

            int qty;
            try {
                qty = Integer.parseInt(qtyText);
            } catch (Exception ex) {
                output.setText("❌ Quantity must be a number.");
                return;
            }

            String orderId = UUID.randomUUID().toString();
            Order order = new Order(orderId, customerId, product, qty, status);
            orderManager.addOrderFromGUI(order, output);

            customerIdField.setText(""); productField.setText("");
            quantityField.setText(""); statusField.setText("");
        });

        listButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Order o : orderManager.getAllOrdersFromDB()) {
                sb.append(o).append("\n");
            }
            output.setText(sb.toString());
        });

        return mainPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager userManager = new UserManager();
            User currentUser = null;

            while (currentUser == null) {
                JTextField userField = new JTextField();
                JPasswordField passField = new JPasswordField();
                Object[] message = {
                        "Username:", userField,
                        "Password:", passField
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String username = userField.getText();
                    String password = new String(passField.getPassword());
                    currentUser = userManager.authenticate(username, password);

                    if (currentUser == null) {
                        JOptionPane.showMessageDialog(null, "❌ Invalid login. Try again.");
                    }
                } else {
                    System.exit(0);
                }
            }

            JOptionPane.showMessageDialog(null, "✅ Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            new AppGUI(currentUser);
        });
    }
}