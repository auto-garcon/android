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
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = (String)getIntent().getSerializableExtra("title");
        setTitle(title);
        this.menuItem = (MenuItem) getIntent().getSerializableExtra("item");

        setContentView(R.layout.activity_menu_item_full);

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
                setResult(2);
                finish();
            }
        });
    }
}

