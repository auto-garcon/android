package com.autogarcon.android.API;

import java.util.List;

/**
 * A class representation of the Restaurant response JSON object
 * @author Riley Tschumper
 */
public class RestaurantResponse {
    private int numRestaurants;
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public int getNumRestaurants() {
        return numRestaurants;
    }

    public void setNumRestaurants(int numRestaurants) {
        this.numRestaurants = numRestaurants;
    }
}
