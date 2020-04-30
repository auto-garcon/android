package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity for the landing page that users see after logging in.
 * Provides a link to the barcode scanner and simple directions on using it.
 * @Author Mitchell Nelson, Riley Tschumper, Tim Callies
 */
public class LandingPageActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    TextView welcomeName;
    TextView welcome;
    Button randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        imageView = findViewById(R.id.scanner);
        textView = findViewById(R.id.directions);
        welcome = findViewById(R.id.welcome);
        randomize = findViewById(R.id.randomize);
        welcomeName = findViewById(R.id.welcomeName);

        welcomeName.append(ActiveSession.getInstance().getGoogleSignInAccount().getGivenName());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        // QR code bypass for Android device emulator
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMenus("5","5");
            }
        });
        // Starts the random restaurant activity when clicked
        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PopActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * createMenu: This method takes in already parsed JSONObject of a single menu
     * @param obj JSONObject representation of a menu
     * @return resulting Menu object
     * @throws JSONException
     * @author Mitchell Nelson, Riley Tschumper
     */
    public Menu createMenu(JSONObject obj) throws JSONException{
        Menu menu = new Menu(obj.getString("menuName"));
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
     *
     * @param jsonObject JSON representation of a single MenuItem object
     * @return resulting MenuItem
     * @throws JSONException
     * @author Mitchell Nelson
     */
    private MenuItem getMenuItemFromJSONObject(JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        double price = jsonObject.getDouble("price");

        // HARDCODED WHILE API IS UPDATED
        int calories = jsonObject.getInt("calories");
        // HARDCODED WHILE API IS UPDATED
        String imagePath = "https://d1doqjmisr497k.cloudfront.net/-/media/mccormick-us/recipes/grill-mates/c/800/cowboy-burger-with-grilled-pickles-and-crispy-onion-straws.jpg"; //jsonObject.getString("imagePath");

        String category = jsonObject.getString("category");

        ArrayList<DietaryTags> dietaryTags = new ArrayList<>();
        JSONArray tags = jsonObject.getJSONArray("allergens");

        // Loops through all allergens in the array and adds any that are present
        for(int i = 0; i < tags.length(); i++){
            if(tags.get(i).equals("MEAT")){
                dietaryTags.add(DietaryTags.MEAT);
            }
            if(tags.get(i).equals("DAIRY")){
                dietaryTags.add(DietaryTags.DAIRY);
            }
            if(tags.get(i).equals("NUTS")){
                dietaryTags.add(DietaryTags.NUTS);
            }
            if(tags.get(i).equals("GLUTEN")){
                dietaryTags.add(DietaryTags.GLUTEN);
            }
            if(tags.get(i).equals("SOY")){
                dietaryTags.add(DietaryTags.SOY);
            }
        }

        MenuItem newItem = new MenuItem(name,description,price,calories,imagePath,category,dietaryTags);
        return newItem;
    }

    /**
     * requestMenus: This method takes in a JSONObject that represents a single MenuItem
     *                            and outputs the corresponding MenuItem object
     *
     * @param restaurantId A string of the restaurant ID parsed from the QR code URL
     * @param tableId A string of the table ID parsed from the QR code URL
     * @author Mitchell Nelson, Riley Tschumper
     */
    private void requestMenus(final String restaurantId, final String tableId){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiURL = getResources().getString(R.string.api) + "restaurant/" + restaurantId + "/menu";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray menuJSON = new JSONArray(response);
                            ArrayList<Menu> menuList = new ArrayList<>();

                            // gets the current time in military time HHMM format
                            SimpleDateFormat sdf = new SimpleDateFormat("HHMM");
                            String dateString = sdf.format(new Date());
                            int dateInt = Integer.parseInt(dateString);

                            for (int i = 0; i < menuJSON.length(); i++){

                                // Finds if a menu is to be shown during the current time
                                JSONArray timeRangeArray = menuJSON.getJSONObject(i).getJSONArray("timeRanges");
                                boolean validTime = false;
                                for(int j = 0; j < timeRangeArray.length(); j++){
                                    int startTime = timeRangeArray.getJSONObject(j).getInt("startTime");
                                    int endTime = timeRangeArray.getJSONObject(j).getInt("endTime");
                                    validTime |= startTime < dateInt && dateInt < endTime;
                                }

                                // Given an active menu that is valid during this time, add it to our current menu list to be displayed
                                if(menuJSON.getJSONObject(i).getString("status").equals("ACTIVE") && validTime) {
                                    menuList.add(createMenu(menuJSON.getJSONObject(i)));
                                }
                            }

                            // Passes menuList to the next activity to be displayed on screen
                            Intent intent = new Intent(getApplicationContext(), TopActivity.class);
                            intent.putExtra("menuList", menuList);
                            intent.putExtra("title", restaurantId + " - Table " + tableId);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEYERROR" ,"That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Processes the result of the barcode scanner. Once a result of a restaurant and table number are given,
     * a query is made to get menus for that restaurant and and starts an intent for TopActivity.
     * @param requestCode channel for intent items
     * @param resultCode channel for intent results
     * @param data the intent
     * @author Mitchell Nelson, Tim Callies, Riley Tschumper
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2) {
            String url = data.getStringExtra("URL");

            // Validates the pattern for the QR code URL
            // Valid restaurantId is all lowercase letters and numbers
            // Valid tableId is all lowercase letters and numbers
            Pattern pattern = Pattern.compile("^https:\\/\\/autogarcon.live\\/download\\?restaurantId=([a-z0-9]+)&tableId=([a-z0-9]+)$");
            Matcher matcher = pattern.matcher(url);

            // The URL is in the proper format
            if(matcher.matches()) {
                // parses out the restaurantId and tableId from the QR code URL
                final String restaurantId = matcher.group(1);
                final String tableId = matcher.group(2);

                requestMenus(restaurantId, tableId);
            }
            // The URL was not correct
            else {
                Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Creates top bar menu objects - link to user profile settings menu
     * @param menu menu item for top bar
     * @return success of menu creation
     * @author Mitchell Nelson
     */
    @Override
    public boolean onCreateOptionsMenu(final android.view.Menu menu){
        final Uri uri = ActiveSession.getInstance().getGoogleSignInAccount().getPhotoUrl();
        if (uri != null) {
            // Get Google profile pic from url in new thread
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Retrieve URL
                        final Bitmap image = BitmapFactory.decodeStream(new URL(uri.toString()).openConnection().getInputStream());
                        // Update UI on main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getMenuInflater().inflate(R.menu.user_profile, menu);
                                android.view.MenuItem user_profile = menu.findItem(R.id.user_profile);
                                user_profile.setIcon(new BitmapDrawable(getResources(), image));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        else{
            getMenuInflater().inflate(R.menu.user_profile, menu);
        }
        return true;
    }


    /**
     * Opens the user Options activity when user profile icon menu is clicked
     * @param item menu item that is clicked - user profile
     * @return success of intent
     * @author Mitchell Nelson
     */
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.user_profile) {
            // Open the options menu
            Intent intent = new Intent(LandingPageActivity.this, UserOptionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        return super.onOptionsItemSelected(item);
    }
}
