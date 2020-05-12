package com.autogarcon.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Redirects to the signin page
 * @author Mitchell Nelson
 */
public class MainActivity extends AppCompatActivity {
    public static Context contextOfApplication;

    @Override
    protected void onResume() {
        super.onResume();
        // Apply the CustomTheme
        ActiveSession.getInstance().getCustomTheme().applyTo(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();
        setContentView(R.layout.activity_signin);
        Intent intent = new Intent(getApplicationContext(), Signin.class);
        startActivity(intent);
    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}