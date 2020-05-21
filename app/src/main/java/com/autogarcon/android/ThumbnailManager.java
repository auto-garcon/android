package com.autogarcon.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Manages the thumbnails that will be temporarily stored on the system as we scroll through the menu.
 * @author Tim Callies
 */
public class ThumbnailManager {
    private Map<String, Bitmap> bitmapMap;
    private Map<String, Deque<ImageView>> imageViewMap;

    private static final ThumbnailManager ourInstance = new ThumbnailManager();

    /**
     * Singleton constructor
     * @return The singleton instance of the class.
     *
     * Author: Tim Callies
     */
    public static ThumbnailManager getInstance() {
        return ourInstance;
    }

    private ThumbnailManager() {
        bitmapMap = new HashMap<>();
        imageViewMap = new HashMap<>();
    }

    /**
     * Pass in an ImageView, along with the URL, and the thumbnail manager will make an HTTP request and lazily update the
     * ImageView as soon as it gets a response. The image will be stored, so it can be accessed in the future without
     * needing an HTTP request
     * @param url The URL where the image exists.
     * @param image The ImageView that will be updated once the image is recieved
     *
     * Author: Tim Callies
     */
    public void getImage(String url, ImageView image) {
        if(bitmapMap.containsKey(url)) {
            image.setImageBitmap(bitmapMap.get(url));
            image.setImageTintList(null);
        }
        else {
            addImageToStack(url, image);
            helper(url,image,true);
        }
    }


    /**
     * Pass in an ImageView, along with the URL, and the thumbnail manager will make an HTTP request and lazily update the
     * ImageView as soon as it gets a response. The image will be stored, so it can be accessed in the future without
     * needing an HTTP request
     * @param url The URL where the image exists.
     * @param image The ImageView that will be updated once the image is recieved
     *
     * Author: Tim Callies
     */
    public void getTemporaryImage(String url, ImageView image) {
        helper(url,image,false);
    }

    /**
     * Helper method for both getImage methods. Performs the actual work.
     * @param url The URL where the image exists.
     * @param imageView The ImageView that will be updated once the image is recieved
     * @param store States whether or not the image will be stored after it is recieved.
     */
    private void helper(final String url, final ImageView imageView, final boolean store) {
        if(url == null) {
            return;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MYURL",url);
                    final Bitmap image = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
                    if(store) {
                        bitmapMap.put(url, image);
                    }

                    if(imageView.getContext() instanceof Activity) {
                        ((Activity)imageView.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Deque<ImageView> deque = imageViewMap.get(url);
                                while(!deque.isEmpty()) {
                                    ImageView item = deque.pop();
                                    item.setImageBitmap(image);
                                    item.setImageTintList(null);
                                }
                            }
                        });
                    }
                } catch (IOException ioe) {
                    Log.d("BITMAP", "Thumbnail could not be read");
                }
            }
        });
        t.start();

    }


    /**
     * Adds a single ImageView to a stack that will be updated once the HTTP request comes back.
     * @param url The URL of the image
     * @param imageView The image that will be updated.
     */
    private void addImageToStack(String url, ImageView imageView) {
        Deque deque;
        if(imageViewMap.containsKey(url)) {
            deque = imageViewMap.get(url);
        }
        else {
            deque = new LinkedList();
            imageViewMap.put(url, deque);
        }
        deque.push(imageView);
    }
}
