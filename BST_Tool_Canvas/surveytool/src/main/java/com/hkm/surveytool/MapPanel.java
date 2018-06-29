package com.hkm.surveytool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.View;


import com.hkm.surveytool.Element.AbPointRuler;
import com.hkm.surveytool.Element.DataPoints.BigObserveDot;
import com.hkm.surveytool.Element.DataPoints.RouteNode;
import com.hkm.surveytool.Element.Dot;
import com.hkm.surveytool.Element.Math.EQPool;
import com.hkm.surveytool.Element.Pen;
import com.hkm.surveytool.Element.ProposedTrialPit;
import com.hkm.surveytool.Element.Route;
import com.hkm.surveytool.Element.SurveyBoundary;
import com.hkm.surveytool.Listener.CanvasThread;
import com.hkm.surveytool.Listener.PanelTouchListener;
import com.hkm.surveytool.Listener.RenMatrixProcess;
import com.hkm.surveytool.Listener.Tool;

import java.util.EnumSet;
import java.util.HashMap;

import static com.hkm.surveytool.AppDataRetain.current_sketch_map;
import static com.hkm.surveytool.AppDataRetain.require_update_pos;
import static com.hkm.surveytool.Element.DataPoints.SymbolC.indicator.FINAL;
import static com.hkm.surveytool.MapPanelShadow.CMode.AB_POINTS;
import static com.hkm.surveytool.MapPanelShadow.CMode.AB_POINT_MEASUREMENT;
import static com.hkm.surveytool.MapPanelShadow.CMode.ADJUSTMENT;
import static com.hkm.surveytool.MapPanelShadow.CMode.AUTO_CONNTECT;
import static com.hkm.surveytool.MapPanelShadow.CMode.CONNECT_MODE;
import static com.hkm.surveytool.MapPanelShadow.CMode.NODE_INSPECTOR;
import static com.hkm.surveytool.MapPanelShadow.CMode.ROUTE_EDITING_MODE;
import static com.hkm.surveytool.MapPanelShadow.CMode.SB_MODE;
import static com.hkm.surveytool.MapPanelShadow.CMode.SL_MODE;
import static com.hkm.surveytool.MapPanelShadow.CMode.TP_MODE;


/**
 * Created by Hesk 2013, Basic Panel Development is in here
 */
public class MapPanel extends MapPanelShadow {
    public MapPanel(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
    }

    public MapPanel(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public MapPanel(Context ctx) {
        super(ctx);
    }

    @Override
    protected void init() {
        thePanelHolder = getHolder();
        thePanelHolder.addCallback(this);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());
        mGestureDetector = new drawMapSpecialGestureDetector(getContext(), new commonGestureListenr());
        thePanelHolder.setFixedSize(getWidth(), getHeight());
        // this.setOnTouchListener(getApplicationContext());
        canvasThread = new CanvasThread(thePanelHolder, new CanvasThread.CallBack() {
            @SuppressLint("WrongCall")
            @Override
            public void threadRender(Canvas c) {
                MapPanel.this.mainCanvas = c;
                MapPanel.this.onDraw(c);
            }
        });
        this.setFocusable(false);
        updateDots = false;
    }

    public void fresh_init(Canvas cvx) {


        if (route == null) {
            route = new Route(cvx, MapPanel.this);
            if (route.check_for_auto_connect_mode()) {
                setMode(AUTO_CONNTECT);
            }
        }

        if (mpen == null)
            mpen = new Pen(cvx, getContext());

        if (sbRoute == null) {
            if (current_sketch_map.getSurveybound() == null) {
                final SurveyBoundary sb = new SurveyBoundary(cvx, getContext(), mpen);
                current_sketch_map.setSurveybound(sb);
                sbRoute = current_sketch_map.getSurveybound();
            } else {
                sbRoute =
                        current_sketch_map
                                .getSurveybound()
                                .initialize(cvx, getContext(), mpen)
                                .start()
                ;
            }
        }

        if (proTP == null) {
            if (current_sketch_map.getTpElement() == null) {
                //initialize and bind the tp to the data set
                final ProposedTrialPit tp = new ProposedTrialPit(cvx, getContext());
                current_sketch_map.setTpElement(tp);
                proTP = current_sketch_map.getTpElement();
            } else {
                //load the TP element
                final ProposedTrialPit tp = current_sketch_map.getTpElement();
                proTP = tp.initalize(cvx, getContext()).initializeTPBoxes();
            }
        }

        if (mRender_matrix_process == null) {
            mRender_matrix_process = new RenMatrixProcess(CURSOR, cvx);
            proTP.setRenderingProcess(mRender_matrix_process);
        }

        if (POINT_A == null && POINT_B == null) {
            POINT_A = current_sketch_map.get_point_a();
            POINT_B = current_sketch_map.get_point_b();
        }


        if (mpen != null && ruler == null) {
            ruler = new AbPointRuler(cvx, POINT_A, POINT_B, this, mpen);
        }

        resume();
    }

    public void restoreInstanceState(Bundle inState) {
        if (AppDataRetain.saved_route != null)
            route = AppDataRetain.saved_route;
    }

    public void saveInstanceState(Bundle outState) {
        AppDataRetain.saved_route = route;
    }

    public boolean can_connect_each_dots() {
        if (render_moving_AB_point_testing_results() == -1) {
            render_new_testing_pairs();
            return true;
        } else return false;
    }

    public int continue_dot_process() {
        updateDots = true;
        final boolean connect_can = can_connect_each_dots();
        final int route_node_length = route.size();
        if (require_update_pos) {
            if (connect_can) {
                if (route_node_length > 0) {
                    setMode(ROUTE_EDITING_MODE);
                    Log.d(TAG, "set to route editing mode");
                    return 3;
                } else {
                    setMode(CONNECT_MODE);
                    Log.d(TAG, "set to connection mode");
                    return 1;
                }
            } else {
                return 2;
            }
        } else {
            if (connect_can) {
                if (route_node_length > 0) {
                    //nothing changes
                    route.update_route_label();
                    return 3;
                } else {
                    setMode(CONNECT_MODE);
                    return 1;
                }
            } else {
                return 2;
            }
        }
    }

    /**
     * ------------------------------- *
     */

    // private Path path;
    public Point getBaseMapSize() {
        return basemapsize;
    }

    private boolean isMode(CMode new_given_mode) {
        return currentMode == new_given_mode;
    }

    private boolean isGesturePressMode() {
        EnumSet<CMode> e = EnumSet.of(AB_POINTS, ROUTE_EDITING_MODE, CONNECT_MODE, NODE_INSPECTOR);
        return e.contains(currentMode);
    }

    private boolean isCommonInteractiveMode() {
        EnumSet<CMode> e = EnumSet.of(SL_MODE, ROUTE_EDITING_MODE, CONNECT_MODE, NODE_INSPECTOR);
        return e.contains(currentMode);
    }

    public CMode getMode() {
        return currentMode;
    }

    /**
     * status initialization callback
     *
     * @param mmode
     * @return
     */
    public boolean setMode(CMode mmode) {
        currentMode = mmode;
        if (isMode(SB_MODE)) {
            isFinal = false;
        } else if (isMode(AB_POINTS)) {
            isFinal = false;
            route.removeAll();
            validMarkerPostion.clear();
            test_result_potential.clear();
        } else if (isMode(CONNECT_MODE)) {
            isFinal = false;

        } else if (isMode(ADJUSTMENT)) {
            isFinal = true;
        } else if (isMode(AB_POINT_MEASUREMENT)) {

        } else if (isMode(ROUTE_EDITING_MODE)) {
            isFinal = false;
            if (Constant.update_existing_index.size() == 0) {
                Tool.trace(getContext(), "error from loading correction data, there is no data found");
                //   panelListenr.onInitDialog(this, Constant.DialogInt.ROUTE_EDIT_MODE_DONE_DIALOG);
            } else {
                route.setRouteComplete(false);
                for (Integer index_update_item : AppDataRetain.update_existing_index) {
                    final RouteNode rn = current_sketch_map.get_route_node_at(index_update_item);
                    if (rn.has_result()) {
                        rn
                                .reset_possible_points()
                                .calculate_test_pairs(POINT_A, POINT_B)
                                .GenerateDotsFromTestResult(getContext(), mainCanvas, new DotTouch());
                    }
                    reselectedPairObject.add(new Dot[]{rn.getResult_0(), rn.getResult_1()});
                }
                current_active_pair = 0;
                panelListenr.onInitDialog(this, Constant.DialogInt.ROUTE_EDIT_MODE_START_DIALOG);
            }
            // assert ((WorkPanel) getContext()) != null;
        } else if (isMode(TP_MODE)) {

        } else if (isMode(NODE_INSPECTOR)) {
            isFinal = false;
        } else if (isMode(ADJUSTMENT)) {
            route.setRenderLabel(true);
        } else {
            route.setRenderLabel(false);
        }
        resume();
        return true;
    }

    @Override
    public BigObserveDot getA() {
        return POINT_A;
    }

    @Override
    public BigObserveDot getB() {
        return POINT_B;
    }

    /**
     * ------------------------------- *
     */
    @Override
    public SurfaceHolder getHolder() {
        return super.getHolder();
    }

    /**
     * This will trigger and render the existing data from the radius list and render them into the testing points
     * <p/>
     * This is to initially render the point pair on each circle as to new a Dot object on the canvas
     */
    private int render_moving_AB_point_testing_results() {
        int found_results = 0;
        final int radius_size = current_sketch_map.get_route_size();
        //there are no existing dot pairs and it will return NO result
        if (radius_size == 0) {
            return found_results;
        }
        for (int t = 0; t < radius_size; t++) {
            final RouteNode rn = current_sketch_map.get_route_node_at(t);
            // PointF[] es = ToolBox.getLayerIntersectPairES(POINT_A, POINT_B, rn.get_cal_r_a(), rn.get_cal_r_b());
            if (rn.has_result()) {
                found_results++;
            }
        }
        //this is to confirm that all potential pairs are valid and also there is an existing potential pairs
        if (radius_size == found_results) {
            return -1;
        }
        //otherwise there will return the number of results from the only existing valid pairs
        return found_results;

    }

    /**
     * render all the new testing pairs and
     * assume that all pairs are valid
     */
    private void render_new_testing_pairs() {
        final int radius_size = current_sketch_map.get_route_size();
        validMarkerPostion.clear();
        test_result_potential.clear();
        AppDataRetain.nresult.clear();
        for (int i = 0; i < radius_size; i++) {
            final RouteNode rn = current_sketch_map.get_route_node_at(i);
            if (rn.has_result()) {
                HashMap<Dot, Dot> pair = rn
                        .calculate_test_pairs(POINT_A, POINT_B)
                        .GenerateDotsFromTestResult(getContext(), mainCanvas, new DotTouch());
       /*     final PointF[] test_sample_pair = ToolBox.getLayerIntersectPairES(POINT_A, POINT_B, rn.get_cal_r_a(), rn.get_cal_r_b());
            final HashMap<Dot, Dot> Pair_of_Dots = new HashMap<Dot, Dot>();
            final Dot da = new Dot(test_sample_pair[0], mainCanvas, i, getContext(), new DotTouch());
            final Dot db = new Dot(test_sample_pair[1], mainCanvas, i, getContext(), new DotTouch());*/
                //got to be done after importing paints from setMaterials
                // Pair_of_Dots.put(da.onDraw(), db.onDraw());
                validMarkerPostion.add(i);    //adding to the valid markers list
                test_result_potential.add(pair);  //adding to total dot list
            }
        }
    }


    /**
     * Capability
     * this is the display from the hidden dots if that mode set to others
     */
    private void setPairActive(int i) {
        current_sketch_map.get_route_node_at(i).control_node_pair_active();
        current_active_pair = i;
    }

    @Override
    protected void touch_single_point_detection() {
        if (onPressed && EQPool.onPressedCicle(POINT_A, CURSOR, main_observe_point_interaction_radius)) {
            interactive_main_a = true;
        }
        if (onPressed && EQPool.onPressedCicle(POINT_B, CURSOR, main_observe_point_interaction_radius)) {
            interactive_main_b = true;
        }
    }

    @Override
    protected void black_box() {
        /**
         * touch detection and then render the points after release
         * section for moving the main points
         */
        if (isMode(AB_POINTS)) {
            touch_single_point_detection();
            /**
             * section for moving all the children points
             */
            try {
                if (route.isComplete()) {
                    route.removeAll();
                }
                if (updateDots) {
                    route.update_dots();
                    route.update_route_label();
                    updateDots = false;
                }
                mpen.render_ref_touch(POINT_A, POINT_B, interactive_main_a, interactive_main_b);
                if (interactive_main_a || interactive_main_b) {
                    int results = render_moving_AB_point_testing_results();
                    mpen.render_testing_pairs_results(results, new PointF[]{POINT_A, POINT_B});
                } else {
                    render_new_testing_pairs();
                }
                //render_new_testing_pairs();
            } catch (Exception e) {
                Log.d(TAG, "black box @432. Error; " + e.toString());
            }
        }
        boolean nextActivate = false;
        if (isMode(CONNECT_MODE)) {
            if (route.size() == 0 && validMarkerPostion.size() > 0) {
                nextActivate = true;
                // -- -- setPairActive(0);
            }
            /**
             * section for drawing path or lines
             */
            try {
                for (int i = 0; i < validMarkerPostion.size(); i++) {
                    // =======================
                    final Integer hp = validMarkerPostion.get(i);
                    final HashMap<Dot, Dot> hashPair = test_result_potential.get(hp);
                    final Dot dotA = (Dot) hashPair.keySet().toArray()[0];
                    final Dot dotB = (Dot) hashPair.values().toArray()[0];
                    if (nextActivate) {
                        nextActivate = false;
                        setPairActive(i);
                    }
                    // =======================
                    if (onPressed) {
                        final boolean dot_a_hit = EQPool.onPressedCicle(dotA, CURSOR, main_observe_point_interaction_radius);
                        final boolean dot_b_hit = EQPool.onPressedCicle(dotB, CURSOR, main_observe_point_interaction_radius);
                        if (dot_a_hit && !dotA.getLock()) {
                            current_sketch_map.get_route_node_at(hp).choose_possible_point(RouteNode.FIRST_POINT_CHOICE);
                            route.add(dotA, dotB);
                            nextActivate = true;
                        } else if (dot_b_hit && !dotB.getLock()) {
                            current_sketch_map.get_route_node_at(hp).choose_possible_point(RouteNode.SECOND_POINT_CHOICE);
                            route.add(dotB, dotA);
                            nextActivate = true;
                        }
                    }
                    // =======================
                    if (nextActivate && i == validMarkerPostion.size() - 1) {
                        panelListenr.onInitDialog(this, Constant.DialogInt.FRESH_CONNECT_NODE_DONE_DIALOG);
                        current_active_pair = -1;

                        route.setRouteComplete(true);
                        route.update_dots();
                        route.update_route_label();
                        updateDots = false;
                    }
                }

            } catch (Exception e) {
                Log.d(TAG, " section for drawing path or lines @505. Error; " + e.toString());
            } finally {
                if (updateDots) {
                    route.update_dots();
                    route.update_route_label();
                    updateDots = false;
                }
                mpen.render_noref_maindoticon(POINT_A, POINT_B);
                if (!route.isComplete()) {
                    final PointF[] a = {POINT_A, POINT_B};
                    // final List<Integer>[] b = new List[]{radius_a_slot, radius_b_slot};
                    //  mpen.render_ring_pair_at(a, b, current_active_pair);
                    mpen.render_ring_pair_at(a, current_active_pair);
                    Pen.render_each_crosses(validMarkerPostion, test_result_potential);
                }
                sbRoute.renderPath();
                route.renderPath();
                proTP.renderPath();
            }
        }
        if (isMode(ADJUSTMENT)) {
            /**
             * section for showing the adjustment of the main point A and the main point B
             */
            mpen.render_ref_maindoticon(POINT_A, POINT_B);
            sbRoute.renderPath();
            route.renderPath();
            proTP.renderPath();
        }
        if (isMode(ROUTE_EDITING_MODE)) {
            try {
                if (Constant.update_existing_index.size() > 0) {
                    final int reSelectIndex = Constant.update_existing_index.get(current_active_pair);
                    if (reselectedPairObject.size() > 0) {

                        Dot[] pair = reselectedPairObject.get(current_active_pair);
                        //  change the display of the dot that shows the dot can interact with the finger
                        pair[0].setCanTouch(true);
                        pair[1].setCanTouch(true);
                        pair[0].SelectPoint(Dot.selection.NO_CONFIRM);
                        pair[1].SelectPoint(Dot.selection.NO_CONFIRM);
                        //  also unlock the dot on the screen in order to be physically interacted
                        pair[0].setLock(false);
                        pair[1].setLock(false);

                        pair[0].onDraw();
                        pair[1].onDraw();

                        if (onPressed) {
                            final boolean dot_a_hit = EQPool.onPressedCicle(pair[0], CURSOR, main_observe_point_interaction_radius);
                            final boolean dot_b_hit = EQPool.onPressedCicle(pair[1], CURSOR, main_observe_point_interaction_radius);
                            final RouteNode RN = current_sketch_map.get_route_node_at(reSelectIndex);
                            if (dot_a_hit && !pair[0].getLock()) {
                                RN.choose_possible_point(RouteNode.FIRST_POINT_CHOICE);
                                RN.setIntdicate(FINAL);
                                route.changeRouteDotAt(pair[0], pair[1]);
                            } else if (dot_b_hit && !pair[1].getLock()) {
                                RN.choose_possible_point(RouteNode.SECOND_POINT_CHOICE);
                                RN.setIntdicate(FINAL);
                                route.changeRouteDotAt(pair[1], pair[0]);
                            }
                            if (dot_a_hit || dot_b_hit) {
                                if (reselectedPairObject.size() - 1 > current_active_pair) {
                                    current_active_pair++;
                                } else {
                                    reselectedPairObject.clear();
                                    Constant.update_existing_index.clear();
                                    route.setRouteComplete(true);
                                    route.update_dots();
                                    route.update_route_label();
                                    panelListenr.onInitDialog(this, Constant.DialogInt.ROUTE_EDIT_MODE_DONE_DIALOG);
                                    current_active_pair = -1;
                                    updateDots = true;
                                }
                            }
                        }
                        mpen.render_ring_pair_at(new PointF[]{POINT_A, POINT_B}, reSelectIndex);
                    }
                } else {
                    setMode(ADJUSTMENT);
                }
            } catch (ArrayIndexOutOfBoundsException e2) {
                e2.printStackTrace();
            } catch (IndexOutOfBoundsException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // mpen.render_route_labels(validMarkerPostion, route, null);
                mpen.render_ref_maindoticon(POINT_A, POINT_B);
                sbRoute.renderPath();
                route.renderPath();
                proTP.renderPath();
            }
        }
        if (isMode(SB_MODE) || isMode(AB_POINTS) || isMode(TP_MODE)) {
            /**
             * section for showing AB_POINTS the adjustment of the main point A and the main point B
             */
            sbRoute.renderPath();
            route.renderPath();
            proTP.renderPath();
        }
        if (isMode(AB_POINT_MEASUREMENT)) {
            //only to render the dots within the the ruler
            ruler.renderPath();
        }
        if (isMode(NODE_INSPECTOR)) {
            if (onPressed) {
                for (Dot d : route.getContainer()) {
                    final boolean dot_hit = EQPool.onPressedCicle(d, CURSOR, main_observe_point_interaction_radius);
                    if (dot_hit && !d.getLock()) {
                        d.dot_event_press();
                    } else {
                        d.dot_event_release();
                    }
                }
            }
            mpen.render_ref_maindoticon(POINT_A, POINT_B);
            sbRoute.renderPath();
            route.renderPath();
            proTP.renderPath();
        }
        if (isMode(AUTO_CONNTECT)) {
            if (mainCanvas != null && POINT_A != null && POINT_B != null && mpen != null && ruler != null) {
                route.setRouteComplete(false);
                route.removeAll();
                for (RouteNode rn : current_sketch_map.getRouteNodeList()) {
                    rn.get_label().refresh_data(getContext());
                    rn.calculate_test_pairs(POINT_A, POINT_B).GenerateDotsFromTestResult(getContext(), mainCanvas, new DotTouch());
                    rn.apply_decision(route);
                }
                route.setRenderLabel(true);
                route.update_dots();
                route.update_route_label();
                route.setRouteComplete(true);
                setMode(ADJUSTMENT);
            }
        }
    }
    //end of black box

    public String getCursorC() {
        final float c1 = EQPool.precision(2, CURSOR.x);
        final float c2 = EQPool.precision(2, CURSOR.y);
        return "[" + Float.toString(c1) + "," + Float.toString(c2) + "]";
    }

    public Canvas getCanvas() {
        return mainCanvas;
    }

    public void setCanvas(Canvas cvs) {
        if (panel_base_map.isRecycled()) {
            mainCanvas = cvs;
        }
    }

    private void render_bitmap(Canvas pointer) {
        final Rect src = new Rect(0, 0, basemapsize.x, basemapsize.y);
        //starting on the position of the render object
        final Rect dst = new Rect(0, 0, basemapsize.x, basemapsize.y);
        final Paint p = new Paint();
        p.setFilterBitmap(true);
        pointer.drawBitmap(panel_base_map, src, dst, p);
    }

    //this is to invalidate the drawings
    public Canvas finalDraw(Canvas new_paper, Matrix m) {
        // final Canvas new_paper = new Canvas();
        try {
            Pen.paintcolor(new_paper); //fill in the color
            render_bitmap(new_paper); //render the base map
            mpen.render_noref_maindoticon_canvas(POINT_A, POINT_B, new_paper);
            sbRoute.renderPath(new_paper); // render the final result path for the boundary border
            proTP.renderPath(new_paper);
            route.renderPath(new_paper, m); // render the final result path for all the reporting paths
            // Pen.render_each_crosses(new_paper, validMarkerPostion, test_result_potential);
            // route.render_crosses(new_paper);
            // mpen.render_route_labels(validMarkerPostion, route, new_paper);
        } catch (Exception e) {
            Log.d(TAG, "final draw map panel");
        }
        new_paper.save();
        return new_paper;
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return false;
    }
*/

    //HAS_ALPHA_LAYER_SAVE_FLAG
    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas cvx) {
        // super.onDraw(cvx);
        Paint pl = new Paint();
        pl.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cvx.drawPaint(pl);
        pl.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        pl.setColor(getResources().getColor(R.color.white));
        if (rOn) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            cvx.drawPaint(paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            cvx.saveLayerAlpha(0, 0,
                    cvx.getWidth(),
                    cvx.getHeight(),
                    0xBB, LAYERS_FLAGS);
        }
        // cvx.drawColor(R.color.white);
        ondraw_scale();
        try {
            render_bitmap(cvx);
            black_box();
            //  mRender_matrix_process.render_helper_debug();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        if (rOn) {
            // cvx.restoreToCount(1);
            cvx.restore();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        final boolean move = me.getAction() == MotionEvent.ACTION_MOVE;
        final boolean down = me.getAction() == MotionEvent.ACTION_DOWN;
        final boolean up = me.getAction() == MotionEvent.ACTION_UP;

        if (!proTP.setInteractive(v, me)) {
            if (isMode(ADJUSTMENT)) {
                if (mScaleGestureDetector.onTouchEvent(me)) {
                    mGestureDetector.onTouchEvent(me);
                }
                //panelListenr.onStream(this);
                return true;
            } else if (isMode(AB_POINTS)) {
                if (up) {
                    interactive_main_a = interactive_main_b = false;
                    onPressed = false;
                } else {
                    mGestureDetector.onEnterEvent(me);
                }
            } else if (isMode(AB_POINT_MEASUREMENT)) {
                if (up) {
                    ruler.onPressed = false;
                } else {
                    mGestureDetector.onEnterEvent(me);
                }
            } else if (isMode(SL_MODE)) {
                if (up) {
                    onPressed = false;
                } else mGestureDetector.onEnterEvent(me);
            } else if (isCommonInteractiveMode()) {
                if (up) {
                    onPressed = false;
                } else {
                    //  mGestureDetector.onEnterEvent(me);
                    if (mScaleGestureDetector.onTouchEvent(me)) {
                        mGestureDetector.onTouchEvent(me);
                    }
                }
            } else if (isMode(SB_MODE)) {
                if (down) {
                    sbRoute.add(mRender_matrix_process.update_screen_pointer(me));
                }
            }
        }
        return true;
    }

    public Matrix getMpanelmatrix() {
        return mRender_matrix_process.getOutPutMatrix();
    }


    /**
     * Set a new, different from the default, PanelSlideListener
     */
    public MapPanel setPanelListener(PanelTouchListener obj) {
        panelListenr = obj;
        return this;
    }

    public MapPanel setBaseMap(Bitmap b, Point map_size) {
        basemapsize = map_size;
        panel_base_map = b;
        AppDataRetain.basemap_input_size.set(map_size.x, map_size.y);
        System.out.print("set base map size done:" + AppDataRetain.basemap_input_size);
        //   panel_base_map = BitmapFactory.decodeResource(getResources(),R.drawable.static_testing);
        return this;
    }

    public void startPanel() {
        canvasThread.start();
    }

    public void pause() {
        canvasThread.onPause();
    }

    public void resume() {
        canvasThread.onResume();
    }

    protected void mechanism_holder(SurfaceHolder h, CanvasThread.CallBack cb) {
        Canvas c = null;
        try {
            if (!h.getSurface().isValid()) throw new Exception("in valid canvas");
            c = h.lockCanvas(null);
            synchronized (h) {
                cb.threadRender(c);
            }
        } catch (Exception e) {
            Log.d(TAG, "+796: surface holder is having error");
        } finally {
            if (c != null) {
                h.unlockCanvasAndPost(c);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mechanism_holder(holder, new CanvasThread.CallBack() {
            @Override
            public void threadRender(Canvas canvas) {
                fresh_init(canvas);
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
        saveInstanceState(null);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //  trace_msg("surfaceDestroyed");
        // canvasThread.setRunning(false);
        /*if (!canvasThread.isInterrupted())
            canvasThread.interrupt();*/
        Log.d(TAG, "surfaceDestroyed");
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceRedrawNeeded");
    }

    public Route getRoute() {
        return route;
    }

    public ProposedTrialPit getPT() {
        return proTP;
    }

    public SurveyBoundary getSB() {
        return sbRoute;
    }

    public AbPointRuler getRuler() {
        return ruler;
    }


    //http://blog.sina.com.cn/s/blog_7bac470701014gnx.html
    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();
        System.out.println("hello");
        // Save=(Button)findViewById(R.id.Save);
        System.out.println("hello2");
    }

    public void onConfigurationChanged(Configuration c) {
        proTP.onChangeConfig(c);
        resume();
    }

    private void ondraw_scale() {
        //https://github.com/andreynovikov/Androzic
        //http://stackoverflow.com/questions/17281409/problems-with-bound-zoom-able-offline-maps-my-own-maps-not-gs
        mRender_matrix_process.postUpdate();
        mainCanvas.setMatrix(mRender_matrix_process.getOutPutMatrix());
        mpen.updateCanvas(mainCanvas);
        route.updateCanvas(mainCanvas);
        sbRoute.updateCanvas(mainCanvas);
        proTP.updateCanvas(mainCanvas, mRender_matrix_process.getOutPutMatrix());
    }

    public void UpdateAB2Constants() {
        //  AppDataRetain.current_sketch_map.get_point_a() = POINT_A;
        //  AppDataRetain.current_sketch_map.get_point_b() = POINT_B;

    }


    /**
     *
     *//*
        public Render_matrix_process() {
            flingmode = false;
            xNew = yNew = xScreen = yScreen = xImage = yImage = xLast = yLast = cvx = cvy = 0;
            scale = scale < MIN_SCALE ? MIN_SCALE : (scale > MAX_SCALE ? MAX_SCALE : scale);
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
        private void update(float dx, float dy, MotionEvent me) {
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
                    pauseAndLabel();
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
            mainCanvas.drawCircle(xNew, yNew, 30, mpen.getPaintInteraction());
        }

        public Matrix getOutPutMatrix() {
            return mMatrix;
        }

        public void flingStart(float speed_x, float speed_y) {
            flingmode = true;
            cvx = speed_x;
            cvy = speed_y;
            resume();
        }

        public void updateCursor(MotionEvent me) {
            CURSOR.set(update_screen_pointer(me.getX(), me.getY()));
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
    }*/

    protected class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        /**
         * Minimum velocity that will be detected as a fling
         */
        private static final int MIN_FLING_VELOCITY = 400; // dips per second
        private float store_scale_factor;

        public ScaleGestureListener() {
            store_scale_factor = 1;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float getscale = detector.getScaleFactor() / 3;
            float delta = getscale - store_scale_factor;
            store_scale_factor = getscale;
            // scaleFactor = detector.getScaleFactor() / 3;
            mRender_matrix_process.update_scale_delta(delta);
            /*

            scaleFactor += delta;
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            */
            // Don't let the object get too small or too large.
            //System.out.print(scaleFactor);
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            store_scale_factor = detector.getScaleFactor() / 3;
            // store_scale_factor = getscale;
            //  final PointF localFocus = new PointF(detector.getFocusX(), detector.getFocusY());
            mRender_matrix_process.move_map_w_cursor(detector.getFocusX(), detector.getFocusY());
            //scale_focus = new PointF((localFocus.x + m_offset.x) / scaleFactor, (localFocus.y + m_offset.y) / scaleFactor);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }

    }

    protected class commonGestureListenr implements GestureDetector.OnGestureListener {

        //  private boolean onMovingMap = false;
        //  private boolean ab_point_move_map = false;
        private Vibrator vibrator;

        public commonGestureListenr() {
            //     onMovingMap = false;
            //     ab_point_move_map = false;
            vibrator = (Vibrator) MapPanel.this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            System.out.print("long");
            final PointF C = mRender_matrix_process.update_screen_pointer(e.getX(), e.getY());
            if (isGesturePressMode()) {
                onPressed = true;
                CURSOR.set(C);
            }
            if (isMode(AB_POINT_MEASUREMENT)) {
                ruler.onPressed = true;
                ruler.cursor.set(C);
            }
            //   ab_point_move_map = false;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            System.out.print("long");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        private void offset_map_exclusive_AB(MotionEvent e2, float distanceX, float distanceY) {
            mRender_matrix_process.updateCursor(e2);
            if (interactive_main_a) {
                POINT_A.set(CURSOR);
                // Constant.changed_to_new_ab_points = onPressed = true;
            } else if (interactive_main_b) {
                POINT_B.set(CURSOR);
                //  Constant.changed_to_new_ab_points = onPressed = true;
            } else {
                mRender_matrix_process.update(distanceX, distanceY, e2);
                // onPressed = false;
            }

        }

        private void offset_map_exclusive_route(MotionEvent e2, float distanceX, float distanceY) {
            final PointF onScreenPointer = mRender_matrix_process.update_screen_pointer(e2.getX(), e2.getY());
            CURSOR.set(onScreenPointer);
            if (!route.is_touch_on_route(CURSOR, main_observe_point_interaction_radius)) {
                mRender_matrix_process.update(distanceX, distanceY, e2);
            } else {
                //        ab_point_move_map = false;
            }
            onPressed = false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isMode(AB_POINTS)) {
                offset_map_exclusive_AB(e2, distanceX, distanceY);
            } else if (isMode(ROUTE_EDITING_MODE)) {
                //   mRender_matrix_process.update(distanceX, distanceY, e2);
                //   onPressed = false;
                offset_map_exclusive_route(e2, distanceX, distanceY);
            } else if (isMode(AB_POINT_MEASUREMENT)) {

                final PointF onScreenPointer = mRender_matrix_process.update_screen_pointer(e2.getX(), e2.getY());
                ruler.touch_detection(onScreenPointer);
            } else if (isMode(ADJUSTMENT)) {
                mRender_matrix_process.update(distanceX, distanceY, e2);
            } else if (isMode(CONNECT_MODE)) {
                offset_map_exclusive_route(e2, distanceX, distanceY);
            }
            if (isMode(ADJUSTMENT)) {
                //onMovingMap = true;
            } else {
                if (!isMode(AB_POINTS)) {
                    //  onMovingMap = false;
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (isMode(AB_POINTS)) {
                // ab_point_move_map = true;
                // vibrator.vibrate(200);
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // if (onMovingMap) {
            //  mRender_matrix_process.flingStart(velocityX / 30, velocityY / 30);
            //    return true;
            // } else {
            //    return false;
            // }
            return false;
        }
    }


}