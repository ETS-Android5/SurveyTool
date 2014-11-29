package com.hkm.oc.panel.corepanel.elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hkm.U.Content;
import com.hkm.datamodel.RouteNode;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;


/**
 * Created by Hesk ons GREAT POINTER SYSTEM
 */
public class Dot extends PointF {
    // private AtomicBoolean PressLock;
    public static final int DOT_LINE_CONNECT = 0;
    public static final int DOT_ICON_SINGLE = 1;
    private static float circle_radius = 20.0f;
    private static float circle_r_additional = 5.f;

    private int current_dot_line_status;
    private Paint
            p_1_hard_line,
            paint_not_ready,
            paint_ready,
            paint_on_selected,
            cross_line_paint,
            bm_paint,
            paint_button_normal,
            paint_button_press;

    private boolean canTouch;
    private Canvas main;
    private boolean PressLock;
    private DotListener mlistener;
    private int dot_order_id;
    private selection whatselection;
    private actionmode action_mode_listener;
    private Context ctx;
    private Point iSize;
    private RouteNode reference_node;
    public final String TAG = "DOT Object";

    public Dot(PointF pos, Canvas cmcanvas, int ord, Context base) {
        set(pos);
        whatselection = selection.NO_CONFIRM;
        reference_node = Content.current_sketch_map.get_route_node_at(dot_order_id = ord);
        canTouch = false;
        PressLock = true;
        // rather it is A or B
        // this will only contains the number for this dot ONLY
        ctx = base;
        main = cmcanvas;
        FinalMaterialDefine();
        current_dot_line_status = DOT_LINE_CONNECT;
        cross_line_paint = new Paint(p_1_hard_line);
        cross_line_paint.setColor(reference_node.get_label().get_paint_color());
    }

    //constructor of the DOT
    public Dot(PointF input_position, Canvas mcanvas, RouteNode rn, Context base, DotListener dl) {
        set(input_position);
        mlistener = dl;
        whatselection = selection.NO_CONFIRM;
        reference_node = rn;
        canTouch = false;
        PressLock = true;
        //rather it is A or B
        // this will only contains the number for this dot ONLY
        dot_order_id = rn.get_index();
        ctx = base;
        main = mcanvas;
        FinalMaterialDefine();
        current_dot_line_status = DOT_LINE_CONNECT;
        cross_line_paint = new Paint(p_1_hard_line);
        cross_line_paint.setColor(reference_node.get_label().get_paint_color());
    }

    public void updateData() {
        cross_line_paint = new Paint(p_1_hard_line);
        cross_line_paint.setColor(reference_node.get_label().get_paint_color());
        if (reference_node.get_label().is_sharp()) {
            current_dot_line_status = DOT_ICON_SINGLE;
            iSize = reference_node.get_label().get_iSize();
        } else {
            current_dot_line_status = DOT_LINE_CONNECT;
        }
    }

    public Dot updatePos(PointF input_position) {
        set(input_position);
        return this;
    }

    public void add(Dot f) {
        super.offset(f.x, f.y);
    }

    private int getc(int ResId) {
        return ctx.getResources().getColor(ResId);
    }

    private void FinalMaterialDefine() {
        final Paint default_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        default_paint.setStyle(Paint.Style.FILL);
        default_paint.setAlpha(90);
        final Paint default_bitmap_paint = new Paint();

        p_1_hard_line = new Paint(Paint.ANTI_ALIAS_FLAG);
        p_1_hard_line.setStyle(Paint.Style.STROKE);

        default_bitmap_paint.setStyle(Paint.Style.FILL);
        default_bitmap_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        //status 1 is selected - NO_CONFIRM
        paint_not_ready = new Paint(default_paint);
        paint_not_ready.setColor(getc(R.color.dot_intercept_dark_face_normal));
        paint_not_ready.setAlpha(90);

        //status 0 is not connected
        paint_ready = new Paint(default_paint);
        paint_ready.setColor(getc(R.color.dot_intercept_dark_face_active));
        paint_ready.setAlpha(90);

        //status 1 is connected there is no circle so there is no need draw the circle
        paint_on_selected = new Paint(default_paint);
        paint_on_selected.setColor(getc(R.color.transparent));
        paint_on_selected.setAlpha(90);

        bm_paint = new Paint();
        bm_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));

        paint_button_normal = new Paint(default_paint);
        paint_button_normal.setColor(getc(R.color.holo_orange_dark));
        paint_button_normal.setAlpha(90);

        paint_button_press = new Paint(default_paint);
        paint_button_press.setColor(getc(R.color.holo_orange_light));
        paint_button_press.setAlpha(90);
    }

    public boolean getLock() {
        return PressLock;
    }

    public void setLock(boolean lock) {
        PressLock = lock;
    }

    public void setCanTouch(boolean can) {
        canTouch = can;
    }

    public boolean isSelected(selection whatkind) {
        return whatselection == whatkind;
    }


    public int get_letter_intrinsic() {
        return reference_node.get_label().get_letterIntrinsic();
    }

    public int getOrder() {
        return dot_order_id;
    }

    public String getTag() {
        if (isSelected(selection.NO_CONFIRM))
            return "new " + reference_node.get_label().get_display_big_button_label();
        else
            return reference_node.get_label().get_display_big_button_label();
    }

    public RouteNode get_route_node_reference() {
        return reference_node;
    }


    public String getDepth() {
        return reference_node.get_tag_depth();
    }

    // is this point selected or not
    public void SelectPoint(selection c) {
        whatselection = c;
        switch (c) {
            case CONFIRM_IS:
                canTouch = false;
                break;
            case CONFIRM_NOT:
                canTouch = false;
                break;
            case NO_CONFIRM:
                canTouch = true;
                break;
            case INSPECT:
                canTouch = true;
                break;
        }
    }

    public PointF getPos() {
        return new PointF(x, y);
    }

    public float radius_state(float r) {
        return r;
    }


    private void render_bitmap_icon() {
        if (reference_node.get_label().is_sharp()) {
            try {
                // main.drawBitmap(bm, x - iSize.x - 9, y - iSize.y / 2, bm_paint);
                Bitmap bm = reference_node.get_label().get_icon_sharp_bitmap();
                main.drawBitmap(bm, x - iSize.x / 2, y - iSize.y / 2, bm_paint);
            } catch (Exception e) {
                Log.d(TAG, "bitmap cannot be rezzed");
            }
        }
    }


    public Point[] getLabelPositionList() {
        Point offset;
        if (isSelected(selection.NO_CONFIRM)) {
            offset = new Point(13, -5);
        } else {
            if (reference_node.get_label().is_sharp()) {
                if (iSize != null) {
                    offset = new Point(iSize.x / 2 + 5, -5);
                } else {
                    offset = new Point(18, -5);
                }
            } else {
                offset = new Point(11, -5);
            }
        }

        final Point _st_line_offset = new Point(offset.x + (int) x, offset.y + (int) y);
        final Point _nd_line_offset = new Point(offset.x + (int) x, offset.y + (int) y + 16);
        final Point[] output = new Point[]{_st_line_offset, _nd_line_offset};
        return output;
    }

    /**
     * to render the tag pair of both potential positions
     * render the first line and the second line of the tag information
     */
    private void render_position_tag() {
        final String[] labels = new String[]{getTag(), getDepth()};
        final Point[] pos = getLabelPositionList();
        render_tag_line(labels[0], pos[0].x, pos[0].y);
        render_tag_line(labels[1], pos[1].x, pos[1].y);
    }

    /**
     * render the text tag on the canvas
     *
     * @param label
     * @param x
     * @param y
     */
    private void render_tag_line(String label, float x, float y) {
        main.drawText(label, 0, label.length(), x, y, cross_line_paint);
    }

    /**
     * render the X on the map
     */
    private void render_cross_x() {
        main.drawPath(EQPool.shape_cross_x(this), cross_line_paint);

    }

    /**
     * render the inspection status on the map
     */
    private void render_inspection() {
        final float r = event_dot_pressed ? circle_radius + circle_r_additional : circle_radius;
        final Paint p = event_dot_pressed ? paint_button_press : paint_button_normal;
        main.drawPath(EQPool.shape_cross_x(this), cross_line_paint);
        main.drawCircle(this.x, this.y, r, p);
    }

    /**
     * render the O shape on the map
     */
    private void render_O_ready_touch_release() {
        main.drawCircle(this.x, this.y, circle_radius, paint_ready);
    }

    /**
     * render the O shape on the map
     */
    private void render_O_not_ready_no_confirm() {
        main.drawCircle(this.x, this.y, circle_radius, paint_not_ready);
    }

    private void rendering() {
        if (canTouch) {
            if (isSelected(selection.NO_CONFIRM)) {
                //with the paint-ready
                render_O_ready_touch_release();
                render_position_tag();
            } else if (isSelected(selection.CONFIRM_IS)) {
                render_cross_x();
            } else if (isSelected(selection.INSPECT)) {
                render_inspection();
            }
        } else {
            if (isSelected(selection.CONFIRM_IS)) {
                //with the paint-on-active
                render_cross_x();
                render_bitmap_icon();
            } else if (isSelected(selection.CONFIRM_NOT)) {
                //do not draw here after selected
                //   Log.d("START canTouch", "true for selected This AfterNotSelected");
            } else if (isSelected(selection.NO_CONFIRM)) {
                //with pain-not-ready
                render_cross_x();
                render_O_not_ready_no_confirm();
            } else if (isSelected(selection.INSPECT)) {
                render_cross_x();
            }
        }
        if (!isSelected(selection.CONFIRM_NOT)) {
            //=============draw a cross x on this point
            if (isSelected(selection.CONFIRM_IS)) {
                if (!canTouch) {
                    // main.drawPath(ToolBox.shape_cross_x(this), cross_line_paint);
                }
            } else {
                // render_O_not_ready_no_confirm();
            }
        }
    }

    public Dot onDraw() {
        rendering();
        return this;
    }

    public Dot onDraw(Canvas pointer_reference) {
        final Canvas cache_pointer = main;
        main = pointer_reference;
        rendering();
        main = cache_pointer;
        return this;
    }

    public void getMetrics(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    }

    public void setListener(DotListener l) {
        mlistener = l;
    }

    static public enum selection {
        CONFIRM_IS, CONFIRM_NOT, NO_CONFIRM,
        INSPECT
    }

    public Dot set_action_mode_listener(actionmode modelistener) {
        this.action_mode_listener = modelistener;
        return this;
    }

    public interface DotListener {
        public void update(boolean isSelected, PointF point);

        public void onSelect(int dot_order_id, Dot interacted_dot);

        public void notifyOnDraw(Dot object_to_draw);
    }

    public interface actionmode {
        public void select(final RouteNode rn);
    }

    private boolean event_dot_pressed = false;

    public void dot_event_press() {
        if (!event_dot_pressed) {
            event_dot_pressed = true;
            if (action_mode_listener != null)
                this.action_mode_listener.select(this.reference_node);
            //  if (mlistener != null) mlistener.onSelect(dot_order_id, this);
        }
    }

    public void dot_event_release() {
        if (event_dot_pressed) {
            event_dot_pressed = false;
        }
    }
}

