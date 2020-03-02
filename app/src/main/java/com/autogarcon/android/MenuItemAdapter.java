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

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> implements Filterable {

    private List<MenuItem> menuList;
    private List<MenuItem> menuListFiltered;

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

    public MenuItemAdapter(List<MenuItem> menuList) {
        this.menuList = menuList;
        this.menuListFiltered = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_list, parent, false);

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
                Log.d("IN FILTER", "Sent to Filter: " + charString);
                if (charString.isEmpty()){
                    Log.d("IS EMPTY", "charString is Empty");
                    menuListFiltered = menuList;
                } else {
                    List<MenuItem> filteredList = new ArrayList<>();
                    for(MenuItem row : menuList){
                        if(row.getName().toLowerCase().contains(charString.toLowerCase())){
                            Log.d("FOUND", "Added: " + row.getName());
                            filteredList.add(row);
                        }
                    }

                    menuListFiltered = filteredList;
                }

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