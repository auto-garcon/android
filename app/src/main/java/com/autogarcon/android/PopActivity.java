package com.autogarcon.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.autogarcon.android.API.Restaurant;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autogarcon.android.API.RestaurantResponse;
import com.google.gson.Gson;

import java.util.concurrent.ThreadLocalRandom;

/** Activity for popup the user sees when they shake to randomly select a restaurant
 * @author Kyzr Snapko
 * */
public class PopActivity extends Activity {

    private TextView tv_resturantName;
    private TextView tv_resturantAddress;
    private ImageView iv;
    private Restaurant randomRestaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        tv_resturantName = (TextView) findViewById(R.id.tv_resurantName);
        tv_resturantAddress = (TextView) findViewById(R.id.tv_resurantAddress);
        iv = (ImageView) findViewById(R.id.imageView);

        try {
            requestRestaurant();

            tv_resturantName.setText(randomRestaurant.getRestaurantName());
            iv.setImageResource(R.drawable.ic_restaurant);


        } catch (Exception e) {
            e.printStackTrace();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.8));
    }

    /** A method that requests all restaurants from the server to display a random one
     * @author Kyzr Snapko
     * */
    private void requestRestaurant(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiURL = getResources().getString(R.string.api) + "restaurant";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RestaurantResponse restaurantList = new Gson().fromJson(response, RestaurantResponse.class);

                        int random = ThreadLocalRandom.current().nextInt(restaurantList.getNumRestaurants());
                        randomRestaurant = restaurantList.getRestaurants().get(random);

                        String fullAddress = randomRestaurant.getAddress() + "\n" + randomRestaurant.getCity() +
                                ", " + randomRestaurant.getState() + " " + randomRestaurant.getZipCode();
                        tv_resturantName.setText(randomRestaurant.getRestaurantName());
                        tv_resturantAddress.setText(fullAddress);


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

    protected void onResume(Bundle savedInstanceState) {
        requestRestaurant();

        tv_resturantName.setText(randomRestaurant.getRestaurantName());
    }

}