package com.hkm.api.handlers;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by Hesk on 12/6/2014.
 */
public class PicassoTransform implements Transformation {
    private int maxwidth;

    public PicassoTransform(int maxwidth) {
        this.maxwidth = maxwidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (maxwidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(source, maxwidth, targetHeight, false);
        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }


        return result;
    }

    @Override
    public String key() {
        return "transformation" + " desiredWidth";
    }
}
