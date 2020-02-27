package com.autogarcon.android;

// import android.support.v7.widget.RecyclerView;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {

    private List<MenuItem> menuList;

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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MenuItem menuItem = menuList.get(position);
        holder.name.setText(String.valueOf(menuItem.getName()));
        holder.description.setText(String.valueOf(menuItem.getDescription()));
        holder.calories.setText("Calories: " + menuItem.getCalories());
        holder.price.setText(String.format("%.2f", menuItem.getPrice()));
        holder.menuImage.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}