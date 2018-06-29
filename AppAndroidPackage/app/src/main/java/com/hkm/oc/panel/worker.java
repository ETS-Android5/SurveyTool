package com.hkm.oc.panel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.U.Tool;
import com.hkm.listviewhkm.NodeList;
import com.hkm.oc.R;
import com.hkm.oc.panel.actionmode.mode.ABPointMeasurement;
import com.hkm.oc.panel.actionmode.mode.ActionModeProposedTrialPit;
import com.hkm.oc.panel.actionmode.mode.ActionModeSurveyBoundary;
import com.hkm.oc.panel.actionmode.mode.NodePointInspector;
import com.hkm.oc.panel.actionmode.module.HandleActionModeInitialization;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.panel.corepanel.handler.PanelTouchListener;
import com.hkm.oc.preF.f.WorkPanelFragment;
import com.hkm.root.Dialog.DCBBool;
import com.hkm.root.Dialog.DialogDoYouWantTo;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.hkm.root.Dialog.DsingleCB;
import com.hkm.root.Tasks.SaveReportImage;
import com.hkm.root.handler.MainHandlerCallBack;

/**
 * including all action modes
 * Created by Hesk on 12/6/2014.
 */
public class worker extends basic_panel_support {
    public static String TAG = "Working Panel Activity";
    /**
     * implementing the action mode object
     */
    protected static HandleActionModeInitialization instance_action_mode;
    private static int PanelMode;
    private final MainHandlerCallBack save_task_handler = new MainHandlerCallBack() {
        @Override
        public boolean handleMessage(Message msg) {
            DialogSimpleNotification d = null;
            switch (msg.what) {
                case SaveReportImage.START:
                    progress_bar_start(R.string.dialog_msg_process);
                    break;
                case SaveReportImage.TIMEOUT:
                    // simple_dialog_one_button(R.string.dialog_msg_failure_timeout, Constant.DialogCallBack.SINGLE_CLICK);
                    d = DialogSimpleNotification.newInstance(R.string.dialog_msg_failure_timeout);
                    d.show(root_context.getSupportFragmentManager(), "single question");
                    break;
                case SaveReportImage.SUCCESS:
                    progressBar_dismiss();
                    d = DialogSimpleNotification.newInstance(R.string.notice_image_generated, new DsingleCB() {
                        @Override
                        public void oncontified(DialogFragment dialog) {
                            if (task_save_and_exit)
                                does_exit();
                            dialog.dismiss();
                        }
                    });
                    d.show(root_context.getSupportFragmentManager(), "single question");
                    break;
                case SaveReportImage.FAILURE:
                    d = DialogSimpleNotification.newInstance(R.string.dialog_msg_failure_export);
                    d.show(root_context.getSupportFragmentManager(), "single question");
                    break;
            }
            return false;
        }
    };
    protected WorkPanelFragment wpf;
    //  private static boolean mActionModeCloseReady = true;
    /* Create object of HashMap */
    private MapPanel panel;
    private TextView statusText;
    /*
        protected MapPanel init_panel(Bundle bund, final Bitmap bitmap) throws Exception {
            root_context.setContentView(R.layout.native_plot_map);
            statusText = (TextView) root_context.findViewById(R.id.statustext);
            panel = (MapPanel) root_context.findViewById(R.id.rendering_panel);
            panel.setPanelListener(new PanelListener());
            panel.setBaseMap(bitmap, new Point(bitmap.getWidth(), bitmap.getHeight()));
            if (bund != null) {
                panel.restoreInstanceState(bund);
            }
            return panel;
        }
    */
    private boolean isHorizontal, isVertical, task_save_and_exit = false;

    public void Take_action_panel_zoom() {
        wpf.getPanel().setMode(MapPanel.CMode.ADJUSTMENT);
    }

    public void Take_action_connect_dots() {
        final int action_result = wpf.getPanel().continue_dot_process();
        switch (action_result) {
            case 0:
                openOneClickMessage(Constant.DialogInt.ROUTE_EDIT_MODE_START_DIALOG);
                return;
            case 1:
                openOneClickMessage(Constant.DialogInt.FRESH_CONNECT_NODE_START_DIALOG);
                return;
            case 2:
                openOneClickMessage(Constant.DialogInt.NO_DATA_TO_START_MEASUREMENT_ERROR);
                return;
            case 4:
                openOneClickMessage(Constant.DialogInt.NO_DATA_TO_START_CONNECTING);
                return;
            default:
                Log.d(TAG, "other return int:" + action_result);
                return;
        }
    }

    protected void Take_action_edit_AB_points() {
        if (Content.radius_list.size() > 0) {
            //showDoYouWant(Constant.DialogInt.DO_NEED_TO_SET_AB_POSITION);
            final DialogDoYouWantTo c = DialogDoYouWantTo.newInstance(R.string.question_do_0, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {
                    instance_action_mode = new HandleActionModeInitialization((worker) root_context, new ABPointMeasurement(wpf, root_context));
                }

                @Override
                public void onno(DialogInterface dialog) {

                }
            });
            c.show(root_context.getSupportFragmentManager(), null);
        } else {
            // ActionModeStart(new ActionModeABSetup());
            instance_action_mode = new HandleActionModeInitialization((worker) root_context, new ABPointMeasurement(wpf, root_context));
        }
    }

    protected void Take_action_enter_measurement() {
        HandleActionModeInitialization handle = new HandleActionModeInitialization((worker) root_context, wpf);
        if (handle.check_if_enter_measurement_nodes_is_okay()) {
            final Intent i = new Intent(ac, NodeList.class);
            wpf.getPanel().UpdateAB2Constants();
            startActivityForResult(i, Constant.IntentKey.INTENT_ROUTE_MANAGEMENT);
        }
    }

    protected void Take_action_point_inspection() {
        instance_action_mode = new HandleActionModeInitialization((worker) root_context, new NodePointInspector(wpf, root_context));
    }

    protected void Take_action_survey() {
        instance_action_mode = new HandleActionModeInitialization((worker) root_context, new ActionModeSurveyBoundary(wpf, root_context));
    }

    protected void Take_action_tp() {
        instance_action_mode = new HandleActionModeInitialization((worker) root_context, new ActionModeProposedTrialPit(wpf, root_context));
    }

    protected void Take_action_save_jpg() {
        task_save_and_exit = false;
        final SaveReportImage f = new SaveReportImage(wpf.getPanel(), root_context, save_task_handler);
        f.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iReturn) {
        super.onActivityResult(requestCode, resultCode, iReturn);
        if (requestCode == Constant.IntentKey.SELECT_PICTURE) {
            try {
                final String physicalPath = Tool.getRealPathFromURI(root_context, iReturn.getData());
                //Tool.decodeAndResizeFile(new File(selectedImagePath));
                Bitmap bm = Tool.decodeScaledBitmapFromSdCard(physicalPath, 2000, 2000);
                wpf.getPanel().setBaseMap(bm, new Point(bm.getWidth(), bm.getHeight()));
            } catch (Exception e) {
                if (wpf.getPanel() == null)
                    Tool.trace(root_context, "panel is not existedr");
            }
        } else if (requestCode == Constant.IntentKey.LEGEND_ICON_PICK) {
            try {
                final Integer return_legend_index = iReturn.getExtras().getInt("legendindex", -1);
                Tool.trace(root_context, return_legend_index + "");
            } catch (Exception e) {
                Tool.trace(root_context, e.toString());
            }
        } else if (requestCode == Constant.IntentKey.INTENT_ROUTE_MANAGEMENT && resultCode == RESULT_OK) {
            Take_action_connect_dots();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void parent_action_panel() {
        super.parent_action_panel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDPindex();
    }

    @Override
    public void onBackPressed() {
        exit_current_sketch_map();
    }


    protected void does_exit() {
        final Intent i = getIntent();
        Bundle b = new Bundle();
        b.putInt("update_attachment_Id", Content.current_sketch_map.getAttachmentId());
        i.putExtras(b);
        setResult(Constant.IntentAction.UPDATE, i);
        root_context.finish();
    }

    protected void exit_current_sketch_map() {
        if (ac.IsDrawMapExitNotification()) {
            final DialogDoYouWantTo dou = DialogDoYouWantTo.newInstance(R.string.question_exit_draw_map_application, new DCBBool() {
                @Override
                public void onyes(DialogInterface dialog) {
                    task_save_and_exit = true;
                    final SaveReportImage f = new SaveReportImage(wpf.getPanel(), root_context, save_task_handler);
                    f.execute();
                }

                @Override
                public void onno(DialogInterface dialog) {
                    does_exit();
                }
            });
            dou.show(getSupportFragmentManager(), "exit question");
        } else {
            does_exit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (wpf.getPanel() != null) wpf.getPanel().saveInstanceState(outState);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        if (wpf.getPanel() != null) wpf.getPanel().restoreInstanceState(savedInstanceState);
        // Always call the superclass so it can restore the view hierarchy

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isVertical = false;
            isHorizontal = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isHorizontal = false;
            isVertical = true;
        }
        if (wpf.getPanel() != null) {
            wpf.getPanel().onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (instance_action_mode != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                // handle your back button code here
                return true; // consumes the back key event - ActionMode is not finished
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        final String DTag = dialog.getTag();
        if (DTag == Constant.DialogCallBack.SINGLE_DISTANCE_RATIO_NOTICE) {
            Take_action_panel_zoom();
        }
        super.DialogCallBackActionBar(DTag);
        super.onDialogNegativeClick(dialog);
    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {
        final String DTag = dialog.getTag();
        //String.valueOf(Constant.DialogInt.DO_NEED_TO_SET_AB_DISTANCE_FOR_RATIO)
        super.DialogCallBackActionBar(DTag);
        super.onDialogNeutral(dialog);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        final String DTag = dialog.getTag();
        if (DTag == Constant.DialogCallBack.DO_AB_NEW_DATAPOINTS) {
            instance_action_mode = new HandleActionModeInitialization((worker) root_context, new ABPointMeasurement(wpf, root_context));
        } else if (DTag == Constant.DialogCallBack.DO_UPLOAD) {
            upload_data_start();
        } else if (DTag == Constant.DialogCallBack.DO_SB) {
            instance_action_mode.close_action_mode();
        }
        super.DialogCallBackActionBar(DTag);
        super.onDialogPositiveClick(dialog);
    }

    public MapPanel getMapPanel() {
        if (wpf != null) return wpf.getPanel();
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wpf != null)
            if (wpf.getPanel() != null) wpf.getPanel().pause();
        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wpf != null)
            if (wpf.getPanel() != null)
                wpf.getPanel().resume();
    }

    public static enum action {
        enter_a_tag, enter_b_tag, define_distance_ab, review_ab_pos
    }

    public class PanelListener implements PanelTouchListener {
        @Override
        public void onInitDialog(MapPanel panel, int triggerID) {
            root_context.openOneClickMessage(triggerID);
        }

        @Override
        public void toastMsg(String b) {
            Tool.trace(root_context, b);
        }

        @Override
        public void onDown(MapPanel pan, MotionEvent me, Canvas Panelcanvas) {
            final int paint_color;
            if (Constant.MODE_EDIT_LINK_CHILDREN_TESTING_RESULT == PanelMode) {
                paint_color = Color.RED;
            } else if (Constant.MODE_EDIT_MAIN_POINT == PanelMode) {
                paint_color = Color.GREEN;
            } else {
                paint_color = Color.CYAN;
            }
            //  mtextureview_1.setBackgroundColor(paint_color);
        }

        @Override
        public void onStream(MapPanel pan) {
            // tv.setText(pan.stream_text());
            //statusText.setText(pan.stream_text());
        }

        @Override
        public void onMode(int i) {
            PanelMode = i;
        }

        @Override
        public void onUp(MapPanel pan, MotionEvent me, Canvas Panelcanvas, int mod) {
            //  mtextureview_1.setBackgroundColor(Color.BLUE);
        }
    }

}
