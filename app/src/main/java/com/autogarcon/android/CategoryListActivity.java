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

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 *
 * @author Mitchell Nelson
 */
public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Menu menu;
    private CategoryListAdapter mAdapter;
    private String title;
    private FloatingActionButton toCart;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = (String)getIntent().getSerializableExtra("title");
        this.menu = (Menu) getIntent().getSerializableExtra("menu");
        setContentView(R.layout.activity_category_list);
        setTitle(title);

        toCart = (FloatingActionButton) findViewById(R.id.goto_cart);
        if(ActiveSession.getInstance().getAllOrders().size() == 0){
            toCart.hide();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CategoryListAdapter(menu);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), MenuItemListActivity.class);
                intent.putExtra("category", menu.getCategories().get(position));
                intent.putExtra("title", menu.getCategories().get(position).getName());
                startActivityForResult(intent,2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCart.hide();
                FragmentManager fm = getSupportFragmentManager();
                ReceiptFragment fragment = new ReceiptFragment();
                fm.beginTransaction().replace(R.id.coordinatorLayout, fragment).commit();
                recyclerView.setVisibility(View.GONE);
            }
        });

        CustomTheme theme = new CustomTheme();
        theme.applyTo(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ActiveSession.getInstance().getAllOrders().size() > 0){
            toCart.show();
        }
    }
}
