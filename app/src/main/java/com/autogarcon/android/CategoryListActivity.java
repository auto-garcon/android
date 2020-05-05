package com.autogarcon.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autogarcon.android.API.APIUtils;
import com.autogarcon.android.API.Menu;

import com.autogarcon.android.API.MenuItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * Activity to display all categories in a recycler view
 * @author Mitchell Nelson
 */
public class CategoryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryListAdapter mAdapter;
    private String title;
    private List<Map.Entry<String, List<MenuItem>>> categories;
    private FloatingTextButton cartButton;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Initializes state and sets on click functionality for recycler view clicks
     * @param savedInstanceState current state
     * @author Mitchell Nelson
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Get extras from previous activity
        title = (String)getIntent().getSerializableExtra("title");
        this.categories = (List<Map.Entry<String, List<MenuItem>>>) getIntent().getSerializableExtra("menu");
        setContentView(R.layout.activity_category_list);

        // Set title to show the name of the category
        setTitle(title);

        cartButton = (FloatingTextButton) findViewById(R.id.c_button);
        double total = 0;
        if(ActiveSession.getInstance().getAllOrders().size() > 0){
            for (int index = 0; index < ActiveSession.getInstance().getAllOrders().size(); index++) {
                total = total + ActiveSession.getInstance().getAllOrders().get(index).getMenuItem().getPrice();
            }
        }
        cartButton.setTitle("$" + String.format("%.2f", total));
        if(ActiveSession.getInstance().getAllOrders().size() == 0){
            cartButton.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CategoryListAdapter(categories);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), MenuItemListActivity.class);
                intent.putExtra("category", (Serializable) categories.get(position).getValue());
                intent.putExtra("title", categories.get(position).getKey());
                startActivityForResult(intent,2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartButton.setVisibility(View.GONE);
                ActiveSession.getInstance().setButtonFlag(true);
                setResult(4);
                finish();
            }
        });

        // Apply the CustomTheme
        ActiveSession.getInstance().getCustomTheme().applyTo(this);
    }

    /**
     * Finishes activity if the "toCart" button was pressed in the menu item list view
     * @param requestCode status code of the request
     * @param resultCode status code of the result
     * @param data curent intent
     * @author Mitchell Nelson
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 4){
            setResult(4);
            finish();
        }
    }

    /**
     * Displays the "toCart" button if at least one order exists
     * @author Mitchell Nelson
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(ActiveSession.getInstance().getAllOrders().size() > 0){
            cartButton.setVisibility(View.VISIBLE);
            double total = 0;
            for (int index = 0; index < ActiveSession.getInstance().getAllOrders().size(); index++) {
                total = total + ActiveSession.getInstance().getAllOrders().get(index).getMenuItem().getPrice();
            }
            cartButton.setTitle("$" + String.format("%.2f", total));
        }
    }
}