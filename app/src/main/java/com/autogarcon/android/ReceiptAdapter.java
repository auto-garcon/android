package com.autogarcon.android;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autogarcon.android.API.OrderItem;

import java.util.List;

/**
 * Creates a view of a single ordered item & it's associated costs for a RecyclerView
 * @author Tim Callies
 */
public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool;
    private List<OrderItem> orderItemList;

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

        }

    }

    /**
     * Constructor. Initializes the ViewPool.
     * @param orderItemList The list of items
     */
    public ReceiptAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
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
        OrderItem orderItem = orderItemList.get(position);
        ReceiptItemAdapter adapter = new ReceiptItemAdapter(orderItem);
        holder.receiptItems.setAdapter(adapter);
        holder.receiptName.setText(orderItem.getMenuItem().getName());
        holder.receiptPrice.setText(String.format("$%.2f",orderItem.getMenuItem().getPrice()));
        holder.receiptOrderTime.setText("Ordered at 1:21PM");
        holder.receiptCalories.setText(String.format("(%d kcal)",orderItem.getMenuItem().getCalories()));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }
}