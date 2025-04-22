import managers.*;
import models.Customer;
import models.Order;
import models.User;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DatabaseManager.initDatabase();

        Scanner scanner = new Scanner(System.in);
        CustomerManager customerManager = new CustomerManager();
        OrderManager orderManager = new OrderManager();

        UserManager userManager = new UserManager();
        User currentUser = null;

        while (currentUser == null) {
            System.out.println("\n=== Welcome to Customer Order Tracker ===");
            System.out.println("1. 🔑 Login");
            System.out.println("2. 📝 Register");
            System.out.println("3. 🚪 Exit");
            System.out.print("Choose option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1": {
                    System.out.print("👤 Username: ");
                    String username = scanner.nextLine();
                    System.out.print("🔒 Password: ");
                    String password = scanner.nextLine();

                    currentUser = userManager.authenticate(username, password);
                    if (currentUser == null) {
                        System.out.println("❌ Invalid login. Try again.");
                    } else {
                        System.out.println("✅ Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
                    }
                    break;
                }

                case "2": {
                    System.out.print("👤 New username: ");
                    String newUser = scanner.nextLine();
                    System.out.print("🔑 New password: ");
                    String newPass = scanner.nextLine();
                    System.out.print("🎭 Role (Admin/User): ");
                    String role = scanner.nextLine();

                    if (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("User")) {
                        System.out.println("❌ Invalid role. Use 'Admin' or 'User'.");
                        break;
                    }

                    boolean created = userManager.registerUser(newUser, newPass, role);
                    if (created) {
                        System.out.println("✅ User registered. Now login.");
                    } else {
                        System.out.println("❌ User already exists or error occurred.");
                    }
                    break;
                }

                case "3":
                    System.out.println("👋 Goodbye.");
                    return;

                default:
                    System.out.println("❌ Invalid option.");
            }
        }


        while (true) {
            printMenu(currentUser);
            System.out.print("Choose an action: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    customerManager.addCustomer(scanner);
                    break;
                case "2":
                    customerManager.listCustomers();
                    break;
                case "3":
                    if (!currentUser.getRole().equals("Admin")) {
                        System.out.println("❌ Access denied. Only Admins can delete customers.");
                    } else {
                        customerManager.deleteCustomer(scanner);
                    }
                    break;

                case "4":
                    orderManager.addOrder(scanner, customerManager);
                    break;
                case "5":
                    orderManager.listOrders();
                    break;
                case "6":
                    if (!currentUser.getRole().equals("Admin")) {
                        System.out.println("❌ Access denied. Only Admins can delete orders.");
                    } else {
                        orderManager.deleteOrder(scanner);
                    }
                    break;

                case "7":
                    orderManager.updateOrderStatus(scanner);
                    break;
                case "8":
                    ReportGenerator.generateSummary(
                            customerManager.getAllCustomers(),
                            orderManager.getAllOrders()
                    );
                    break;
                case "9":
                    handleExportData(customerManager, orderManager, scanner);
                    break;
                case "10":
                    handleImportData(customerManager, orderManager, scanner);
                    break;
                case "11":
                    if (!currentUser.getRole().equals("Admin")) {
                        System.out.println("❌ Access denied. Only Admins can clear data.");
                    } else {
                        FileManager.clearCustomers();
                        FileManager.clearOrders();
                    }
                    break;
                case "12":
                    System.out.println("Exiting the program. Goodbye 👋");
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void printMenu(User currentUser) {
        System.out.println("\n===== Customer Order Tracker =====");
        System.out.println("Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        System.out.println("1. ➕ Add customer");
        System.out.println("2. 👤 Show customers");

        if (currentUser.getRole().equals("Admin")) {
            System.out.println("3. ❌ Delete customer");
        }

        System.out.println("4. 🛒 Add order");
        System.out.println("5. 📦 Show orders");

        if (currentUser.getRole().equals("Admin")) {
            System.out.println("6. 🗑️ Delete order");
        }

        System.out.println("7. 🔄 Update order status");
        System.out.println("8. 📋 Generate report");
        System.out.println("9. 💾 Export data (JSON)");
        System.out.println("10. 📤 Import data (JSON)");

        if (currentUser.getRole().equals("Admin")) {
            System.out.println("11. 🔥 Clear all data");
        }

        System.out.println("12. 🚪 Exit");
        System.out.println("==================================");
    }


    private static void handleExportData(CustomerManager customerManager, OrderManager orderManager, Scanner scanner) {
        System.out.print("Enter export directory path (leave empty for default 'export/'): ");
        String exportPath = scanner.nextLine().trim();
        if (exportPath.isEmpty()) {
            exportPath = "export/";
        }

        new File(exportPath).mkdirs();

        FileManager.exportCustomersToJson(customerManager.getAllCustomers(), exportPath + "customers.json");
        FileManager.exportOrdersToJson(orderManager.getAllOrders(), exportPath + "orders.json");
    }

    private static void handleImportData(CustomerManager customerManager, OrderManager orderManager, Scanner scanner) {
        System.out.print("Enter import directory path (leave empty for default 'import/'): ");
        String importPath = scanner.nextLine().trim();
        if (importPath.isEmpty()) {
            importPath = "import/";
        }

        List<Customer> importedCustomers = FileManager.importCustomersFromJson(importPath + "customers.json");
        List<Order> importedOrders = FileManager.importOrdersFromJson(importPath + "orders.json");

        if (!importedCustomers.isEmpty()) {
            customerManager.getAllCustomers().addAll(importedCustomers);
            FileManager.saveCustomers(customerManager.getAllCustomers());
            System.out.println("✅ Successfully imported " + importedCustomers.size() + " customers");
        }

        if (!importedOrders.isEmpty()) {
            orderManager.getAllOrders().addAll(importedOrders);
            FileManager.saveOrders(orderManager.getAllOrders());
            System.out.println("✅ Successfully imported " + importedOrders.size() + " orders");
        }

        if (importedCustomers.isEmpty() && importedOrders.isEmpty()) {
            System.out.println("❌ No data found to import");
        }
    }
}