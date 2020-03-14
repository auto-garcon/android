package com.autogarcon.android;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that holds all user and order data for a user's session on the application. This
 * object is used as a way to pass a large amount of useful information through activities
 * on the application and update any necessary state based off of user interaction.
 * @author Mitchell Nelson
 */
public class ActiveSession implements Serializable {

    private static final ActiveSession ourInstance = new ActiveSession();

    private GoogleSignInAccount googleSignInAccount;
    private String currentRestaurant;
    private int tableNumber;
    private ArrayList<OrderItem> orderItems;

    /**
     * @return ActiveSession Singleton instance
     * @author Mitchell Nelson
     */
    public static ActiveSession getInstance(){return ourInstance;}

    /**
     * Constructor for ActiveSession object
     * @author Mitchell Nelson
     */
    public ActiveSession(){
        this.orderItems = new ArrayList<>();
    }

    /**
     * Getter method for GoogleSignInAccount information
     * @return GoogleSignInAccount for user signin data
     * @author Mitchell Nelson
     */
    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    /**
     * Setter Method for GoogleSignInAccount
     * @param googleSignInAccount new GoogleSigninAccount - should be added on successful sign in
     * @author Mitchell Nelson
     */
    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount){this.googleSignInAccount = googleSignInAccount;}

    /**
     * Getter method for the restaurant name
     * @return String representing the restaurant name
     * @author Mitchell Nelson
     */
    public String getCurrentRestaurant() {
        return currentRestaurant;
    }

    /**
     * Setter method for the name of the restauraunt that the customer is current at
     * @param restaurantName New restrauant name to update the object with
     * @author Mitchell Nelson
     */
    public void setCurrentRestaurant(String restaurantName){
        currentRestaurant = restaurantName;
    }

    /**
     * Getter method for the customer's table number
     * @return int representing the customer's table number
     * @author Mitchell Nelson
     */
    public int getTableNumber(){
        return tableNumber;
    }

    /**
     * Setter method for the customer's table number
     * @param newTableNumber new int representing the customer's table number
     * @author Mitchell Nelson
     */
    public void setTableNumber(int newTableNumber){
        tableNumber = newTableNumber;
    }

    /**
     * Adds an Order object to the current session
     * @param orderItem new order to add to the orders list
     * @author Mitchell Nelson
     */
    public void addOrder(OrderItem orderItem){
        orderItems.add(orderItem);
    }

    /**
     * Returns an ArrayList of all known Orders
     * @return ArrayList of all Orders
     * @Mitchell Nelson
     */
    public ArrayList<OrderItem> getAllOrders(){
        return orderItems;
    }

    /**
     * Performs an HTTP request to the server to get an updated status of all orders
     * that have been submitted by the user
     * @author Mitchell Nelson
     */
    public void refreshOrderStatuses(){
        //HTTP Request to Server to get an update on the statuses of all orders
    }

    /**
     * Formats orders into JSON object and sends a POST request to the server will all
     * current order. Clears the orders array so that the server is the source of
     * truth, rather than the user's phone
     * @author Mitchell Nelson
     */
    public void summitOrders(){
        //Todo - Convert Orders into JSON
        //Todo - POST Request
        orderItems.clear();
    }
}
