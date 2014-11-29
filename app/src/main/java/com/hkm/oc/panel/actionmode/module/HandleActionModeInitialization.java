package com.hkm.oc.panel.actionmode.module;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.view.View;

import com.hkm.U.Content;
import com.hkm.oc.R;
import com.hkm.oc.panel.actionmode.mode.ABPointMeasurement;
import com.hkm.U.Tool;
import com.hkm.oc.panel.worker;
import com.hkm.oc.preF.f.WorkPanelFragment;
import com.hkm.root.Dialog.DCBBool;
import com.hkm.root.Dialog.DialogDoYouWantTo;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.hkm.root.Dialog.DsingleCB;

/**
 * Created by hesk on 6/1/2014.
 */
public class HandleActionModeInitialization implements View.OnClickListener {
    private static ActionMode mActionMode;
    private View doneButton;
    private worker ctx;
    private boolean ratio_okay, route_exist;
    private ActionBar ab;
    private WorkPanelFragment wpf;


    public HandleActionModeInitialization(worker ctx, final PanelActionModeExt mActionModeCB) {
        this.ctx = ctx;
        this.ab = ctx.getSupportActionBar();
        if (!check_ratio_setup()) {
         /*   DialogDoYouWantTo h = DialogDoYouWantTo.newInstance(R.string.notice_meter_dialog_part, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {

                }
                @Override
                public void onno(DialogInterface dialog) {

                }
            });*/
            //  ab.hide();
            DialogSimpleNotification h = DialogSimpleNotification.newInstance(R.string.question_map_ab_1, new DsingleCB() {
                @Override
                public void oncontified(DialogFragment dialog) {
                    init(new ABPointMeasurement(mActionModeCB.getRootfragment(), mActionModeCB.getRootContext()));
                    //  ab.show();
                }
            });
            h.show(ctx.getSupportFragmentManager(), null);
        } else
            init(mActionModeCB);
    }

    public HandleActionModeInitialization(worker c, WorkPanelFragment workPanelFragment) {
        this.wpf = workPanelFragment;
        this.ctx = c;
    }

    public boolean check_if_enter_measurement_nodes_is_okay() {
        if (check_ratio_setup()) {
            return true;
        } else {
            init(new ABPointMeasurement(wpf, ctx));
            return false;
        }
    }

    private void init(ActionMode.Callback am) {
        // mActionMode = ctx.startActionMode(mActionModeCB);
        mActionMode = ctx.startSupportActionMode(am);
        // check button handler
        final int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
        doneButton = ctx.findViewById(doneButtonId);
        if (doneButton != null) {
            doneButton.setOnClickListener(this);
            doneButton.setTag(mActionMode.getTag());
        } else {
            //  doneButton = mActionMode.getCustomView().findViewById(doneButtonId);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            ratio_okay = Content.current_sketch_map.getratio() > 0.0f;
            route_exist = Content.current_sketch_map.get_route_size() > 0;

            Object ob = mActionMode.getTag();
            if (ob instanceof ModeTag) {
                ModeTag tagobject = (ModeTag) ob;
                final boolean close = tagobject.closeReady;
                switch (tagobject.now) {
                    case SB_MODE:
                        close_action_mode();
                        break;
                    case TP_MODE:
                        close_action_mode();
                        break;
                    case AB_1_MODE:
                        if (status_check_ab_potin_action_mode())
                            close_action_mode();
                        break;
                    case AB_2_MODE:
                        close_action_mode();
                        break;
                    case AB_LABEL:
                        close_action_mode();
                        break;
                    case NODE_INSPECTOR:
                        close_action_mode();
                        break;
                    default:
                        Tool.trace(ctx, "you are missing the ModeTag");
                        break;
                }
            } else {
                Tool.trace(ctx, "you are missing the ModeTag");
            }
            // do whatever you want
            // in android source code it's calling mMode.finish();
            // Tool.trace(WorkPanel.this, "done button pressed");
        } catch (Exception e) {
            Tool.trace(ctx, e.toString());
        }
    }

    public void close_action_mode() {
        mActionMode.finish();
        ctx.getActionBar().show();
        ctx.Take_action_panel_zoom();
        mActionMode = null;
    }

    private boolean check_ratio_setup() {
        return ratio_okay = Content.current_sketch_map.getratio() > 0.0f;
    }

    private boolean check_ratio_affect_to_nodes() {
        ratio_okay = Content.current_sketch_map.getratio() > 0.0f;
        route_exist = Content.current_sketch_map.get_route_size() > 0;
        return ratio_okay && route_exist;
    }

    private boolean status_check_ab_potin_action_mode() {
        if (check_ratio_affect_to_nodes() && Content.ratio_changed) {
            Content.ratio_changed = false;
            final DialogDoYouWantTo dialog = DialogDoYouWantTo.newInstance(R.string.question_distance_ratio, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {
                    ctx.getMapPanel().getRuler().ApplyCurrentABIntoPanelAB();
                    ctx.Take_action_connect_dots();
                    close_action_mode();
                    dialog.dismiss();
                }

                @Override
                public void onno(DialogInterface dialog) {
                    ctx.Take_action_panel_zoom();
                    close_action_mode();
                    dialog.dismiss();
                }
            });
            dialog.show(ctx.getSupportFragmentManager(), null);
            return false;
        }

        if (!Content.ratio_changed && !check_ratio_affect_to_nodes()) {
            final DialogSimpleNotification d = DialogSimpleNotification.newInstance(R.string.set_ab_3, new
                    DsingleCB() {
                        @Override
                        public void oncontified(DialogFragment dialog) {
                            dialog.dismiss();
                        }
                    });
            d.show(ctx.getSupportFragmentManager(), null);
            return false;
        }
        //   if (Content.ratio_changed && ratio_okay && !route_exist) {
        //      return true;
        //   }
        return true;
    }


/*    public void status_check() {
        if (ratio_okay && route_exist) {
            //  statusText.setText("");
            ctx.Take_action_panel_zoom();
            close_action_mode();
        } else if (!ratio_okay && !route_exist) {

            final DialogDoYouWantTo dialog = DialogDoYouWantTo.newInstance(R.string.set_ab_first, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {
                    ctx.Take_action_panel_zoom();
                    close_action_mode();
                    dialog.dismiss();
                }

                @Override
                public void onno(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show(ctx.getSupportFragmentManager(), "simplesimpelsimple");

        } else if (!ratio_okay && !route_exist) {
            //  statusText.setText("");
            //jump into the ruler mode
            final DialogDoYouWantTo dialog = DialogDoYouWantTo.newInstance(R.string.set_ab_second, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {
                    dialog.dismiss();
                }

                @Override
                public void onno(DialogInterface dialog) {
                    ctx.Take_action_panel_zoom();
                    dialog.dismiss();
                }
            });
            dialog.show(ctx.getSupportFragmentManager(), null);
        } else if (ratio_okay && !route_exist) {
            //if ratio set and ab main also already set
            //   showDoYouWant(Constant.DialogInt.DO_NEED_TO_SET_AB_DISTANCE_FOR_RATIO);
            // statusText.setText(R.string.notice_route_data);
            final DialogDoYouWantTo dialog = DialogDoYouWantTo.newInstance(R.string.question_distance_ratio, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {
                    ctx.Take_action_panel_zoom();
                    close_action_mode();
                    dialog.dismiss();
                }

                @Override
                public void onno(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show(ctx.getSupportFragmentManager(), null);
        } else {
            Tool.trace(ctx, "works as usual");
        }
    }*/
}
