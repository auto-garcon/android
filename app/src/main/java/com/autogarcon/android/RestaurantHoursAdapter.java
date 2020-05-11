package com.autogarcon.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.autogarcon.android.API.Menu;
import com.autogarcon.android.API.Restaurant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Creates a view for a menu name along with the associated hours.
 */
public class RestaurantHoursAdapter extends RecyclerView.Adapter<RestaurantHoursAdapter.MyViewHolder> {
    private List<Menu> menuList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantHoursStart;
        TextView restaurantHoursEnd;
        TextView restaurantHoursName;
        /**
         * Binds layout views to local variables
         * @param view the current view
         */
        public MyViewHolder(View view) {
            super(view);
            restaurantHoursName = (TextView) view.findViewById(R.id.list_restaurant_hours_name);
            restaurantHoursEnd = (TextView) view.findViewById(R.id.list_restaurant_hours_end);
            restaurantHoursStart = (TextView) view.findViewById(R.id.list_restaurant_hours_start);
        }
    }

    /**
     * Constructor
     * @param restaurant The restaurant that we want to look at.
     */
    public RestaurantHoursAdapter(Restaurant restaurant) {
        this.menuList = restaurant.getMenuList();
    }

    @Override
    public RestaurantHoursAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_restaurant_hours, parent, false);
        return new RestaurantHoursAdapter.MyViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(RestaurantHoursAdapter.MyViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        holder.restaurantHoursName.setText(menu.getMenuName());
        SimpleDateFormat inFormat = new SimpleDateFormat("HHMM");
        SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
        try{
            holder.restaurantHoursStart.setText(outFormat.format(inFormat.parse(
                    String.valueOf(menu.getTimeRanges().get(0).getStartTime()))));
            holder.restaurantHoursEnd.setText(outFormat.format(inFormat.parse(
                    String.valueOf(menu.getTimeRanges().get(0).getEndTime()))));



        }catch (ParseException pe) {}
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
