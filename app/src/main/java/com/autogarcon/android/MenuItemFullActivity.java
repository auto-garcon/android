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
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = (String)getIntent().getSerializableExtra("title");
        setTitle(title);
        this.menuItem = (MenuItem) getIntent().getSerializableExtra("item");

        setContentView(R.layout.activity_menu_item_full);

        name = (TextView) findViewById(R.id.itemFullName);
        description = (TextView) findViewById(R.id.itemFullDescription);
        price = (TextView) findViewById(R.id.itemFullPrice);
        largeImage = (ImageView) findViewById(R.id.itemFullImage);

        ThumbnailManager.getInstance().getImage(menuItem.getImagePath(),largeImage);

        name.setText(menuItem.getName());
        description.setText(menuItem.getDescription());
        price.setText(String.format("$%.2f",menuItem.getPrice()));

        CustomTheme theme = new CustomTheme();
        theme.applyTo(this);
    }
}