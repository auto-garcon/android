package com.autogarcon.android;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A custom theme that can be applied to an activity, which will recolor all of it's themed text views and buttons.
 * @author Tim Callies
 */
public class CustomTheme {
    private int colorPrimary;
    private int colorPrimaryDark;
    private int colorAccent;
    private final int originalColorPrimary = Color.parseColor("#333a4f");
    private final int originalColorPrimaryDark = Color.parseColor("#333a4f");
    private final int originalColorAccent = Color.parseColor("#333a4f");

    /**
     * Generic constructor that creates a simple 'red' theme.
     * Author:   Tim Callies
     */
    public CustomTheme() {
        this.colorPrimary = Color.parseColor("#333a4f");
        this.colorPrimaryDark = Color.parseColor("#333a4f");
        this.colorAccent = Color.parseColor("#546b60");
    }

    /**
     * Constructs a theme by passing in all of the needed color values as strings.
     * @param colorPrimary The primary color of the theme.
     * @param colorPrimaryDark A darker variant of the primary color of the theme.
     * @param colorAccent An accent color that may complement the primary color.
     */
    public CustomTheme(String colorPrimary, String colorPrimaryDark, String colorAccent) {
        //TODO: If any of the colors are null, generate a suitable value for them.
        this.colorPrimary = Color.parseColor(colorPrimary);
        this.colorPrimaryDark = Color.parseColor(colorPrimaryDark);
        this.colorAccent = Color.parseColor(colorAccent);
    }

    /**
     * Applies the theme to a activity. Only call this after the view has been inflated.
     * Author: Tim Callies
     * @param activity The activity that the theme will be applied to
     */
    public void applyTo(Activity activity) {
        activity.getWindow().setStatusBarColor(colorPrimaryDark);
        applyToView(activity.findViewById(android.R.id.content).getRootView());
    }

    /**
     * Private helper function. Applies the theme to a single view.
     * Author: Tim Callies
     * @param view The view that the theme will be applies to.
     */
    private void applyToView(View view) {
        Deque<View> viewStack = new ArrayDeque<>();
        viewStack.push(view);

        while(!viewStack.isEmpty()) {
            final View thisView = viewStack.pop();

            Log.d("VIEW", thisView.getClass().getName());

            // If the view has a background
            if(thisView.getBackground() instanceof ColorDrawable) {
                ColorDrawable thisColorDrawable = (ColorDrawable) thisView.getBackground();

                if(thisColorDrawable.getColor() == originalColorPrimary) {
                    thisColorDrawable.setColor(colorPrimary);
                }
                else if(thisColorDrawable.getColor() == originalColorPrimaryDark) {
                    thisColorDrawable.setColor(colorPrimaryDark);
                }
                else if(thisColorDrawable.getColor() == originalColorAccent) {
                    thisColorDrawable.setColor(colorAccent);
                }
            }


            // If the view is a toolbar
            if(thisView instanceof Toolbar) {
                ((Toolbar) thisView).setBackgroundColor(colorPrimary);
            }

            // If the view is a group
            if(thisView instanceof  ViewGroup) {
                ViewGroup thisViewGroup = (ViewGroup)thisView;

                for(int i=0; i<thisViewGroup.getChildCount(); i++) {
                    View child = thisViewGroup.getChildAt(i);
                    viewStack.push(child);
                }
            }

            // If the view is a textView
            if(thisView instanceof TextView) {
                TextView thisTextView = (TextView) thisView;

                if(thisTextView.getCurrentTextColor() == originalColorPrimary) {
                    thisTextView.setTextColor(colorPrimary);
                }
                else if(thisTextView.getCurrentTextColor() == originalColorPrimaryDark) {
                    thisTextView.setTextColor(colorPrimaryDark);
                }
                else if(thisTextView.getCurrentTextColor() == originalColorAccent) {
                    thisTextView.setTextColor(colorAccent);
                }
            }

            // If the view is a recyclerview
            if(thisView instanceof RecyclerView) {
                RecyclerView thisRecyclerView = (RecyclerView) thisView;

                thisRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(@NonNull View view) {
                        applyToView(view);
                    }

                    @Override
                    public void onChildViewDetachedFromWindow(@NonNull View view) {

                    }
                });
            }
        }
    }
}
