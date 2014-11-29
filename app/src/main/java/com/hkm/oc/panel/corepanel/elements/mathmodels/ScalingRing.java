package com.hkm.oc.panel.corepanel.elements.mathmodels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.hkm.oc.panel.corepanel.elements.shapes.Element;
import com.hkm.oc.panel.corepanel.elements.Pen;

/**
 * Created by Hesk on 13.
 */
public class ScalingRing extends Element {
    private float gRadius, gCenterX, gCenterY;
    private float start_r, end_r;
    private Pen mpen;

    public ScalingRing(Canvas c, Context x, Pen common_pen) {
        super(c, x);
        mpen = common_pen;
    }

    public void motion_update_start() {
        start_r = gRadius;
    }

    public void motion_update_end() {
        end_r = gRadius;
    }

    public void motion_update(MotionEvent me) {
        //while the motion is updating
        final PointF newPoint1 = new PointF(me.getX(0), me.getY(0));
        final PointF newPoint2 = new PointF(me.getX(1), me.getY(1));
        final PointF gCenter = EQPool.centerPoint(newPoint1, newPoint2);
        gRadius = EQPool.dist(newPoint1, newPoint2) / 2;
        gCenterX = gCenter.x;
        gCenterY = gCenter.y;
    }

    @Override
    protected void rendering() {
        main.drawCircle(gCenterX, gCenterY, gRadius, mpen.getPaintInteraction());
    }
}
