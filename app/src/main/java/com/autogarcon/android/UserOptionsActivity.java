package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to show user options, along with a list.
 * @Author Tim Callies
 */
public class UserOptionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView userOptionsName;
    private ImageView userOptionsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        recyclerView = (RecyclerView) findViewById(R.id.user_options_restaurant_list);
        userOptionsName = (TextView) findViewById(R.id.user_options_name);
        userOptionsImage = (ImageView) findViewById(R.id.user_options_image);

        // Dummy data
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Blue Door Pub");
        restaurant1.setLogoUrl("https://www.sporcle.com/blog/wp-content/uploads/2018/08/1-36.jpg");
        restaurant1.addRestaurantMenu("Lunch", "10:00AM", "4:00PM");
        restaurant1.addRestaurantMenu("Dinner", "3:00PM", "8:00PM");
        restaurantList.add(restaurant1);
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("McDonalds");
        restaurant2.setLogoUrl("https://1000logos.net/wp-content/uploads/2017/03/McDonalds-Logo.png");
        restaurant2.addRestaurantMenu("Breakfast", "7:00AM", "11:00AM");
        restaurant2.addRestaurantMenu("Lunch", "11:00AM", "3:30PM");
        restaurant2.addRestaurantMenu("Dinner", "3:00PM", "8:00PM");
        restaurantList.add(restaurant2);

        // Prepare the restaurant adapter
        RestaurantAdapter mAdapter = new RestaurantAdapter(restaurantList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Fill in the user data
        userOptionsName.setText(ActiveSession.getInstance().getGoogleSignInAccount().getDisplayName());
        ThumbnailManager.getInstance().getImage(ActiveSession.getInstance().getGoogleSignInAccount().getPhotoUrl().toString(), userOptionsImage);
    }
}
