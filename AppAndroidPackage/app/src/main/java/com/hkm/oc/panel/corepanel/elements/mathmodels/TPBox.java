package com.hkm.oc.panel.corepanel.elements.mathmodels;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;
import com.hkm.oc.panel.corepanel.elements.shapes.Element;

import java.io.Serializable;

import static com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox.Draw_progress.MOVE;
import static com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox.Draw_progress.START;

/**
 * Created by hesk on 11/3/13.
 */
public class TPBox extends Element implements Serializable {

    transient private final Paint filledP = new Paint(), textP = new Paint();
    transient private Point[] points; // Points making up the boundary
    transient private boolean render_w_h, editingLabel, selected, show_measurement_detail, isLockFinal; // done
    @SerializedName("ori_l")
    private line_eq ori_l;

    transient private line_eq l1;
    @SerializedName("l2")
    private line_eq l2;
    @SerializedName("group_start")
    private float[] group_start;
    @SerializedName("group_end")
    private float[] group_end;
    //the label box for displaying the label
    @SerializedName("box_label")
    private String box_label;
    //the label position offset
    @SerializedName("position_offset")
    private PointF position_offset = new PointF();

    @SerializedName("box_center")
    private PointF centerp;

    //the touch point location
    transient private PointF touch0, touch, centerp0;
    transient public static String TAG = "RootTPBOX";

    @SerializedName("box_height")
    protected double box_height;

    @SerializedName("box_width")
    protected double box_width;

    transient private Draw_progress mdraw_progress;

    public static enum Draw_progress {
        START, MOVE, END
    }

    public TPBox() {
        default_setting();
    }

    public TPBox setRun(Canvas mcanvas, Context ctx) {
        if (this.main == null)
            this.main = mcanvas;
        if (this.ctx == null)
            this.ctx = ctx;
        return this;
    }

    public TPBox setL2(line_eq L) {
        this.l2 = L;
        return this;
    }

    public TPBox setPointsGroup(float[] group1, float[] group2) {
        this.group_start = group1;
        this.group_end = group2;
        return this;
    }

    public TPBox(Canvas mcanvas, Context ctx) {
        super(mcanvas, ctx);
        default_setting();
    }

    private void default_setting() {
        // set_n = 0;
        centerp0 = new PointF();
        centerp = new PointF();
        render_w_h = show_measurement_detail = isLockFinal = editingLabel = selected = false;
        box_label = "";
        box_height = box_width = 0d;
    }

    public TPBox setPaint(Paint customPaint, BitmapShader bs) {
        normal_paint = customPaint;
        filledP.set(normal_paint);
        filledP.setColor(Color.RED);
        filledP.setStyle(Paint.Style.FILL);
        filledP.setShader(bs);
        ColorFilter filter = new LightingColorFilter(Color.RED, 1);
        filledP.setColorFilter(filter);
        // filledP.setAlpha(60);
        textP.set(normal_paint);
        textP.setTextSize(25);
        //textP.setAlpha();
        textP.setColor(Color.BLACK);
        textP.setStyle(Paint.Style.FILL);
        return this;
    }

    public TPBox setBaseOnLine(line_eq L) {
        this.ori_l = L;
        return this;
    }

    public TPBox setTouchPointStart(PointF p) {
        mdraw_progress = START;
        touch0 = new PointF(p.x, p.y);
        centerp0 = new PointF(ori_l.getMinDistancePoint(p));
        l1 = new line_eq(centerp0, touch0);
        return this;
    }

    public TPBox setTouchPointMove(PointF p) {
        mdraw_progress = MOVE;
        touch = new PointF(p.x, p.y);
        centerp = new PointF(ori_l.getMinDistancePoint(p));
        return this;
    }

    public TPBox loadWH(double width, double height, PointF box_center) {
        box_width = width;
        box_height = height;
        centerp = box_center;
        if (box_height > 0 || box_width > 0) {
            render_w_h = true;
        }
        return this;
    }

    public TPBox setWidth(double d) {
        render_w_h = true;
        box_width = EQPool.m2p((float) d);
        return this;
    }

    public TPBox setHeight(double d) {
        render_w_h = true;
        box_height = EQPool.m2p((float) d);
        return this;
    }

    public TPBox setLabelPositionOffsetPreview(PointF movement) {
        position_offset = movement;
        return this;
    }

    public TPBox setLabel(String e) {
        box_label = e;
        editingLabel = true;
        return this;
    }

    public TPBox setSelected(boolean b) {
        selected = b;
        return this;
    }

    public TPBox setEditingLabel(boolean b) {
        this.editingLabel = b;
        return this;
    }

    protected PointF getLabelPosition() {
        Point d_middle = EQPool.centerPoint(points[0], points[2]);
        return new PointF(
                d_middle.x + position_offset.x,
                d_middle.y + position_offset.y
        );
    }

    public double getWidth() {
        return this.box_width;
    }

    public double getHeight() {
        return this.box_height;
    }

    public PointF getPosition_offset() {
        return this.position_offset;
    }

    public String getLabel() {
        return this.box_label;
    }

    public Point[] getPairCenters() {
        final Point p0 = new Point((int) centerp0.x, (int) centerp0.y);
        final Point p1 = new Point((int) centerp.x, (int) centerp.y);
        return new Point[]{p0, p1};
    }

    public line_eq getL2() {
        return l2;
    }

    public line_eq getLOriginal() {
        return ori_l;
    }

    public float[] exportFloatGroup1() {
        return group_start;
    }

    public float[] exportFloatGroup2() {
        return group_end;
    }

    @Override
    protected void rendering() {
        if (selected) main.drawPath(getPathFromRenderPointsData(), filledP);
        main.drawPath(getPathFromRenderPointsData(), normal_paint);
        if (show_measurement_detail) render_measurement();
        if (editingLabel) render_big_cross_target(getLabelPosition());
        if (box_label.length() > 0)
            main.drawText(box_label, getLabelPosition().x, getLabelPosition().y, textP);
    }

    private void render_measurement() {

    }

    /**
     * display the big black cross lines
     *
     * @param pos
     */
    private void render_big_cross_target(PointF pos) {
        Path p = new Path();
        p.moveTo(-5000, pos.y);
        p.lineTo(5000, pos.y);
        p.moveTo(pos.x, -5000);
        p.lineTo(pos.x, 5000);
        main.drawPath(p, normal_paint);
    }

    /**
     * construct the line path based on the point data
     *
     * @return
     */
    private Path getPathFromRenderPointsData() {
        final Path p = new Path();
        if (!render_w_h) {
            p.moveTo(group_start[0], group_start[1]);
            p.lineTo(group_start[2], group_start[3]);
            p.lineTo(group_end[2], group_end[3]);
            p.lineTo(group_end[0], group_end[1]);
            p.lineTo(group_start[0], group_start[1]);
        } else {
            final Matrix mx = new Matrix();
            mx.postRotate((float) l2.getAngle(), (float) box_width / 2, (float) box_height / 2);
            mx.postTranslate((float) -box_width / 2, (float) -box_height / 2);
            mx.postTranslate(centerp.x, centerp.y);
            p.addRect(0, 0, (float) box_width, (float) box_height, Path.Direction.CW);
            p.transform(mx);
        }
        return p;
    }

    public void kickin() {
        switch (mdraw_progress) {
            case START:
                //the first line points group from the start
                final float[] f = new float[]{
                        touch0.x, touch0.y, touch0.x,
                        touch0.y - 2 * Math.abs(touch0.y - centerp0.y)
                };
                group_start = f.clone();
                group_end = f.clone();
                l2 = new line_eq(centerp0, touch0);
                box_width = box_height = 0d;

                break;
            case MOVE:
                float a_p2 = Geometry.powered(touch.x - centerp.x);
                float b_p2 = Geometry.powered(touch.y - centerp.y);
                float R2 = a_p2 + b_p2;//the distance power of 2
                l2 = new line_eq(centerp, touch); //the line 2
                group_start = Geometry.circle_and_linear_line(l1, centerp0, R2);
                group_end = Geometry.circle_and_linear_line(l2, centerp, R2);
                double d = Geometry.distance(centerp, touch);
                box_width = 2 * d * Math.sin(l2.getAngle());
                box_height = 2 * d * Math.cos(l2.getAngle());
                //    group_end = new float[]{touch.x, touch.y, touch.x, touch.y - 2 * Math.abs(touch.y - centerp.y)};
                break;
            case END:
                break;
        }
    }

    public TPBox drawDone(boolean bh) {
        if (bh) {
            points = new Point[]{
                    new Point((int) group_start[0], (int) group_start[1]),
                    new Point((int) group_start[2], (int) group_start[3]),
                    new Point((int) group_end[2], (int) group_end[3]),
                    new Point((int) group_end[0], (int) group_end[1])
            };
        }
        isLockFinal = bh;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }


    public TPBox toggleSelect() {
        //  Log.d(TAG, l2.getAngle() + " o");
        if (selected) {
            selected = false;
            return this;
        } else {
            selected = true;
            return this;
        }
    }
    /*
    private void setCloneGroup() {
        group_start = cloneArrayFloat(group_start);
        group_end = cloneArrayFloat(group_end);
    }
    private float[] cloneArrayFloat(float[] arraytobecopied) {
        final float[] newarray = new float[arraytobecopied.length];
        System.arraycopy(arraytobecopied, 0, newarray, 0, arraytobecopied.length);
        return newarray;
    }
    public Box clone(Canvas mcanvas) {
        final Box cc = new Box(mcanvas, ctx);
        cc.setCloneGroup();
        return cc;
    }
    */

    /**
     * Hit test method
     * Return true if the given point is contained inside the boundary.
     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     *
     * @param test The point to check
     * @return true if the point is inside the boundary, false otherwise
     */

    public boolean onSelected(Point test) {
        int i;
        int j;
        boolean result = false;
        if (points.length > 0) {
            for (i = 0, j = points.length - 1; i < points.length; j = i++) {
                if ((points[i].y > test.y) != (points[j].y > test.y) &&
                        (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
                    result = !result;
                }
            }
        }
        return result;
    }

    public TPBox startBoxDraw(Point current_center_point, Point touch) {

        group_end = new float[]{
                (float) touch.x, (float) touch.y,
                (float) touch.x, (float) touch.y - 2 * Math.abs(touch.y - current_center_point.y)
        };


        return this;
    }


    public TPBox show_measurement(boolean b) {
        show_measurement_detail = b;
        return this;
    }
}
