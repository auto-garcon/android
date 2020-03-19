package com.autogarcon.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.Gravity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static java.lang.Thread.sleep;

/**
 * The top level navigation for the menu of a single restaurant. This activity contains a navigation bar, as well
 * as two fragments.
 * @author Tim Callies
 */
public class TopActivity extends AppCompatActivity {
    
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        final Fragment reciept = new ReceiptFragment();
        final Fragment menu = new MenuListFragment();
        constraintLayout = (ConstraintLayout) findViewById(R.id.container);

        String intentFragment = getIntent().getExtras().getString("frgToLoad");

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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.menu_help,menu);

        return true;
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

        return super.onOptionsItemSelected(item);
    }
}