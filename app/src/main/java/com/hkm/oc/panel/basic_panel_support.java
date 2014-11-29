package com.hkm.oc.panel;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.hkm.Application.appWork;
import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.listviewhkm.NodeList;
import com.hkm.oc.R;

import com.hkm.U.Tool;
import com.hkm.root.Dialog.DialogCB;
import com.hkm.root.Dialog.DialogDoYouWantTo;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.hkm.root.Dialog.DsingleCB;
import com.hkm.root.handler.AycnInfo;

/**
 * Hesk
 * OCFoundation Based FragmentActivity using DialogCB
 */
public abstract class basic_panel_support extends ActionBarActivity implements DialogCB {

    /* group fire missle dialog end */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.act_menu, menu);
        return true;
    }

    protected void initDPindex() {
        if (Content.current_sketch_map.getdpi() == 0 || Content.dpi == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            super.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Content.current_sketch_map.setDpi(dm.densityDpi);
            Content.dpi = dm.densityDpi;
        }
    }
/*
    protected void end_process_dialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_dialer)
                .setTitle("Exit?")
                .setMessage(R.string.question_do_exit)
                .setPositiveButton(R.string.myes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Stop the activity
                        //maintenancetabs.this.finish();
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                })
                .setNegativeButton(R.string.mno, null)
                .show();
    }*/


    /**
     * This is a universal dialog fragment for do you want ...
     *
     * @param triggerID the Id to be trigger
     */
    protected void showDoYouWant(int triggerID) {
        try {
            int res = R.string.notice_done;
            String cb_tag = Constant.DialogCallBack.BOOLEAN;
            switch (triggerID) {
                case Constant.DialogInt.DO_NEED_TO_SET_AB_POSITION:
                    res = R.string.question_do_0;
                    cb_tag = Constant.DialogCallBack.DO_AB_NEW_DATAPOINTS;
                    break;
                case Constant.DialogInt.DO_NEED_TO_SET_AB_DISTANCE_FOR_RATIO:
                    res = R.string.question_distance_ratio;
                    cb_tag = Constant.DialogCallBack.SINGLE_DISTANCE_RATIO_NOTICE;
                    break;
                case Constant.DialogInt.DO_NEED_TO_UPLOAD:
                    res = R.string.question_upload;
                    cb_tag = Constant.DialogCallBack.DO_UPLOAD;
                    break;
                case Constant.DialogInt.DO_NEED_TO_QUIT_SB_INCOMPLETE:
                    res = R.string.question_sb_quit;
                    cb_tag = Constant.DialogCallBack.DO_SB;
                    break;
            }
            final String message = root_context.getResources().getString(res);
            final DialogDoYouWantTo dialog = new DialogDoYouWantTo(message);
            dialog.setCancelable(false);
            dialog.show(root_context.getSupportFragmentManager(), cb_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(root_context, e.toString());
        }
    }

    protected void noCBDialogSingleButton(int resId) {
        final DialogSimpleNotification d = DialogSimpleNotification.newInstance(resId);
        d.show(root_context.getSupportFragmentManager(), null); //with this tag name
    }

    protected void openOneClickMessage(int triggerid) {
        try {
            int res = R.string.notice_done;
            String cb_tag = Constant.DialogCallBack.SINGLE_CLICK;
            switch (triggerid) {
                case Constant.DialogInt.FRESH_CONNECT_NODE_START_DIALOG:
                    res = R.string.notice_start;
                    cb_tag = Constant.DialogCallBack.SINGLE_CLICK_HIDE_ACTIONBAR;
                    break;
                case Constant.DialogInt.FRESH_CONNECT_NODE_DONE_DIALOG:
                    res = R.string.notice_end;
                    cb_tag = Constant.DialogCallBack.SINGLE_CLICK_SHOW_ACTIONBAR;
                    break;
                case Constant.DialogInt.NO_DATA_TO_START_CONNECTING:
                    res = R.string.warn_nodatatostart;
                    break;
                case Constant.DialogInt.NO_DATA_TO_START_MEASUREMENT_ERROR:
                    res = R.string.warn_no_enough_data_tostart;
                    break;
                case Constant.DialogInt.SET_AB_DISTANCE_DONE:
                    res = R.string.warn_radiuschanged;
                    break;
                case Constant.DialogInt.ROUTE_EDIT_MODE_START_DIALOG:
                    res = R.string.route_edit_mode_start;
                    cb_tag = Constant.DialogCallBack.SINGLE_CLICK_HIDE_ACTIONBAR;
                    break;
                /**
                 * when the route editing mode is done editing, this will start
                 */
                case Constant.DialogInt.ROUTE_EDIT_MODE_DONE_DIALOG:
                    res = R.string.route_edit_mode_done;
                    cb_tag = Constant.DialogCallBack.SINGLE_CLICK_SHOW_ACTIONBAR;
                    break;
                /**
                 * when starting the app this will run
                 */
                case Constant.DialogInt.SET_AB_DISTANCE_NOTICE:
                    res = R.string.set_ab_first;
                    cb_tag = Constant.DialogCallBack.SINGLE_DISTANCE_NOTICE;
                    break;
                case Constant.DialogInt.SET_AB_DISTANCE_RATIO_NOTICE:
                    res = R.string.set_ab_second;
                    cb_tag = Constant.DialogCallBack.SINGLE_DISTANCE_RATIO_NOTICE;
                    break;
            }
            final DialogSimpleNotification d = DialogSimpleNotification.newInstance(res);
            d.show(root_context.getSupportFragmentManager(), cb_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(root_context, e.toString());
        }
        //getActionBar().hide();
    }

    /**
     * @param string_id
     * @param callback_tag
     */
    protected void simple_dialog_one_button(int string_id, String callback_tag) {
        try {
            final DialogSimpleNotification d = DialogSimpleNotification.newInstance(string_id);
            d.setCancelable(false);
            d.show(root_context.getSupportFragmentManager(), callback_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(root_context, e.toString());
        }
    }

    protected void simple_dialog_one_button(String message, String callback_tag) {
        try {
            final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(message);
            dialog.show(root_context.getSupportFragmentManager(), callback_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(root_context, e.toString());
        }
    }

    protected void simple_dialog_one_button2(String message, DsingleCB cb) {
        try {
            final DialogSimpleNotification d = DialogSimpleNotification.newInstance(message, cb);
            d.show(root_context.getSupportFragmentManager(), "SINGLE"); //with this tag name
        } catch (Exception e) {
            Tool.trace(root_context, e.toString());
        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() cb_tag, which it uses to call the following methods
    // defined by the DialogCB interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        final String dtag = dialog.getTag();
        if (dtag.equalsIgnoreCase(Constant.DialogCallBack.BOOLEAN)) {
            parent_action_doyouwant();
        }
        root_context.getActionBar().show();
    }

    protected void parent_action_doyouwant() {
    }

    protected void parent_action_panel() {
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        final String dtag = dialog.getTag();
        // User touched the dialog's negative button
    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {
        final String dtag = dialog.getTag();
    }

    protected void DialogCallBackActionBar(String DTag) {
        final ActionBar ab = getActionBar();
        if (DTag == Constant.DialogCallBack.SINGLE_CLICK_HIDE_ACTIONBAR) {
            ab.hide();
        }
        if (DTag == Constant.DialogCallBack.SINGLE_CLICK_SHOW_ACTIONBAR) {
            ab.show();
        }

    }

    public void pickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);//one can be replced with any action code
    }

    public void pickPicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replced with any action code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iReturn) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = iReturn.getData();
                    // imageview.setImageURI(selectedImage);
                    Log.d(Constant.DETAG, "return result from code result_ok case 0");
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = iReturn.getData();
                    //imageview.setImageURI(selectedImage);
                    Log.d(Constant.DETAG, "return result from code result_ok case 1");

                }
                break;
            case Constant.IntentKey.CHOOSE_FILES_PATHS:
                if (resultCode == RESULT_OK) {
                    chosen_files(iReturn);

                } else {
                    Tool.trace(this, "Cancelled.");
                }
                break;
            case Constant.IntentKey.REQUEST_CODE_CHOOSE_FILE_TO_UPLOAD:
                if (resultCode == RESULT_OK) {
                    start_upload_progress(iReturn);
                } else {
                    Tool.trace(this, "Cancelled.");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, iReturn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected_id = item.getItemId();
        Intent i = null;
       /*    switch (selected_id) {
         case R.id.act_add_boundary:
                break;
            case R.id.inputImage:
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select a base map image"),
                        Constant.IntentKey.SELECT_PICTURE);
                break;

            case R.id.act_plotjs:
               *//* i = new Intent(
                        getApplicationContext(),
                        PlotJS.class);*//*
                break;
            case R.id.act_plotp:
             *//*   i = new Intent(
                        getApplicationContext(),
                        PlotNative.class);*//*
                break;
            //  case R.id.load_image:

            case R.id.act_upload:
                showFileChooser();
                break;
            case R.id.act_plots:
                i = new Intent(
                        getApplicationContext(),
                        WorkPanel.class);
                break;
            case R.id.action_settings:
                i = new Intent(
                        getApplicationContext(),
                        OCPreference.class);
                break;

            default:
                Log.d(Constant.DETAG, "selected_id: " + selected_id);
                return super.onOptionsItemSelected(item);
        }*/
        if (i != null) startActivity(i);
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select an image to Upload"), Constant.IntentKey.REQUEST_CODE_CHOOSE_FILE_TO_UPLOAD);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }


    private ProgressDialog progressBar;
    private boolean mBound = false;


    private AycnInfo mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            AycnInfo.HandleBinder binder = (AycnInfo.HandleBinder) service;
            mService = binder.getService();
            mBound = true;
            binder.bindActivity(basic_panel_support.this);
            //   progress_bar_upload_data();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;
            /*
             mContext.stopService(new Intent(mContext, ServiceRemote.class));
    mContext.unbindService(mServerConn);
             */
            //  progressBar_dismiss();
        }
    };

    public void progressBar_dismiss() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    public void progressBar_dismiss(int string_id) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        simple_dialog_one_button(string_id, Constant.DialogCallBack.SINGLE_CLICK);
    }

    public void progressBar_dismiss(String str) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        simple_dialog_one_button(str, Constant.DialogCallBack.SINGLE_CLICK);
    }

    public void progressBar_dismiss(String str, DsingleCB cb) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        simple_dialog_one_button2(str, cb);
    }

    private void prepare_progress_bar() {
        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    public void progress_bar_upload_data() {
        prepare_progress_bar();
        progressBar.setMessage("Start uploading info...");
        progressBar.show();
    }

    protected void progress_bar_start(int loading_message) {
        prepare_progress_bar();
        progressBar.setMessage(root_context.getResources().getString(loading_message));
        progressBar.show();
    }

    protected void progress_bar_start_panel() {
        prepare_progress_bar();
        progressBar.setMessage("preparing the panel now...");
        progressBar.show();
    }

    protected void upload_data_start() {
        final Intent in = new Intent();
        try {
            in.putExtra("action", Constant.IntentKey.UPLOAD_PICTURE);
            in.putExtra("uri", Content.recent_uploaded_basemap_file_path.toString());
            in.setClass(this, AycnInfo.class);
            // Tool.trace(this, Content.recent_uploaded_basemap_file_path.toString());
            if (!mBound) {
                bindService(in, mConnection, BIND_AUTO_CREATE);
            }
            startService(in);
        } catch (OutOfMemoryError e) {
            Tool.trace(this, e.toString());
        } catch (Exception ex) {
            Tool.trace(this, "wrong actions");
        } finally {

        }
    }


    protected Intent chosen_files(Intent data) {
        final Uri uri = data.getData();
        return data;
    }

    protected void start_upload_progress(Intent intent) {
        // prepare for a progress bar dialog
        final Intent in = new Intent();
        final Uri uri = intent.getData();
        try {

            if (uri != null) {
                uri.toString();
                uri.getPath();
                in.putExtra("action", Constant.IntentKey.UPLOAD_PICTURE);
                in.putExtra("uri", uri.toString());
                in.setClass(this, AycnInfo.class);
                if (!mBound) {
                    bindService(in, mConnection, BIND_AUTO_CREATE);
                }
                startService(in);
            } else {
                Tool.trace(this, "Invalid Path.");
            }
        } catch (Exception ex) {
            Tool.trace(this, "Abort");
            Log.d("ABORTED", ex.toString());
        } finally {

        }
    }

    protected basic_panel_support root_context;
    protected int bitwiseFeatures;
    protected appWork ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.app_start_new, R.anim.app_start_old);

        root_context = this;
        ac = (appWork) getApplicationContext();
        bitwiseFeatures = Window.FEATURE_ACTION_BAR_OVERLAY;

        final Window window = root_context.getWindow();
        window.requestFeature(windowFeature(bitwiseFeatures));

        configActionBar(root_context.getActionBar());
        Bundle extras = root_context.getIntent().getExtras();
        loadingmain(savedInstanceState, extras);
    }

    //    private final ActionBar ab = getActionBar();
    protected void init_actionbar() {
        final ActionBar ab = root_context.getActionBar();
        final Resources ctx = root_context.getResources();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
        }
        ab.setDisplayShowTitleEnabled(false);
        ab.setBackgroundDrawable(ctx.getDrawable(R.drawable.gradient));
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.NAVIGATION_MODE_LIST);
    }


    /**
     * with the saved states
     *
     * @param states
     * @param extras
     */
    protected void loadingmain(final Bundle states, final Bundle extras) {

    }


    protected ActionBar configActionBar(final ActionBar ab) {
        if (ab != null) {
            ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            ab.setDisplayOptions(
                    ActionBar.DISPLAY_SHOW_CUSTOM,
                    ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.NAVIGATION_MODE_LIST
            );
            custom_action_bar(ab);
        }
        return ab;
    }

    protected void custom_action_bar(final ActionBar ab) {
        final LayoutInflater i = getLayoutInflater();
        View v = null;
        //===================================================================

    }

    protected int windowFeature(int options) {
        //  win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  win.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        return options;
    }

    public void view_node_list(final int row_num) {
        final Intent i = new Intent(ac, NodeList.class);
        Bundle b = new Bundle();
        b.putInt("inspect", row_num);
        i.putExtras(b);
        startActivityForResult(i, Constant.IntentKey.INTENT_ROUTE_MANAGEMENT);
    }
}
