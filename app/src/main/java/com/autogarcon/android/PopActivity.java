package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import java.util.Random;
/** Activity for popup the user sees when they shake to randomly select a restaurant
 * @author Kyzr Snapko
 * */
public class PopActivity extends Activity {

    private TextView tv_resturantName;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        tv_resturantName = (TextView) findViewById(R.id.tv_resurantName);
        iv = (ImageView) findViewById(R.id.imageView);

        try {
            ArrayList<Restaurant> restaurants = createRestaurantList(readRestaurantToString("restaurant"));
            Random r = new Random();
            int index = r.nextInt(restaurants.size());
            tv_resturantName.setText(restaurants.get(index).name);
            //this part doesn't quite work
            iv.setImageResource(Integer.parseInt(restaurants.get(index).imagePath));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //here i will set the textview and imageview to the name and logo of the randomly selected resturant
        //tv_resturantName.setText("test");
        //iv.setImageResource(R.drawable.scan);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.5));
    }


    private Restaurant getRestaurantFromJSONObject(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("name");
        String imagePath = jsonObject.getString("imagePath");;
        Restaurant restaurant = new Restaurant(name,imagePath);
        return restaurant;
    }

    /**
     * Takes in the name of a menu type and reads in the file from the 'assets/menu' directory.
     * reused method from mitchel in order to read restaurant json file
     * @param fileName The file to be stringified
     * @return The contents of the file.
     * @throws IOException If no file such as 'assets;menu/(fileName).json' exists.
     */
    private String readRestaurantToString(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream is = getApplicationContext().getAssets().open("menu/"+fileName+".json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));

        String str;
        while((str = reader.readLine()) != null) {
            sb.append(str);
        }

        return sb.toString();
    }
    /**
     * method that gets a list of resturants from a passed in JSON string
     * @return an arrayList of resturant objects
     * */
    public ArrayList<Restaurant> createRestaurantList(String jsonStr) throws JSONException{
        ArrayList<Restaurant> restaurantList = new ArrayList<>();
        JSONObject obj = new JSONObject(jsonStr);
        JSONArray restaurants = obj.getJSONArray("restaurants");
        for (int i = 0; i < restaurants.length(); i++) {
            try {
                Restaurant newRest = getRestaurantFromJSONObject(restaurants.getJSONObject(i));
                restaurantList.add(newRest);
                //Log.d("items", "newItem: " + newItem.toString());
            }
            catch (JSONException e) {
                // Handle exception
                Log.d("MENU", "JSONException: " + e.toString());
            }
        }
        return restaurantList;
    }
    /**
     * @author Kyzr Snapko
     * */
    private class Restaurant {
        private String name;
        private String imagePath;

        public Restaurant(String name, String imagePath){
            this.name = name;
            this.imagePath = imagePath;
        }

        public String getImagePath() { return imagePath; }

        public String getName() { return name;}

    }
}

