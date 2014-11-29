package com.hkm.oc.pre;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.hkm.U.Tool;
import com.hkm.oc.R;
import com.hkm.root.Dialog.DCBBool;
import com.hkm.root.Dialog.DialogCB;
import com.hkm.root.Dialog.DialogDoYouWantTo;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.hkm.root.Dialog.DsingleCB;

/**
 * Created by hesk on 5/27/2014.
 */
public class BaseBottom extends ActionBarActivity implements DialogCB {

    protected FragmentActivity mFragmentActivity;
    private ProgressDialog progressBar;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.app_end_new, R.anim.app_end_old);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    /**
     * @param string_id
     * @param callback_tag
     */
    protected void simple_dialog_one_button(int string_id, String callback_tag) {
        try {
            final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(string_id);
            dialog.setCancelable(false);
            dialog.show(mFragmentActivity.getSupportFragmentManager(), callback_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(mFragmentActivity, e.toString());
        }
    }

    protected void simple_dialog_one_button(String message, String callback_tag) {
        try {
            final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(message);
            dialog.setCancelable(false);
            dialog.show(mFragmentActivity.getSupportFragmentManager(), callback_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(mFragmentActivity, e.toString());
        }
    }


    public void progressBar_dismiss() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    public void progressBar_dismiss(int resId) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        final DialogSimpleNotification d = DialogSimpleNotification.newInstance(resId);
        d.show(mFragmentActivity.getSupportFragmentManager(), null);
    }

    public void progressBar_dismiss(String str, DsingleCB ccb) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        final DialogSimpleNotification d = DialogSimpleNotification.newInstance(str, ccb);
        d.show(mFragmentActivity.getSupportFragmentManager(), null);
        //simple_dialog_one_button(str, Constant.DialogCallBack.SINGLE_CLICK);
    }

    public void progressBar_dismiss(String str, DCBBool ccb) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        final DialogDoYouWantTo d = DialogDoYouWantTo.newInstance(str, ccb);
        d.show(mFragmentActivity.getSupportFragmentManager(), null);
        //simple_dialog_one_button(str, Constant.DialogCallBack.SINGLE_CLICK);
    }

    public void progressBar_dismiss(int strId, DCBBool ccb) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        final DialogDoYouWantTo d = DialogDoYouWantTo.newInstance(strId, ccb);
        d.show(mFragmentActivity.getSupportFragmentManager(), null);
        //simple_dialog_one_button(str, Constant.DialogCallBack.SINGLE_CLICK);
    }

    public void progressBar_dismiss(String str) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        final DialogSimpleNotification d = DialogSimpleNotification.newInstance(str);
        d.show(mFragmentActivity.getSupportFragmentManager(), null);
        //simple_dialog_one_button(str, Constant.DialogCallBack.SINGLE_CLICK);
    }

    private void prepare_progress_bar() {
        progressBar = new ProgressDialog(mFragmentActivity);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    public void progress_bar_start(final String info) {
       /* runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        prepare_progress_bar();
        progressBar.setMessage(info);
        progressBar.show();
        /*    }
        });*/
    }

    public void progress_bar_start(final int resId) {
      /*  runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        final String n = mFragmentActivity.getResources().getString(resId);
        prepare_progress_bar();
        progressBar.setMessage(n);
        progressBar.show();
          /*  }
        });*/
    }

}
