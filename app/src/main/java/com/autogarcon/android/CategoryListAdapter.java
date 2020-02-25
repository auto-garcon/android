package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private RecyclerViewClickListener mListener;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView categoryName;
        public ImageView categoryImage;
        private RecyclerViewClickListener mListener;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            mListener = listener;
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

        public CategoryListAdapter(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        @Override
        public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View categoryView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list, parent, false);
            return new CategoryListAdapter.MyViewHolder(categoryView, mListener);
        }

        @Override
        public void onBindViewHolder(CategoryListAdapter.MyViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.categoryName.setText(String.valueOf(category.getName()));
            holder.categoryImage.setImageResource(R.drawable.placeholder);
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }
}