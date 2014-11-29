package com.hkm.api.handlers;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Hesk on 12/6/2014.
 */
public class PicassoTarget implements Target {
    public FrameLayout frame;
    public TextView name;

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

    }

    /**
     * Callback indicating the image could not be successfully loaded.
     *
     * @param errorDrawable
     */
    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    /**
     * Callback invoked right before your request is submitted.
     *
     * @param placeHolderDrawable
     */
    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}