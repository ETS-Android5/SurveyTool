package com.hkm.oc.panel.corepanel.elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.hkm.U.Content;
import com.hkm.datamodel.BigObserveDot;
import com.hkm.datamodel.RouteNode;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Hesk on draw any thing on the paper images
 */
public class Pen {
    private final PathEffect f1 = new DashPathEffect(new float[]{10, 20}, 0);
    private final PathEffect f2 = new CornerPathEffect(10);
    private final PathEffect f3 = new PathDashPathEffect(makePathDashArrow(), 12, 0, PathDashPathEffect.Style.ROTATE);
    private final Point MainDotDrawableSize = new Point();
    private Canvas main;
    private Context ctx;
    private Paint minteractive_blue,
            minteractive_press_blue,
            p_line_dash,
            p_line_cross_hair, p_label, red_circle_paint, bm_paint, bm_paint_dot;
    private Bitmap MainDotDrawable;

    public Pen(Canvas cc, Context context) {
        ctx = context;
        main = cc;
        setmaterial();
    }

    public Paint getSurveyBoundaryPaint() {
        Paint p_default = new Paint();

        p_default.setStyle(Paint.Style.STROKE);
        p_default.setStrokeWidth(1.9f);
        p_default.setColor(ctx.getResources().getColor(R.color.oc_sb));
        p_default.setAntiAlias(true);
        p_default.setPathEffect(new DashPathEffect(new float[]{2, 10, 50, 10}, 0));

        return p_default;
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

    private static Paint setRColor(Paint p_default) {
        final Random rnd = new Random();
        final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        p_default.setColor(color);
        return p_default;
    }

    private static Paint setColorCode(Paint p_default, String kk) {
        p_default.setColor(Color.parseColor(kk));
        return p_default;
    }

    public static void paintcolor(Canvas paper) {
        Paint cf = new Paint();
        cf.setStyle(Paint.Style.FILL);
        Pen.setColorCode(cf, "#FFFFFF");
        paper.drawPaint(cf);
    }

    /**
     * @param a validMarkerPostion
     * @param b test_result_potential
     */
    public static void render_each_crosses(ArrayList<Integer> a, ArrayList<HashMap<Dot, Dot>> b) {
        if (a.size() > 0) {
            for (int i = 0; i < a.size(); i++) {
                //get the working pair's order
                final Integer order = a.get(i);
                final HashMap<Dot, Dot> hashPair = b.get(order);
                final Dot dotA = (Dot) hashPair.keySet().toArray()[0];
                final Dot dotB = (Dot) hashPair.values().toArray()[0];
                dotA.onDraw();
                dotB.onDraw();
            }
        }
    }

    public static void render_each_crosses(Canvas mcanvas, ArrayList<Integer> a, ArrayList<HashMap<Dot, Dot>> b) {
        if (a.size() > 0) {
            for (int i = 0; i < a.size(); i++) {
                //get the working pair's order
                final Integer order = a.get(i);
                final HashMap<Dot, Dot> hashPair = b.get(order);
                final Dot dotA = (Dot) hashPair.keySet().toArray()[0];
                final Dot dotB = (Dot) hashPair.values().toArray()[0];
                dotA.onDraw(mcanvas);
                dotB.onDraw(mcanvas);
            }
        }
    }

    public void render_testing_pairs_results(int results, PointF[] AB_points) {
        final int should_be_results_total = Content.current_sketch_map.get_route_size();
        String label_distance;
        if (results == 0) {
            label_distance = "There is nothing found.";
        } else if (results == 1) {
            label_distance = "Only " + results + " is found out of " + should_be_results_total + ".";
        } else if (results > 1) {
            label_distance = "Only " + results + " are found out of " + should_be_results_total + ".";
        } else if (results == -1) {
            label_distance = "All set.";
        } else {
            label_distance = "All set.";
        }

        final PointF c = EQPool.centerPoint(AB_points[0], AB_points[1]);
        rtext(label_distance, c.x, c.y, true);
    }

    public Paint getPaintInteraction() {
        return red_circle_paint;
    }

    public void render_ref_touch(BigObserveDot pointA, BigObserveDot pointB, boolean interaction_a, boolean interaction_b) {
        String a = pointA.getlabel().isEmpty() ? "Ref. Point A" : pointA.getlabel();
        String b = pointB.getlabel().isEmpty() ? "Ref. Point B" : pointB.getlabel();
        render_interactive_circle(a, pointA, interaction_a);
        render_interactive_circle(b, pointB, interaction_b);
    }

    public void render_noref_touch(PointF pointA, PointF pointB, boolean interaction_a, boolean interaction_b) {
        render_interactive_circle("", pointA, interaction_a);
        render_interactive_circle("", pointB, interaction_b);
    }

    public void render_noref_touch(BigObserveDot a, BigObserveDot b, PointF pointA, PointF pointB, boolean interaction_a, boolean interaction_b) {
        render_interactive_circle(a.getlabel(), pointA, interaction_a);
        render_interactive_circle(b.getlabel(), pointB, interaction_b);
    }

    public void render_ref_maindoticon(PointF pointA, PointF pointB) {
        render_only_main_dot("A", pointA);
        render_only_main_dot("B", pointB);
    }

    public void render_noref_maindoticon(PointF pointA, PointF pointB) {
        render_only_main_dot("", pointA);
        render_only_main_dot("", pointB);
    }

    public void render_noref_maindoticon_canvas(BigObserveDot pointA, BigObserveDot pointB, Canvas custom_canvas) {

        String a = pointA.getlabel() == "" ? "Ref. Point A" : pointA.getlabel();
        String b = pointB.getlabel() == "" ? "Ref. Point B" : pointB.getlabel();

        render_only_main_dot(a, pointA, custom_canvas);
        render_only_main_dot(b, pointB, custom_canvas);
    }

    public void drawMainPoint(Canvas paper, PointF dotpoint, String label) {
        Point iSize = new Point(70, 70);
        Bitmap b = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_reference_point);
        Bitmap bm = Bitmap.createScaledBitmap(b, iSize.x, iSize.y, false);


        paper.drawText(label, dotpoint.x + iSize.x / 2 + 5, dotpoint.y, p_label);
        paper.drawBitmap(bm, dotpoint.x - iSize.x / 2, dotpoint.y - iSize.y / 2, bm_paint);
    }

    public void render_ring_pair_at(PointF[] main_points, int pair_loc) {
    /*    final int a_r = Content.radius_a_slot.get(pair_loc);
        final int b_r = Content.radius_b_slot.get(pair_loc);*/
        final RouteNode rn = Content.current_sketch_map.get_route_node_at(pair_loc);
        render_ring_at(main_points[0], rn.get_cal_r_a());
        render_ring_at(main_points[1], rn.get_cal_r_b());
    }

    public void render_ring_pair_at(PointF[] main_points, List<Integer>[] srcList, int c) {
        if (c > -1) {
            float radius1 = srcList[0].get(c);
            float radius2 = srcList[1].get(c);
            render_ring_at(main_points[0], radius1);
            render_ring_at(main_points[1], radius2);
        }
    }

/*
    public void render_route_labels(ArrayList<Integer> vlist, Route r, Canvas forcustomOutput) {
        final int length = Content.radius_list.size();
        final int rlen = r.size();
        if (length > 0 && rlen > 0) {
            for (int i = 0; i < length; i++) {
                final Dot d = r.getListDot(i);
                final Point[] pos = d.getLabelPositionList();
                final Integer order = d.getOrder();
                final String[] labels = r.getLabel(i);
                final int intient = DataHandler.get_label_letter_intrinsic(order);
                final Paint fpaint = new Paint(p_label);
                fpaint.setColor(Tool.getPaintColorCodeByName(ctx, DataHandler.get_label_color_instrinsic(intient)));
                rtext(labels[0], pos[0].x, pos[0].y, fpaint, forcustomOutput);
                rtext(labels[1], pos[1].x, pos[1].y, fpaint, forcustomOutput);
            }
        }
    }*/
/*
    private void rtext(String label, float x, float y, Paint f, Canvas mccanvas) {
        //with custom paint
        if (mccanvas != null) {
            mccanvas.drawText(label, 0, label.length(), x, y, f);
        } else {
            main.drawText(label, 0, label.length(), x, y, f);
        }
    }*/

    private void rtext(String label, float x, float y) {
        //with black
        main.drawText(label, 0, label.length(), x, y, p_label);
    }

    private void rtext(String label, float x, float y, boolean center) {
        //with black
        float x_offset = 0;
        if (center) {
            Rect ebound = new Rect();
            p_label.getTextBounds(label, 0, label.length(), ebound);
            x_offset = ebound.width() >> 1;
        }
        main.drawText(label, 0, label.length(), x - x_offset, y, p_label);
    }

    private void render_only_main_dot(String label, PointF pos, Canvas cvs) {
        if (label.length() > 0) {
            cvs.drawText(label, 0, label.length(), pos.x - 10, pos.y - 10, p_label);
        }
        cvs.drawBitmap(MainDotDrawable, pos.x - MainDotDrawableSize.x / 2, pos.y - MainDotDrawableSize.y / 2, bm_paint_dot);
    }

    private void render_only_main_dot(String label, PointF pos) {
        if (label.length() > 0) {
            main.drawText(label, 0, label.length(), pos.x - 5, pos.y - 20, p_label);
        }
        main.drawBitmap(MainDotDrawable, pos.x - MainDotDrawableSize.x / 2, pos.y - MainDotDrawableSize.y / 2, bm_paint_dot);
    }

    private void render_interactive_circle(String label, PointF position, boolean interaction) {
        Paint pcontrol;
        float rcontrol;
        if (interaction) {
            pcontrol = minteractive_blue;
            rcontrol = 90;
        } else {
            pcontrol = minteractive_press_blue;
            rcontrol = 45;
        }
        Point p = new Point(5, -5);
        main.drawCircle(position.x, position.y, rcontrol, pcontrol);
        main.drawPath(EQPool.shape_cross_t(position), p_line_cross_hair);
      /*  main.drawText(label, 0, label.length(), position.x + p.x, position.y + p.y, p_label);*/
        rtext(label, position.x + p.x, position.y + p.y);
    }

    private void setmaterial() {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        final Paint basic_line = new Paint();
        basic_line.setStyle(Paint.Style.STROKE);
        basic_line.setAntiAlias(true);

        minteractive_blue = new Paint(paint);
        minteractive_blue.setColor(ctx.getResources().getColor(R.color.dot_basic_dark_face_active));
        minteractive_blue.setAlpha(70);

        minteractive_press_blue = new Paint(paint);
        minteractive_press_blue.setColor(ctx.getResources().getColor(R.color.dot_basic_dark_cross_pressed));
        minteractive_press_blue.setAlpha(80);

        p_line_dash = new Paint(basic_line);
        p_line_dash.setPathEffect(f1);
        p_line_dash.setColor(Color.BLACK);
        p_line_dash.setStrokeWidth(1.3f);

        p_line_cross_hair = new Paint(basic_line);
        p_line_cross_hair.setColor(Color.BLACK);
        // p_line_cross_hair.setAlpha(100);

        p_label = new Paint();
        p_label.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        p_label.setAntiAlias(true);
        p_label.setTextSize(20);
        p_label.setTextScaleX(1.f);
        p_label.setAlpha(0);

        red_circle_paint = new Paint(basic_line);
        red_circle_paint.setStrokeWidth(2.9f);
        red_circle_paint.setColor(Color.RED);


        bm_paint = new Paint();
        bm_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        bm_paint.setAntiAlias(true);
        bm_paint.setFilterBitmap(true);
        bm_paint.setColor(Color.BLACK);
        bm_paint.setAlpha(90);


        bm_paint_dot = new Paint();
        bm_paint_dot.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));


        // int g = ctx.getResources().getIdentifier(png_filename, "drawable", ctx.getPackageName());
        // int g = ctx.getResources().getDrawable(R.drawable.ic_reference_point);
        BitmapDrawable md = (BitmapDrawable) ctx.getResources().getDrawable(R.drawable.ic_reference_point);
        MainDotDrawableSize.set(md.getIntrinsicWidth(), md.getIntrinsicHeight());
        Bitmap b = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_reference_point);
        MainDotDrawable = Bitmap.createScaledBitmap(b, MainDotDrawableSize.x, MainDotDrawableSize.y, false);
    }

    public void updateCanvas(Canvas mCanvas) {
        main = mCanvas;
    }

    private void render_ring_at(PointF position, float r) {
        main.drawCircle(position.x, position.y, r, p_line_dash);
    }

    public void render_rings_by_point(PointF position, List<Integer> srcList, boolean interaction) {
        Paint pcontrol;
        float rcontrol;
        if (interaction) {
            pcontrol = minteractive_blue;
            rcontrol = 90;
        } else {
            pcontrol = minteractive_press_blue;
            rcontrol = 45;
        }
        int total_tests = srcList.size();
        //if (!isFinal) {
        for (int i = 0; i < total_tests; i++) {
            main.drawCircle(position.x, position.y, srcList.get(i), p_line_dash);
        }
        main.drawCircle(position.x, position.y, rcontrol, pcontrol);
        main.drawPath(EQPool.shape_cross_t(position), p_line_cross_hair);
        // if (show_label) {
        //main.drawText(Label, 0, Label.length(), position.x, position.y, p_label);
        //  }
        // }
    }
}
