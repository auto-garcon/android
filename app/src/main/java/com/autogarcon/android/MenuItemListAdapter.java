package com.autogarcon.android;

// import android.support.v7.widget.RecyclerView;
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

/**
 *
 * @author Riley Tschumper
 */
public class MenuItemListAdapter extends RecyclerView.Adapter<MenuItemListAdapter.MyViewHolder> implements Filterable {

    private List<MenuItem> menuList;
    private List<MenuItem> menuListFiltered;
    private List<MenuItem> currentlyDisplayed;
    private List<DietaryTags> currentFilters = new ArrayList<>();

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
        ThumbnailManager.getInstance().getImage(menuItem.getImagePath(),holder.menuImage);
    }

    @Override
    public int getItemCount() {
        return menuListFiltered.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                boolean filter = false;
                if(charString.substring(0,6).equals("filter")){
                    filter = true;
                }
                charString = charString.substring(6);
                Log.d("IN FILTER", "Sent to Filter: " + charString);

                if (charString.isEmpty()){
                    Log.d("IS EMPTY", "charString is Empty");

                    currentlyDisplayed = new ArrayList<>(menuList);
                    for(MenuItem row : menuList){
                        for(DietaryTags tag : currentFilters) {
                            if (row.getDietaryTags().contains(tag)) {
                                currentlyDisplayed.remove(row);
                                break;
                            }
                        }
                    }
                } else {
                    if(filter){
                        if(charString.equals("nomeat")){
                            currentFilters.add(DietaryTags.MEAT);
                        }
                        if (charString.equals("meat")) {
                            currentFilters.remove(DietaryTags.MEAT);
                        }
                        if(charString.equals("nodairy")){
                            currentFilters.add(DietaryTags.DAIRY);
                        }
                        if (charString.equals("dairy")) {
                            currentFilters.remove(DietaryTags.DAIRY);
                        }
                        if (charString.equals("nonuts")){
                            currentFilters.add(DietaryTags.NUTS);
                        }
                        if (charString.equals("nuts")){
                            currentFilters.remove(DietaryTags.NUTS);
                        }
                        if (charString.equals("nogluten")){
                            currentFilters.add(DietaryTags.GLUTEN);
                        }
                        if (charString.equals("gluten")){
                            currentFilters.remove(DietaryTags.GLUTEN);
                        }
                        if (charString.equals("nosoy")){
                            currentFilters.add(DietaryTags.SOY);
                        }
                        if (charString.equals("soy")){
                            currentFilters.remove(DietaryTags.SOY);
                        }

                        currentlyDisplayed = new ArrayList<>(menuList);
                        for(MenuItem row : menuList){
                            for(DietaryTags tag : currentFilters) {
                                if (row.getDietaryTags().contains(tag)) {
                                    currentlyDisplayed.remove(row);
                                    break;
                                }
                            }
                        }
                    } else {
                        currentlyDisplayed = new ArrayList<>(menuList);
                        for (MenuItem row : menuList) {
                            for (DietaryTags tag : currentFilters) {
                                if (row.getDietaryTags().contains(tag)) {
                                    currentlyDisplayed.remove(row);
                                    break;
                                }
                            }
                        }
                        for (MenuItem row : menuList) {
                            if (!row.getName().toLowerCase().contains(charString.toLowerCase()) && !row.getDescription().toLowerCase().contains(charString.toLowerCase())) {
                                Log.d("FOUND", "Added: " + row.getName());
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
                Log.d("PUBLISH RESULTS", "Here");
                Log.d("RESULTS", "results" + results.values);
                menuListFiltered = (ArrayList<MenuItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}