package com.autogarcon.android;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Creates a view of a single ordered item & it's associated costs for a RecyclerView
 * @author Tim Callies
 */
public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool;

    /**
     * ViewHolder for the receipt.
     * @author Tim Callies
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView receiptItems;
        public TextView receiptName;
        public TextView receiptPrice;
        public TextView receiptOrderTime;
        public TextView receiptCalories;

        /**
         * Binds layout views to local variables
         * @param view the current view
         */
        public MyViewHolder(View view) {
            super(view);
            receiptItems = (RecyclerView)view.findViewById(R.id.list_receipt_items);
            receiptName = (TextView)view.findViewById(R.id.list_receipt_name);
            receiptPrice = (TextView)view.findViewById(R.id.list_receipt_price);
            receiptOrderTime = (TextView)view.findViewById(R.id.list_receipt_order_time);
            receiptCalories = (TextView)view.findViewById(R.id.list_receipt_calories);

            receiptItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
            receiptItems.setRecycledViewPool(viewPool);
            ReceiptItemAdapter adapter = new ReceiptItemAdapter();
            receiptItems.setAdapter(adapter);
        }

    }

    /**
     * Constructor. Initializes the ViewPool.
     */
    public ReceiptAdapter() {
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public ReceiptAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_receipt, parent, false);
        return new ReceiptAdapter.MyViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(ReceiptAdapter.MyViewHolder holder, int position) {

        holder.receiptName.setText("Big Snacktime");
        holder.receiptPrice.setText("$199.99");
        holder.receiptOrderTime.setText("Ordered at 1:21PM");
        holder.receiptCalories.setText("(200 kcal)");


    }

    @Override
    public int getItemCount() {
        return 10;
    }
}