package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MenuListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    ArrayList<Menu> menuList;
    private MenuListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menuList = (ArrayList<Menu>)getIntent().getSerializableExtra("menuList");
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MenuListAdapter(menuList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (menuList.get(position).getCategories().size() == 1){
                    Intent intent = new Intent(getApplicationContext(), MenuItemActivity.class);
                    intent.putExtra("category", menuList.get(position).getCategories().get(0));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
                    intent.putExtra("menu", menuList.get(position));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        CustomTheme theme = new CustomTheme();
        theme.applyTo(this);
    }
}
