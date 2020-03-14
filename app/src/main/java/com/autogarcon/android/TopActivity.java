package com.autogarcon.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class TopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        final Fragment reciept = new ReceiptFragment();
        final Fragment menu = new MenuListFragment();

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
                CustomTheme theme = new CustomTheme();
                theme.applyTo(TopActivity.this);
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

}