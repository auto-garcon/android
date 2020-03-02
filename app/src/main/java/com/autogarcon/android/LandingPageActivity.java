package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import android.view.View;

/**
 * Activity for the landing page that users see after logging in.
 * Provides a link to the barcode scanner and simple directions on using it.
 * @Author Mitchell Nelson
 */
public class LandingPageActivity extends AppCompatActivity {

    private List<MenuItem> menuList = new ArrayList<>();

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        imageView = findViewById(R.id.scanner);
        textView = findViewById(R.id.directions);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_scan_barcode);
                Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                startActivityForResult(intent,2);
            }
        });
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


    /**
     * Processes the result of the barcode scanner. Once a result of a restaurant and table number are given,
     * a query is made to get menus for that restaurant and and starts an intent for MenuListActivity.
     * @param requestCode channel for intent items
     * @param resultCode channel for intent results
     * @param data the intent
     * @author Mitchell Nelson
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            String restaurant = data.getStringExtra("RESTAURANT");
            int tableNum = data.getIntExtra("TABLE", 0);

            try {
                Menu drinksMenu = createMenu(readMenuToString("drinks"));
                Menu dinnerMenu = createMenu(readMenuToString("dinner"));
                Menu specialsMenu = createMenu(readMenuToString("specials"));

                ArrayList<Menu> menuList = new ArrayList<>();
                menuList.add(dinnerMenu);
                menuList.add(drinksMenu);
                menuList.add(specialsMenu);

                Intent intent = new Intent(getApplicationContext(), MenuListActivity.class);
                intent.putExtra("menuList", menuList);
                intent.putExtra("title", restaurant + " - Table " + tableNum);
                startActivity(intent);
                finish();
            }
            catch (
                    JSONException e) {
                Log.d("CREATION", "JSONException: " + e.toString());
            }
            catch (
                    IOException ioe) {
                Log.d("FILE", "Could not read file");
            }
        }
    }
}
