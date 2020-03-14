package com.autogarcon.android;

import java.util.ArrayList;

/**
 * Class that holds information for a single menu item that a customer chooses to
 * order. An Order object contains a status, chefNote, and the menuItem.
 * @author Mitchell Nelson
 */
public class OrderItem {

    public enum OrderStatus {
        INCOMPLETE, SUBMITTED, COMPLETE;
    }

    private MenuItem menuItem;
    private OrderStatus orderStatus;
    private String chefNote;

    /**
     * Constructor for an Order object.
     * @param menuItem MenuIem for the Order
     * @param chefNote String to make a note for the chef
     * @author Mitchell Nelson
     */
    public OrderItem(MenuItem menuItem, String chefNote){
        this.menuItem = menuItem;
        this.chefNote = chefNote;
    }

    /**
     * @return MenuItem pertaining to the Order
     * @author Mitchell Nelson
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Getter method for the chef note
     * @return String of the chef note for the Order
     * @author Mitchell Nelson
     */
    public String getChefNote() {
        return chefNote;
    }

    /**
     * Getter method for the order status
     * @return OrderStatus Enum representing the status for the Order
     * @author Mitchell Nelson
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the order status
     * @param orderStatus New OrderStatus Enum representing the status for the Order
     * @author Mitchell Nelson
     */
    public void updateStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }
}
