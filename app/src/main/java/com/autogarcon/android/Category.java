package com.autogarcon.android;

import java.io.Serializable;
import java.util.*;

public class Category implements Serializable {

    private ArrayList<MenuItem> menuItems;
    private String name;

    public Category(String name){
        this.name = name;
        menuItems = new ArrayList<MenuItem>();
    }

    /**
     * @param menuItem the item to add
     */
    public void addFoodItem(MenuItem menuItem){
        menuItems.add(menuItem);
    }

    /**
     * @return the menuItems ArrayList
     */
    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
    }

    /**
     * @return the Category name
     */
    public String getName(){
        return name;
    }

    /**
     * @return human-readable representation of the category with all menu items
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("categoryName: " + this.getName() + "\n");
        for (int i = 0; i < getMenuItems().size(); i++){
            str.append("\t" + getMenuItems().get(i).toString() + "\n");
        }
        return str.toString();
    }
}