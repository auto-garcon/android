package com.autogarcon.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.autogarcon.android.API.Allergen;
import com.autogarcon.android.API.MenuItem;
import com.autogarcon.android.API.OrderItem;
import com.autogarcon.android.API.Restaurant;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that holds all user and order data for a user's session on the application. This
 * object is used as a way to pass a large amount of useful information through activities
 * on the application and update any necessary state based off of user interaction.
 * @author Mitchell Nelson
 */
public class ActiveSession implements Serializable {

    private static ActiveSession ourInstance;

    private GoogleSignInAccount googleSignInAccount;
    private int tableNumber;
    private ArrayList<OrderItem> orderItems;
    private String userId;
    private Restaurant restaurant;
    private CustomTheme colorblindTheme;
    private CustomTheme defaultTheme;
    private CustomTheme restaurantTheme;
    private ArrayList<Allergen> allergenPreferences;
    private Context applicationContext;
    private boolean buttonFlag = false;
    private boolean favoritesStarFlag = false;

    /**
     * @return ActiveSession Singleton instance
     * @author Mitchell Nelson
     */
    public static ActiveSession getInstance(){
        if(ourInstance == null) {
            ourInstance = new ActiveSession();
        }
        return ourInstance;
    }

    /**
     * Constructor for ActiveSession object
     * @author Mitchell Nelson
     */
    public ActiveSession(){
        this.orderItems = new ArrayList<>();
        this.restaurant = new Restaurant();
        this.colorblindTheme = new CustomTheme("#D81B60", null, "#1E88E5");
        this.defaultTheme = new CustomTheme();

        applicationContext = MainActivity.getContextOfApplication();
        setPreferredAllergens();
    }

    public boolean getFavoritesStarFlag() {
        return favoritesStarFlag;
    }

    public void setFavoritesStarFlag(boolean favoritesStarFlag) {
        this.favoritesStarFlag = favoritesStarFlag;
    }

    /**
     * Sets the preferredAllergen settings from the shared preferences file
     * @author Riley Tschumper
     */
    public void setPreferredAllergens(){
        // Set default value for all allergens in Shared preferences file
        allergenPreferences = new ArrayList<Allergen>();

        SharedPreferences sharedPref = applicationContext.getSharedPreferences(applicationContext.getString(R.string.preferences),Context.MODE_PRIVATE);
        String defaultValue = "False";
        String meat = sharedPref.getString("Meat", defaultValue);
        if (meat.equals("True")){
            allergenPreferences.add(Allergen.MEAT);
        }
        String dairy = sharedPref.getString("Dairy", defaultValue);
        if (dairy.equals("True")){
            allergenPreferences.add(Allergen.DAIRY);
        }
        String nuts = sharedPref.getString("Nuts", defaultValue);
        if (nuts.equals("True")){
            allergenPreferences.add(Allergen.NUTS);
        }
        String gluten = sharedPref.getString("Gluten", defaultValue);
        if (gluten.equals("True")){
            allergenPreferences.add(Allergen.GLUTEN);
        }
        String soy = sharedPref.getString("Soy", defaultValue);
        if (soy.equals("True")){
            allergenPreferences.add(Allergen.SOY);
        }
    }
    /**
     * Gets the preferredAllergen that were set from the shared preferences file
     * @return Arraylist<DietaryTags> for all set allergens
     * @author Riley Tschumper
     */
    public ArrayList<Allergen> getAllergenPreferences(){
        return allergenPreferences;
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
        this.tableNumber = newTableNumber;
    }

    /**
     * Gets the needed theme. If accessibility mode is enabled, it will return
     * the colorblind theme instead
     * @return The theme that will be applied to an activity.
     * @author Tim Callies
     */
    public CustomTheme getCustomTheme() {
        //TODO: Determine if the user is in colorblind mode.
        if(getColorblindMode()) {
            return colorblindTheme;
        }
        else if (restaurantTheme != null) {
            return restaurantTheme;
        }
        else {
            return defaultTheme;
        }
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
     * @author Mitchell Nelson
     */
    public ArrayList<OrderItem> getAllOrders(){
        return orderItems;
    }

    /**
     * Returns an double of the total price of all orderItems
     * @return int of size of orderItems
     * @author Riley Tschumper
     */
    public int getOrderSize(){
        return orderItems.size();
    }


    /**
     * Getter method for userID
     * @return userID
     * @author Mitchell Nelson
     */
    public String getUserId(){
        return applicationContext.getSharedPreferences(applicationContext.getString(R.string.preferences),Context.MODE_PRIVATE)
                .getString("userID", null);
    }

    /**
     * Gets the colorblind mode preference from the shared preferences
     * @return The colorblind preference
     * @author Tim Callies
     */
    public boolean getColorblindMode(){
        return applicationContext.getSharedPreferences(applicationContext.getString(R.string.preferences),Context.MODE_PRIVATE)
                .getBoolean("colorblindMode", false);
    }

    /**
     * Sets the colorblind shared preference
     * @param colorblindMode True if colorblind mode should be enabled.
     * @author Tim Callies
     */
    public void setColorblindMode(boolean colorblindMode){
        applicationContext.getSharedPreferences(applicationContext.getString(R.string.preferences),Context.MODE_PRIVATE)
                .edit().putBoolean("colorblindMode", colorblindMode).apply();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Attempts to set the restaurant, and will generate the restaurant theme if possible.
     * @param restaurant The new restaurant
     * @author Tim Callies
     */
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        if(this.restaurant.getPrimaryColor() != null) {
            this.restaurantTheme = new CustomTheme(restaurant.getPrimaryColor(),restaurant.getSecondaryColor());
        }
        else {
            this.restaurantTheme = null;
        }
    }

    /**
     * Setter method for userID which is requested from Server upon signin
     * @param userID new userID
     * @author Mitchell Nelson
     */
    public void setUserId(String userID){
        applicationContext.getSharedPreferences(applicationContext.getString(R.string.preferences),Context.MODE_PRIVATE)
                .edit().putString("userID", userID).apply();
    }

    /**
     * Returns a double of the total price of all orderItems
     * @return double of total price
     * @author Riley Tschumper
     */
    public double getTotalPrice(){
        double totalPrice = 0.0;
        if(getOrderSize() != 0){
            for (int i=0; i < getOrderSize(); i++){
                totalPrice += orderItems.get(i).getPrice();
            }

        }
        return totalPrice;
    }

    public boolean getButtonFlag(){
        return buttonFlag;
    }

    public void setButtonFlag(boolean wasClicked){
        buttonFlag = wasClicked;
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
     * Converts the current orderItems into a JSON string
     * @return Stringified JSON version of the current orderItems
     * @author Mitchell Nelson
     */
    public JSONObject getOrdersJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tableID", tableNumber);
            JSONArray orders = new JSONArray();
            for (int i = 0; i < orderItems.size(); i++) {
                JSONObject order = new JSONObject();
                order.put("menuItem", orderItems.get(i).getPrice());
                order.put("chefNote", orderItems.get(i).getComments());
                orders.put(order);
            }
            jsonObject.put("orderItems", orders);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Clears orderItems array
     * @author Mitchell Nelson
     */
    public void clearOrders(){
        orderItems.clear();
    }

    /**
     * Removes a specified OrderItem from orderItems
     * @param orderItem OrderItem to remove from orderItems
     * @author Mitchell Nelson
     */
    public void removeOrderItem(OrderItem orderItem){
        orderItems.remove(orderItem);
    }
}
