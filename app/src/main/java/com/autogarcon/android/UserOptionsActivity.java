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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autogarcon.android.API.APIUtils;
import com.autogarcon.android.API.Allergen;
import com.autogarcon.android.API.FavoriteMenu;
import com.autogarcon.android.API.Menu;
import com.autogarcon.android.API.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
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
    private Button addCurrentRestaurant;
    private RequestQueue queue;

    @Override
    protected void onResume() {
        super.onResume();
        // Apply the CustomTheme
        ActiveSession.getInstance().getCustomTheme().applyTo(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        queue = Volley.newRequestQueue(this);

        ArrayList<Allergen> dietaryTagsArrayList = ActiveSession.getInstance().getAllergenPreferences();
        if(dietaryTagsArrayList != null) {
            String sizeAsString = Integer.toString(dietaryTagsArrayList.size());
            Log.d("NUMOFTRUETAGS", sizeAsString);
        }
        else{
            Log.d("NUMOFTRUETAGS", "0");
        }

        recyclerView = (RecyclerView) findViewById(R.id.user_options_restaurant_list);
        userOptionsName = (TextView) findViewById(R.id.user_options_name);
        userOptionsImage = (ImageView) findViewById(R.id.user_options_image);
        userSignout = (Button) findViewById(R.id.user_signout);
        addCurrentRestaurant = (Button) findViewById(R.id.user_options_add_restaurant);
        viewAccount = (Button) findViewById(R.id.viewAccount);

        userSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        addCurrentRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRestaurantAsFavorite();
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
        CheckBox checkBoxColorblind = (CheckBox)findViewById(R.id.user_options_colorblind_box);
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
        checkBoxColorblind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        ActiveSession.getInstance().setColorblindMode(isChecked);
                       ActiveSession.getInstance().getCustomTheme().applyTo(UserOptionsActivity.this);

                   }
               }
        );

        // Set default value for all allergens in Shared preferences file
        SharedPreferences sharedPref = UserOptionsActivity.this.getSharedPreferences(getString(R.string.preferences),Context.MODE_PRIVATE);
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
        checkBoxColorblind.setChecked(ActiveSession.getInstance().getColorblindMode());


        getFavoriteRestaurants();

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
     * Makes a request to add a single restaurant to the favorites
     */
    public void addRestaurantAsFavorite() {
        // Request a string response from the provided URL.
        String apiURL = String.format("https://autogarcon.live/api/users/%s/favorites/restaurant/%d/add",
                ActiveSession.getInstance().getUserId(), ActiveSession.getInstance().getRestaurant().getRestaurantID());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getFavoriteRestaurants();
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
     * Makes a request to retrieve a user's favorite restaurants.
     * @author Tim Callies
     */
    public void getFavoriteRestaurants() {
        String apiURL = String.format("https://autogarcon.live/api/users/%s/favorites",
                ActiveSession.getInstance().getUserId());


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<Restaurant>>(){}.getType();
                        List<Restaurant> restaurants = new Gson().fromJson(response, listType);
                        //List<Restaurant> restaurants = APIUtils.getRestuarantsFromFavoritesList(favoriteMenus);
                        RestaurantAdapter mAdapter = new RestaurantAdapter(restaurants) {
                            @Override
                            public void removeItem(int restauarantID) {
                                String apiURL = String.format("https://autogarcon.live/api/users/%s/favorites/restaurant/%d/remove",
                                        ActiveSession.getInstance().getUserId(), ActiveSession.getInstance().getRestaurant().getRestaurantID());

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                getFavoriteRestaurants();
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
                        };
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);

                        if(ActiveSession.getInstance().getRestaurant() != null) {
                            addCurrentRestaurant.setVisibility(View.VISIBLE);

                            for (Restaurant restaurant : restaurants) {
                                if(restaurant.getRestaurantID() ==  ActiveSession.getInstance().getRestaurant().getRestaurantID()) {
                                    addCurrentRestaurant.setVisibility(View.GONE);
                                }
                            }
                        }
                        else {
                            addCurrentRestaurant.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(null);

                if(ActiveSession.getInstance().getRestaurant() != null) {
                    addCurrentRestaurant.setVisibility(View.VISIBLE);
                }
                else {
                    addCurrentRestaurant.setVisibility(View.GONE);
                }
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Updates Shared Preferences file for allergens
     * @param buttonView checkbox for allergen
     * @param tag allergen name
     * @author Mitchell Nelson
     */
    public void savePreferences(CompoundButton buttonView, String tag) {
        SharedPreferences sharedPref = UserOptionsActivity.this.getSharedPreferences(getString(R.string.preferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("check", "checked: " + tag);
        if(buttonView.isChecked()){
            editor.putString(tag, "True");
        }
        else{
            editor.putString(tag, "False");
        }
        editor.commit();
        ActiveSession.getInstance().setPreferredAllergens();
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
