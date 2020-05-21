package com.autogarcon.android;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

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
        setColors(colorPrimary,colorPrimaryDark,colorAccent);
    }

    /**
     * Constructs a theme by passing in two of the needed color values as strings.
     * @param colorPrimary The primary color of the theme.
     * @param colorAccent An accent color that may complement the primary color.
     */
    public CustomTheme(String colorPrimary, String colorAccent) {
        setColors(colorPrimary,null,colorAccent);
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
     * @param activity The activity that the theme will be applied to
     */
    public void applyTo(Activity activity) {
        activity.getWindow().setStatusBarColor(colorPrimaryDark);
        applyToView(activity.findViewById(android.R.id.content).getRootView());
    }

    /**
     * Private helper function. Applies the theme to a single view.
     * @param view The view that the theme will be applies to.
     */
    private void applyToView(View view) {
        Deque<View> viewStack = new ArrayDeque<>();
        viewStack.push(view);

        while(!viewStack.isEmpty()) {
            final View thisView = viewStack.pop();
            boolean colored = false;

            // If the view has a background tint
            if(thisView.getBackgroundTintList() != null) {
                // Attempts to set the tag
                if(getColorTag(thisView) == -1) {
                    for (int i=0; i<colorArray.length; i++) {
                        if(thisView.getBackgroundTintList().getDefaultColor() == originalColorArray[i]) {
                            setColorTag(thisView, i);
                        }
                    }
                }

                // Applies the color, if a tag is set
                if(getColorTag(thisView) != -1) {
                    thisView.setBackgroundTintList(new ColorStateList(new int[][] {new int[] {}}, new int[] {colorArray[getColorTag(thisView)]}));
                    colored = true;
                }
            }

            // If the view is a tinted image
            if(thisView instanceof ImageView) {
                ImageView thisImage = (ImageView) thisView;
                if(thisImage.getImageTintList() != null) {
                    // Attempts to set the tag
                    if(getColorTag(thisImage) == -1) {
                        for (int i=0; i<colorArray.length; i++) {
                            if(thisImage.getImageTintList().getDefaultColor() == originalColorArray[i]) {
                                setColorTag(thisImage, i);
                            }
                        }
                    }

                    // Applies the color, if a tag is set
                    if(getColorTag(thisImage) != -1) {
                        thisImage.setImageTintList(new ColorStateList(new int[][] {new int[] {}}, new int[] {colorArray[getColorTag(thisImage)]}));
                        colored = true;
                    }
                }
            }

            // If the view is a textView
            if(thisView instanceof TextView && !colored) {
                TextView thisTextView = (TextView) thisView;

                if(thisTextView.getTextColors().getDefaultColor() == myColorStateList.getDefaultColor()) {
                    thisTextView.setTextColor(myColorStateList);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        thisTextView.setCompoundDrawableTintList(myColorStateList);
                    }
                }
                else {


                    for(int i=0; i<3; i++) {
                        if(thisTextView.getTextColors().getDefaultColor() == originalColorArray[i]) {
                            thisTextView.setTextColor(colorArray[i]);
                            setColorTag(thisTextView, i);
                            i=100;
                        }
                    }

                    // Applies the color, if a tag is set
                    if(getColorTag(thisTextView) != -1) {
                        thisTextView.setTextColor(colorArray[getColorTag(thisTextView)]);
                        //thisView.setBackgroundTintList(new ColorStateList(new int[][] {new int[] {}}, new int[] {colorArray[getColorTag(thisView)]}));
                    }
                }
            }

            // If the view has a background
            if(thisView.getBackground() instanceof ColorDrawable  && !colored) {
                ColorDrawable thisColorDrawable = (ColorDrawable) thisView.getBackground();


                if(thisColorDrawable.getColor() == originalColorPrimary) {
                    thisColorDrawable.setTint(colorPrimary);
                    colored = true;
                }
                else if(thisColorDrawable.getColor() == originalColorPrimaryDark) {
                    thisColorDrawable.setTint(colorPrimaryDark);
                    colored = true;
                }
                else if(thisColorDrawable.getColor() == originalColorAccent) {
                    thisColorDrawable.setTint(colorAccent);
                    colored = true;
                }
            }

            // If the view is a toolbar
            if(thisView instanceof Toolbar) {
                ((Toolbar) thisView).setBackgroundColor(colorPrimary);
                colored = true;
            }

            // If the view is a group
            if(thisView instanceof  ViewGroup) {
                ViewGroup thisViewGroup = (ViewGroup)thisView;

                for(int i=0; i<thisViewGroup.getChildCount(); i++) {
                    View child = thisViewGroup.getChildAt(i);
                    viewStack.push(child);
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

    /**
     * Sets a color tag, that will be used to store  the original color of a view in the future.
     * @param view The view you want to set the tag on
     * @param value The value you want the tag to be
     */
    private void setColorTag(final View view, int value) {
        view.setTag(new Integer(value));
    }

    /**
     * Gets a color tag
     * @param view The view you want to get a tag from
     * @return The tag that you want to retrieve. -1 means it does not exist.
     */
    private int getColorTag(final View view) {
        if(view.getTag() instanceof Integer) {
            return (Integer)view.getTag();
        }
        return -1;
    }
}
