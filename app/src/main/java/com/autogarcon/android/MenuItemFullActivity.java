package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Activity for the full view of a single menu item.
 * @author Tim Callies
 */
public class MenuItemFullActivity extends AppCompatActivity {

    ImageView largeImage;
    MenuItem menuItem;
    TextView name;
    TextView description;
    TextView price;
    EditText chefNote;
    Button button;
    ProgressDialog dialog;
    String title;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        title = (String)getIntent().getSerializableExtra("title");
        setTitle(title);
        this.menuItem = (MenuItem) getIntent().getSerializableExtra("item");

        setContentView(R.layout.activity_menu_item_full);

        dialog = new ProgressDialog(MenuItemFullActivity.this);
        button = (Button) findViewById(R.id.button);
        name = (TextView) findViewById(R.id.itemFullName);
        description = (TextView) findViewById(R.id.itemFullDescription);
        price = (TextView) findViewById(R.id.itemFullPrice);
        largeImage = (ImageView) findViewById(R.id.itemFullImage);
        chefNote = (EditText) findViewById(R.id.itemFullChefNotes);
        ThumbnailManager.getInstance().getImage(menuItem.getImagePath(),largeImage);

        name.setText(menuItem.getName());
        description.setText(menuItem.getDescription());
        price.setText(String.format("$%.2f",menuItem.getPrice()));

        CustomTheme theme = new CustomTheme();
        theme.applyTo(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderItem orderItem = new OrderItem(menuItem, chefNote.getText().toString());
                ActiveSession.getInstance().addOrder(orderItem);
                dialog.setCancelable(false);
                dialog.show();
                dialog.setTitle("Ordering...");
                //TODO: Apply a theme to the dialog
                //TODO: Actually do something with the request

                String url ="https://jsonplaceholder.typicode.com/todos/1";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setResult(2);
                                dialog.dismiss();
                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        setResult(2);
                        finish();
                    }
                });

                queue.add(stringRequest);
            }
        });
    }
}

