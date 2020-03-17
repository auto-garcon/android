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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);
        Intent intent = new Intent(getApplicationContext(), Signin.class);
        startActivity(intent);
    }
}