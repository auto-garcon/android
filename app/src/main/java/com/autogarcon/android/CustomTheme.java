package com.autogarcon.android;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A custom theme that can be applied to an activity, which will recolor all of it's themed text views and buttons.
 * @author Tim Callies
 */
public class CustomTheme {
    private Integer colorPrimary;
    private Integer colorPrimaryDark;
    private Integer colorAccent;
    private int[] colorArray;
    private ColorStateList myColorStateList;

    // DO NOT CHANGE THESE VALUES
    private final int originalColorPrimary = Color.parseColor("#505050");
    private final int originalColorPrimaryDark = Color.parseColor("#404040");
    private final int originalColorAccent = Color.parseColor("#909090");
    private int[] originalColorArray = {originalColorPrimary,originalColorPrimaryDark,originalColorAccent};

    /**
     * Generic constructor that creates a simple 'red' theme.
     * Author:   Tim Callies
     */
    public CustomTheme() {
        setColors("#457B9D", null, "#2B2D42");
    }

    /**
     * Constructs a theme by passing in all of the needed color values as strings.
     * @param colorPrimary The primary color of the theme.
     * @param colorPrimaryDark A darker variant of the primary color of the theme.
     * @param colorAccent An accent color that may complement the primary color.
     */
    public CustomTheme(String colorPrimary, String colorPrimaryDark, String colorAccent) {
        //TODO: If any of the colors are null, generate a suitable value for them.
        setColors(colorPrimary,colorPrimaryDark,colorAccent);
    }

    /**
     * Mostly a helper function. Used to set the colors of the theme.
     * @param colorPrimary The primary color of the theme.
     * @param colorPrimaryDark A darker variant of the primary color of the theme.
     * @param colorAccent An accent color that may complement the primary color.
     */
    public void setColors(String colorPrimary, String colorPrimaryDark, String colorAccent) {
        this.colorPrimary = Color.parseColor(colorPrimary);
        if(colorPrimaryDark  == null) {
            this.colorPrimaryDark = ColorUtils.blendARGB(this.colorPrimary, Color.BLACK, 0.2f);
        }
        else {
            this.colorPrimaryDark = Color.parseColor(colorPrimaryDark);
        }
        this.colorAccent = Color.parseColor(colorAccent);
        this.colorArray = new int[] {this.colorPrimary,this.colorPrimaryDark,this.colorAccent};

        this.myColorStateList = new ColorStateList(
                new int[][] {new int[] {-16842910}, new int[] {16842912}, new int[] {}},
                new int[] {1107296256, this.colorPrimary, -1979711488});
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
                Log.d("COLORZ", thisTextView.getTextColors().toString());
                if(thisTextView.getTextColors().getDefaultColor() == myColorStateList.getDefaultColor()) {
                    thisTextView.setTextColor(myColorStateList);
                }
                else {
                    for(int i=0; i<3; i++) {
                        if(thisTextView.getTextColors().getDefaultColor() == originalColorArray[i]) {
                            thisTextView.setTextColor(colorArray[i]);
                            i=100;
                        }
                    }
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
