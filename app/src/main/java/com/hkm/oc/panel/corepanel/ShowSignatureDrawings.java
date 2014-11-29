package com.hkm.oc.panel.corepanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Hesk on 13/10/07
 */
public class ShowSignatureDrawings extends Activity {
    final int LEGEND_ICON_PICK = 2313;
    MapPanel panel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getIntent().hasExtra("CAPTUREIMAGE")) {
                Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("CAPTUREIMAGE");
                ImageView previewThumbnail = new ImageView(this);
                previewThumbnail.setImageBitmap(bitmap);
            }
            if (getIntent().hasExtra("DRAWPANEL")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(new capture(this));
    }

    private class capture extends View {

        public capture(Context ctx) {
            super(ctx);
        }

        @Override
        public void onDraw(Canvas c) {
         //   panel.finalDraw(c);
        }
    }

}
