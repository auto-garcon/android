package com.autogarcon.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A fragment that shows all of the items that were ordered in the past 24 hours, as well as the customer's current
 * cart.
 */
public class ReceiptFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    private TextView receiptNothingHere;
    private LinearLayout receiptCurrentOrder;
    private LinearLayout receiptInProgress;
    private LinearLayout receiptCompleted;
    private RecyclerView receiptCurrentOrderReceipts;
    private ReceiptAdapter receiptAdapter;
    private RecyclerView receiptInProgressReceipts;
    private ReceiptAdapter inProgressAdapter;
    private RecyclerView receiptCompletedReceipts;
    private ReceiptAdapter completedAdapter;
    private Button receiptClearButton;
    private RequestQueue queue;
    private Button receiptOrderButton;
    private View view;
    private List<OrderItem> inProgressOrders;
    private List<OrderItem> completedOrders;

    /**
     * A client for interacting with the Google Pay API.
     *
     * @see <a
     *     href="https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient">PaymentsClient</a>
     */
    private PaymentsClient mPaymentsClient;


    public ReceiptFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inProgressOrders = new ArrayList<>();
        completedOrders = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_reciept, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        this.view = view;
        queue = Volley.newRequestQueue(view.getContext());

        receiptNothingHere = (TextView) view.findViewById(R.id.receipt_nothing_here);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        receiptInProgress = (LinearLayout) view.findViewById(R.id.receipt_in_progress);
        receiptCurrentOrder = (LinearLayout) view.findViewById(R.id.receipt_current_order);
        receiptCompleted = (LinearLayout) view.findViewById(R.id.receipt_completed);
        receiptOrderButton = (Button) view.findViewById(R.id.receipt_order_button);
        receiptClearButton = (Button) view.findViewById(R.id.receipt_clear_button);
        receiptCurrentOrderReceipts = (RecyclerView) view.findViewById(R.id.receipt_current_order_receipts);
        receiptInProgressReceipts = (RecyclerView) view.findViewById(R.id.receipt_in_progress_receipts);
        receiptCompletedReceipts = (RecyclerView) view.findViewById(R.id.receipt_completed_receipts);

        receiptAdapter = new ReceiptAdapter(ActiveSession.getInstance().getAllOrders());
        receiptCurrentOrderReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        receiptCurrentOrderReceipts.setAdapter(receiptAdapter);
        receiptCurrentOrderReceipts.setItemAnimator(new DefaultItemAnimator());
        receiptCurrentOrderReceipts.setNestedScrollingEnabled(false);

        inProgressAdapter = new ReceiptAdapter(inProgressOrders);
        receiptInProgressReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        receiptInProgressReceipts.setAdapter(inProgressAdapter);
        receiptInProgressReceipts.setItemAnimator(new DefaultItemAnimator());
        receiptInProgressReceipts.setNestedScrollingEnabled(false);

        completedAdapter = new ReceiptAdapter(completedOrders);
        receiptCompletedReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        receiptCompletedReceipts.setAdapter(completedAdapter);
        receiptCompletedReceipts.setItemAnimator(new DefaultItemAnimator());
        receiptCompletedReceipts.setNestedScrollingEnabled(false);

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                ActiveSession.getInstance().removeOrderItem(ActiveSession.getInstance().getAllOrders().get(position));
                receiptAdapter.notifyItemRemoved(position);
                updateViews();
            }
        };

        receiptClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveSession.getInstance().clearOrders();
                updateViews();
            }
        });

        receiptOrderButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String url ="https://jsonplaceholder.typicode.com/todos/1";
                ((TopActivity) getActivity()).requestPayment(view);

                //TopActivity.requestPayment(view);

                Toast.makeText(view.getContext(), "Ordering...", Toast.LENGTH_SHORT).show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                for(OrderItem item : ActiveSession.getInstance().getAllOrders()) {
                                    inProgressOrders.add(item);
                                }
                                ActiveSession.getInstance().clearOrders();
                                updateViews();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeContainer.setRefreshing(false);
                        Toast.makeText(view.getContext(), "Could not make order", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(receiptCurrentOrderReceipts);
        refreshPage();
        updateViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Updates the view of the receipt. This should be called whenever the length of the order is changed.
     */
    private void updateViews() {
        boolean hasSomething = false;

        if(ActiveSession.getInstance().getAllOrders().size() == 0) {
            receiptCurrentOrder.setVisibility(View.GONE);
        }
        else {
            receiptCurrentOrder.setVisibility(View.VISIBLE);
            hasSomething = true;
        }

        if(inProgressOrders.size() == 0) {
            receiptInProgress.setVisibility(View.GONE);
        }
        else {
            receiptInProgress.setVisibility(View.VISIBLE);
            hasSomething = true;
        }

        if(completedOrders.size() == 0) {
            receiptCompleted.setVisibility(View.GONE);
        }
        else {
            receiptCompleted.setVisibility(View.VISIBLE);
            hasSomething = true;
        }
        if(hasSomething) {
            receiptNothingHere.setVisibility(View.GONE);
        }
        else {
            receiptNothingHere.setVisibility(View.VISIBLE);
        }
    }

    private void refreshPage() {
        String url ="https://jsonplaceholder.typicode.com/todos/1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeContainer.setRefreshing(false);
                        updateViews();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(view.getContext(), "Could not load order", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

}
