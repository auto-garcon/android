package com.autogarcon.android.API;

public class OrderItem {
    private MenuItem menuItem;
    private OrderStatus orderStatus;
    private String chefNote;

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getChefNote() {
        return chefNote;
    }

    public void setChefNote(String chefNote) {
        this.chefNote = chefNote;
    }

    public enum OrderStatus {
        INCOMPLETE, SUBMITTED, COMPLETE;
    }

}
