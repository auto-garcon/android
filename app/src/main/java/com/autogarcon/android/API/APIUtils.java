package com.autogarcon.android.API;

import android.util.ArrayMap;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
                if(timeRange.startTime > timeRange.endTime) {
                    if(currentTime > timeRange.startTime || currentTime < timeRange.endTime) {
                        active = true;
                    }
                }
                // If the time range is normal.
                else {
                    if(currentTime >= timeRange.startTime && currentTime <= timeRange.endTime) {
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
}
