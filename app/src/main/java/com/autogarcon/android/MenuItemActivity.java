package com.autogarcon.android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
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
<<<<<<< HEAD
    private SearchView searchView;
=======
    private String title;
>>>>>>> 895b5dd76b736c8a944def12dc23622e5e770a85

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = (String)getIntent().getSerializableExtra("title");
        setTitle(title);
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
                intent.putExtra("title", title);
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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.menu_filtering,menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Input Text", "Search Text: " + newText );
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
