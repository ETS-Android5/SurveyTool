package com.hkm.oc.panel.corepanel.handler;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by Hesk on
 */
public interface Displacement {
    public void displacementReset();

    public PointF getDisplacement();

    public void onDown(MotionEvent me, PointF down_location);

    public void onUp(MotionEvent me, PointF up_location);
}
