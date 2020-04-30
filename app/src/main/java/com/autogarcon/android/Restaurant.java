package com.autogarcon.android;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class that contains all of the restaurant info. Mainly used for the RestaurantAdapter class.
 * @Author Tim Callies
 */
public class Restaurant {
    private List<RestaurantMenu> restaurantMenuList;
    private String name;
    private String logoUrl;

    /**
     * Initialize the restaurant with no data
     */
    public Restaurant() {
        this.restaurantMenuList = new ArrayList<>();
    }

    /**
     * Creates a new RestaurantMenu based on the parameters, and adds it to the list of menus
     * @param name The name of the menu
     * @param startTime The time when the menu starts, represented as a string
     * @param endTime The time when the menu ends, represented as a string
     * @return The new RestaurantMenu item that was generated.
     */
    public RestaurantMenu addRestaurantMenu(String name, String startTime, String endTime) {
        RestaurantMenu menu = new RestaurantMenu();
        menu.setName(name);
        menu.setStartTime(startTime);
        menu.setEndTime(endTime);
        restaurantMenuList.add(menu);
        return menu;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public List<RestaurantMenu> getRestaurantHoursList() {
        return restaurantMenuList;
    }

    /**
     * Subclass that contains the info for a single menu in a Restaurant
     * @Author Tim Callies
     */
    public class RestaurantMenu {
        private String name;
        private String startTime;
        private String endTime;

        /**
         * Initializes the class with empty strings.
         */
        public RestaurantMenu() {
            this.name = "";
            this.startTime = "";
            this.endTime = "";
        }

        // Setters
        public void setName(String name) {
            this.name = name;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        //Getters
        public String getName() {
            return name;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }
    }
}
