package com.hkm.root.adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.datamodel.BigObserveDot;
import com.hkm.datamodel.Label;
import com.hkm.datamodel.RouteNode;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;

import java.util.ArrayList;

import static com.hkm.U.Tool.trace;
import static com.hkm.root.adapters.Pattern.tag.CANCEL;
import static com.hkm.root.adapters.Pattern.tag.C_EDIT;
import static com.hkm.root.adapters.Pattern.tag.C_INSERT;
import static com.hkm.root.adapters.Pattern.tag.C_LINECUT;
import static com.hkm.root.adapters.Pattern.tag.C_OK;
import static com.hkm.root.adapters.Pattern.tag.C_REMOVE;
import static com.hkm.root.adapters.Pattern.tag.EDIT;
import static com.hkm.root.adapters.Pattern.tag.LABEL_CONTROL;
import static com.hkm.root.adapters.Pattern.tag.ROW_NUMBER_SET_A;
import static com.hkm.root.adapters.Pattern.tag.ROW_NUMBER_SET_B;
import static com.hkm.root.adapters.Pattern.tag.ROW_NUMBER_SET_C;
import static com.hkm.root.adapters.Pattern.tag.ROW_NUMBER_SET_D;
import static com.hkm.root.adapters.Pattern.tag.YES;

/**
 * Developed By Hesk Working on the List Adapter of the Plot and Chart System 2013
 * All Rights Reserved.
 * Created by hesk on 7/25/13.
 */
public class Pattern implements
        View.OnTouchListener,
        TextView.OnEditorActionListener,
        View.OnClickListener,
        NumberPickerDialogFragment.NumberPickerDialogHandler {
    /**
     * this is the width from the left edge
     */
    public static final float
            OFFSET_CONFIRM_PANEL = 450f,
            OFFSET_OPTION_PANEL = 500f,
            ALPHA_DISABLE = 0.8f,
            ALPHA_ENABLE = 0f;
    public static final String TAG = "PATTERN HOLDER";
    public TextView reference_label, distance_a, distance_b, depth_from_ground, cable_width;

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
    public View l_bottom, l_control_editing_field, l_control_modify, l_color, line_cute_divier;
    public boolean isOpened;
    public int pos;
    public ImageView tn;
    public boolean isOptionsShown, new_rezzed_row;
    public ImageButton cancel_control, yes, no, edit_button, remove_row_control, edit_control, line_break_control;
    public Button insert_above_control, label_control_button, layerba, layerbb, layerbc, layerbd;
    public status currentStatus;
    public breaktype mMarkType;
    public Context ctx;
    private float offset_cp, offset_op;
    private Paint mPaint;
    private boolean hasResult, reference_label_completed;
    private int totalWidth, orientation;
    private String number_row_a, number_row_b, number_row_c, number_row_d;
    private RouteNodeAdapter route_adapter;
    private tag numberic_dialog_set_position;
    private NumberPickerBuilder mNumberPickerBuilder;


    public Pattern() {
        isOpened = false;
        currentStatus = status.INCOMPLETE;
        mMarkType = breaktype.CONTINUE;
    }

    public Pattern(int p, Context c) {
        pos = p;
        ctx = c;
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

    public void setAdapter(RouteNodeAdapter rd) {
        route_adapter = rd;
    }

    private static boolean isZero(EditText t) {
        try {
            final Float contenttext = Float.parseFloat(t.getText().toString());
            Log.d("TAG STRING CONTENT", contenttext.toString());
            if (contenttext == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("TAG STRING CONTENT", "NO CONTEST");
            e.printStackTrace();
            return false;
        }
    }


    public Pattern addConvertViewToElements(View rv) {

        l_control_modify = (View) rv.findViewById(R.id.toplayer);
        l_control_editing_field = (View) rv.findViewById(R.id.layer_editing_fields);
        l_bottom = (View) rv.findViewById(R.id.bottomlayer);
        l_color = (View) rv.findViewById(R.id.coloring_layer);
        line_cute_divier = (View) rv.findViewById(R.id.line_cue);

        distance_a = (TextView) rv.findViewById(R.id.distance_from_a);
        distance_b = (TextView) rv.findViewById(R.id.distance_from_b);
        depth_from_ground = (TextView) rv.findViewById(R.id.depth);
        reference_label = (TextView) rv.findViewById(R.id.point_label);
        cable_width = (TextView) rv.findViewById(R.id.cable_radius);

        tn = (ImageView) rv.findViewById(R.id.indication);
        new_rezzed_row = true;

        edit_button = (ImageButton) rv.findViewById(R.id.modi);
        no = (ImageButton) rv.findViewById(R.id.b_no);
        yes = (ImageButton) rv.findViewById(R.id.b_yes);
        edit_control = (ImageButton) rv.findViewById(R.id.iv_edit);
        cancel_control = (ImageButton) rv.findViewById(R.id.iv_ok_control);
        line_break_control = (ImageButton) rv.findViewById(R.id.iv_linecut);
        remove_row_control = (ImageButton) rv.findViewById(R.id.iv_remove_row_control);

        label_control_button = (Button) rv.findViewById(R.id.label_control_button_l);
        layerba = (Button) rv.findViewById(R.id.label_control_button_a);
        layerbb = (Button) rv.findViewById(R.id.label_control_button_b);
        layerbc = (Button) rv.findViewById(R.id.label_control_button_c);
        layerbd = (Button) rv.findViewById(R.id.label_control_button_d);


        return this;
    }
    /*public Pattern addViewObject(ImageButton a, Button b, ImageButton c) {
        remove_row_control = a;
        insert_above_control = b;
        line_break_control = c;
        return this;
    }*/


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


        if (distance_a != null && distance_a instanceof EditText) {
            distance_a.setOnEditorActionListener(this);
        } else {
            Log.d(TAG, "distance_a is not set");
        }
        if (distance_b != null && distance_b instanceof EditText) {
            //edittext only
            distance_b.setOnEditorActionListener(this);
        } else {
            Log.d(TAG, "distance_b is not set");
        }
        if (depth_from_ground != null && depth_from_ground instanceof EditText) {
            //edittext only
            depth_from_ground.setOnEditorActionListener(this);
        } else {
            Log.d(TAG, "depth_from_ground is not set");
        }

        if (remove_row_control != null) {
            remove_row_control.setOnClickListener(this);
            remove_row_control.setTag(C_REMOVE);
        }

        if (insert_above_control != null) {
            insert_above_control.setOnClickListener(this);
            insert_above_control.setTag(C_INSERT);
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

    private static String DEFAULT_NOTHING = "----";

    public void bind_data2display(final RouteNode rn) {

        final Label lb = rn.get_label();

        if (lb == null) {
            reference_label.setText(Constant.ROW_NOT_COMPLETE_TEXT);
            reference_label_completed = false;
            set_result_indicator(false);
        } else {
            reference_label.setText(lb.get_display_big_button_label());
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
            mMarkType = Pattern.breaktype.values()[0];
            line_cute_divier.setVisibility(View.GONE);
            set_result_indicator(false);
        } else {

            number_row_a = String.format("%.2f", rn.get_distance_a());
            number_row_b = String.format("%.2f", rn.get_distance_b());
            number_row_c = String.format("%.2f", rn.get_depth());
            number_row_d = String.format("%.1f", rn.get_cable_radius());
            distance_a.setText(number_row_a);
            distance_b.setText(number_row_b);
            depth_from_ground.setText(number_row_c);
            cable_width.setText(number_row_d);
            if (!rn.get_cut()) {
                mMarkType = Pattern.breaktype.values()[0];
                line_cute_divier.setVisibility(View.GONE);
            } else {
                mMarkType = Pattern.breaktype.values()[1];
                line_cute_divier.setVisibility(View.VISIBLE);
            }
            analyze_initial();
        }
        if (rn.highLighted()) l_bottom.setBackground(getImg(R.drawable.rowset_yellow_strip));
        else l_bottom.setBackground(null);
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

    public void setMarkerBreak(boolean x) {
        if (x) {
            mMarkType = breaktype.BREAK_POINT;
            line_cute_divier.setVisibility(View.VISIBLE);
        } else {
            mMarkType = breaktype.CONTINUE;
            line_cute_divier.setVisibility(View.INVISIBLE);
        }
    }

    private boolean analyze_initial() {
        final BigObserveDot a = Content.current_sketch_map.get_point_a();
        final BigObserveDot b = Content.current_sketch_map.get_point_b();
        final int r1 = EQPool.m2p(Float.parseFloat(number_row_a));
        final int r2 = EQPool.m2p(Float.parseFloat(number_row_b));
        final PointF[] found_pair = EQPool.getLayerIntersectPairES(a, b, r1, r2);
        if (found_pair == null) {
            set_result_indicator(false);
        } else {
            set_result_indicator(true);
        }
        return true;
    }

    /**
     * the problem is in here
     *
     * @return
     */
    public boolean analyze_distances_possible_and_update() {
        if (!number_row_a.isEmpty() && !number_row_b.isEmpty()) {
            final BigObserveDot a = Content.current_sketch_map.get_point_a();
            final BigObserveDot b = Content.current_sketch_map.get_point_b();
            final float ca = Float.parseFloat(number_row_a);
            final float cb = Float.parseFloat(number_row_b);
            final int r1 = EQPool.m2p(ca);
            final int r2 = EQPool.m2p(cb);
            final PointF[] found_pair = EQPool.getLayerIntersectPairES(a, b, r1, r2);

            route_adapter.getItem(pos).set_distance_a(ca).set_distance_b(cb);
            if (found_pair == null) {
                /** checking of the pair is not found **/
                set_result_indicator(false);
                trace(ctx, R.string.no_result_found);
                route_adapter.setSelectedPositionChanged(pos);
                return false;
            } else {
                if (Content.saved_route.size() > 0) {
                    Content.require_update_pos = true;
                    /**
                     * adding this update position into the list
                     */

                    final int result = Constant.update_existing_index.indexOf(pos);
                    if (result == -1) {
                        Constant.update_existing_index.add(pos);
                        trace(ctx, R.string.set_results);
                    } else {
                        trace(ctx, R.string.set_results_updated);
                    }
                } else {
                    Content.require_update_pos = true;
                    trace(ctx, R.string.set_result_first_time);
                }
                // Content.current_sketch_map.update_routenode(pos, prepare_current_node(r1, r2));
                set_result_indicator(true);
                route_adapter.getItem(pos).set_r(r1, r2);
                route_adapter.setSelectedPositionChanged(pos);
                return true;
            }

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
            final ArrayList<Integer> j = Constant.update_existing_index;
            final int result = j.indexOf(pos);
            if (result > -1) {
                tn.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_enabled_result_positive_changing));
            } else {
                tn.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_enabled_result_positive));
            }
        } else {
            tn.setBackgroundColor(ctx.getResources().getColor(R.color.indicator_enabled_result_negative));
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

    /**
     * prepare the node object to be updated. there is no label object
     *
     * @return
     */
    private RouteNode prepare_current_node(int r_a, int r_b) {
        final RouteNode node = new RouteNode();
        node.set_distance_a(Float.parseFloat(number_row_a));
        node.set_distance_b(Float.parseFloat(number_row_b));
        node.set_depth(Float.parseFloat(number_row_c));
        node.set_r(r_a, r_b);
        node.set_has_result(hasResult);
        if (mMarkType == breaktype.CONTINUE) {
            node.set_cut(false);
        } else {
            node.set_cut(true);
        }
        return node;
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

    private boolean isCompleteAllFields() {
        final boolean a = Tool.isEmpty(distance_a);
        final boolean b = Tool.isEmpty(distance_b);
        final boolean c = Tool.isEmpty(depth_from_ground);
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
    public Pattern setStatus(status m) {
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
    public void onClick(View view) {
        final tag feat = (tag) view.getTag();
        final boolean use_num_picker = feat == ROW_NUMBER_SET_A || feat == ROW_NUMBER_SET_B || feat == ROW_NUMBER_SET_C || feat == ROW_NUMBER_SET_D;
        try {
            //  final int position_ini = H.pos;
            Log.d("START", "Get Pos at:" + currentStatus.toString() + " , p:" + pos);
            switch (feat) {
                case EDIT:
                    setStatus(Pattern.status.MODIFY);
                    break;
                case CANCEL:
                    route_adapter.removeItem();
                    break;
                case YES:
                    if (action_submit_data()) {
                        route_adapter.updateListItem(this);
                    }
                    break;
                case C_EDIT:
                    route_adapter.removeItem();
                    break;
                case C_INSERT:
                    setStatus(Pattern.status.COMPLETE);
                    route_adapter.addnew(pos);
                    break;
                case C_LINECUT:
                    if (mMarkType == Pattern.breaktype.CONTINUE) {
                        setMarkerBreak(true);
                    } else {
                        setMarkerBreak(false);
                    }
                    setStatus(Pattern.status.COMPLETE);
                    route_adapter.updateListItem(this);
                    Tool.trace(ctx, "Marker Notified!");
                    break;
                case C_OK:
                    //this is the cancel button from the layout
                    setStatus(Pattern.status.COMPLETE);
                    break;
                case C_REMOVE:
                    Tool.trace(ctx, "C_REMOVE");
                    route_adapter.removeItem(pos);
                    break;
                case LABEL_CONTROL:
                    route_adapter.adapterListener.onPickLegend(pos);
                    break;
                default:
                    if (use_num_picker) {
                        if (feat == ROW_NUMBER_SET_A) {
                            mNumberPickerBuilder.setMinNumber(-10);
                        } else mNumberPickerBuilder.setMinNumber(0);

                        numberic_dialog_set_position = feat;
                        mNumberPickerBuilder.show();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Tool.trace(ctx, e.toString());
        }
    }

    //TODO: working on something for double digits..
    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        // text.setText("Number: " + number + "\nDecimal: " + decimal + "\nIs negative: " + isNegative + "\nFull number: "  + fullNumber);
        boolean do_anaylsis = false;
        switch (numberic_dialog_set_position) {
            case ROW_NUMBER_SET_A:
                do_anaylsis = true;
                number_row_a = String.format("%.2f", fullNumber);
                distance_a.setText(number_row_a);
                break;
            case ROW_NUMBER_SET_B:
                do_anaylsis = true;
                number_row_b = String.format("%.2f", fullNumber);
                distance_b.setText(number_row_b);
                break;
            case ROW_NUMBER_SET_C:
                number_row_c = String.format("%.2f", fullNumber);
                depth_from_ground.setText(number_row_c);
                //   Content.current_sketch_map.get_route_node_at(pos)
                route_adapter.getItem(pos).set_depth((float) fullNumber);
                break;
            case ROW_NUMBER_SET_D:
                number_row_d = String.format("%.1f", fullNumber);
                cable_width.setText(number_row_d);
                // Content.current_sketch_map.get_route_node_at(pos)
                //.set_cable_r((float) fullNumber);
                route_adapter.getItem(pos).set_cable_r((float) fullNumber);
                break;
            default:
                break;
        }
        if (do_anaylsis) {
            analyze_distances_possible_and_update();
            Content.require_update_depth = true;
            route_adapter.setSelectedPositionChanged(pos);
        }
        numberic_dialog_set_position = tag.TAG_NONE;
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    static enum tag {
        TAG_NONE,
        LABEL_CONTROL, ROW_NUMBER_SET_A, ROW_NUMBER_SET_B, ROW_NUMBER_SET_C, ROW_NUMBER_SET_D,
        CANCEL, YES, EDIT,
        C_EDIT, C_REMOVE, C_INSERT, C_OK, C_LINECUT;
    }

    public static enum status {
        INITIAL, FOCUS, COMPLETE, INCOMPLETE, MODIFY, DEFAULT;
    }

    public static enum breaktype {
        // mMarkType= 0 that is LINE
        // mMarkType= 1 that is BREAK POINT
        // mMarkType= 2 that is FEATURE there is no single marker now
        CONTINUE, BREAK_POINT, WITH_ICON;

        public breaktype byStringOrder(String t) {
            return Pattern.breaktype.values()[Integer.parseInt(t)];
        }
    }

}
