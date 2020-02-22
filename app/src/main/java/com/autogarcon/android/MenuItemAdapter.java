package com.autogarcon.android;

// import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {

    private List<MenuItem> menuList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
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
        holder.title.setText(String.valueOf(menuItem.getName()));
        holder.genre.setText(String.valueOf(menuItem.getCategoryName()));
        holder.year.setText(String.valueOf(menuItem.getCalories()));
        //holder.title.setText("test1");
        //holder.genre.setText("Test2");
        //holder.year.setText("Test3");
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}