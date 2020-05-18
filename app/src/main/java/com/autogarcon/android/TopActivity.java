package com.autogarcon.android;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autogarcon.android.API.APIUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

/**
 * The top level navigation for the menu of a single restaurant. This activity contains a navigation bar, as well
 * as two fragments.
 * @author Tim Callies
 */
public class TopActivity extends AppCompatActivity {

    private Menu menu;
    private ToggleButton toggleButton;
    private Boolean inFavorites = false;
    BottomNavigationView navView;
    
    ConstraintLayout constraintLayout;
    /**
     * A client for interacting with the Google Pay API.
     *
     * @see <a
     *     href="https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient">PaymentsClient</a>
     */
    private PaymentsClient mPaymentsClient;
    /**
     * Arbitrarily-picked constant integer you define to track a request for payment data activity.
     *
     * @value #LOAD_PAYMENT_DATA_REQUEST_CODE
     */
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        navView = findViewById(R.id.nav_view);
        mPaymentsClient = PaymentsUtil.createPaymentsClient(this);
        final Fragment reciept = new ReceiptFragment();
        final Fragment menu = new MenuListFragment();
        constraintLayout = (ConstraintLayout) findViewById(R.id.container);
        String intentFragment = getIntent().getExtras().getString("frgToLoad");
        setTitle(ActiveSession.getInstance().getRestaurant().getRestaurantName());

        openFragment(menu);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.menu_menu) {
                    openFragment(menu);
                    return true;
                }
                if(menuItem.getItemId() == R.id.menu_receipt) {
                    openFragment(reciept);
                    return true;
                }
                return false;
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        f.runOnCommit(new Runnable() {
            @Override
            public void run() {
                // Apply the CustomTheme
                ActiveSession.getInstance().getCustomTheme().applyTo(TopActivity.this);
            }
        });
        f.replace(R.id.top_frame, fragment);
//        f.addToBackStack(null);
        f.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        getMenuInflater().inflate(R.menu.menu_help,menu);

        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_border_white_24dp));

        setFavoriteStar();
        Log.d("FAVORITE", "here");

        final Uri uri = ActiveSession.getInstance().getGoogleSignInAccount().getPhotoUrl();
        if (uri != null) {
            // Get Google profile pic from url in new thread
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Retrieve URL
                        final Bitmap image = BitmapFactory.decodeStream(new URL(uri.toString()).openConnection().getInputStream());
                        // Update UI on main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getMenuInflater().inflate(R.menu.user_profile, menu);
                                MenuItem user_profile = menu.findItem(R.id.user_profile);
                                user_profile.setIcon(new BitmapDrawable(getResources(), image));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        else{
            getMenuInflater().inflate(R.menu.user_profile, menu);
        }
        return true;
    }

    public void setFavoriteStar(){
        final String userId = ActiveSession.getInstance().getUserId();
        Log.d("UserID", userId);
        final String restaurantId = String.valueOf(ActiveSession.getInstance().getRestaurant().getRestaurantID());

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String getFavoritesRequestURL = getResources().getString(R.string.api) + "users/" + userId + "/favorites";



        //If this restaurant is already on our favorites, then leave it
        // Send request with api/users/:userid/favorites


        //final String restaurantId = ActiveSession.getInstance().getRestaurant()

        // Request a string response from the provided URL.
        StringRequest favoritesRequest = new StringRequest(Request.Method.GET, getFavoritesRequestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Type listType = new TypeToken<ArrayList<com.autogarcon.android.API.Restaurant>>(){}.getType();
                        //Type listType = new TypeToken<ArrayList<com.autogarcon.android.API.Favorites>>(){}.getType();
                        List<com.autogarcon.android.API.Restaurant> favoritesList = new Gson().fromJson(response, listType);
                        Log.d("ISTHISCALLED", "YES");
                        inFavorites = APIUtils.currentlyFavorite(favoritesList);
                        if(inFavorites) {
                            Log.d("INFAVORITES", "TRUE");
                            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_white_24dp));
                            ActiveSession.getInstance().setFavoritesStarFlag(true);
                        } else{
                            Log.d("INFAVORITES", "FALSE");
                            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_border_white_24dp));
                            ActiveSession.getInstance().setFavoritesStarFlag(false);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEYERROR" ,"That didn't work!");
            }
        });
        MyRequestQueue.add(favoritesRequest);
        //return inFavorites;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.help) {
            //instantiate the popup.xml layout file
            LayoutInflater layoutInflater = (LayoutInflater) TopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.popup_help,null);
            ImageView closePopupBtn;
            final Button request;
            final PopupWindow popupWindow;

            closePopupBtn = (ImageView) customView.findViewById(R.id.closePopupBtn);
            request = (Button) customView.findViewById(R.id.requestButton);

            //instantiate popup window
            popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            //display the popup window
            popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

            //close the popup window on button click
            closePopupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            //send request help
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request.setText("Sent!");

                    //INSERT HTTP POST

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            popupWindow.dismiss();
                        }
                    }, 1000);
                }
            });
            return true;
        }

        if(id == R.id.favoritesswitch){

            final String userId = ActiveSession.getInstance().getUserId();
            Log.d("UserID", userId);
            final String restaurantId = String.valueOf(ActiveSession.getInstance().getRestaurant().getRestaurantID());

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

            String addToFavoritesURL = getResources().getString(R.string.api) + "users/" + userId + "/favorites/restaurant/" + restaurantId + "/add";
            String removeFromFavoritesURL = getResources().getString(R.string.api) + "users/" + userId + "/favorites/restaurant/" + restaurantId + "/remove";

            // If currently set to true, remove from favorites and change icon to border star
            if(ActiveSession.getInstance().getFavoritesStarFlag()){
                StringRequest removeRequest = new StringRequest(Request.Method.POST, removeFromFavoritesURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("REMOVED", "YES");
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_border_white_24dp));
                                ActiveSession.getInstance().setFavoritesStarFlag(false);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYERROR" ,"That didn't work!");
                    }
                });
                MyRequestQueue.add(removeRequest);
            }
            else{
                StringRequest addRequest = new StringRequest(Request.Method.POST, addToFavoritesURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("ADDED", "YES");
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_white_24dp));
                                ActiveSession.getInstance().setFavoritesStarFlag(true);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYERROR" ,"That didn't work!");
                    }
                });
                MyRequestQueue.add(addRequest);
            }

        }

        if(id == R.id.user_profile) {
            // Open the options menu
            Intent intent = new Intent(TopActivity.this, UserOptionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
     *     from an Activity</a>
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case Activity.RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                    default:
                        // Do nothing.
                }

                // Re-enables the Google Pay payment button.
                //mGooglePayButton.setClickable(true);
                break;
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     *     WalletConstants.ERROR_CODE_* constants.
     * @see <a
     *     href="https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants#constant-summary">
     *     Wallet Constants Library</a>
     */
    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see <a
     *     href="https://developers.google.com/pay/api/android/reference/object#PaymentData">Payment
     *     Data</a>
     */
    private void handlePaymentSuccess(PaymentData paymentData) {
        String paymentInformation = paymentData.toJson();

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        if (paymentInformation == null) {
            return;
        }
        JSONObject paymentMethodData;

        try {
            paymentMethodData = new JSONObject(paymentInformation).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type")
                    .equals("PAYMENT_GATEWAY")
                    && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
                    .equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog =
                        new AlertDialog.Builder(this)
                                .setTitle("Warning")
                                .setMessage(
                                        "Gateway name set to \"example\" - please modify "
                                                + "Constants.java and replace it with your own gateway.")
                                .setPositiveButton("OK", null)
                                .create();
                alertDialog.show();
            }

            String billingName =
                    paymentMethodData.getJSONObject("info").getJSONObject("billingAddress").getString("name");
            Log.d("BillingName", billingName);
            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG)
                    .show();

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData.getJSONObject("tokenizationData").getString("token"));
        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString());
            return;
        }
    }


    // This method is called when the Pay with Google button is clicked.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        //mGooglePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.

        double totalPrice = ActiveSession.getInstance().getTotalPrice();

        String price = Double.toString(totalPrice);

        // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }


    protected void onResume() {
        super.onResume();

        setFavoriteStar();

        // Apply the CustomTheme
        ActiveSession.getInstance().getCustomTheme().applyTo(this);

        if(ActiveSession.getInstance().getButtonFlag() == true){
            ActiveSession.getInstance().setButtonFlag(false);
            final Fragment reciept = new ReceiptFragment();
            navView.setSelectedItemId(R.id.menu_receipt);
            openFragment(reciept);
        }

    }
}

