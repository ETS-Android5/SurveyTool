package com.hkm.surveytool.Listener;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.hkm.surveytool.MapPanel;


/**
 * Listener for monitoring events about Touch panes.
 */
public interface PanelTouchListener {
    /**
     * Called when a Touch pane's position changes.
     */
    void onDown(MapPanel panel, MotionEvent me, Canvas Panelcanvas);

    void onStream(MapPanel panel);

    void onMode(int mode_constant);

    void onUp(MapPanel panel, MotionEvent me, Canvas Panelcanvas, int mode);

    void onInitDialog(MapPanel panel, int resid);

    void toastMsg(String message);
}
