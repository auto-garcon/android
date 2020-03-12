package com.autogarcon.android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuItemListActivity extends AppCompatActivity {
    ImageView largeImage;
    Category category;
    private RecyclerView recyclerView;
    private MenuItemListAdapter mAdapter;
    private SearchView searchView;
    private String title;


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
        setContentView(R.layout.activity_menu_item_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MenuItemListAdapter(category.getMenuItems());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MenuItemListActivity.this, MenuItemFullActivity.class);
                intent.putExtra("item", category.getMenuItems().get(position));
                intent.putExtra("title", title);
                ImageView image = ((MenuItemListAdapter.MyViewHolder)(recyclerView.getChildViewHolder(recyclerView.getChildAt(position)))).getMenuImage();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MenuItemListActivity.this, (View)image, "itemImage");
                startActivityForResult(intent, 2, options.toBundle());
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                mAdapter.getFilter().filter("search" + newText);
                return false;
            }
        });

        return true;
    }
 /*
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtering, menu);
        return true;
    }
*/
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.meat_filter:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    mAdapter.getFilter().filter("filternomeat");
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    mAdapter.getFilter().filter("filtermeat");
                }
                return true;
            case R.id.nuts_filter:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    mAdapter.getFilter().filter("filternonuts");
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    mAdapter.getFilter().filter("filternuts");
                }
                return true;
            case R.id.dairy_filter:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    mAdapter.getFilter().filter("filternodairy");
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    mAdapter.getFilter().filter("filterdairy");
                }
                return true;
            case R.id.gluten_filter:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    mAdapter.getFilter().filter("filternogluten");
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    mAdapter.getFilter().filter("filtergluten");
                }
                return true;
            case R.id.soy_filter:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                    mAdapter.getFilter().filter("filternosoy");
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    mAdapter.getFilter().filter("filtersoy");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            setResult(2);
            finish();
        }
    }
}
