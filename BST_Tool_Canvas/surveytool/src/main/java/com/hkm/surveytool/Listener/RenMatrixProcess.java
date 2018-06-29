package com.hkm.surveytool.Listener;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by hesk on 6/13/2014.
 */

public class RenMatrixProcess implements Displacement {
    private final float MIN_SCALE = 1;
    private final float MAX_SCALE = 64;
    private final Matrix mMatrix = new Matrix();
    private float scale = 1;  // scale of the image
    private float xLast;  // last x location on the screen
    private float yLast;  // last y location on the screen
    private float xImage; // last x location on the image
    private float yImage; // last y location on the image
    private float xScreen;
    private float yScreen;
    private boolean flingmode;
    private float cvx, cvy;
    private float xNew, yNew;
    /**
     * function 2 displacement pure integer
     */
    private PointF downpoint = new PointF();
    private PointF uppoint = new PointF();
    private PointF external_cursor_position = new PointF();
    private Canvas external_output_canvas = new Canvas();
    private Paint debug_paint_red_circle_paint = new Paint();

    /**
     *
     */
    public RenMatrixProcess(PointF cursor, Canvas canvas) {
        flingmode = false;
        xNew = yNew = xScreen = yScreen = xImage = yImage = xLast = yLast = cvx = cvy = 0;
        scale = scale < MIN_SCALE ? MIN_SCALE : (scale > MAX_SCALE ? MAX_SCALE : scale);
        external_cursor_position = cursor;
        external_output_canvas = canvas;


        final Paint basic_line = new Paint();
        basic_line.setStyle(Paint.Style.STROKE);
        basic_line.setAntiAlias(true);
        debug_paint_red_circle_paint = new Paint(basic_line);
        debug_paint_red_circle_paint.setStrokeWidth(2.9f);
        debug_paint_red_circle_paint.setColor(Color.RED);
    }

    public PointF update_screen_pointer(MotionEvent me) {
        return update_screen_pointer(me.getX(), me.getY());
    }

    public PointF update_screen_pointer(Point point_on_screen) {
        float xS = point_on_screen.x;
        float yS = point_on_screen.y;
        return update_screen_pointer(xS, yS);
    }

    public PointF update_screen_pointer(float xS, float yS) {
        final float xN = (xS - xImage) / scale;
        final float yN = (yS - yImage) / scale;
        return new PointF(xN, yN);
    }

    public PointF move_map_w_cursor(float x, float y) {
        xScreen = x;
        yScreen = y;
        update();
        return get_coordination_on_image();
    }

    private PointF get_coordination_on_image() {

        return new PointF(xNew, yNew);
    }

    //this is used for translating
    public void update(float dx, float dy, MotionEvent me) {
        PointF screen_point = new PointF(me.getX(), me.getY());
        //update by dx and dy
        xImage = xImage + (dx * -1 / scale);
        yImage = yImage + (dy * -1 / scale);
        xLast = xScreen = screen_point.x;
        yLast = yScreen = screen_point.y;
        //this is the interpretation of  ( 1 < scale < 64 )
        // scale = scale < MIN_SCALE ? MIN_SCALE : (scale > MAX_SCALE ? MAX_SCALE : scale);
        //or using this..   scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
        // determine the location on the screen at the new scale
    }

    public void update_scale_delta(float delta) {
        // determine the new scale
        scale += delta;
        //this is the interpretation of  ( 1 < scale < 64 )
        scale = scale < MIN_SCALE ? MIN_SCALE : (scale > MAX_SCALE ? MAX_SCALE : scale);
        //     update();
    }

    //this is used for scaling
    private void update() {
        // find current location on the image at the current scale
        xImage = xImage + ((xScreen - xLast) / scale);
        yImage = yImage + ((yScreen - yLast) / scale);
        //this is the interpretation of  ( 1 < scale < 64 )
        //... scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
        // determine the location on the screen at the new scale
        // save the current screen location
        xLast = xScreen;
        yLast = yScreen;
    }

    //this is used on the lower machine
    public void postUpdate() {
        xNew = (xScreen - xImage) / scale;
        yNew = (yScreen - yImage) / scale;
        if (flingmode) {
            cvx *= 0.56f;
            cvy *= 0.56f;
            if (cvx < .0000001f && cvy < .0000001f) {
                cvx = 0;
                cvy = 0;
                flingmode = false;
                //  pauseAndLabel();
            }
            //testing not implement first
            xImage += cvx;
            yImage += cvy;
            //xImage = scale < MIN_SCALE ? MIN_SCALE : (scale > MAX_SCALE ? MAX_SCALE : scale);
            //xImage = Math.min(0, m_offset.x);
            //yImage = Math.min(0, m_offset.y);
        }
        //mainCanvas.drawCircle(xNew, yNew, 20, mpen.getPaintInteraction());
        mMatrix.reset();
        //mMatrix.preTranslate(xNew, yNew);
        //mMatrix.postScale(scale, scale, xNew, yNew);
        mMatrix.postScale(scale, scale);
        mMatrix.postTranslate(xImage, yImage);
    }

    private void render_helper_debug() {
        external_output_canvas.drawCircle(xNew, yNew, 30, debug_paint_red_circle_paint);
    }

    public Matrix getOutPutMatrix() {
        return mMatrix;
    }

    public void flingStart(float speed_x, float speed_y) {
        flingmode = true;
        cvx = speed_x;
        cvy = speed_y;
        //   resume();
    }

    public void updateCursor(MotionEvent me) {
        external_cursor_position.set(update_screen_pointer(me.getX(), me.getY()));
    }

    @Override
    public void displacementReset() {
        downpoint = new PointF();
        uppoint = new PointF();
    }

    @Override
    public PointF getDisplacement() {
        return new PointF(uppoint.x - downpoint.x, uppoint.y - downpoint.y);
    }

    @Override
    public void onDown(MotionEvent me, PointF down_location) {
        downpoint = down_location;
    }

    @Override
    public void onUp(MotionEvent me, PointF up_location) {
        uppoint = up_location;
    }
}