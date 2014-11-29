package com.hkm.oc.panel.corepanel.handler;


import android.content.Context;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.hkm.oc.R;

/**
 * Created by hesk on 8/18/13.
 */
public class PaintsetBase {
    public static final int
            BASIC_DOT = 3,
            BASIC_DOT_READY = 19,
            BASIC_DOT_SELECTED = 20,
            BASIC_DOT_DARK = 4,
            BASIC_DOT_LIGHT = 5,
            DOT_LABEL = 6,
            BASIC_DOT_HOLO_LIGHT = 7,
            OBSERVED_RADIUS_CIRCLE = 2,
            INTERCEPT_DARK_READY = 8,
            INTERCEPT_DARK_NOTREADY = 9,
            INTERCEPT_DARK_SELECTED = 10,
            INTERCEPT_LIGHT_READY = 11,
            INTERCEPT_LIGHT_NOTREADY = 12,
            INTERCEPT_LIGHT_SELECTED = 13,
            BASIC_CROSS_DARK = 14,
            BASIC_CROSS_LIGTH = 15,
            HOLO_DOT = 16,
            HOLO_DOT_DARK = 17,
            HOLO_DOT_LIGHT = 18,
            CHILD_DOT = 22,
            CHILD_DOT_READY = 23,
            CHILD_DOT_SELECTED = 24,
            LABEL_RESULT_ROUTE_POINT = 21,
            RED_LINE = 1;
    private static Paint
            p_label,
            p_filled_blue_plus,
            p_line_cross_hair,
            p_line_solid,
            p_line_dash,
            p_filled_red,
            p_filled_blue,
            p_indication_center,
            p_indication,
            pdot_label;
    private final PathEffect f1 = new DashPathEffect(new float[]{10, 20}, 0);
    private final PathEffect f2 = new CornerPathEffect(10);
    private final PathEffect f3 = new PathDashPathEffect(makePathDashArrow(), 12, 0, PathDashPathEffect.Style.ROTATE);
    private Context ctx;

    public PaintsetBase(Context c) {
        ctx = c;
        final Paint basic = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Paint basic_paint = new Paint();
        basic_paint.set(basic);
        basic_paint.setStyle(Paint.Style.FILL);
        final Paint basic_line = new Paint();
        basic_line.set(basic);
        basic_line.setStyle(Paint.Style.STROKE);

        p_label = new Paint();
        p_label.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        p_label.setTextSize(20);
        p_label.setTextScaleX(1.f);
        p_label.setAlpha(0);
        p_label.setAntiAlias(true);


        pdot_label = new Paint();
        pdot_label.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        pdot_label.setTextSize(10);
        pdot_label.setTextScaleX(1.f);
        pdot_label.setAlpha(0);
        pdot_label.setAntiAlias(true);


        p_line_solid = new Paint();
        p_line_solid.set(basic_line);
        p_line_solid.setStrokeWidth(2.9f);
        p_line_solid.setColor(Color.RED);
        //p_line_solid.setPathEffect(new ComposePathEffect(f1, f3));

        p_line_cross_hair = new Paint();
        p_line_cross_hair.set(basic_line);
        p_line_cross_hair.setAlpha(80);
        p_line_cross_hair.setColor(Color.WHITE);

        p_line_dash.set(basic_line);
        p_line_dash.setPathEffect(f1);
        p_line_dash.setStrokeWidth(1.3f);

        p_filled_red = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_filled_red.setColor(Color.RED);


        //pair dot color
        p_filled_blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_filled_blue.setColor(Color.BLUE);
        p_filled_blue.setAlpha(50);

        //pair dot color ON
        p_filled_blue_plus = new Paint(p_filled_blue);
        p_filled_blue_plus.setColor(Color.GREEN);
        p_filled_blue_plus.setAlpha(80);

        //pair dot color
        p_indication_center = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_indication_center.setColor(Color.GREEN);
        p_indication_center.setAlpha(50);
        p_indication_center.setStrokeWidth(2);

        //================================
        p_indication = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_indication.setStyle(Paint.Style.FILL);
        p_indication.setColor(Color.CYAN);
        p_indication.setAlpha(90);

    }

    public static Paint k_paint() {
        Paint basic = new Paint(Paint.ANTI_ALIAS_FLAG);
        basic.setStyle(Paint.Style.FILL);
        basic.setColor(Color.YELLOW);
        return basic;
    }

    private static Path makePathDashArrow() {
        Path p = new Path();
        p.moveTo(4, 0);
        p.lineTo(0, -4);
        p.lineTo(8, -4);
        p.lineTo(12, 0);
        p.lineTo(8, 4);
        p.lineTo(0, 4);
        return p;
    }

    public static Paint lineMeter() {
        final Paint p_indication = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_indication.setStyle(Paint.Style.FILL_AND_STROKE);
        p_indication.setColor(Color.BLUE);
        p_indication.setTextSize(10f);
        // p_indication.setAlpha(100);
        return p_indication;
    }

    public static Paint dotLabel() {
        final Paint p_indication = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_indication.setStyle(Paint.Style.FILL_AND_STROKE);
        p_indication.setColor(Color.GRAY);
        p_indication.setTextSize(5f);
        return p_indication;
    }

    //get the list of the pain from the set developments
    public Paint g(int n) {
        Paint p = new Paint();
        switch (n) {
            case PaintsetBase.BASIC_CROSS_LIGTH:
                p.set(p_line_cross_hair);
                break;
            case PaintsetBase.BASIC_CROSS_DARK:
                p.set(p_line_cross_hair);
                p.setColor(Color.BLACK);
                break;
            case PaintsetBase.RED_LINE:
                p.set(p_line_solid);
                break;
            case PaintsetBase.OBSERVED_RADIUS_CIRCLE:
                p.set(p_line_dash);
                break;
            case PaintsetBase.BASIC_DOT:
                p.set(p_indication);
                p.setColor(ctx.getResources().getColor(R.color.dot_basic_dark_cross_normal));
                break;
            case PaintsetBase.BASIC_DOT_READY:
                p.set(p_indication);
                p.setColor(ctx.getResources().getColor(R.color.dot_basic_dark_face_active));
                p.setAlpha(70);
                break;
            case PaintsetBase.BASIC_DOT_SELECTED:
                p.set(p_indication);
                p.setColor(ctx.getResources().getColor(R.color.dot_basic_dark_cross_pressed));
                p.setAlpha(80);
                break;
            case PaintsetBase.CHILD_DOT:
                p.set(p_indication);
                p.setColor(ctx.getResources().getColor(R.color.dot_intercept_dark_face_normal));
                p.setAlpha(80);
                break;
            case PaintsetBase.CHILD_DOT_READY:
                p.set(p_indication);
                p.setColor(ctx.getResources().getColor(R.color.dot_intercept_dark_face_active));
                p.setAlpha(80);
                break;
            case PaintsetBase.CHILD_DOT_SELECTED:
                p.set(p_indication);
                p.setColor(ctx.getResources().getColor(R.color.dot_intercept_dark_face_selected));
                p.setAlpha(80);
                break;
            case PaintsetBase.BASIC_DOT_DARK:
                p.set(p_indication);
                //   p.setColor(ctx.getResources().getColor(R.color.holo_blue_dark));
                break;
            case PaintsetBase.BASIC_DOT_LIGHT:
                p.set(p_indication);
                //  p.setColor(ctx.getResources().getColor(R.color.holo_blue_bright));
                break;
            case PaintsetBase.HOLO_DOT:
                p.set(p_indication);
                //   p.setColor(ctx.getResources().getColor(R.color.holo_blue_light));
                break;
            case PaintsetBase.HOLO_DOT_DARK:
                p.set(p_indication);
                //  p.setColor(ctx.getResources().getColor(R.color.holo_blue_dark));
                break;
            case PaintsetBase.HOLO_DOT_LIGHT:
                p.set(p_indication);
                //   p.setColor(ctx.getResources().getColor(R.color.holo_blue_bright));
                break;
            case PaintsetBase.LABEL_RESULT_ROUTE_POINT:
                p.set(p_label);
                break;
            case PaintsetBase.DOT_LABEL:
                p.set(pdot_label);
            default:
                break;
        }

        return p;
    }
}
