package com.hkm.oc.panel.corepanel.elements;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.google.gson.annotations.SerializedName;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.elements.mathmodels.DotPair;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;
import com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox;
import com.hkm.U.Tool;
import com.hkm.oc.panel.corepanel.elements.mathmodels.line_eq;
import com.hkm.oc.panel.corepanel.elements.shapes.Element;
import com.hkm.oc.panel.corepanel.handler.PTModeCallback;
import com.hkm.oc.panel.corepanel.handler.RenMatrixProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Hesk on
 */
public class ProposedTrialPit extends Element implements View.OnClickListener, Serializable {

    transient public final static int
            TPmodeOFF = 0, TPmodeON = 1,
            AngleMode = 4, EditAngleMode = 5,
            DrawRectMode = 8, SelectMode = 32,
            EditLabel = 7, EditLabelPosition = 44, Edit_tp_dimension = 45;

    public static enum action {
        TPV2_confirm_center_point, // step 1
        TPV2_drag_distance, // step 2
        TPV2_Edit_rectangle_dimension, // step 3
        Edit_tp_dimension,
        Edit_Label_text, // step 1
        Edit_Label_position, // step 2
        TPV1_drag_angle, // step 1
        TPV1_drag_rectangle, // step 2
        Select_rectangle //
    }

    transient final Paint p_default = new Paint(), p_paint_dot = new Paint(), tp_line = new Paint();
    transient final PointF center_checking_point = new PointF();

    @SerializedName("boxcontainer")
    private ArrayList<TPBox> BoxContainer = new ArrayList<TPBox>();

    transient private int mode;
    transient private line_eq le;
    transient private DotPair de;
    transient private action maction;
    transient private static TPBox current_working_box;
    transient private ActionMode action_mode_bar_object;
    transient private Button label_button;
    transient private EditText medittext;
    transient private View v_input_box;
    transient private boolean onlabeling, locked_angle_on_EditAngleMode;
    transient private BitmapShader fillBMPshader;
    transient private RenMatrixProcess renderingp;
    transient private Configuration use_orientation = null;
    transient private Point device_default_center_landscape = null, device_default_center_portrait = null;
    transient private boolean bound_update = false;
    transient private long updateBoundTime = 0;
    transient private int current_selected_boxes;
    transient private int selected_box_id;
    transient private PTModeCallback tp_cb;
    transient public final String TAG = "TP - ProposedTrialPit - CLASS";
    transient private FragmentManager mfragmentgr;

    public ProposedTrialPit(Canvas mainCanvas, Context ctx) {
        initalize(mainCanvas, ctx).clearThis();
    }

    public ProposedTrialPit(ArrayList<TPBox> loaddata) {
        this.BoxContainer = loaddata;
    }

    public ProposedTrialPit initalize(final Canvas mainCanvas, final Context ctx) {
        this.main = mainCanvas;
        this.ctx = ctx;

        p_default.setStyle(Paint.Style.STROKE);
        p_default.setStrokeWidth(2.9f);
        p_default.setColor(ctx.getResources().getColor(R.color.holo_red_dark));
        p_default.setAntiAlias(true);
        le = new line_eq(new PointF(0, 0), new PointF(0, 0));
        tp_line.set(p_default);
        tp_line.setStrokeWidth(1.0f);
        tp_line.setColor(Color.BLACK);
        p_paint_dot.set(p_default);
        p_paint_dot.setStyle(Paint.Style.FILL);
        p_paint_dot.setColor(ctx.getResources().getColor(R.color.holo_orange_dark));
        p_paint_dot.setAlpha(80);
        de = new DotPair(ctx);
        mode = TPmodeOFF;
        onlabeling = locked_angle_on_EditAngleMode = false;
        //Initialize the bitmap object by loading an image from the resources folder
        Bitmap fillBMP = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.filledstrip);
        //Initialize the BitmapShader with the Bitmap object and set the texture tile mode
        fillBMPshader = new BitmapShader(fillBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return this;
    }

    public ProposedTrialPit initializeTPBoxes() {
        if (BoxContainer.size() > 0) {
            for (TPBox b : BoxContainer) {
                b.drawDone(true)
                 .setPaint(tp_line, fillBMPshader)
                 .setRun(this.main, this.ctx);
            }
        }
        return this;
    }

    public void setTPListener(PTModeCallback f) {
        tp_cb = f;
    }

    public ProposedTrialPit setRenderingProcess(RenMatrixProcess rm) {
        renderingp = rm;
        return this;
    }

    public ProposedTrialPit setActionModeObject(ActionMode mmode) {
        action_mode_bar_object = mmode;
        return this;
    }

    private boolean isAct(action m) {
        return maction == m;
    }

    /**
     * this function will run in a loop and constantly going
     *
     * @param v
     * @param event
     * @return
     */
    public boolean setInteractive(View v, MotionEvent event) {
        //screen offset points correction
        float[] val = new float[9];

        boolean move = event.getAction() == MotionEvent.ACTION_MOVE;
        boolean down = event.getAction() == MotionEvent.ACTION_DOWN;
        boolean up = event.getAction() == MotionEvent.ACTION_UP;
        boolean two_fingers = event.getPointerCount() == 2;
        boolean single_finger_down = event.getPointerCount() == 1;

        final PointF pf = renderingp.update_screen_pointer(event.getX(), event.getY());
        final Point pn = new Point((int) pf.x, (int) pf.y);
        if (isAct(action.Edit_Label_position)) {


        } else if (mode == ProposedTrialPit.AngleMode || mode == ProposedTrialPit.EditAngleMode) {
            if (two_fingers) {
                final PointF p0 = renderingp.update_screen_pointer(event.getX(0), event.getY(0));
                final PointF p1 = renderingp.update_screen_pointer(event.getX(1), event.getY(1));
                // final PointF p0 = new PointF(event.getX(0) - dp.x, event.getY(0) - dp.y);
                // final PointF p1 = new PointF(event.getX(1) - dp.x, event.getY(1) - dp.y);
                //screen offset points correction
                if (move || down) {
                    le.update_line_by_points(p1, p0);
                    de.setPair(p0, p1);
                    if (mode == ProposedTrialPit.EditAngleMode && locked_angle_on_EditAngleMode) {
                        deSelectAll();
                        locked_angle_on_EditAngleMode = false;
                    }
                }
            }
            return true;
        } else if (mode == ProposedTrialPit.EditLabelPosition) {
            if (current_working_box != null && single_finger_down) {
                if (down) {
                    renderingp.displacementReset();
                    renderingp.onDown(event, new PointF(event.getX(), event.getY()));
                } else {
                    renderingp.onUp(event, new PointF(event.getX(), event.getY()));
                }
                current_working_box.setLabelPositionOffsetPreview(renderingp.getDisplacement());
            }
            return true;
        } else if (mode == ProposedTrialPit.DrawRectMode) {
            if (!onlabeling) {
                //   final Point center = le.getMinDistancePoint(pf);
                if (down) {
                    //adding new box TP
                    onNewTP(pn);
                } else if (move) {
                    //adjusting and updating the current box
                    onConstructingTP(pn);
                } else if (up) {
                    //confirm and finalize the box and the labeling box will pops up
                    onConfirmTP();
                }
            }
            return true;
        } else if (mode == ProposedTrialPit.SelectMode) {
            if (down) {
                onSelectBox(pn);
            }
            return true;
        }


        return false;

    }

    /**
     * changing Mode with the given Mode Constant integer
     *
     * @param mode_constant
     * @return
     */
    public ProposedTrialPit onMode(int mode_constant) {
        mode = mode_constant;
        switch (mode_constant) {
            case ProposedTrialPit.AngleMode:
                updateBoundTime = System.currentTimeMillis();
                bound_update = false;
                if (le == null) {
                    le = new line_eq(new PointF(0, 0), new PointF(0, 0));
                }

                break;
            case ProposedTrialPit.SelectMode:
                display_S_boxes();

                break;
            case ProposedTrialPit.DrawRectMode:
                onlabeling = false;
                break;
            case ProposedTrialPit.EditAngleMode:
                final TPBox p = getFirstSelected();
                locked_angle_on_EditAngleMode = true;
                updateBoundTime = System.currentTimeMillis();
                bound_update = false;
                if (p != null) {
                    final Point[] pair = p.getPairCenters();
                    le.update_line_by_points(pair[0], pair[1]);
                    de.setPair(pair);
                } else {
                    Tool.trace(ctx, "please select one box");
                }
                break;

            case ProposedTrialPit.EditLabelPosition:

                break;
            case ProposedTrialPit.EditLabel:

                break;
            case ProposedTrialPit.TPmodeON:
                display_N_boxes();
                break;
            case ProposedTrialPit.TPmodeOFF:
                deSelectAll();
                break;
        }
        return this;
    }

    /**
     * select the first box
     *
     * @return
     */
    private TPBox getFirstSelected() {
        if (BoxContainer.size() > 0) {
            for (int i = 0; i < BoxContainer.size(); i++) {
                TPBox mbox = BoxContainer.get(i);
                if (mbox.isSelected()) {
                    return mbox;
                }
            }
        }
        return null;
    }

    /**
     * remove the selected box
     *
     * @return
     */
    public ProposedTrialPit removeSelected() {
        if (BoxContainer.size() > 0) {
            Iterator<TPBox> iter = BoxContainer.iterator();
            while (iter.hasNext()) {
                final TPBox mbox = iter.next();
                if (mbox.isSelected()) {
                    iter.remove();
                    if (mbox == current_working_box) {
                        current_working_box = null;
                    }
                }
            }
            current_selected_boxes = 0;
        }
        return this;
    }

    /**
     * Looking up the BoxContainer and set Selection to none for all of them
     *
     * @return
     */
    public ProposedTrialPit deSelectAll() {
        if (BoxContainer.size() > 0) {
            for (int i = 0; i < BoxContainer.size(); i++) {
                TPBox mbox = BoxContainer.get(i);
                if (mbox.isSelected()) {
                    mbox.setSelected(false);
                }
            }
        }
        return this;
    }

    /**
     * Interactive component to detect point from the rectangle
     *
     * @param touch
     * @return
     */

    private boolean onSelectBox(Point touch) {
        if (BoxContainer.size() > 0) {
            for (int i = 0; i < BoxContainer.size(); i++) {
                final TPBox mbox = BoxContainer.get(i);
                if (mbox.onSelected(touch)) {
                    mbox.toggleSelect();
                    current_working_box = mbox;
                }
            }
        }
        display_S_boxes();
        return true;
    }

    /**
     * The first frame of initialization for creating a new box
     * triggered from touch down
     *
     * @param touch
     * @return
     */
    private boolean onNewTP(Point touch) {
        current_working_box = new TPBox(main, ctx);
        current_working_box
                .setPaint(tp_line, fillBMPshader)
                .setBaseOnLine(le)
                .setTouchPointStart(new PointF(touch))
                .kickin();
        set_tp_control_view();
        return true;
    }

    private static class tp_action_mode_cb implements View.OnClickListener, NumberPickerDialogFragment.NumberPickerDialogHandler {
        private NumberPickerBuilder nb;
        private int control_tag;
        private TextView th, tw;

        tp_action_mode_cb(final FragmentManager mfragmentgr, TextView th, TextView tw) {
            nb = new NumberPickerBuilder()
                    .setFragmentManager(mfragmentgr)
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setDecimalVisibility(View.VISIBLE)
                    .addNumberPickerDialogHandler(this);
            this.th = th;
            this.tw = tw;
        }

        @Override
        public void onDialogNumberSet(int i, int i2, double v, boolean b, double v2) {
            switch (control_tag) {
                case R.id.tp_height_but:
                    if (current_working_box != null) {
                        current_working_box.setHeight(v2);
                        this.th.setText(String.format("%.2fm", Math.abs(v2)));
                    }
                    break;
                case R.id.tp_width_but:
                    if (current_working_box != null) {
                        current_working_box.setWidth(v2);
                        this.tw.setText(String.format("%.2fm", Math.abs(v2)));
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            control_tag = v.getId();
            switch (control_tag) {
                case R.id.tp_height_but:
                    nb.show();
                    break;
                case R.id.tp_width_but:
                    nb.show();
                    break;
                default:
                    break;
            }
        }

        public void set_status_dimension(double h, double w) {
            th.setText(String.format("%.2fm", EQPool.p2m((float) Math.abs(h))));
            tw.setText(String.format("%.2fm", EQPool.p2m((float) Math.abs(w))));
        }
    }

    public ProposedTrialPit setFragmentmanager(FragmentManager f) {
        this.mfragmentgr = f;
        return this;
    }

    private static tp_action_mode_cb tp_action_mode_cb_controller;

    private void set_tp_control_view() {
        View layout = LayoutInflater.from(ctx).inflate(R.layout.actionmode_tp_dimension, null);
        action_mode_bar_object.setCustomView(layout);
        Button bw = (Button) layout.findViewById(R.id.tp_width_but);
        Button bh = (Button) layout.findViewById(R.id.tp_height_but);
        TextView tw = (TextView) layout.findViewById(R.id.tp_width_view);
        TextView th = (TextView) layout.findViewById(R.id.tp_height_view);
        tp_action_mode_cb_controller = new tp_action_mode_cb(mfragmentgr, th, tw);
        bw.setOnClickListener(tp_action_mode_cb_controller);
        bh.setOnClickListener(tp_action_mode_cb_controller);
    }

    private boolean onConstructingTP(Point touch) {
        // Current_working_box.setTouchPointMove(new PointF(touch));
        current_working_box
                .setTouchPointMove(new PointF(touch))
                .kickin();
        tp_action_mode_cb_controller.set_status_dimension(current_working_box.getHeight(), current_working_box.getWidth());
        return true;
    }

    private boolean onConfirmTP() {
        // When touch is up
        current_working_box.drawDone(true);
        this.onMode(ProposedTrialPit.Edit_tp_dimension);
        action_mode_switch_view(8);
        return true;
    }

    private void startEditingLabel() {
        // shows the label input
        onlabeling = true;
        this.onMode(ProposedTrialPit.EditLabel);
        action_mode_switch_view(5);
        display_N_boxes();
    }

    /**
     * this is the old edit text box interactions
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (onlabeling) {
            String e = medittext.getText().toString();
            if (e.equalsIgnoreCase("")) {
                Tool.trace(ctx, "Please enter the label of this Trial Pit");
            } else {
                medittext.clearFocus();
                medittext.setText("");
                current_working_box.setLabel(e);
                BoxContainer.add(current_working_box);
                current_working_box = null;
                v_input_box.setVisibility(View.GONE);

                Tool.hideKeyBoard(ctx, medittext);


                onlabeling = false;
                /**
                 * this is the callback for setting mode num to the action mode to invalidate
                 */
                action_mode_switch_view(0);
                display_N_boxes();
            }
        }
    }

    /**
     * this is the callback for setting mode num to the action mode to invalidate
     *
     * @param modeSelection
     */
    private void action_mode_switch_view(int modeSelection) {
        action_mode_bar_object.setTag(new TagPointer(modeSelection));
        action_mode_bar_object.invalidate();
    }

    /**
     * call back from the X button
     */
    public void reset_label_field() {
        medittext.setText("");
        Tool.hideKeyBoard(ctx, medittext);
    }

    public ProposedTrialPit setInputLabelField(EditText e) {
        medittext = e;
        return this;
    }

    public void onChangeConfig(Configuration c) {
        use_orientation = c;
        bound_update = false;
        updateBoundTime = System.currentTimeMillis();
    }


    public Boolean onConfirmLabelingTP() {
        String e = medittext.getText().toString();
        if (e.isEmpty()) {
            Tool.trace(ctx, "Please enter the label of this Trial Pit");
            return false;
        } else {
            current_working_box.setLabel(e);
            /**
             * hide input keyboard
             */
            Tool.hideKeyBoard(ctx, medittext);
            display_N_boxes();
            Tool.trace(ctx, "Label saved");
            return true;
        }
    }

    public void onFinalNewTp() {
        current_working_box.setEditingLabel(false);
        BoxContainer.add(current_working_box);
        onMode(TPmodeON);
        //current_working_box = null;
        Tool.trace(ctx, "A new TP is added");
    }

    public void onFinalizeTP() {
        //final int t = BoxContainer.indexOf(current_working_box);
        //BoxContainer.set(t, current_working_box);
        current_working_box.setEditingLabel(false);
        onMode(TPmodeON);
        deSelectAll();
        //current_working_box = null;
        Tool.trace(ctx, "This TP is update");
    }

    public ArrayList<TPBox> getList() {
        return BoxContainer;
    }

    //display the constructed boxes
    private void display_N_boxes() {
        int g = 0;
        if (BoxContainer != null) {
            g = BoxContainer.size();
        }
        // action_mode_bar_object.setTitle("There are " + g + " TPs");
        if (tp_cb != null) {
            tp_cb.PT_trigger_setTitle("There are " + g + " TPs");
        }
    }

    //display the selected boxes
    private void display_S_boxes() {
        int g = 0;
        if (BoxContainer != null) {
            if (BoxContainer.size() > 0) {
                for (int i = 0; i < BoxContainer.size(); i++) {
                    TPBox mbox = BoxContainer.get(i);
                    if (mbox.isSelected()) g++;

                }
            }
        }
        if (tp_cb != null) {
            tp_cb.PT_trigger_setTitle(g + " TPs are selected.");
        }
        // action_mode_bar_object.setTitle(g + " TPs are selected.");
        current_selected_boxes = g;
    }

    public boolean isSingleBox() {
        return current_selected_boxes == 1;
    }

    public void clearThis() {
        BoxContainer.clear();
    }

    private void rendering_boxes() {
        if (BoxContainer.size() > 0) {
            for (TPBox b : BoxContainer) {
                b.renderPath(main);
            }
        }
    }

    private void rendering_boxes_editing() {
        /**
         * when the lines are on editing
         */
        if (mode == ProposedTrialPit.EditAngleMode || mode == ProposedTrialPit.AngleMode || mode == ProposedTrialPit.DrawRectMode) {
            main.drawPath(le.getPath(), p_default);
            de.rendering(main);
            if (mode == ProposedTrialPit.EditAngleMode || mode == ProposedTrialPit.AngleMode) {
                rendering_boxes();
            }
        } else {
            rendering_boxes();
        }
        final int current_working_box_i = current_working_box == null ? -1 : BoxContainer.indexOf(current_working_box);
        if (BoxContainer.size() > 0) {
            for (int i = 0; i < BoxContainer.size(); i++) {
                // }
                // _text_at_point.postConcat(m_matrix);
                // main.save();
                //  main.setMatrix(global_matrix);
                // main.drawPath(le.getPath(), p_default);
                //  de.rendering(main);
                // main.restore();
                if (mode == EditLabelPosition || mode == DrawRectMode || mode == EditLabel) {
                    if (current_working_box_i > -1 && current_working_box_i == i) {
                        //do not draw this
                    } else {
                        final TPBox mbox = BoxContainer.get(i);
                        mbox.renderPath(main);
                    }
                }
            }
        }
        if (mode == DrawRectMode || mode == EditLabelPosition || mode == EditLabel || mode == Edit_tp_dimension) {
            if (current_working_box != null)
                current_working_box.renderPath(main);
        }
        if (mode != TPmodeOFF) {
            /**
             * this is for debug only
             *
             *   main.drawCircle(center_checking_point.x, center_checking_point.y, 30, p_default);
             */
        }
    }

    @Override
    protected void rendering() {
        if (mode > ProposedTrialPit.TPmodeON && le != null) {
            try {
                rendering_boxes_editing();
            } catch (Exception e) {
                Log.d(TAG, "ERROR1: " + e.toString());
            }
        } else {
            try {
                rendering_boxes();
            } catch (Exception e) {
                Log.d(TAG, "ERROR2: " + e.toString());
            }
        }
        // main.drawRect(mActionRect, p_default);
    }

    private void calibrate_center(int a, int b) {
        if (use_orientation == null) {
            /**
             * we use the default settings as the portrait
             */
            if (device_default_center_portrait == null && device_default_center_landscape == null) {
                device_default_center_portrait = new Point(a / 2, b / 2);
                device_default_center_landscape = new Point(b / 2, a / 2);
            }
        } else {
            if (device_default_center_portrait == null && use_orientation.orientation == Configuration.ORIENTATION_PORTRAIT) {
                device_default_center_portrait = new Point(a / 2, b / 2);
            }
            if (device_default_center_landscape == null && use_orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                device_default_center_landscape = new Point(a / 2, b / 2);
            }
        }
    }

    public ProposedTrialPit updateCanvas(Canvas cv, Matrix mx) {
        long curr = System.currentTimeMillis();
        boolean passFlag = curr > updateBoundTime + 2000 && !bound_update;
        if (mode != ProposedTrialPit.TPmodeOFF && cv != null && passFlag) {
            bound_update = true;
            final Rect rc = cv.getClipBounds();
            int a = rc.height(); //screen height
            int b = rc.width();
            Point center = new Point(rc.centerX(), rc.centerY());
            if (a * b > 153600) {
                //screen width
                //float c = rc.centerX(), d = rc.centerY();
                //  calibrate_center(a, b);
                float min_Y = 0, max_Y = 0, max_X = 0, min_X = 0;
                center_checking_point.set(renderingp.update_screen_pointer(center));
                min_Y = center_checking_point.y - a;
                max_Y = center_checking_point.y + a;
                max_X = center_checking_point.x + b;
                min_X = center_checking_point.x - b;
                le.setBound(min_X, max_X, min_Y, max_Y);
            }
        }
        super.updateCanvas(cv, mx);
        return this;
    }

    public class TagPointer {
        private int action_mode_selection;

        public TagPointer(int b) {
            action_mode_selection = b;
        }

        public int getModeNum() {
            return action_mode_selection;
        }
    }
}
