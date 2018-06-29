package com.hkm.oc.panel.corepanel.elements;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hkm.oc.panel.corepanel.handler.RenMatrixProcess;
import com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox;
import com.hkm.oc.panel.corepanel.elements.shapes.Element;
import com.hkm.oc.panel.corepanel.elements.mathmodels.DotPair;
import com.hkm.oc.panel.corepanel.elements.mathmodels.line_eq;
import com.hkm.oc.panel.corepanel.handler.PTModeCallback;
import com.hkm.oc.R;

import java.util.ArrayList;

/**
 * Created by Hesk on 25/4/2014.
 */
public class BaseTP extends Element {
    public static enum action {
        TPV2_confirm_center_point, // step 1
        TPV2_drag_distance, // step 2
        TPV2_Edit_rectangle_dimension, // step 3
        Edit_Label_text, // step 1
        Edit_Label_position, // step 2
        Edit_angle, //
        TPV1_drag_angle, // step 1
        TPV1_drag_rectangle, // step 2
        Select_rectangle //
    }

    final Paint p_default = new Paint(), p_paint_dot = new Paint(), tp_line = new Paint();
    final PointF center_checking_point = new PointF();
    protected ArrayList<TPBox> BoxContainer = new ArrayList<TPBox>();
    protected int mode;
    protected line_eq le;
    protected DotPair de;
    protected action maction;
    protected TPBox current_working_box;
    protected ActionMode action_mode_bar_object;
    protected Button label_button;
    private EditText medittext;
    protected View v_input_box;
    protected boolean locked_angle_on_EditAngleMode = false;
    protected BitmapShader fillBMPshader;
    protected RenMatrixProcess renderingp;
    protected Configuration use_orientation = null;
    protected Point device_default_center_landscape = null, device_default_center_portrait = null;
    protected boolean bound_update = false;
    protected long updateBoundTime = 0;
    protected int current_selected_boxes;
    protected int selected_box_id;
    protected PTModeCallback tp_cb;
    public final String TAG = "TP - ProposedTrialPit - CLASS";
    protected boolean TPmode = false;

    public BaseTP(Canvas mainCanvas, Context ctx) {
        super(mainCanvas, ctx);
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

        //Initialize the bitmap object by loading an image from the resources folder
        Bitmap fillBMP = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.filledstrip);
        //Initialize the BitmapShader with the Bitmap object and set the texture tile mode
        fillBMPshader = new BitmapShader(fillBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        clearThis();
    }

    public class TagPointer {
        private int action_mode_selection;
        private action current_action_point;

        public TagPointer(int b) {
            action_mode_selection = b;
        }

        public TagPointer(action a) {
            current_action_point = a;
        }

        public action getCurrenAction() {
            return current_action_point;
        }

        public int getModeNum() {
            return action_mode_selection;
        }

        public boolean isAct(action a) {
            return current_action_point == a;
        }
    }

    protected void clearThis() {
        BoxContainer.clear();
    }

    /**
     * this is the callback for setting mode num to the action mode to invalidate
     *
     * @param modeSelection
     */
    protected void setActionModeSelection(int modeSelection) {
        action_mode_bar_object.setTag(new TagPointer(modeSelection));
        action_mode_bar_object.invalidate();
    }

    //display the constructed boxes
    protected void display_N_boxes() {
        int g = 0;
        if (BoxContainer != null) {
            g = BoxContainer.size();
        }
        if (tp_cb != null) {
            String title = String.format("There are %d TPs", g);
            tp_cb.PT_trigger_setTitle(title);
        }
    }

    protected void render_line_2dots() {
        //draw the straight red line
        main.drawPath(le.getPath(), p_default);
        //draw 2 dots
        de.rendering(main);
    }

    protected void render_all_boxes() {
        //  if (BoxContainer.size() > 0) {
        for (final TPBox b : BoxContainer) {
            b.renderPath(main);
        }
        // }
    }

    protected void render_undraw_boxes() {
        for (final TPBox b : BoxContainer) {
            if (b != current_working_box) {
                b.renderPath(main);
            }
        }
    }

    protected void render_current_box() {
        if (current_working_box != null)
            current_working_box.renderPath(main);
    }

    protected boolean isSingleBox() {
        return current_selected_boxes == 1;
    }

    protected boolean isAct(action m) {
        return maction == m;
    }

    /**
     * Looking up the BoxContainer and set Selection to none for all of them
     *
     * @return
     */
    protected BaseTP deSelectAll() {
        for (final TPBox mbox : BoxContainer) {
            if (mbox.isSelected()) {
                mbox.setSelected(false);
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

    protected boolean onSelectBox(Point touch) {
        for (final TPBox mBox : BoxContainer) {
            if (mBox.onSelected(touch)) {
                mBox.toggleSelect();
                current_working_box = mBox;
            }
        }
        display_S_boxes();
        return true;
    }

    protected TPBox getFirstSelected() {
        for (final TPBox mBox : BoxContainer) {
            if (mBox.isSelected()) {
                return mBox;
            }
        }
        return null;
    }

    protected void TurnOnTPMode(boolean n) {
        TPmode = n;
        if (n)
            display_N_boxes();
        else
            deSelectAll();
    }

    public void setTPListener(PTModeCallback f) {
        tp_cb = f;
    }

    public BaseTP setRenderingProcess(RenMatrixProcess rm) {
        renderingp = rm;
        return this;
    }

    public BaseTP setActionModeObject(ActionMode mmode) {
        action_mode_bar_object = mmode;
        return this;
    }

    //display the selected boxes
    protected void display_S_boxes() {
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

    /**
     * changing Mode with the given Mode Constant integer
     *
     * @param onaction
     * @return
     */
    protected BaseTP onMode(action onaction) {
        maction = onaction;
        return this;
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
                render_all_boxes();
            } catch (Exception e) {
                Log.d(TAG, "ERROR2: " + e.toString());
            }
        }
        // main.drawRect(mActionRect, p_default);
    }

    /**
     * when the lines are on editing
     */
    private void rendering_boxes_editing() {

        if (isAct(action.TPV1_drag_angle) || isAct(action.TPV1_drag_rectangle) || isAct(action.Edit_angle)) {
            render_line_2dots();
            if (isAct(action.TPV1_drag_angle)) {
                //rendering the TP boxes
                render_all_boxes();
            }
        }

        if (isAct(action.Edit_Label_position) || isAct(action.Edit_Label_text) ||
                isAct(action.TPV1_drag_rectangle) || isAct(action.TPV2_confirm_center_point) ||
                isAct(action.TPV2_drag_distance) || isAct(action.TPV2_Edit_rectangle_dimension))
            render_undraw_boxes();

        if (isAct(action.TPV1_drag_rectangle) || isAct(action.Edit_Label_position) || isAct(action.Edit_Label_text))
            render_current_box();

        if (!TPmode) {
            /**
             * this is for debug only
             *
             *   main.drawCircle(center_checking_point.x, center_checking_point.y, 30, p_default);
             */
        } else {

        }
    }
}
