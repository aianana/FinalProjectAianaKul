package models;

public class Order {
    private String orderId;
    private String customerId;
    private String product;
    private int quantity;
    private String status;

    public Order() {

    }
    public Order(String orderId, String customerId, String product, int quantity, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId='" + orderId + '\'' + ", customerId='" + customerId + '\'' + ", product='" + product + '\'' + ", quantity=" + quantity + ", status='" + status + '\'' + '}';
    }
}


