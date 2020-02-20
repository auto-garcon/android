package com.autogarcon.android;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MenuItem{

    private String name;
    private String description;
    private double price;
    private int calories;
    private String imagePath;
    private String category;
    private ArrayList<String> allergens;

    /**
     * MenuItem: Constructor for MenuItem class. This class represents individual items on a menu
     * Author:   Mitchell Nelson
     *
     * @param name        name of the menuItem
     * @param description description of the menuItem
     * @param price       price of the menuItem
     * @param calories    calories of the menuItem
     * @param imagePath   path to the image for the menuItem
     * @param category    menu category for the menu item
     * @param allergens   list of allergens in the menu item
     */
    public MenuItem(String name, String description, double price, int calories, String imagePath, String category,
                    ArrayList<String> allergens){
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.imagePath = imagePath;
        this.category = category;
        this.allergens = allergens;
    }

    /**
     * @return name String
     */
    public String getName(){
        return name;
    }

    /**
     * @return description string
     */
    public String getDescription(){
        return description;
    }

    /**
     * @return price double
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return calories int
     */
    public int getCalories() {
        return calories;
    }

    /**
     * @return imagePath String
     */
    public String getImagePath(){
        return imagePath;
    }

    /**
     * @return category String
     */
    public String getCategoryName(){
        return category;
    }

    /**
     * @return allergens ArrayList
     */
    public ArrayList<String> getAllergens(){
        return allergens;
    }

    /**
     * toString: creates a human-readable representation of the menu item
     * Author: Mitchell Nelson
     *
     * @return resulting String
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("menuItemName: " + getName() + "\n");
        str.append("\t\tdescription: " + getDescription() + "\n");
        str.append("\t\tprice: " + getPrice() + "\n");
        str.append("\t\tcalories: " + getCalories() + "\n");
        str.append("\t\timagePath: " + getImagePath() + "\n");
        str.append("\t\tcategoryName: " + getCategoryName() + "\n");
        str.append("\t\tallergens:");
        for(int i = 0; i < getAllergens().size(); i++){
            str.append(" " + getAllergens().get(i) + ",");
        }
        str.append("\n");
        return str.toString();
    }
}