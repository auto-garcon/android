package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autogarcon.android.API.NewOrderRequest;
import com.autogarcon.android.API.Restaurant;
import com.google.gson.Gson;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Activity for the landing page that users see after logging in.
 * Provides a link to the barcode scanner and simple directions on using it.
 * @Author Mitchell Nelson, Riley Tschumper, Tim Callies
 */
public class LandingPageActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private TextView welcomeName;
    private TextView welcome;
    private Button randomize;

    @Override
    protected void onResume() {
        super.onResume();
        // Clear the restaurant
        ActiveSession.getInstance().setRestaurant(new Restaurant());
        ActiveSession.getInstance().clearOrders();

        // Apply the CustomTheme
        ActiveSession.getInstance().getCustomTheme().applyTo(this);
    }

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
                //ActiveSession.getInstance().setCurrentRestaurantId("5");
                requestMenus("5","1");
                initOrder("5","1");
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
     * requestMenus: This method takes in a JSONObject that represents a single MenuItem
     *                            and outputs the corresponding MenuItem object
     *
     * @param restaurantId A string of the restaurant ID parsed from the QR code URL
     * @param tableId A string of the table ID parsed from the QR code URL
     * @author Mitchell Nelson, Riley Tschumper, Tim Callies
     */
    private void requestMenus(final String restaurantId, final String tableId){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiURL = getResources().getString(R.string.api) + "restaurant/" + restaurantId + "/withmenus";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Restaurant restaurant = new Gson().fromJson(response, Restaurant.class);

                        ActiveSession.getInstance().setRestaurant(restaurant);

                        // Passes menuList to the next activity to be displayed on screen
                        Intent intent = new Intent(getApplicationContext(), TopActivity.class);
                        intent.putExtra("menuList", (Serializable) restaurant.getMenus());
                        intent.putExtra("title", restaurant.getRestaurantName());
                        startActivity(intent);

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
     * initOrder performs an HTTP POST request to the API server to setup a new order
     *
     * @param restaurantId current restaruant that the user is at
     * @param tableId current table number that the user is at
     * @author Mitchell Nelson
     */
    private void initOrder(final String restaurantId, final String tableId){
        NewOrderRequest request = new NewOrderRequest();
        final String jsonBody = new Gson().toJson(request);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiURL = getResources().getString(R.string.api) + "restaurant/" + restaurantId + "/tables/" + tableId + "/order/new";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Set the userId from server
                        Log.d("order", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("signin","Request Failure");
                    }
                })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("order", "json = " + jsonBody);
                return jsonBody.getBytes();
            }
        };
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
                //ActiveSession.getInstance().setCurrentRestaurantId(restaurantId);
                final String tableId = matcher.group(2);
                ActiveSession.getInstance().setTableNumber(Integer.parseInt(tableId));
                requestMenus(restaurantId, tableId);
                initOrder(restaurantId, tableId);
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