package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Menu menu;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public ImageView categoryImage;

        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
        }

    }

        public CategoryListAdapter(Menu menu) {
            this.menu = menu;
        }

        @Override
        public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View categoryView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list, parent, false);
            return new CategoryListAdapter.MyViewHolder(categoryView);
        }

        @Override
        public void onBindViewHolder(CategoryListAdapter.MyViewHolder holder, int position) {
            Category category = menu.getCategories().get(position);
            holder.categoryName.setText(String.valueOf(category.getName()));
            holder.categoryImage.setImageResource(R.drawable.placeholder);
        }

        @Override
        public int getItemCount() {
            return menu.getCategories().size();
        }
}