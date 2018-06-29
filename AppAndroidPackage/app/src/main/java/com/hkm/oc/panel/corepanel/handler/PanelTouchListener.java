package com.hkm.oc.panel.corepanel.handler;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.hkm.oc.panel.corepanel.MapPanel;

/**
 * Listener for monitoring events about Touch panes.
 */
public interface PanelTouchListener {
    /**
     * Called when a Touch pane's position changes.
     */
    public void onDown(MapPanel panel, MotionEvent me, Canvas Panelcanvas);

    public void onStream(MapPanel panel);

    public void onMode(int mode_constant);

    public void onUp(MapPanel panel, MotionEvent me, Canvas Panelcanvas, int mode);

    public void onInitDialog(MapPanel panel, int resid);

    public void toastMsg(String message);
}
