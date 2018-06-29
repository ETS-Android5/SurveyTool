package com.hkm.surveytool.Element.Math;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;

import com.hkm.surveytool.Element.DataPoints.Paths;
import com.hkm.surveytool.Element.DataPoints.RouteNode;
import com.hkm.surveytool.Element.Dot;

import java.util.ArrayList;

/**
 * Created by Hesk on 25/3/2014.
 */
public class Route_render_loop {
    private int routesize;
    private ArrayList<Dot> containDots = new ArrayList<Dot>();
    private Canvas mcanvas;
    private Path pan;
    private Paint paint_line;
    private final ArrayList<Paths> mpaths = new ArrayList<Paths>();
    public final static String TAG = "route render loop";

    public Route_render_loop(int route_size, ArrayList<Dot> dotList, Canvas canvas) {
        routesize = route_size;
        containDots = dotList;
        mcanvas = canvas;
        embark();
    }

    public ArrayList<Paths> getPaths() {
        return mpaths;
    }

    public void renderPath() {
        for (Paths p : mpaths) {
            final Paint lb = p.getLabel().get_stroke_paint();
            final Path pathd = p.getPath();
            mcanvas.drawPath(pathd, lb);
        }
    }

    public void renderPath(Canvas custom) {
        for (Paths p : mpaths) {
            final Paint lb = p.getLabel().get_stroke_paint();
            final Path pathd = p.getPath();
            custom.drawPath(pathd, lb);
        }
    }

    private void embark() {
        pan = new Path();
        paint_line = new Paint();
        paint_line.setStyle(Paint.Style.STROKE);
        paint_line.setAntiAlias(true);
        paint_line.setStrokeWidth(1.1f);
        mpaths.clear();
        if (routesize > 1) {
            final Dot first_dot_position = containDots.get(0);
            pan.moveTo(first_dot_position.x, first_dot_position.y);
            paint_line = first_dot_position.get_route_node_reference().get_label().get_stroke_paint();
            main();
        }
    }

    private void record_path(RouteNode info_bundle) {
        final PathMeasure contour = new PathMeasure(new Path(pan), false);
        final float path_length = contour.getLength();
        Log.d(TAG, "record the path length is now: " + path_length);
        if (path_length > 0) {
            mpaths.add(new Paths(info_bundle.get_label(), pan));
        }
    }

    private void progress(int n, Dot dot) {
        final RouteNode m1 = containDots.get(n).get_route_node_reference();
        final RouteNode m2 = containDots.get(n - 1).get_route_node_reference();
        //paint_line = m1.get_label().get_stroke_paint();
        if (test(m1, m2)) {
            pan.lineTo(dot.x, dot.y);
        } else {
            record_path(m2);
            pan = new Path();
            pan.moveTo(dot.x, dot.y);
        }
    }

    private void last(int last_n) {
        final RouteNode rnm = containDots.get(last_n).get_route_node_reference();
        final RouteNode rnm2 = containDots.get(last_n - 1).get_route_node_reference();
        final Dot dotd = containDots.get(last_n);
        if (test(rnm, rnm2)) {
            pan.lineTo(dotd.x, dotd.y);
            record_path(rnm2);
        } else {
            record_path(rnm2);
        }
    }

    /**
     * this test will carry out if the line to be continue or not
     *
     * @param current_zero
     * @param plus_one
     * @return rather this part of the connection is break ot not
     * true -> break
     * false -> not break
     */
    private static boolean test(RouteNode current_zero, RouteNode plus_one) {
        final boolean cut_1 = current_zero.get_cut();
        final boolean cut_2 = plus_one.get_cut();
        final int line_base_1 = current_zero.get_label().get_line_label_int();
        final int line_base_2 = plus_one.get_label().get_line_label_int();
        final boolean no_cuts = !cut_1 && !cut_2;
        final boolean match_on_the_line_base = line_base_1 == line_base_2;
        if (!match_on_the_line_base) return false;
        if (cut_1 && !cut_2) return true;
        return no_cuts;
    }


    private void main() {
        try {
            if (routesize > 1) {
                for (int k = 0; k < routesize; k++) {
                    Dot dotc = containDots.get(k);
                    if (k > 0 && k < routesize - 1) {
                        progress(k, dotc);
                    } else if (k == 0) {
                        pan = new Path();
                        pan.moveTo(dotc.x, dotc.y);
                    } else if (k == routesize - 1) {
                        last(k);
                    }
                }
            }
        } catch (Exception e) {
            //   last(k);
        }
    }
}
