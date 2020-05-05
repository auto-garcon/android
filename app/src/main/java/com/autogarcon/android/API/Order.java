package com.autogarcon.android.API;

        import java.io.Serializable;
        import java.util.List;

public class Order implements Serializable {
    private int orderID;
    private int tableID;
    private int customerID;
    private OrderStatus status;
    private float chargeAmount;
    private int restaurantID;
    private int numMenuItems;
    private List<OrderItem> orderItems;

    public enum OrderStatus {
        OPEN, CLOSED
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public float getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(float chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getNumMenuItems() {
        return numMenuItems;
    }

    public void setNumMenuItems(int numMenuItems) {
        this.numMenuItems = numMenuItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
