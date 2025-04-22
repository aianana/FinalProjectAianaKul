package managers;

import models.Customer;
import models.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public static void generateSummary(List<Customer> customers, List<Order> orders) {
        System.out.println("\n📋 ===== REPORT =====");

        System.out.println("🔢 Total number of customers: " + customers.size());
        System.out.println("📦 Total number of orders: " + orders.size());

        Map<String, Integer> statusCount = new HashMap<>();
        for (Order order : orders) {
            statusCount.put(order.getStatus(),
                    statusCount.getOrDefault(order.getStatus(), 0) + 1);
        }

        System.out.println("📊 Order status frequency:");
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            System.out.println("  - " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("====================\n");
    }
}
