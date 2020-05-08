package com.autogarcon.android.API;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

public class Restaurant implements Serializable {
    private int restaurantID;
    private String restaurantName;
    private String description;
    private List<Menu> menuList;

    public Restaurant(int restaurantID, String restaurantName, String description) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.description = description;
        this.menuList = new ArrayList<>();
    }

    public Restaurant() {
        this.menuList = new ArrayList<>();
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
