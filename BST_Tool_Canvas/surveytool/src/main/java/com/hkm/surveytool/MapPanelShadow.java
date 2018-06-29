package com.hkm.surveytool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.hkm.surveytool.Element.AbPointRuler;
import com.hkm.surveytool.Element.DataPoints.BigObserveDot;
import com.hkm.surveytool.Element.Dot;
import com.hkm.surveytool.Element.Pen;
import com.hkm.surveytool.Element.ProposedTrialPit;
import com.hkm.surveytool.Element.Route;
import com.hkm.surveytool.Element.SurveyBoundary;
import com.hkm.surveytool.Listener.CanvasThread;
import com.hkm.surveytool.Listener.PanelTouchListener;
import com.hkm.surveytool.Listener.RenMatrixProcess;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MapPanelShadow extends SurfaceView implements SurfaceHolder.Callback2, SurfaceHolder.Callback, View.OnTouchListener {

    //vine
    //this is a list of valid positions with the respective order number from the starting route
    //===========================
    public static final ArrayList<HashMap<Dot, Dot>> test_result_potential = new ArrayList<HashMap<Dot, Dot>>();
    public static final String TAG = "MapPanel - CLASS error";
    protected static final int LAYERS_FLAGS = Canvas.MATRIX_SAVE_FLAG |
            Canvas.CLIP_SAVE_FLAG |
            //      Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
            Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
            Canvas.CLIP_TO_LAYER_SAVE_FLAG;
    protected static final ArrayList<Integer> validMarkerPostion = new ArrayList<Integer>();
    //      sequence of pair points that contains all the results including the null for no result
    protected static Point basemapsize;
    protected static int main_observe_point_interaction_radius = 35;
    //, centerFocual, scale_focus;
    protected static boolean interactive_layer_testing_points;
    protected static boolean interactive_layer_displacement_map;
    protected static int MODE;
    protected static boolean show_label = true, flingmode = false;
    protected static boolean interactive_main_a, interactive_main_b;
    protected static int touch_status;
    protected static boolean scale2Zero = false;
    protected static Bitmap panel_base_map;
    protected static boolean rOn = false;
    protected static boolean updateDots;
    protected static int current_active_pair = -1;
    protected BigObserveDot POINT_A, POINT_B;
    protected CanvasThread canvasThread;
    protected Canvas mainCanvas;
    protected SurfaceHolder thePanelHolder;
    protected boolean isFinal = false;
    protected Route route;
    protected SurveyBoundary sbRoute;
    protected AbPointRuler ruler;
    protected boolean onPressed = false;
    // private boolean PressLock = false;
    protected PointF CURSOR = new PointF();
    //private float scaleFactor = 1.0f, scaleOldFactor = 1.0f;
    // private float start_scale = 200f, require_to_start_scale = 200f;
    //  private float gTouchX1 = 0, gTouchX2 = 0, gTouchY1 = 0, gTouchY2 = 0, gCenterX = 0, gCenterY = 0, gRadius = 0;
    protected Pen mpen;
    protected drawMapSpecialGestureDetector mGestureDetector = null;
    protected ScaleGestureDetector mScaleGestureDetector = null;
    /**
     * Panel listener, for doing an action after expanded/collapsed/sliding state.
     */
    protected PanelTouchListener panelListenr = null;
    protected CMode currentMode = CMode.RESULT_POINTS;
    protected ProposedTrialPit proTP;
    protected RenMatrixProcess mRender_matrix_process;
    //it tells what the layer can be control
    protected ArrayList<Dot[]> reselectedPairObject = new ArrayList<Dot[]>();

    public MapPanelShadow(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        init();
    }

    public MapPanelShadow(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }

    public MapPanelShadow(Context ctx) {
        super(ctx);
        init();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }


    /**
     * added by Heskeyo Kam 20/8/2013
     * This method will restart the route from all the children interceptions
     */

    protected static enum RouteConfig {
        NONE, REMOVE_ALL_LINES;
    }

    public static enum CMode {
        //this is to show the current mode of the panel to tell which mode is on now
        NOT_USE,
        //DEFAULT or INITALIZED
        RESULT_POINTS,
        //ADJUSTING THE MAIN POINTS A AND B
        AB_POINTS,
        //ADJUSTING THE POINT ZOOM AND ZOOM OUT
        ADJUSTMENT,
        // SETTING SURVEY BOUNDARY MODE
        SB_MODE,
        // SETTING TRIAL PIT MODE
        TP_MODE,
        //CONSTRUCT LINES FROM THE TESTING OBSERVATION LIST
        SL_MODE,
        //Connecting all dots from the result listing
        CONNECT_MODE,
        //measureing ab distance and find the direct ratio on the map
        AB_POINT_MEASUREMENT,
        //editing the existing route points which the updated list have differences from drawn route
        ROUTE_EDITING_MODE,
        //this mode is to automatically connecting all the points from the route nodes
        AUTO_CONNTECT,
        //node inspector to display and show all the node points for interactions
        NODE_INSPECTOR
    }

    protected class drawMapSpecialGestureDetector extends GestureDetector {

        //    private int time_count = 0;
        //    private boolean additional_long_press = false;

        public drawMapSpecialGestureDetector(Context context, OnGestureListener listener) {
            super(context, listener);
            //         time_count = 0;
            //         additional_long_press = false;
        }

        public boolean onEnterEvent(MotionEvent e) {

            final boolean move = e.getAction() == MotionEvent.ACTION_MOVE;
            final boolean down = e.getAction() == MotionEvent.ACTION_DOWN;
            final boolean up = e.getAction() == MotionEvent.ACTION_UP;


            final int countpointer = e.getPointerCount();
            if (countpointer > 1) {
                /**
                 * running on the additional gestures
                 */

                return true;
            } else {

               /*

                additional_long_press = false;

                if (move) {
                    time_count++;
                    if (time_count > 1000) {
                        additional_long_press = true;
                    }
                } else if (up || down) {
                    time_count = 0;
                    additional_long_press = false;
                }

                */

                this.onTouchEvent(e);
                return false;
            }
        }
    }

    protected class DotTouch implements Dot.DotListener {
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

    public abstract BigObserveDot getA();

    public abstract BigObserveDot getB();

    protected abstract void black_box();

    protected abstract void touch_single_point_detection();

    protected abstract void init();

}
