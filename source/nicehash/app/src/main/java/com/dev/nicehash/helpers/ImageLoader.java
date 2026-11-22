package com.dev.nicehash.helpers;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Alex Gladkov on 21.06.18.
 * class helper for loading images with Picasso library
 */
public class ImageLoader {

    public static void loadImage(String url, ImageView imageView) {
        if (url == null || imageView == null || url.equals("")) return;
        Picasso.get().load(url).into(imageView);
    }
}
