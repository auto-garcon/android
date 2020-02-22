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
        public TextView name, description, calories;
        public ImageView menuImage;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            calories = (TextView) view.findViewById(R.id.caloriesNumber);
            menuImage = (ImageView) view.findViewById(R.id.menuImage);
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
        holder.calories.setText(String.valueOf(menuItem.getCalories()));
        holder.menuImage.setImageResource(R.drawable.placeholder);
        //holder.title.setText("test1");
        //holder.genre.setText("Test2");
        //holder.year.setText("Test3");
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}