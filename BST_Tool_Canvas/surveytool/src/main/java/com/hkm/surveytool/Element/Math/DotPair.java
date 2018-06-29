package com.hkm.surveytool.Element.Math;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.ColorRes;


/**
 * Created by Hesk on
 * can render circle round the dot
 * can also render other things
 */
public class DotPair {
    final Paint p_default = new Paint();
    final Paint p_default_2 = new Paint();
    boolean enable_reveal;
    private PointF p0 = new PointF();
    private PointF p1 = new PointF();

    public DotPair(Context res) {
        enable_reveal = true;
        p_default.setStyle(Paint.Style.FILL);
        p_default.setAntiAlias(true);
        p_default.setAlpha(80);
        p_default_2.set(p_default);
    }

    public void setDefaulColors(Context res, @ColorRes int pdefault_1, @ColorRes int pdefault_2) {
        p_default.setColor(res.getResources().getColor(pdefault_1));
        p_default_2.setColor(res.getResources().getColor(pdefault_2));
    }

    public void setPair(Point[] p) {
        p0 = new PointF(p[0]);
        p1 = new PointF(p[1]);
    }

    public void setPair(PointF a, PointF b) {
        p0 = a;
        p1 = b;
    }

    public void rendering(Canvas pointer) {
        pointer.drawCircle(p0.x, p0.y, 50, p_default);
        pointer.drawCircle(p0.x, p0.y, 10, p_default_2);
        pointer.drawCircle(p1.x, p1.y, 50, p_default);
        pointer.drawCircle(p1.x, p1.y, 10, p_default_2);
    }
}
