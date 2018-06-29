package com.hkm.oc.panel.actionmode.module;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.root.Dialog.DialogCB;
import com.hkm.root.Dialog.DialogDoYouWantTo;
import com.hkm.root.Dialog.DialogSimpleNotification;

/**
 * Created by hesk on 6/1/2014.
 */
public class basebottom extends FragmentActivity implements DialogCB {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_menu, menu);
        return true;
    }

    protected void initDPindex() {
        if (Content.dpi == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            super.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Content.dpi = dm.densityDpi;
        }
    }

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
                case Constant.DialogInt.DO_NEED_TO_UPLOAD:
                    res = R.string.question_upload;
                    cb_tag = Constant.DialogCallBack.DO_UPLOAD;
                    break;
                case Constant.DialogInt.DO_NEED_TO_QUIT_SB_INCOMPLETE:
                    res = R.string.question_sb_quit;
                    cb_tag = Constant.DialogCallBack.DO_SB;
                    break;
            }
            final String message = getResources().getString(res);
            final DialogDoYouWantTo dialog = new DialogDoYouWantTo(message);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), cb_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(this, e.toString());
        }
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

            }
            final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(res);
            dialog.show(getSupportFragmentManager(), cb_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(this, e.toString());
        }
        //getActionBar().hide();
    }

    /**
     * @param string_id
     * @param callback_tag
     */
    protected void simple_dialog_one_button(int string_id, String callback_tag) {
        final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(string_id);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), callback_tag); //with this tag name

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
        getActionBar().show();
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
       /* switch (selected_id) {
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
            case R.id.act_quit:
                quit();
                break;
            default:
                Log.d(Constant.DETAG, "selected_id: " + selected_id);
                return super.onOptionsItemSelected(item);
        }*/
        if (i != null) startActivity(i);
        return true;
    }

    protected void quit() {

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


    private Aycninfoservice mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            Aycninfoservice.HandleBinder binder = (Aycninfoservice.HandleBinder) service;
            mService = binder.getService();
            mBound = true;
            binder.bindActivity(basebottom.this);
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
        DialogSimpleNotification d = DialogSimpleNotification.newInstance(str);
        d.show(getSupportFragmentManager(), null);
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
            in.setClass(this, Aycninfoservice.class);
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
                in.setClass(this, Aycninfoservice.class);
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

}
