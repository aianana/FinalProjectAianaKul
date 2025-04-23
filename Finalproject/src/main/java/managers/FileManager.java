package managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Customer;
import models.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String CUSTOMER_FILE = "data/customers.json";
    private static final String ORDER_FILE = "data/orders.json";

    private static final ObjectMapper mapper = new ObjectMapper();


    public static List<Customer> loadCustomers() {
        try {
            File file = new File(CUSTOMER_FILE);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Customer>>() {});
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveCustomers(List<Customer> customers) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CUSTOMER_FILE), customers);
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }


    public static List<Order> loadOrders() {
        try {
            File file = new File(ORDER_FILE);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Order>>() {});
        } catch (IOException e) {
            System.out.println("Error loading orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveOrders(List<Order> orders) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(ORDER_FILE), orders);
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    public static void clearCustomers() {
        try {
            File file = new File(CUSTOMER_FILE);
            if (file.exists()) {
                file.delete();
            }

            new File(CUSTOMER_FILE).createNewFile();
            System.out.println("Customer information has been cleared.");
        } catch (IOException e) {
            System.out.println("Error while deleting customer data: " + e.getMessage());
        }
    }


    public static void clearOrders() {
        try {
            File file = new File(ORDER_FILE);
            if (file.exists()) {
                file.delete();
            }

            new File(ORDER_FILE).createNewFile();
            System.out.println("Order information has been cleared.");
        } catch (IOException e) {
            System.out.println("Error while deleting order data: " + e.getMessage());
        }
    }

    public static void exportCustomersToJson(List<Customer> customers, String filePath) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), customers);
            System.out.println("✅ Customers exported to: " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Error exporting customers: " + e.getMessage());
        }
    }
    public static void exportOrdersToJson(List<Order> orders, String filePath) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), orders);
            System.out.println("✅ Orders exported to: " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Error exporting orders: " + e.getMessage());
        }
    }

    public static List<Customer> importCustomersFromJson(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("❌ File not found: " + filePath);
                return new ArrayList<>();
            }
            List<Customer> importedCustomers = mapper.readValue(file, new TypeReference<List<Customer>>() {});
            System.out.println("✅ Customers imported from: " + filePath);
            return importedCustomers;
        } catch (IOException e) {
            System.err.println("❌ Error importing customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<Order> importOrdersFromJson(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("❌ File not found: " + filePath);
                return new ArrayList<>();
            }
            List<Order> importedOrders = mapper.readValue(file, new TypeReference<List<Order>>() {});
            System.out.println("✅ Orders imported from: " + filePath);
            return importedOrders;
        } catch (IOException e) {
            System.err.println("❌ Error importing orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
