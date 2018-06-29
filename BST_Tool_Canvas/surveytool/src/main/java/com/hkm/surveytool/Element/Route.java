package com.hkm.surveytool.Element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import com.hkm.surveytool.AppDataRetain;
import com.hkm.surveytool.Element.DataPoints.RouteNode;
import com.hkm.surveytool.Element.Math.EQPool;
import com.hkm.surveytool.Element.Math.Route_render_loop;
import com.hkm.surveytool.Element.Math.Route_render_path_labels;
import com.hkm.surveytool.MapPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Route {

    private ArrayList<Dot> containDots = new ArrayList<Dot>();

    private ArrayList<Integer> containOrders = new ArrayList<Integer>();

    private int routesize = 0;

    private boolean is_route_complete = false;


    private Route_render_path_labels rlabel;
    private Canvas main;
    private MapPanel ctx;

    final String TAG = "Route Module";
    final Paint p_default = new Paint();
    /**
     * http://stackoverflow.com/questions/5367950/android-drawing-an-animated-line
     * <p/>
     * http://stackoverflow.com/questions/16925836/how-do-i-pivot-a-matrix-rotation-on-a-point-other-than-0-0-when-using-pathmeasur
     * <p/>
     * http://stackoverflow.com/questions/7712712/measuring-text-on-scaled-canvas
     */
    private boolean update_route_rendering = false;
    private Matrix apply_label_matrix;

    enum route_status {
        UPDATING, DISPLAY
    }

    public Route(ArrayList<Dot> k) {
        routeSet(k);
        is_route_complete = false;
    }

    public Route(Canvas cv) {
        main = cv;
        is_route_complete = false;
    }

    public Route(Canvas cv, MapPanel cc) {
        main = cv;
        ctx = cc;
        routesize = 0;
        is_route_complete = update_route_rendering = false;
    }

    public Route(Canvas canvas, MapPanel cc, ArrayList<Dot> k) {
        main = canvas;
        ctx = cc;
        p_default.setStyle(Paint.Style.STROKE);
        p_default.setStrokeWidth(1.9f);
        p_default.setColor(Color.RED);
        is_route_complete = update_route_rendering = false;
        routeSet(k);
    }

    public void updateCanvas(Canvas cv) {
        main = cv;
    }

    public Dot getListDot(int i) {
        return containDots.get(i);
    }

    public String[] getLabel(int i) {
        final Dot d = containDots.get(i);
        final String label1 = d.getTag();
        final String label2 = d.getDepth();
        final String[] a = {label1, label2};
        return a;
    }

    /**
     * force the engine to render the label
     *
     * @param k bool
     */
    public void setRenderLabel(boolean k) {
        update_route_rendering = k;
    }

    /**
     * simpliy copy and paste the route the route into the route list
     */
    private void routeSet(ArrayList<Dot> k) {
        for (Dot d : k) {
            containOrders.add(d.getOrder()); // getting the ID of the DOT, the ID is also linked to the radius_list
        }
    }

    public void setRouteComplete(boolean b) {
        is_route_complete = b;
    }

    public boolean isComplete() {
        return is_route_complete;
    }

    /**
     * external use
     *
     * @param touch_point position
     * @param radius_detection the radius detection length
     * @return bool
     */
    public boolean is_touch_on_route(PointF touch_point, float radius_detection) {
        Iterator<Dot> iterator = containDots.iterator();
        while (iterator.hasNext()) {
            // - System.out.println(iterator.next());
            Dot point_pos = iterator.next();
            if (EQPool.onPressedCicle(point_pos, touch_point, radius_detection)) {
                return true;
            }
        }
        return false;
    }

    public void removeAll() {
        containOrders.clear();
        containDots.clear();
        routesize = 0;
        is_route_complete = false;
    }

    public int size() {
        return routesize;
        // return routesize;
    }

    public boolean changeRouteDotAt(Dot Positive, Dot Negative) {
        int tag_id = Positive.getOrder();
        int order = containOrders.indexOf(tag_id);
        final Dot Positive_Dot = new Dot(Positive, main, Positive.get_route_node_reference(), ctx.getContext(), new DotTouch());
        if (order > -1) {
            //set the existing position
            Positive_Dot.setCanTouch(false);
            Positive_Dot.SelectPoint(Dot.selection.CONFIRM_IS);
            Positive_Dot.setLock(true);

            containDots.set(order, Positive_Dot);

            Positive.setCanTouch(false);
            Positive.SelectPoint(Dot.selection.CONFIRM_NOT);
            Positive.setLock(true);

            Negative.setCanTouch(false);
            Negative.SelectPoint(Dot.selection.CONFIRM_NOT);
            Negative.setLock(true);
        } else {
            //add new position
            add(Positive, Negative);
        }

        return true;
    }

    /**
     * @param Positive, this is chosen dot
     * @param Negative, this is not the chosen dot
     * @return na
     */
    public boolean add(Dot Positive, Dot Negative) {
        int tag_id = Positive.getOrder();
        int order = containOrders.indexOf(tag_id);
        if (order == -1) {
            //adding new points ... version 15
            containOrders.add(tag_id);
            final Dot Positive_Dot = new Dot(Positive, main, Positive.get_route_node_reference(), ctx.getContext(), new DotTouch());
            Positive_Dot.SelectPoint(Dot.selection.CONFIRM_IS);
            Positive_Dot.setCanTouch(false);
            Positive_Dot.setLock(true);
            containDots.add(Positive_Dot);

            Negative.setCanTouch(false);
            Positive.SelectPoint(Dot.selection.CONFIRM_NOT);
            Negative.SelectPoint(Dot.selection.CONFIRM_NOT);
            Positive.setLock(true);
            Negative.setLock(true);
            routesize = containDots.size();
        }
        return true;
    }

    public ArrayList<Dot> getContainer() {
        return containDots;
    }

    public void update_dots() {
        for (Dot d : containDots) {
            d.updateData();
        }
    }

    public void update_route_label() {
        update_route_rendering = true;
    }

    private Route_render_loop rr_loop;


    private void render_each_dot(boolean render_label, boolean render_cross, Canvas direct_draw_on) {
        for (Dot d : containDots) {
            final Point[] pos = d.getLabelPositionList();
            final Paint paint_text = d.get_route_node_reference().get_label().get_line_label_paint();
            final String label1 = d.getTag();
            final String label2 = d.getDepth();
            if (render_label) {
                direct_draw_on.drawText(label1, 0, label1.length(), pos[0].x, pos[0].y, paint_text);
                direct_draw_on.drawText(label2, 0, label2.length(), pos[1].x, pos[1].y, paint_text);
            }
            if (render_cross) {
                d.updateData();
                d.onDraw(direct_draw_on);
            }
        }
    }

    private void rendering() {
        if (rr_loop == null || update_route_rendering || !is_route_complete) {
            rr_loop = new Route_render_loop(containDots.size(), containDots, main);
        }
        if (rr_loop != null) {
            rr_loop.renderPath();
        }
        if (update_route_rendering) {
            rlabel = new Route_render_path_labels(main, ctx.getContext(), rr_loop.getPaths(), containDots);
        } else {
            if (rlabel != null) {
                rlabel.change_matrix(apply_label_matrix);
                rlabel.renderPath();
            }
        }
        if (is_route_complete) {
            render_each_dot(true, true, main);
        }
        if (update_route_rendering) {
            update_route_rendering = false;
        }
    }

    public void renderPath(Canvas pointer_reference, Matrix custom_matrix) {
        // final Canvas cache_pointer = main;
        //  main = pointer_reference;
        update_dots();
        rr_loop.renderPath(pointer_reference);
        render_each_dot(true, true, pointer_reference);
        try {
            rlabel.render_label_path(pointer_reference, custom_matrix);
        } catch (Exception e) {
            e.printStackTrace();
            //Tool.trace(ctx.getContext(), e.toString());
        }
        //main = cache_pointer;
    }

    public void renderPath() {
        apply_label_matrix = ctx.getMpanelmatrix();
        rendering();
        // render_label_path();
    }

    public ArrayList<HashMap<String, PointF>> getDataRoute() {
        ArrayList<HashMap<String, PointF>> chain = new ArrayList<HashMap<String, PointF>>();
        for (int i = 0; i < routesize; i++) {
            PointF cur_dot = containDots.get(i);
            String tag = containDots.get(i).getTag();
            String depth = containDots.get(i).getDepth();
            HashMap<String, PointF> chain_item = new HashMap<String, PointF>();
            //adding labels for each object but not rendered
            chain_item.put(tag, cur_dot);
            chain.add(chain_item);
        }
        return chain;
    }

    public boolean check_for_auto_connect_mode() {
        boolean fact1 = AppDataRetain.current_sketch_map.getRouteNodeList().size() > 0;
        boolean fact2 = containDots.size() == 0;
        if (fact1 && fact2) {
            for (RouteNode rn : AppDataRetain.current_sketch_map.getRouteNodeList()) {
                if (rn.is_this_pair_undecided()) return false;
            }
            return true;
        }
        return false;
    }

    private class DotTouch implements Dot.DotListener {
        @Override
        public void notifyOnDraw(Dot ob) {

        }

        @Override
        public void update(boolean isSelected, PointF point) {
        }

        @Override
        public void onSelect(int tag, Dot which) {

        }
    }

}
