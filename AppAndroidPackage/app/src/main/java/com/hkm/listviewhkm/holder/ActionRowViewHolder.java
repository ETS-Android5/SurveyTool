package com.hkm.listviewhkm.holder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.hkm.U.Content;
import com.hkm.datamodel.Label;
import com.hkm.datamodel.RouteNode;
import com.hkm.listviewhkm.RNList.RNAdapter;
import com.hkm.oc.R;
import com.hkm.U.Tool;

import java.util.ArrayList;

import static com.hkm.listviewhkm.model.C.DEFAULT_NOTHING;
import static com.hkm.listviewhkm.model.C.ROW_NOT_COMPLETE_TEXT;
import static com.hkm.listviewhkm.model.C.breaktype;
import static com.hkm.listviewhkm.model.C.indicator;
import static com.hkm.listviewhkm.model.C.indicator.CHANGED;
import static com.hkm.listviewhkm.model.C.indicator.FINAL;
import static com.hkm.listviewhkm.model.C.indicator.INVALID;
import static com.hkm.listviewhkm.model.C.status;
import static com.hkm.listviewhkm.model.C.tag;
import static com.hkm.listviewhkm.model.C.tag.CANCEL;
import static com.hkm.listviewhkm.model.C.tag.C_EDIT;
import static com.hkm.listviewhkm.model.C.tag.C_INSERT;
import static com.hkm.listviewhkm.model.C.tag.C_LINECUT;
import static com.hkm.listviewhkm.model.C.tag.C_OK;
import static com.hkm.listviewhkm.model.C.tag.C_REMOVE;
import static com.hkm.listviewhkm.model.C.tag.EDIT;
import static com.hkm.listviewhkm.model.C.tag.LABEL_CONTROL;
import static com.hkm.listviewhkm.model.C.tag.ROW_NUMBER_SET_A;
import static com.hkm.listviewhkm.model.C.tag.ROW_NUMBER_SET_B;
import static com.hkm.listviewhkm.model.C.tag.ROW_NUMBER_SET_C;
import static com.hkm.listviewhkm.model.C.tag.ROW_NUMBER_SET_D;
import static com.hkm.listviewhkm.model.C.tag.YES;

/**
 * Developed By Hesk Working on the List Adapter of the Plot and Chart System 2013
 * All Rights Reserved.
 * Created by hesk on 7/25/13.
 */
public class ActionRowViewHolder extends item {
    /**
     * this is the width from the left edge
     */
    public static final float
            OFFSET_CONFIRM_PANEL = 450f,
            OFFSET_OPTION_PANEL = 500f,
            ALPHA_DISABLE = 0.8f,
            ALPHA_ENABLE = 0f;
    public static final String TAG = "ActionRowViewHolder HOLDER";

    protected TextView reference_label, distance_a, distance_b, depth_from_ground, cable_width;
    /**
     * l_control_editing_field: input confirm panel
     * l_control_modify: contextual buttons panel
     * l_bottom: map to the id - bottomlayer
     * line_cute_divier: map to the id - line_cue
     * edit_button: map to the id - modi
     * <p/>
     * isOptionsShown: tells if the input confirm panel is shown or not
     * new_rezzed_row: tells if the new row has been created or not
     */
    protected View l_bottom, l_control_editing_field, l_control_modify, l_color, line_cute_divier;
    protected ImageView tn;
    protected Paint mPaint;
    protected float offset_cp, offset_op;
    protected boolean isOpened, isOptionsShown, new_rezzed_row, hasResult, reference_label_completed;
    protected ImageButton insert_below_c, cancel_control, yes, no, edit_button, remove_row_control, edit_control, line_break_control;
    protected Button label_control_button, layerba, layerbb, layerbc, layerbd;
    protected status currentStatus;
    protected breaktype mMarkType;
    protected String number_row_a, number_row_b, number_row_c, number_row_d;
    protected RouteNode data_row;
    protected tag numberic_dialog_set_position;
    protected indicator indic;

    public ActionRowViewHolder(int p, Context c, RNAdapter ad) {
        super(p, c, ad);
        isOpened = false;
        isOptionsShown = true;
        currentStatus = status.INCOMPLETE;
        mMarkType = breaktype.CONTINUE;
        orientation = -1;
        totalWidth = 0;
        numberic_dialog_set_position = tag.TAG_NONE;
        number_row_a = "";
        number_row_b = "";
        number_row_c = "";
        FragmentActivity fm = (FragmentActivity) c;
        mNumberPickerBuilder = new NumberPickerBuilder()
                .setFragmentManager(fm.getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setReference(p)
                .setDecimalVisibility(View.VISIBLE)
                .addNumberPickerDialogHandler(this);
        // .setStyleResId(R.style.BetterPickersDialogFragment);
    }

    private void enable_buttons_normal(boolean on, boolean editbutton) {
        label_control_button.setEnabled(on);
        layerba.setEnabled(on);
        layerbb.setEnabled(on);
        layerbc.setEnabled(on);
        layerbd.setEnabled(on);
        edit_button.setEnabled(editbutton);
        isOptionsShown = !editbutton;
    }

    private void enable_buttons_layer_init() {
        label_control_button.setEnabled(true);
        layerba.setEnabled(false);
        layerbb.setEnabled(false);
        layerbc.setEnabled(false);
        layerbd.setEnabled(false);
        edit_button.setEnabled(false);
    }

    @Override
    public ActionRowViewHolder getCacheView(View rv) {
        l_control_modify = (View) rv.getTag(R.id.toplayer);
        l_control_editing_field = (View) rv.getTag(R.id.layer_editing_fields);
        l_bottom = (View) rv.getTag(R.id.bottomlayer);
        l_color = (View) rv.getTag(R.id.coloring_layer);
        line_cute_divier = (View) rv.getTag(R.id.line_cue);
        distance_a = (TextView) rv.getTag(R.id.distance_from_a);
        distance_b = (TextView) rv.getTag(R.id.distance_from_b);
        depth_from_ground = (TextView) rv.getTag(R.id.depth);
        reference_label = (TextView) rv.getTag(R.id.point_label);
        cable_width = (TextView) rv.getTag(R.id.cable_radius);
        tn = (ImageView) rv.getTag(R.id.indication);
        edit_button = (ImageButton) rv.getTag(R.id.modi);
        no = (ImageButton) rv.getTag(R.id.b_no);
        yes = (ImageButton) rv.getTag(R.id.b_yes);
        edit_control = (ImageButton) rv.getTag(R.id.iv_edit);
        cancel_control = (ImageButton) rv.getTag(R.id.iv_ok_control);
        line_break_control = (ImageButton) rv.getTag(R.id.iv_linecut);
        remove_row_control = (ImageButton) rv.getTag(R.id.iv_remove_row_control);
        insert_below_c = (ImageButton) rv.getTag(R.id.iv_insert);
        label_control_button = (Button) rv.getTag(R.id.label_control_button_l);
        layerba = (Button) rv.getTag(R.id.label_control_button_a);
        layerbb = (Button) rv.getTag(R.id.label_control_button_b);
        layerbc = (Button) rv.getTag(R.id.label_control_button_c);
        layerbd = (Button) rv.getTag(R.id.label_control_button_d);
        return this;
    }

    @Override
    public ActionRowViewHolder addConvertViewToElements(View rv) {

        l_control_modify = (View) rv.findViewById(R.id.toplayer);
        rv.setTag(R.id.toplayer, l_control_modify);
        l_control_editing_field = (View) rv.findViewById(R.id.layer_editing_fields);
        rv.setTag(R.id.layer_editing_fields, l_control_editing_field);
        l_bottom = (View) rv.findViewById(R.id.bottomlayer);
        rv.setTag(R.id.bottomlayer, l_bottom);
        l_color = (View) rv.findViewById(R.id.coloring_layer);
        rv.setTag(R.id.coloring_layer, l_color);
        line_cute_divier = (View) rv.findViewById(R.id.line_cue);
        rv.setTag(R.id.line_cue, line_cute_divier);
        distance_a = (TextView) rv.findViewById(R.id.distance_from_a);
        rv.setTag(R.id.distance_from_a, distance_a);
        distance_b = (TextView) rv.findViewById(R.id.distance_from_b);
        rv.setTag(R.id.distance_from_b, distance_b);
        depth_from_ground = (TextView) rv.findViewById(R.id.depth);
        rv.setTag(R.id.depth, depth_from_ground);
        reference_label = (TextView) rv.findViewById(R.id.point_label);
        rv.setTag(R.id.point_label, reference_label);
        cable_width = (TextView) rv.findViewById(R.id.cable_radius);
        rv.setTag(R.id.cable_radius, cable_width);


        tn = (ImageView) rv.findViewById(R.id.indication);
        rv.setTag(R.id.indication, tn);

        new_rezzed_row = true;

        edit_button = (ImageButton) rv.findViewById(R.id.modi);
        rv.setTag(R.id.modi, edit_button);
        no = (ImageButton) rv.findViewById(R.id.b_no);
        rv.setTag(R.id.b_no, no);
        yes = (ImageButton) rv.findViewById(R.id.b_yes);
        rv.setTag(R.id.b_yes, yes);
        edit_control = (ImageButton) rv.findViewById(R.id.iv_edit);
        rv.setTag(R.id.iv_edit, edit_control);
        cancel_control = (ImageButton) rv.findViewById(R.id.iv_ok_control);
        rv.setTag(R.id.iv_ok_control, cancel_control);
        line_break_control = (ImageButton) rv.findViewById(R.id.iv_linecut);
        rv.setTag(R.id.iv_linecut, line_break_control);
        remove_row_control = (ImageButton) rv.findViewById(R.id.iv_remove_row_control);
        rv.setTag(R.id.iv_remove_row_control, remove_row_control);
        insert_below_c = (ImageButton) rv.findViewById(R.id.iv_insert);
        rv.setTag(R.id.iv_insert, insert_below_c);

        label_control_button = (Button) rv.findViewById(R.id.label_control_button_l);
        rv.setTag(R.id.label_control_button_l, label_control_button);
        layerba = (Button) rv.findViewById(R.id.label_control_button_a);
        rv.setTag(R.id.label_control_button_a, layerba);
        layerbb = (Button) rv.findViewById(R.id.label_control_button_b);
        rv.setTag(R.id.label_control_button_b, layerbb);
        layerbc = (Button) rv.findViewById(R.id.label_control_button_c);
        rv.setTag(R.id.label_control_button_c, layerbc);
        layerbd = (Button) rv.findViewById(R.id.label_control_button_d);
        rv.setTag(R.id.label_control_button_d, layerbd);

        return this;
    }

    public void startEngine() {

        layerba.setTag(ROW_NUMBER_SET_A);
        layerba.setOnClickListener(this);
        layerbb.setTag(ROW_NUMBER_SET_B);
        layerbb.setOnClickListener(this);
        layerbc.setTag(ROW_NUMBER_SET_C);
        layerbc.setOnClickListener(this);
        layerbd.setTag(ROW_NUMBER_SET_D);
        layerbd.setOnClickListener(this);

        label_control_button.setTag(LABEL_CONTROL);
        label_control_button.setOnClickListener(this);

        if (remove_row_control != null) {
            remove_row_control.setOnClickListener(this);
            remove_row_control.setTag(C_REMOVE);
        }

        if (insert_below_c != null) {
            insert_below_c.setOnClickListener(this);
            insert_below_c.setTag(C_INSERT);
        }
        if (edit_control != null) {
            edit_control.setOnClickListener(this);
            edit_control.setTag(C_EDIT);
        }
        if (cancel_control != null) {
            cancel_control.setOnClickListener(this);
            cancel_control.setTag(C_OK);
        }
        if (line_break_control != null) {
            line_break_control.setOnClickListener(this);
            line_break_control.setTag(C_LINECUT);
        }
        if (edit_button != null) {
            edit_button.setOnClickListener(this);
            edit_button.setTag(EDIT);
        }
        if (yes != null) {
            yes.setOnClickListener(this);
            yes.setTag(YES);
        }
        if (no != null) {
            no.setOnClickListener(this);
            no.setTag(CANCEL);
        }

    }

    public void bind_data2display(final RouteNode rn) {
      /*  if (data_row != null) data_row = mroutenode;
        final RouteNode rn = data_row;*/

        final Label lb = rn.get_label();

        if (lb == null) {
            reference_label.setText(ROW_NOT_COMPLETE_TEXT);
            reference_label_completed = false;
            enable_buttons_layer_init();
        } else {
            String t = lb.get_display_big_button_label();
            reference_label.setText(t);
            reference_label.setTextColor(lb.get_paint_color());
            reference_label_completed = true;
        }

        if (rn == null) {
            number_row_a = number_row_b = number_row_c = "";
            distance_a.setText(DEFAULT_NOTHING);
            distance_b.setText(DEFAULT_NOTHING);
            depth_from_ground.setText(DEFAULT_NOTHING);
            cable_width.setText(DEFAULT_NOTHING);
            reference_label_completed = false;
            line_cute_divier.setVisibility(View.INVISIBLE);
        } else {
            number_row_a = String.format("%.2f", rn.get_distance_a());
            number_row_b = String.format("%.2f", rn.get_distance_b());
            number_row_c = String.format("%.2f", rn.get_depth());
            number_row_d = String.format("%.1f", rn.get_cable_radius());
            distance_a.setText(number_row_a);
            distance_b.setText(number_row_b);
            depth_from_ground.setText(number_row_c);
            cable_width.setText(number_row_d);
            mMarkType = rn.get_cut() ? breaktype.BREAK_POINT : breaktype.CONTINUE;

            if (indic == null)
                indic = rn.getIndicate();

            if (rn.highLighted())
                l_bottom.setBackground(getImg(R.drawable.rowset_yellow_strip));
            else
                l_bottom.setBackground(null);

            indicatorInvaliate();
            line_cue_invalidate();
        }

        data_row = rn;
        //Log.d(TAG, number_row_a);
    }

    private Drawable getImg(int ResId) {
        return ctx.getResources().getDrawable(ResId);
    }

    private void set_w(int w) {
        totalWidth = w;
        offset_cp = Math.min((float) (w * 0.3), OFFSET_CONFIRM_PANEL);
        offset_op = Math.max((float) (w * 0.9), OFFSET_OPTION_PANEL);
    }

    public void set_interactive_screen_width(int w) {
        if (totalWidth == 0) {
            set_w(w);
        } else if (totalWidth != w) {
            set_w(w);
            final boolean a = currentStatus == status.INCOMPLETE;
            final boolean b = currentStatus == status.COMPLETE;
            final boolean c = currentStatus == status.MODIFY;
            if (a || b) {
                l_control_modify.setTranslationX(w);
            }
            if (a || c) {
                l_control_editing_field.setTranslationX(w);
            }
        }
    }


    private void line_cue_invalidate() {
        if (mMarkType == breaktype.CONTINUE) {
            line_cute_divier.setVisibility(View.INVISIBLE);
        } else {
            line_cute_divier.setVisibility(View.VISIBLE);
        }
    }

    public void setMarkerBreak(boolean x) {
        if (x) {
            mMarkType = breaktype.BREAK_POINT;
            line_cute_divier.setVisibility(View.VISIBLE);
        } else {
            mMarkType = breaktype.CONTINUE;
            line_cute_divier.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * the problem is in here
     *
     * @return
     */
    public boolean analyze_distances_possible_and_update() {
        if (!number_row_a.isEmpty() && !number_row_b.isEmpty()) {
            if (adapter_pointer.analyze_distances(number_row_a, number_row_b, pos)) {
                // final boolean internaluse = data_row.getIndicate() == C.indicator.FINAL;
                //  set_result_indicator(true);
                indic = data_row.getIndicate();
            } else {
                //  set_result_indicator(false);
                indic = data_row.getIndicate();
            }
            indicatorInvaliate();
            return true;
        } else {
            Tool.trace(ctx, "please complete both disance A and distance B");
            return true;
        }

    }

    /**
     * set indication light to be green or red
     * set the color of the indicator on the layout.
     *
     * @param h
     */
    public void set_result_indicator(boolean h) {
        hasResult = h;
        if (h) {
            final ArrayList<Integer> j = Content.update_existing_index;
            final int result = j.indexOf(pos);
            if (result > -1) {
                indic = CHANGED;
            } else {
                indic = FINAL;
            }
        } else {
            indic = INVALID;
        }
        data_row.setIntdicate(indic);
        indicatorInvaliate();
    }

    public void indicatorInvaliate() {
        Log.d(TAG, indic.toString());
        switch (indic) {
            case NA:
                tn.setBackgroundColor(ctx.getResources().getColor(R.color.darker_gray));
                break;
            case CHANGED:
                tn.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_enabled_result_positive_changing));
                break;
            case INVALID:
                tn.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_enabled_result_negative));
                break;
            case FINAL:
                tn.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_enabled_result_positive));
                break;
        }
    }

    private boolean checkFields() {
        try {
            distance_a.getScaleX();
        } catch (Exception e) {
            Log.d("TAG prepare_current_db_row", "field distance_a is not defined");
            return false;
        }
        try {
            distance_b.getScaleX();
        } catch (Exception e) {
            Log.d("TAG prepare_current_db_row", "field distance_b is not defined");
            return false;
        }
        try {
            depth_from_ground.getScaleX();
        } catch (Exception e) {
            Log.d("TAG prepare_current_db_row", "field depth_from_ground is not defined");
            return false;
        }
        try {
            reference_label.getScaleX();
        } catch (Exception e) {
            Log.d("TAG prepare_current_db_row", "field reference_label is not defined");
            return false;
        }
        try {
            //reference_type.getScaleX();
        } catch (Exception e) {
            Log.d("TAG prepare_current_db_row", "field reference_type is not defined");
            return false;
        }
        return true;
    }


    public boolean action_submit_data() {
        if (isCompleteAllFields()) {
            if (new_rezzed_row) {
                new_rezzed_row = false;
                Log.d("TAG ACTION SUBMIT", "NEW REZZZ : TRUE");
                removenobutton();
            } else {
                Log.d("TAG ACTION SUBMIT", "NEW REZZZ : FALSE");
            }
            setStatus(status.COMPLETE);
            return true;
        } else {
            if (!new_rezzed_row) {
                setStatus(status.INCOMPLETE);// if that is not incomplete then set it to incomplete
            }
            Toast.makeText(ctx, R.string.warning01, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    protected boolean isCompleteAllFields() {
        final boolean a = distance_a.getText().toString().isEmpty();
        final boolean b = distance_b.getText().toString().isEmpty();
        final boolean c = depth_from_ground.getText().toString().isEmpty();
        // final boolean d = reference_label_completed;
        // final boolean a = Tool.isEmpty(distance_a);
        if (!a && !b && !c && reference_label_completed) {
            return true;
        } else
            return false;
    }


    public void removenobutton() {
        if (no.getVisibility() == View.VISIBLE) {
            no.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setStatusNoAnimation() {
        try {
            getLayerProcessNoAnimation();
            line_cue_invalidate();
            indicatorInvaliate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ActionRowViewHolder setStatus(status m) {
        try {
            switch (m) {
                case INITIAL:
                    currentStatus = status.INCOMPLETE;
                    enable_buttons_layer_init();
                    l_color.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_disabled));
                    l_color.setAlpha(ALPHA_ENABLE);
                    l_control_modify.setTranslationX(totalWidth);
                    l_control_editing_field.setTranslationX(totalWidth);
                    l_control_editing_field.animate().translationX(totalWidth - offset_cp);
                    return this;
                default:
                    currentStatus = m;
                    Log.d("TAG STRING CONTENT", "getLayerProcess");
                    getLayerProcess();
                    return this;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return this;
        }
    }

    @Override
    @SuppressLint("NewApi")
    public void getLayerProcessNoAnimation() throws Exception {
        if (currentStatus == status.COMPLETE) {
            l_control_modify.setTranslationX(totalWidth);
            l_control_editing_field.setTranslationX(totalWidth);
            l_color.setAlpha(ALPHA_ENABLE);
            enable_buttons_normal(true, true);
        } else if (currentStatus == status.INCOMPLETE) {
            //if the text field has been focused or if all the text fields are still empty
            l_control_modify.setTranslationX(totalWidth);
            l_control_editing_field.setTranslationX(totalWidth - offset_cp);
            l_color.setAlpha(ALPHA_ENABLE);
            //label_control_button.setEnabled(true);
            set_result_indicator(false);
            enable_buttons_normal(true, false);
        } else if (currentStatus == status.MODIFY) {
            l_control_modify.setTranslationX(totalWidth - offset_op);
            l_control_editing_field.setTranslationX(totalWidth);
            l_color.setAlpha(ALPHA_DISABLE);
            enable_buttons_normal(false, false);
        }
        if (isCompleteAllFields()) {
            no.setVisibility(View.GONE);
        }
    }

    @Override
    @SuppressLint("NewApi")
    public void getLayerProcess() throws Exception {
        if (currentStatus == status.COMPLETE) {
            l_control_modify.animate().translationX(totalWidth);
            l_control_editing_field.animate().translationX(totalWidth);
            l_color.animate().alpha(ALPHA_ENABLE);
            enable_buttons_normal(true, true);
        } else if (currentStatus == status.INCOMPLETE) {
            //if the text field has been focused or if all the text fields are still empty
            l_control_modify.animate().translationX(totalWidth);
            l_control_editing_field.animate().translationX(totalWidth - offset_cp);
            l_color.animate().alpha(ALPHA_ENABLE);
            //label_control_button.setEnabled(true);
            set_result_indicator(false);
            enable_buttons_normal(true, false);
        } else if (currentStatus == status.MODIFY) {
            l_control_modify.animate().translationX(totalWidth - offset_op);
            l_control_editing_field.animate().translationX(totalWidth);
            l_color.animate().alpha(ALPHA_DISABLE);
            enable_buttons_normal(false, false);
        }
        if (isCompleteAllFields()) {
            no.setVisibility(View.GONE);
        }
    }


    @Override
    protected void update_stack_list(tag n, double newNum) {
        switch (n) {
            case ROW_NUMBER_SET_A:
                data_row.set_distance_a((float) newNum);
                break;
            case ROW_NUMBER_SET_B:
                data_row.set_distance_b((float) newNum);
                break;
            case ROW_NUMBER_SET_C:
                data_row.set_depth((float) newNum);
                break;
            case ROW_NUMBER_SET_D:
                data_row.set_cable_r((float) newNum);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        final boolean a = actionId == EditorInfo.IME_ACTION_SEARCH;
        final boolean b = actionId == EditorInfo.IME_ACTION_DONE;
        final boolean c = actionId == EditorInfo.IME_ACTION_GO;
        // final int id = v.getId();
        // String tag = "AndroidEnterKeyActivity";
        if (a || b || c) {
            if (v == distance_a) {
                action_submit_data();
                return false;
            } else if (v == distance_b) {
                action_submit_data();
                return false;
            } else if (v == depth_from_ground) {
                action_submit_data();
                return false;
            }
        }
        return false; // close the keyboard
    }


}
