package com.autogarcon.android;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autogarcon.android.API.MenuItem;

import java.util.List;
import java.util.Map;

/**
 * A bridge between CategoryList layout and data source that helps us to fill data into the layout.
 * @author Mitchell Nelson
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private List<Map.Entry<String, List<MenuItem>>> categories;

    /**
     * A class that represents each item in our collection and will be used to display it.
     * @author Mitchell Nelson
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public ImageView categoryImage1;
        public ImageView categoryImage2;
        public ImageView categoryImage3;
        public ImageView categoryImage4;
        public ImageView categoryImageFull;
        /**
         * Binds layout views to local variables
         * @param view the current view
         */
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

    public CategoryListAdapter(List<Map.Entry<String, List<MenuItem>>> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_category, parent, false);
        return new CategoryListAdapter.MyViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(CategoryListAdapter.MyViewHolder holder, int position) {
        Map.Entry<String, List<MenuItem>> category = categories.get(position);

        holder.categoryName.setText(category.getKey());

        if  (category.getValue().size() == 1) {
            holder.categoryImage1.setVisibility(View.VISIBLE);
            ThumbnailManager.getInstance().getImage(category.getValue().get(0).getImageURL(), holder.categoryImageFull);
        }

        else if(category.getValue().size() == 2) {
            holder.categoryImage1.setVisibility(View.INVISIBLE);
            ThumbnailManager.getInstance().getImage(category.getValue().get(0).getImageURL(), holder.categoryImage1);
            ThumbnailManager.getInstance().getImage(category.getValue().get(1).getImageURL(), holder.categoryImage2);
            ThumbnailManager.getInstance().getImage(category.getValue().get(1).getImageURL(), holder.categoryImage3);
            ThumbnailManager.getInstance().getImage(category.getValue().get(0).getImageURL(), holder.categoryImage4);
        }

        else if(category.getValue().size() == 3) {
            ThumbnailManager.getInstance().getImage(category.getValue().get(0).getImageURL(), holder.categoryImage1);
            ThumbnailManager.getInstance().getImage(category.getValue().get(1).getImageURL(), holder.categoryImage2);
            ThumbnailManager.getInstance().getImage(category.getValue().get(2).getImageURL(), holder.categoryImage3);
            ThumbnailManager.getInstance().getImage(category.getValue().get(0).getImageURL(), holder.categoryImage4);
        }

        else if(category.getValue().size() >= 4) {
            ThumbnailManager.getInstance().getImage(category.getValue().get(0).getImageURL(), holder.categoryImage1);
            ThumbnailManager.getInstance().getImage(category.getValue().get(1).getImageURL(), holder.categoryImage2);
            ThumbnailManager.getInstance().getImage(category.getValue().get(2).getImageURL(), holder.categoryImage3);
            ThumbnailManager.getInstance().getImage(category.getValue().get(3).getImageURL(), holder.categoryImage4);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}