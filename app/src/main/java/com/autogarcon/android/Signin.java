package com.autogarcon.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.autogarcon.android.API.SignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to integrate Google Sign-In on the application
 * MainActivity redirects to this activity once the application loads
 * @author Riley Tschumper
 */
public class Signin extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 22;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        error = (TextView) findViewById(R.id.error);




        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        findViewById(R.id.button_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras() != null){
            String action = getIntent().getExtras().getString("action");
            signOut();
        }
    }

    /**
     * When the activity enters the Started state, the system invokes this callback
     * Checks for existing signed in account
     * @author Riley Tschumper
     */
    protected void onStart(){
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // If signed in, go straight to landing page
        if (account != null){
            ActiveSession.getInstance().setGoogleSignInAccount(account);
            Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Invoked by clicking on the Sign-in button
     * Starts the signInIntent for the client
     * @author Riley Tschumper
     */
    private void signIn() {
        error.setText("");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Display information on the UI and move onto next activity
     * @param completedTask a returned Task of GoogleSignInAccount type
     * @author Riley Tschumper Mitchell Nelson
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);



            // Set ActiveSession account info
            ActiveSession.getInstance().setGoogleSignInAccount(account);

            SignInRequest request = new SignInRequest();
            request.setEmail(ActiveSession.getInstance().getGoogleSignInAccount().getEmail());
            request.setFirstName(ActiveSession.getInstance().getGoogleSignInAccount().getGivenName());
            request.setLastName(ActiveSession.getInstance().getGoogleSignInAccount().getFamilyName());
            request.setToken(ActiveSession.getInstance().getGoogleSignInAccount().getId());
            final String jsonBody = new Gson().toJson(request);


            // Post to server to gain userId
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getResources().getString(R.string.api) + "users/signin";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Set the userId from server
                        ActiveSession.getInstance().setUserId(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("signin","Request Failure");
                    }
                })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return jsonBody.getBytes();
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

            Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("FAILURE", "signInResult:failed code=" + e.getStatusCode());
            error.setText("Something went wrong. Please try again.");

        }
    }
}
