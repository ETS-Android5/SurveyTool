package com.hkm.surveytool.Listener;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by Hesk on
 */
public interface Displacement {
    void displacementReset();

    PointF getDisplacement();

    void onDown(MotionEvent me, PointF down_location);

    void onUp(MotionEvent me, PointF up_location);
}
