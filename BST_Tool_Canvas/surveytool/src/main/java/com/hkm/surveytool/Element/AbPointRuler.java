package com.hkm.surveytool.Element;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.hkm.surveytool.AppDataRetain;
import com.hkm.surveytool.Constant;
import com.hkm.surveytool.Element.DataPoints.BigObserveDot;
import com.hkm.surveytool.Element.Math.EQPool;
import com.hkm.surveytool.Element.Math.Geometry;
import com.hkm.surveytool.Element.Shapes.Element;
import com.hkm.surveytool.MapPanel;
import com.hkm.surveytool.R;

import java.util.ArrayList;

public class AbPointRuler extends Element implements View.OnClickListener {
    public final PointF cursor = new PointF();
    final Paint p_default = new Paint();
    private final PointF RulerPointA = new PointF(), RulerPointB = new PointF();
    public boolean onPressed, onA, onB, enableRuler;
    private ArrayList<PointF> containDots = new ArrayList<PointF>();
    private ArrayList<Integer> containOrders = new ArrayList<Integer>();
    private Canvas main;
    private int routesize;
    private Pen mpen;
    private boolean closegap, onSBmode;
    private float pixel_dist = 0f;
    private float actual_distance = 10f;
    private float ratio_m = 1f;
    private MapPanel mpanel;
    private Rect rc;
    private RChange listener;
    public static String TAG = "The Ruler::AbPointRuler";
    private BigObserveDot realA, realB;

    public AbPointRuler(Canvas mainCanvas, BigObserveDot RefPointA, BigObserveDot RefPointB, MapPanel panel, Pen pen) {
        p_default.setStyle(Paint.Style.STROKE);
        p_default.setStrokeWidth(1.9f);
        mpanel = panel;
        p_default.setColor(panel.getResources().getColor(R.color.holo_blue_dark));
        p_default.setAntiAlias(true);
        p_default.setPathEffect(new DashPathEffect(new float[]{2, 10, 50, 10}, 0));
        mpen = pen;
        RulerPointB.set(RefPointB);
        RulerPointA.set(RefPointA);
        realA = RefPointA;
        realB = RefPointB;
        actual_distance = AppDataRetain.current_sketch_map.getRealDistanceAB();
        enableRuler = true;
    }

    public void touch_detection(PointF onScreenPoint) {
        if (enableRuler) {
            cursor.set(onScreenPoint);
            if (onPressed && EQPool.onPressedCicle(RulerPointA, cursor, 59f)) {
                onA = true;
                onB = false;
                RulerPointA.set(onScreenPoint);
            } else if (onPressed && EQPool.onPressedCicle(RulerPointB, cursor, 59f)) {
                onB = true;
                onA = false;
                RulerPointB.set(onScreenPoint);
            }
        }
    }

    public AbPointRuler setRatioChangeListener(RChange listener) {
        this.listener = listener;
        return this;
    }

    public PointF getA() {
        return RulerPointA;
    }

    public PointF getB() {
        return RulerPointB;
    }

    /*   private void UpdateDistanceRatio() {
           pixel_dist = ToolBox.dist(RulerPointA, RulerPointB);
           ratio_m = ToolBox.measure_distance_ratio(RulerPointA, RulerPointB, actual_distance);
       }
   */

    public interface RChange {
        public void changeInvaliate();
    }


    public String showBarInfo() {
        ratio_m = (float) AppDataRetain.current_sketch_map.getratio();
        String text = String.format("Distance: %1$.2f; Machine Ratio: 1: %2$.1f", actual_distance, ratio_m);
        return text;
    }

    private void updateBounce() {
        rc = main.getClipBounds();
    }

    public String setMeterNow(float meter) {
        AppDataRetain.ratio_changed = true;
        actual_distance = meter;
        return ratio_calculate(meter, RulerPointA, RulerPointB);
    }

    public void setHorizontalOrientation() {
        final float start = rc.centerY() - rc.height() / 2 + 100;
        final float mid = (RulerPointA.x + RulerPointB.x) / 2;
        RulerPointB.set(new PointF(mid, start));
        RulerPointA.set(new PointF(mid, start + pixel_dist));
    }

    public void setVerticalOrientation() {
        final float start = rc.centerX() - rc.width() / 2 + 100;
        final float mid = (RulerPointA.y + RulerPointB.y) / 2;
        RulerPointB.set(new PointF(start, mid));
        RulerPointA.set(new PointF(start + pixel_dist, mid));
    }

    public AbPointRuler setEnable(boolean b) {
        enableRuler = b;
        return this;
    }

    public AbPointRuler toggle() {
        enableRuler = !enableRuler;
        return this;
    }


    @Override
    protected void rendering() {
        if (enableRuler) {
            main = mpanel.getCanvas();
            updateBounce();
            main.drawPath(EQPool.ruler(RulerPointB, RulerPointA), p_default);
            //mpen.render_noref_maindoticon(RulerPointA, RulerPointB);
            if (!onPressed && (onB || onA)) {
                onB = onA = false;
                AppDataRetain.ratio_changed = true;
                ratio_calculate(actual_distance, RulerPointA, RulerPointB);
                if (this.listener != null) this.listener.changeInvaliate();
            }
            mpen.render_noref_touch(realA, realB, RulerPointA, RulerPointB, onA, onB);
        } else {
            main.drawPath(EQPool.ruler(RulerPointB, RulerPointA), p_default);
            mpen.render_noref_maindoticon(RulerPointA, RulerPointB);
            //RulerPointA, RulerPointB, onA, onB);
        }
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * this is response to OneCall requirement to set Point A and Point B to the current measured AB points on the Basemap
     *
     * @param a
     * @param b
     */
    public void setABPoints(PointF a, PointF b) {
        RulerPointB.set(b);
        RulerPointA.set(a);
    }

    public void ApplyCurrentABIntoPanelAB() {
        mpanel.getA().set(RulerPointA);
        mpanel.getB().set(RulerPointB);
    }

    public static String ratio_calculate(final float meter, final PointF A, final PointF B) {
        final float screen_distance_dp = Geometry.distance(A, B);
        //    final double compare = Math.ceil(meter / screen_distance);
        final double screen2inches = screen_distance_dp / AppDataRetain.dpi;
        final double displaymeter = Constant.inch_meter_ratio * screen2inches;
        final double compare = meter / displaymeter;
        final String e = "Original ratio::" +
                AppDataRetain.current_sketch_map.getratio()
                +
                ". :: meter::" + meter +
                " : screen distance: "
                + screen_distance_dp + ", New Ratio 1:" + compare;
        Log.d(TAG, e);
        AppDataRetain.current_sketch_map.setRatio(compare);
        AppDataRetain.current_sketch_map.setRealDistanceAB(meter);
        return e;
    }
}