package com.autogarcon.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Creates a view for a restaurant that is used on the user settings screen to display favorites.
 * @Author Tim Callies
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool;
    private List<Restaurant> restaurantList;

    /**
     * ViewHolder for the restaurant.
     *
     * @author Tim Callies
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView restaurantHours;
        public ImageView restaurantLogo;
        public TextView restaurantName;

        /**
         * Binds layout views to local variables
         * @param view the current view
         */
        public MyViewHolder(View view) {
            super(view);
            restaurantLogo = (ImageView) view.findViewById(R.id.list_restaurant_image);
            restaurantName = (TextView) view.findViewById(R.id.list_restaurant_name);
            restaurantHours = (RecyclerView) view.findViewById(R.id.list_restaurant_hours);
            restaurantHours.setLayoutManager(new LinearLayoutManager(view.getContext()));
            restaurantHours.setRecycledViewPool(viewPool);

        }

    }

    /**
     * Constructor. Initializes the ViewPool.
     * @param restaurantList The list of items
     */
    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public RestaurantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_restaurant, parent, false);
        return new RestaurantAdapter.MyViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(RestaurantAdapter.MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        RestaurantHoursAdapter adapter = new RestaurantHoursAdapter(restaurant);
        holder.restaurantHours.setAdapter(adapter);
        holder.restaurantName.setText(restaurant.getName());
        ThumbnailManager.getInstance().getImage(restaurant.getLogoUrl(), holder.restaurantLogo);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}