package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<MenuItem> menuList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            Menu drinksMenu = createMenu(readMenuToString("drinks"));
            Menu dinnerMenu = createMenu(readMenuToString("dinner"));
            Menu specialsMenu = createMenu(readMenuToString("specials"));
            Log.d("CREATION", "MENU: " + dinnerMenu.toString());

            ArrayList<Menu> menuList = new ArrayList<>();
            menuList.add(dinnerMenu);
            menuList.add(drinksMenu);
            menuList.add(specialsMenu);

            Intent intent = new Intent(getApplicationContext(), MenuListActivity.class);
            intent.putExtra("menuList", menuList);
            startActivity(intent);
        }
        catch (JSONException e) {
            Log.d("CREATION", "JSONException: " + e.toString());
        }
        catch (IOException ioe) {
            Log.d("FILE", "Could not read file");
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
                menuList.add(newItem);
                Log.d("items", "newItem: " + newItem.toString());

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

    /**
     * Takes in the name of a menu type and reads in the file from the 'assets/menu' directory.
     * @param menuType The type of the menu, as a string.
     * @return The contents of the file.
     * @throws IOException If no file such as 'assets;menu/(menuType).json' exists.
     */
    private String readMenuToString(String menuType) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream is = getApplicationContext().getAssets().open("menu/"+menuType+".json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));

        String str;
        while((str = reader.readLine()) != null) {
            sb.append(str);
        }

        return sb.toString();
    }
}