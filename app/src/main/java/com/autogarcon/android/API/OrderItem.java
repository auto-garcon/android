package com.autogarcon.android.API;

/**
 * A class representation of a single OrderItem JSON object that will be packaged into an Order
 * @author Tim Callies
 */
public class OrderItem {
    private int orderItemID;
    private int menuItemID;
    private int menuID;
    private int quantity;
    private String comments;
    private int orderID;
    private float price;
    private String orderTime;
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public OrderItem(MenuItem menuItem, String comments) {
        setMenuItemID(menuItem.getItemID());
        setMenuID(menuItem.getMenuID());
        setQuantity(1);
        setComments(comments);
        setPrice(menuItem.getPrice());
        setItemName(menuItem.getName());
    }

    public int getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
