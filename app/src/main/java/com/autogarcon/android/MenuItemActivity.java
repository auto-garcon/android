package com.autogarcon.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuItemActivity extends AppCompatActivity {
    ImageView largeImage;
    Category category;
    private RecyclerView recyclerView;
    private MenuItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.category = (Category) getIntent().getSerializableExtra("category");
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MenuItemAdapter(category.getMenuItems());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), MenuItemFullActivity.class);
                intent.putExtra("item", category.getMenuItems().get(position));

                ImageView image = ((MenuItemAdapter.MyViewHolder)(recyclerView.getChildViewHolder(recyclerView.getChildAt(position)))).getMenuImage();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MenuItemActivity.this, (View)image, "itemImage");
                startActivity(intent, options.toBundle());
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        CustomTheme theme = new CustomTheme();
        theme.applyTo(this);
    }
}
