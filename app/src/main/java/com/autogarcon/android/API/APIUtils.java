package com.autogarcon.android.API;

import android.util.Log;

import com.autogarcon.android.ActiveSession;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A collection of methods to be used with the API classes, in order to keep their functionality
 * minimal.
 * @author Tim Callies
 */
public class APIUtils {

    /**
     * Calculates the categories from a Menu object.
     * @param menu Some menu that you want to get categories from.
     * @author Tim Callies
     * @return A 'map' of the categories, using their name as the key, and a list of the contained
     *             menuItems as the value.
     */
    public static List<Map.Entry<String, List<MenuItem>>> getCategories(Menu menu){
        List<Map.Entry<String, List<MenuItem>>> output = new ArrayList<>();
        for (MenuItem menuItem : menu.getMenuItems()) {
            List<MenuItem> category = null;

            // Find the category from the list
            for (Map.Entry<String, List<MenuItem>> listCategory : output) {
                if(listCategory.getKey().equals(menuItem.getCategory())) {
                    category = listCategory.getValue();
                }
            }

            // Add a new category if it is not present
            if(category == null) {
                Map.Entry<String, List<MenuItem>> newCategory =
                        new AbstractMap.SimpleEntry<String, List<MenuItem>>(menuItem.getCategory(),
                                new ArrayList<MenuItem>());
                category = newCategory.getValue();
                output.add(newCategory);
            }

            category.add(menuItem);
        }

        return output;
    }



    /**
     * Looks through a list of menus, and returns only the ones that are active, based on the
     * current time
     * @author Tim Callies
     * @param menuList A list of menus.
     * @return The current active menus
     */
    public static List<Menu> getActiveMenus(List<Menu> menuList) {
        List<Menu> output = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHMM");
        for (Menu menu : menuList) {
            boolean active = false;
            // Checks if any of the time ranges for the menu are active
            for (Menu.TimeRange timeRange : menu.getTimeRanges()) {
                int currentTime = Integer.parseInt(dateFormat.format(new Date()));
                // If the time range goes through midnight
                if(timeRange.getStartTime() > timeRange.getEndTime()) {
                    if(currentTime > timeRange.getStartTime() || currentTime < timeRange.getEndTime()) {
                        active = true;
                    }
                }
                // If the time range is normal.
                else {
                    if(currentTime >= timeRange.getStartTime() && currentTime <= timeRange.getEndTime()) {
                        active = true;
                    }
                }
            }
            if(active) {
                output.add(menu);
            }
        }
        return output;
    }

    /**
     * Parse through a list of favorites received by the API to match with current user
     * and current restaurant
     * @author Riley Tschumper
     * @param favoritesList A list of all the favorites for all users.
     * @return if the current restaurant is in the user's favorites
     */
    public static boolean currentlyFavorite(List<Favorites> favoritesList) {
        for (Favorites fav : favoritesList) {
            if (fav.getRestaurantID() == ActiveSession.getInstance().getRestaurant().getRestaurantID()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the menuItem for a given orderItem
     * @param orderItem The orderItem that you want to find a menuItem for
     * @return The menuItem that is being ordered.
     */
    public static MenuItem getMenuItem(OrderItem orderItem) {
        for (Menu menu : ActiveSession.getInstance().getRestaurant().getMenus()) {
            for (MenuItem menuItem : menu.getMenuItems()) {
                if(menuItem.getItemID() == orderItem.getMenuItemID()) {
                    return menuItem;
                }
            }
        }

        // If the menuItem is not found, remove this item.
        ActiveSession.getInstance().removeOrderItem(orderItem);
        return new MenuItem();
    }

    /**
     * Generates a more useful list of restaurants and menus based on the input we recieve from
     * the FavoriteMenu
     * @author Tim Callies
     * @param favoriteMenus The list that is recieved from /user/:userID/favorites/
     * @return A list of Restaurants that are only populated with the data that can be inferred
     * from FavoriteMenu.
     */
    public static List<Restaurant> getRestuarantsFromFavoritesList(List<FavoriteMenu> favoriteMenus) {
        List<Restaurant> restaurants = new ArrayList<>();


        for (FavoriteMenu favoriteMenu : favoriteMenus) {
            Restaurant restaurant = null;
            // Try to find the restauarant.
            for (Restaurant r : restaurants) {
                if(r.getRestaurantID() == favoriteMenu.getRestaurantID()) {
                    restaurant = r;
                }
            }
            // If it does not exist, add the restaurant.
            if(restaurant == null) {
                restaurant = new Restaurant();
                restaurant.setRestaurantName(favoriteMenu.getRestaurantName());
                restaurant.setRestaurantID(favoriteMenu.getRestaurantID());
                restaurants.add(restaurant);
            }

            // Try to find the menu.
            Menu menu = null;
            for (Menu m : restaurant.getMenus()) {
                if(m.getMenuID() == favoriteMenu.getMenuID()) {
                    menu = m;
                }
            }
            // If it does not exist, add the menu.
            if(menu == null) {
                menu = new Menu();
                menu.setMenuName(favoriteMenu.getMenuName());
                menu.setMenuID(favoriteMenu.getMenuID());
                restaurant.getMenus().add(menu);
            }

            // Add the timeRange
            menu.getTimeRanges().add(new Menu.TimeRange(favoriteMenu.getStartTime(), favoriteMenu.getEndTime()));
        }

        return restaurants;
    }
}
