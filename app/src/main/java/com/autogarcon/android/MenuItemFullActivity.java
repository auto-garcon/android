package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menuItem = (MenuItem) getIntent().getSerializableExtra("item");
        setContentView(R.layout.activity_menu_item_full);

        name = (TextView) findViewById(R.id.itemFullName);
        description = (TextView) findViewById(R.id.itemFullDescription);
        price = (TextView) findViewById(R.id.itemFullPrice);

        name.setText(menuItem.getName());
        description.setText(menuItem.getDescription());
        price.setText(String.format("%.2f",menuItem.getPrice()));
    }
}
