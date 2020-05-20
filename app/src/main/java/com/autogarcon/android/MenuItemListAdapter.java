package com.autogarcon.android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.autogarcon.android.API.Allergen;
import com.autogarcon.android.API.MenuItem;

/**
 * The adapter for the menu list RecyclerView
 * @author Riley Tschumper
 */
public class MenuItemListAdapter extends RecyclerView.Adapter<MenuItemListAdapter.MyViewHolder> implements Filterable {

    private List<MenuItem> menuList;
    private List<MenuItem> menuListFiltered;
    private List<MenuItem> currentlyDisplayed;
    private List<Allergen> currentFilters = new ArrayList<>();

    /**
     * Describes an item view and metadata about its place within the RecyclerView.
     * @author Riley Tschumper
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, calories, price;
        public ImageView menuImage;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            calories = (TextView) view.findViewById(R.id.calories);
            price = (TextView) view.findViewById(R.id.price);
            menuImage = (ImageView) view.findViewById(R.id.menuImage);
        }

        public ImageView getMenuImage() {
            return menuImage;
        }
    }

    /**
     * Constructor
     * @param menuList takes in the list of menu items in the category
     * @author Riley Tschumper
     */
    public MenuItemListAdapter(List<MenuItem> menuList) {
        this.menuList = menuList;
        this.menuListFiltered = menuList;
        this.currentlyDisplayed = new ArrayList<>(menuList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_menu_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MenuItem menuItem = menuListFiltered.get(position);
        holder.name.setText(String.valueOf(menuItem.getName()));
        holder.description.setText(String.valueOf(menuItem.getDescription()));
        holder.calories.setText(String.format("(%d kcal)",menuItem.getCalories()));
        holder.price.setText(String.format("%.2f", menuItem.getPrice()));
        ThumbnailManager.getInstance().getImage(menuItem.getImageURL(),holder.menuImage);
    }

    @Override
    public int getItemCount() {
        return menuListFiltered.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            /**
             * This filtering is used for both allergen filtering dropdown and text search
             * @author Riley Tschumper
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                // Check if it is an allergen filter. All of them have "filter" appended to the beginning
                boolean filter = false;
                if(charString.substring(0,6).equals("filter")){
                    filter = true;
                }
                charString = charString.substring(6);

                if (charString.isEmpty()){
                    Log.d("IS EMPTY", "charString is Empty");

                    currentlyDisplayed = new ArrayList<>(menuList);
                    for(MenuItem row : menuList){
                        for(Allergen tag : currentFilters) {
                            if (row.getAllergens().contains(tag)) {
                                currentlyDisplayed.remove(row);
                                break;
                            }
                        }
                    }
                } else {
                    if(filter){
                        /*
                            Depending upon the filter, it will either be added or removed from the
                            list of active filters
                        */
                        if(charString.equals("nomeat")){
                            currentFilters.add(Allergen.MEAT);
                        }
                        if (charString.equals("meat")) {
                            currentFilters.remove(Allergen.MEAT);
                        }
                        if(charString.equals("nodairy")){
                            currentFilters.add(Allergen.DAIRY);
                        }
                        if (charString.equals("dairy")) {
                            currentFilters.remove(Allergen.DAIRY);
                        }
                        if (charString.equals("nonuts")){
                            currentFilters.add(Allergen.NUTS);
                        }
                        if (charString.equals("nuts")){
                            currentFilters.remove(Allergen.NUTS);
                        }
                        if (charString.equals("nogluten")){
                            currentFilters.add(Allergen.GLUTEN);
                        }
                        if (charString.equals("gluten")){
                            currentFilters.remove(Allergen.GLUTEN);
                        }
                        if (charString.equals("nosoy")){
                            currentFilters.add(Allergen.SOY);
                        }
                        if (charString.equals("soy")){
                            currentFilters.remove(Allergen.SOY);
                        }

                        currentlyDisplayed = new ArrayList<>(menuList);
                        for(MenuItem row : menuList){
                            for(Allergen tag : currentFilters) {
                                if (row.getAllergens().contains(tag)) {
                                    currentlyDisplayed.remove(row);
                                    break;
                                }
                            }
                        }
                    } else {
                        // Filtering using the text search bar

                        // First only displays the items based on the allergen filters
                        currentlyDisplayed = new ArrayList<>(menuList);
                        for (MenuItem row : menuList) {
                            for (Allergen tag : currentFilters) {
                                if (row.getAllergens().contains(tag)) {
                                    currentlyDisplayed.remove(row);
                                    break;
                                }
                            }
                        }

                        // Loops through the remaining menu items, not filtered to check if
                        // the title or description match the entered text
                        for (MenuItem row : menuList) {
                            if (!row.getName().toLowerCase().contains(charString.toLowerCase()) && !row.getDescription().toLowerCase().contains(charString.toLowerCase())) {
                                currentlyDisplayed.remove(row);
                            }
                        }
                    }
                }
                menuListFiltered = currentlyDisplayed;
                FilterResults filterResults = new FilterResults();
                filterResults.values = menuListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                menuListFiltered = (ArrayList<MenuItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}