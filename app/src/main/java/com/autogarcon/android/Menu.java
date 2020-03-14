package com.autogarcon.android;

import android.util.Log;

import java.io.Serializable;
import java.util.*;

/**
 * A Menu consists of Categories and MenuItems
 * @author Mitchell Nelson
 */
public class Menu implements Serializable {

    public enum MenuType {
        DRINKS, BREAKFAST, BRUNCH, LUNCH, DINNER, SPECIALS, DESSERT;
    }

    private MenuType menuType;

    private ArrayList<Category> categories;


    /**
     * Menu:   Constructor for Menu class. This class represents and entire menu that can take the form of the specified
     *         types. A menu is made up of a list of Categories, each with a list of MenuItems.
     * Author: Mitchell Nelson
     *
     * @param menuType menuType for constructor
     */
    public Menu(MenuType menuType){
        this.menuType = menuType;
        this.categories = new ArrayList<>();
    }

    /**
     * getAllMenuItems: returns an ArrayList of all menuItems
     * Author:          Mitchell Nelson
     *
     * @return menuItems ArrayList
     */
    public ArrayList<MenuItem> getAllMenuItems(){
        ArrayList<MenuItem> allItems = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++){
            ArrayList<MenuItem> categoryItems = categories.get(i).getMenuItems();
            allItems.addAll(categoryItems);
        }
        return allItems;
    }

    /**
     * addMenuItem: adds a menuItem to the menu. New category is created if one doesn't exist yet
     * Author:      Mitchell Nelson
     *
     * @param newItem menuItem to add
     */
    public void addMenuItem(MenuItem newItem){
        // Check if this menu has category based on name
        if (!hasCategory(newItem.getCategoryName())){
            // Make new category in menu if one doesn't yet exist
            Category newCategory = new Category(newItem.getCategoryName());
            addCategory(newCategory);
        }
        // Add newItem to category
        Category existingCategory = this.getCategoryFromName(newItem.getCategoryName());
        existingCategory.addFoodItem(newItem);
    }

    /**
     * getCategoryFromName: returns a Category corresponding to input String
     * Author:              Mitchell Nelson
     *
     * @param categoryName String representation of a category
     * @return             Category corresponding to input string. Null otherwise
     */
    public Category getCategoryFromName(String categoryName){
        for (int i = 0; i < categories.size(); i++){
            if (categories.get(i).getName().equals(categoryName)){
                return categories.get(i);
            }
        }
        return null;
    }

    /**
     * hasCategory: Returns true if a category exists that matches the input String. False otherwise
     * author:      Mitchell Nelson
     *
     * @param categoryName String representation of a category
     * @return             corresponding boolean value
     */
    private boolean hasCategory(String categoryName){
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(categoryName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param category category to add
     */
    private void addCategory(Category category){
        categories.add(category);
    }

    /**
     * @return categories ArrayList
     */
    public ArrayList<Category> getCategories(){
        return categories;
    }

    /**
     * @return menuType
     */
    public MenuType getMenuType(){
        return menuType;
    }

    /**
     * toString: creates a human-readable representation of the full menu with all categories and corresponding menu items
     * Author:   Mitchell Nelson
     *
     * @return formatted string
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("menuType: " + getMenuType().toString() + "\n");
        str.append("menuItems:\n");
        for (int i = 0; i < getCategories().size(); i++){
            str.append(getCategories().get(i).toString() + "\n");
        }
        return str.toString();
    }
}