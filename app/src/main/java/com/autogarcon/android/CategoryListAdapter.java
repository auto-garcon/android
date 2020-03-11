package com.autogarcon.android;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Menu menu;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public ImageView categoryImage1;
        public ImageView categoryImage2;
        public ImageView categoryImage3;
        public ImageView categoryImage4;
        public ImageView categoryImageFull;

        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            categoryImage1 = (ImageView) view.findViewById(R.id.categoryImage1);
            categoryImage2 = (ImageView) view.findViewById(R.id.categoryImage2);
            categoryImage3 = (ImageView) view.findViewById(R.id.categoryImage3);
            categoryImage4 = (ImageView) view.findViewById(R.id.categoryImage4);
            categoryImageFull = (ImageView) view.findViewById(R.id.categoryImageFull);
        }

    }

        public CategoryListAdapter(Menu menu) {
            this.menu = menu;
        }

        @Override
        public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View categoryView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_category, parent, false);
            return new CategoryListAdapter.MyViewHolder(categoryView);
        }

        @Override
        public void onBindViewHolder(CategoryListAdapter.MyViewHolder holder, int position) {
            Category category = menu.getCategories().get(position);
            holder.categoryName.setText(String.valueOf(category.getName()));

            if  (category.getMenuItems().size() == 1) {
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(0).getImagePath(), holder.categoryImageFull);
            }

            else if(category.getMenuItems().size() == 2) {
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(0).getImagePath(), holder.categoryImage1);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(1).getImagePath(), holder.categoryImage2);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(1).getImagePath(), holder.categoryImage3);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(0).getImagePath(), holder.categoryImage4);
            }

            else if(category.getMenuItems().size() == 3) {
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(0).getImagePath(), holder.categoryImage1);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(1).getImagePath(), holder.categoryImage2);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(2).getImagePath(), holder.categoryImage3);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(0).getImagePath(), holder.categoryImage4);
            }

            else if(category.getMenuItems().size() >= 4) {
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(0).getImagePath(), holder.categoryImage1);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(1).getImagePath(), holder.categoryImage2);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(2).getImagePath(), holder.categoryImage3);
                ThumbnailManager.getInstance().getImage(category.getMenuItems().get(3).getImagePath(), holder.categoryImage4);
            }
        }

        @Override
        public int getItemCount() {
            return menu.getCategories().size();
        }
}