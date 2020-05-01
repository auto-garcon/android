package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Activity for the full view of a single menu item.
 * @author Tim Callies
 */
public class MenuItemFullActivity extends AppCompatActivity {

    private ImageView largeImage;
    private MenuItem menuItem;
    private TextView name;
    private TextView description;
    private TextView price;
    private EditText chefNote;
    private Button button;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adds the text passed from the previous activity to the title bar
        title = (String)getIntent().getSerializableExtra("title");
        setTitle(title);

        // sets menuItem to the clicked item from the previous activity
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

        // Apply the CustomTheme
        ActiveSession.getInstance().getCustomTheme().applyTo(this);

        // creates an OrderItem and adds it to order once button is clicked
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

