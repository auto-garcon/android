package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder> {

    private ArrayList<Menu> menuList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView menuName;
        public ImageView menuImage;

        public MyViewHolder(View view) {
            super(view);
            menuName = (TextView) view.findViewById(R.id.categoryName);
            menuImage = (ImageView) view.findViewById(R.id.categoryImage);
        }
    }

    public MenuListAdapter(ArrayList<Menu> menuList) {
        this.menuList = menuList;
    }

    @Override
    public MenuListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View menuView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list, parent, false);
        return new MenuListAdapter.MyViewHolder(menuView);
    }

    @Override
    public void onBindViewHolder(MenuListAdapter.MyViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        holder.menuName.setText(String.valueOf(menu.getMenuType()));
        holder.menuImage.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}