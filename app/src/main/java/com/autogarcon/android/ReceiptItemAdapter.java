package com.autogarcon.android;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autogarcon.android.API.OrderItem;
import com.autogarcon.android.API.Restaurant;

/**
 * Creates a a list of price modifications that will be shown on every ReceiptAdapter.
 * @author Tim Callies
 */
public class ReceiptItemAdapter extends RecyclerView.Adapter<ReceiptItemAdapter.MyViewHolder> {

    private OrderItem orderItem;

    /**
     * Creates a view for the ReceiptItem.
     * @author Tim Callies
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView receiptItemName;
        public TextView receiptItemPrice;

        /**
         * Binds layout views to local variables
         * @param view the current view
         */
        public MyViewHolder(View view) {
            super(view);
            receiptItemName = (TextView) view.findViewById(R.id.list_restaurant_hours_name);
            receiptItemPrice = (TextView) view.findViewById(R.id.list_restaurant_hours_start);
        }

    }

    /**
     * Constructor.
     * @param orderItem The item that the receipt item is associated with.
     */
    public ReceiptItemAdapter(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public ReceiptItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_receipt_item, parent, false);
        return new ReceiptItemAdapter.MyViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(ReceiptItemAdapter.MyViewHolder holder, int position) {
        double price = orderItem.getPrice();

        float tax = ActiveSession.getInstance().getRestaurant().getSalesTax();

        // Show the tax
        if(position == 0) {
            holder.receiptItemPrice.setText(String.format("$%.2f",price*tax));
            holder.receiptItemName.setText("Tax");
        }

        // Show the total
        if (position == 1) {

            holder.receiptItemPrice.setText(String.format("$%.2f",price+price*tax));
            holder.receiptItemName.setText("Total");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}