package com.autogarcon.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
    private Button userSignout;
    private Button viewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        recyclerView = (RecyclerView) findViewById(R.id.user_options_restaurant_list);
        userOptionsName = (TextView) findViewById(R.id.user_options_name);
        userOptionsImage = (ImageView) findViewById(R.id.user_options_image);
        userSignout = (Button) findViewById(R.id.user_signout);
        viewAccount = (Button) findViewById(R.id.viewAccount);

        userSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        viewAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://myaccount.google.com/"));
                startActivity(intent);
            }
        });

        CheckBox checkBoxMeat = (CheckBox)findViewById(R.id.checkBoxMeat);
        CheckBox checkBoxDairy = (CheckBox)findViewById(R.id.checkBoxDairy);
        CheckBox checkBoxNuts = (CheckBox)findViewById(R.id.checkBoxNuts);
        CheckBox checkBoxGluten = (CheckBox)findViewById(R.id.checkBoxGluten);
        CheckBox checkBoxSoy = (CheckBox)findViewById(R.id.checkBoxSoy);

        // Set allergen checkbox onclick listeners
        checkBoxMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        savePreferences(buttonView, "Meat");
                    }
            }
        );
        checkBoxDairy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    savePreferences(buttonView, "Dairy");
                }
            }
        );
        checkBoxNuts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                     savePreferences(buttonView, "Nuts");
                 }
             }
        );
        checkBoxGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    savePreferences(buttonView, "Gluten");
                }
            }
        );
        checkBoxSoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                      savePreferences(buttonView, "Soy");
                  }
            }
        );

        // Set default value for all allergens in Shared preferences file
        SharedPreferences sharedPref = UserOptionsActivity.this.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "False";
        String meat = sharedPref.getString("Meat", defaultValue);
        if (meat.equals("True")){
            checkBoxMeat.setChecked(true);
        }
        String dairy = sharedPref.getString("Dairy", defaultValue);
        if (dairy.equals("True")){
            checkBoxDairy.setChecked(true);
        }
        String nuts = sharedPref.getString("Nuts", defaultValue);
        if (nuts.equals("True")){
            checkBoxNuts.setChecked(true);
        }
        String gluten = sharedPref.getString("Gluten", defaultValue);
        if (gluten.equals("True")){
            checkBoxGluten.setChecked(true);
        }
        String soy = sharedPref.getString("Soy", defaultValue);
        if (soy.equals("True")){
            checkBoxSoy.setChecked(true);
        }

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
        if (ActiveSession.getInstance().getGoogleSignInAccount().getPhotoUrl() != null){
            ThumbnailManager.getInstance().getImage(ActiveSession.getInstance().getGoogleSignInAccount().getPhotoUrl().toString(), userOptionsImage);
        }
        else{
            ThumbnailManager.getInstance().getImage("https://www.sackettwaconia.com/wp-content/uploads/default-profile.png", userOptionsImage);
        }
    }

    /**
     * Updates Shared Preferences file for allergens
     * @param buttonView checkbox for allergen
     * @param tag allergen name
     * @author Mitchell Nelson
     */
    public void savePreferences(CompoundButton buttonView, String tag) {
        SharedPreferences sharedPref = UserOptionsActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("check", "checked: " + tag);
        if(buttonView.isChecked()){
            editor.putString(tag, "True");
        }
        else{
            editor.putString(tag, "False");
        }
        editor.commit();
    }


    /**
     * Go back to signin activity and provide a "SIGNPUT" signal to that activity
     * @author Mitchell Nelson
     */
    private void signOut() {
        Intent gotoScreenVar = new Intent(UserOptionsActivity.this, Signin.class);
        gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        gotoScreenVar.putExtra("action","SIGNOUT");
        startActivity(gotoScreenVar);
    }
}
