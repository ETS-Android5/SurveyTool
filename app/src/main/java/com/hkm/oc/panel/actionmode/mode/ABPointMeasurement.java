package com.hkm.oc.panel.actionmode.mode;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ActionMode;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.hkm.U.Content;
import com.hkm.oc.R;
import com.hkm.oc.panel.actionmode.module.ModeTag;
import com.hkm.oc.panel.actionmode.module.PanelActionModeExt;
import com.hkm.oc.panel.basic_panel_support;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.panel.corepanel.elements.AbPointRuler;
import com.hkm.U.Tool;
import com.hkm.oc.panel.worker;
import com.hkm.oc.preF.f.WorkPanelFragment;
import com.hkm.root.Dialog.DCBBool;
import com.hkm.root.Dialog.DialogDoYouWantTo;

/**
 * Created by Hesk on 26/6/2014.
 */

public class ABPointMeasurement extends PanelActionModeExt implements NumberPickerDialogFragment.NumberPickerDialogHandler {

    private worker.action current_state;
    private EditText text_field;
    private AbPointRuler ruler;
    private NumberPickerBuilder mNumberPickerBuilder;
    private MapPanel.CMode currentstoremode;

    public ABPointMeasurement(WorkPanelFragment workPanelFragment, basic_panel_support c) {
        super(workPanelFragment, c);
    }

    //  private String field_1, field_2;

    private void setNumberDialog() {
        FragmentManager fm = root_context.getSupportFragmentManager();
        mNumberPickerBuilder = new NumberPickerBuilder();
        mNumberPickerBuilder.setFragmentManager(fm);
        mNumberPickerBuilder.setStyleResId(R.style.BetterPickersDialogFragment_Light);
        mNumberPickerBuilder.setReference(1);
        mNumberPickerBuilder.setDecimalVisibility(View.VISIBLE);
        mNumberPickerBuilder.addNumberPickerDialogHandler(this);
        mNumberPickerBuilder.setStyleResId(R.style.hkmKeyPad);
        mNumberPickerBuilder.setLabelText("m");
        mNumberPickerBuilder.setMinNumber(0);
    }

    private void setup_mode_header(ActionMode mode, Menu menu) {


        View ruler_view = LayoutInflater.from(root_context).inflate(R.layout.enter_text_actionmode, null);
        mode.setCustomView(ruler_view);
        text_field = (EditText) ruler_view.findViewById(R.id.action_mode_text_field);

        // mode.setTitle(R.string.notice_title_abpoint);
        mode.getMenuInflater().inflate(R.menu.actionmode_absetup, menu);
    }

    private void setup_ruler(final ActionMode mode) {
        ruler = panel.getRuler();
        ruler.setABPoints(panel.getA(), panel.getB());
        initTag(mode, ModeTag.Tag.AB_1_MODE, true);
        currentstoremode = panel.getMode();
        panel.setMode(MapPanel.CMode.AB_POINT_MEASUREMENT);
        ruler.setRatioChangeListener(new AbPointRuler.RChange() {
            @Override
            public void changeInvaliate() {
                root_context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mode.invalidate();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        current_state = worker.action.review_ab_pos;
        setNumberDialog();
        setup_mode_header(mode, menu);
        setup_ruler(mode);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        if (current_state == worker.action.enter_a_tag || current_state == worker.action.enter_b_tag) {
            StringBuilder sb = new StringBuilder();
            sb.append("Labeling for ");

            text_field.setVisibility(View.VISIBLE);
            text_field.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_CLASS_TEXT);
            text_field.setFocusableInTouchMode(true);
            text_field.setFocusable(true);
            text_field.setOnEditorActionListener(this);
            text_field.setImeOptions(EditorInfo.IME_ACTION_DONE);
            text_field.setEnabled(true);
            if (current_state == worker.action.enter_a_tag) {
                sb.append("A");
                text_field.setText(panel.getA().getlabel());
            }
            if (current_state == worker.action.enter_b_tag) {
                sb.append("B");
                text_field.setText(panel.getB().getlabel());
            }

            text_field.setHint(sb.toString());

        } else if (current_state == worker.action.review_ab_pos) {
            if (text_field != null) {
                text_field.setVisibility(View.VISIBLE);
                text_field.setEnabled(false);
                text_field.setText(ruler.showBarInfo());
                if (ruler.enableRuler) {
                    Tool.trace(root_context, R.string.notice_title_abpoint);
                }
            }
        } else if (current_state == worker.action.define_distance_ab) {
            if (text_field != null) {
                text_field.setVisibility(View.GONE);
            }
        }
        return false;
    }

    protected void onDone() {
        save_label();
    }

    private void save_label() {
        if (current_state == worker.action.enter_a_tag) {
            panel.getA().setlabel(text_field.getText().toString());
        }
        if (current_state == worker.action.enter_b_tag) {
            panel.getB().setlabel(text_field.getText().toString());
        }
    }

    private boolean check_enter_label() {
        if (current_state == worker.action.enter_a_tag || current_state == worker.action.enter_b_tag) {
            final boolean empty = text_field.getText().toString().isEmpty();
            if (empty) {
                Tool.trace(root_context, "Please enter the label for this location point.");
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.define_distance:
                if (check_enter_label()) {
                    current_state = worker.action.define_distance_ab;
                    mNumberPickerBuilder.show();
                    Tool.trace(root_context, "Enter the measurement distance between point A and point B.");
                    mode.invalidate();
                }
                ruler.setEnable(false);
                return true;
            case R.id.tag_a_point:
                if (check_enter_label()) {
                    current_state = worker.action.enter_a_tag;
                    mode.invalidate();
                }
                ruler.setEnable(false);
                return true;
            case R.id.tag_b_point:
                if (check_enter_label()) {
                    current_state = worker.action.enter_b_tag;
                    mode.invalidate();
                }
                ruler.setEnable(false);
                return true;
            case R.id.move_point:
                ///   boolean ratio_okay = Content.current_sketch_map.getratio() > 0.0f;
                boolean route_exist = Content.current_sketch_map.get_route_size() > 0;
                if (route_exist && !ruler.enableRuler) {
                    DialogDoYouWantTo d = DialogDoYouWantTo.newInstance(R.string.question_move_points, new DCBBool() {
                        @Override
                        public void onyes(DialogInterface dialog) {
                            ruler.setEnable(true);
                        }

                        @Override
                        public void onno(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                    d.setCancelable(true);
                    d.show(root_context.getSupportFragmentManager(), null);
                }
                if (!route_exist)
                    ruler.toggle();
                current_state = worker.action.review_ab_pos;
                mode.invalidate();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDialogNumberSet(int i, int i2, double v, boolean b, double v2) {
        //v2
        try {
            if (v2 > 0) {
                final String ed = ruler.setMeterNow((float) v2);
                Tool.trace(root_context, ed);
            } else {
                Tool.trace(root_context, "unable to set Ratio for these points");
            }
            current_state = worker.action.review_ab_pos;
            _model.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Tool.hideKeyBoard(root_context, inputbra);
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (Content.ratio_changed) {
            Content.ratio_changed = false;
            ruler.ApplyCurrentABIntoPanelAB();
        }
        if (ruler.enableRuler)
            ruler.setEnable(false);
    }

}
