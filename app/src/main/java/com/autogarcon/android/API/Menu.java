package com.autogarcon.android.API;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

public class Menu implements Serializable {
    private int menuID;
    private List<TimeRange> timeRanges;
    private List<MenuItem> menuItems;
    private MenuStatus status;
    private String menuName;
    private int restaurantID;

    public class TimeRange implements Serializable {
        public int startTime;
        public int endTime;
    }

    public enum MenuStatus {
        DRAFT, ACTIVE
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(List<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public MenuStatus getStatus() {
        return status;
    }

    public void setStatus(MenuStatus status) {
        this.status = status;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }
}
