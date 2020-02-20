package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import org.json.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMP - Replace string with API call once it is finished
        String jsonStr = "{\"menuType\":\"DINNER\",\"timeRange\":[{\"start\":1600,\"stop\":2359}],\"menuItems\":[{\"name\":\"Cheese Curds\",\"description\":\"Lightly breaded and deep fried Wisconsin cheddar\",\"price\":5.75,\"calories\":760,\"imagePath\":\"./\",\"category\":\"appetizer\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Bavarian Pretzels\",\"description\":\"spicy mustard, beer cheese\",\"price\":9.95,\"calories\":800,\"imagePath\":\"./\",\"category\":\"appetizer\",\"allergens\":[\"wheat\"]},{\"name\":\"Sweet Potato Tator Tots\",\"description\":\"chipotle peanut pesto aioli\",\"price\":8.95,\"calories\":720,\"imagePath\":\"./\",\"category\":\"appetizer\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Nachos\",\"description\":\"pulled chicken, jalapeño, aioli, pico de gallo, guacamole, mozzarella\",\"price\":11.95,\"calories\":1100,\"imagePath\":\"./\",\"category\":\"appetizer\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Housemade Beer Cheese Soup\",\"description\":\"Lightly breaded and deep fried Wisconsin cheddar\",\"price\":5.95,\"calories\":510,\"imagePath\":\"./\",\"category\":\"soup\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Chili\",\"description\":\"cheese, onion, saltines\",\"price\":6.95,\"calories\":500,\"imagePath\":\"./\",\"category\":\"soup\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Turkey Burger\",\"description\":\"poblano pesto, peanuts, pepper jack, lettuce\",\"price\":9.95,\"calories\":800,\"imagePath\":\"./\",\"category\":\"Hand-Pattied Burgers\",\"allergens\":[\"wheat\",\"milk, nuts\"]},{\"name\":\"Patty Melt\",\"description\":\"white cheddar, pepper jack, wisconsin cheddar, onion rings, Tap sauce\",\"price\":10.5,\"calories\":1000,\"imagePath\":\"./\",\"category\":\"Hand-Pattied Burgers\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Western Burger\",\"description\":\"crispy onion, bbq sauce, bacon, sharp cheddar\",\"price\":9.95,\"calories\":900,\"imagePath\":\"./\",\"category\":\"Hand-Pattied Burgers\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Veggie Burger\",\"description\":\"vegetarian patty, tomato basil mayo, cheddar, caramelized onions, lettuce, tomato\",\"price\":9.5,\"calories\":710,\"imagePath\":\"./\",\"category\":\"Hand-Pattied Burgers\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"BBQ Pork Sandwich\",\"description\":\"roasted pulled pork, bbq sauce, onion rings, apples, coleslaw\",\"price\":11.95,\"calories\":800,\"imagePath\":\"./\",\"category\":\"Sandwhiches\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"BLT\",\"description\":\"guacamole, fried egg, mayo, ciabatta\",\"price\":11.5,\"calories\":600,\"imagePath\":\"./\",\"category\":\"Sandwhiches\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Grilled Cheese\",\"description\":\"parmesan-crusted thick cut sourdough, american cheese, tomato chutney, avocado\",\"price\":10.95,\"calories\":810,\"imagePath\":\"./\",\"category\":\"Sandwhiches\",\"allergens\":[\"wheat\",\"milk\"]},{\"name\":\"Hot Italian Beef\",\"description\":\"sliced ribeye, pepper jack cheese, giardiniera, hoagie\",\"price\":9.5,\"calories\":710,\"imagePath\":\"./\",\"category\":\"Sandwhiches\",\"allergens\":[\"wheat\",\"milk\"]}]}";
        try {
            Menu dinnerMenu = createMenu(jsonStr);
            Log.d("CREATION", "MENU: " + dinnerMenu.toString());
        }
        catch (JSONException e) {
            Log.d("CREATION", "JSONException: " + e.toString());
        }
    }

    /**
     * createMenu: This method converts Stringified-JSON and creates a corresponding Menu object
     * Author:     Mitchell Nelson
     *
     * @param jsonStr String representation of a Menu in JSON format
     * @return        resulting Menu object
     * @throws JSONException
     */
    public Menu createMenu(String jsonStr) throws JSONException{
        JSONObject obj = new JSONObject(jsonStr);
        Menu.MenuType menuType = Menu.MenuType.valueOf(obj.getString("menuType"));
        Menu menu = new Menu(menuType);
        JSONArray menuItems = obj.getJSONArray("menuItems");
        for (int i = 0; i < menuItems.length(); i++) {
            try {
                MenuItem newItem = getMenuItemFromJSONObject(menuItems.getJSONObject(i));
                menu.addMenuItem(newItem);
            }
            catch (JSONException e) {
                // Handle exception
                Log.d("MENU", "JSONException: " + e.toString());
            }
        }
        return menu;
    }

    /**
     * getMenuItemFromJSONObject: This method takes in a JSONObject that represents a single MenuItem
     *                            and outputs the corresponding MenuItem object
     * Author:                    Mitchell Nelson
     *
     * @param jsonObject JSON representation of a single MenuItem object
     * @return           resulting MenuItem
     * @throws JSONException
     */
    private MenuItem getMenuItemFromJSONObject(JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        double price = jsonObject.getDouble("price");
        int calories = jsonObject.getInt("calories");;
        String imagePath = jsonObject.getString("imagePath");;
        String category = jsonObject.getString("category");
        JSONArray allergensJSONArr = jsonObject.getJSONArray("allergens");
        // Convert JSONArray to ArrayList for allergens
        ArrayList<String> allergens = new ArrayList<>();
        for (int i = 0; i < allergensJSONArr.length(); i++){
            allergens.add(allergensJSONArr.getString(i));
        }
        MenuItem newItem = new MenuItem(name,description,price,calories,imagePath,category,allergens);
        return newItem;
    }
}