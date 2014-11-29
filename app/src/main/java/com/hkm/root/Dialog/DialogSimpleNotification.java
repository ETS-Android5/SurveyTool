package com.hkm.root.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.hkm.oc.R;

/**
 * Created by Hesk ons
 * to show no fields input from a dialog
 * need to implement onDialogNeutral from the listener event
 */
public class DialogSimpleNotification extends RootDialog {
    protected String notice = "fill message in here...";
    protected DsingleCB cb;
    protected boolean enable_response_cb = true;

    @SuppressLint("ValidFragment")
    public DialogSimpleNotification() {
    }

    @SuppressLint("ValidFragment")
    public DialogSimpleNotification(DsingleCB cb) {
        this.cb = cb;
    }

    public static DialogSimpleNotification newInstance(int resId) {
        DialogSimpleNotification newFragment = new DialogSimpleNotification();
        Bundle bundle = new Bundle();
        bundle.putInt("resId", resId);
        bundle.putBoolean("enableCB", false);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static DialogSimpleNotification newInstance(int resId, DsingleCB cb) {
        DialogSimpleNotification newFragment = new DialogSimpleNotification(cb);
        Bundle bundle = new Bundle();
        bundle.putInt("resId", resId);
        bundle.putBoolean("enableCB", true);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static DialogSimpleNotification newInstance(String notice_string, DsingleCB cb) {
        DialogSimpleNotification newFragment = new DialogSimpleNotification(cb);
        Bundle bundle = new Bundle();
        bundle.putString("notice", notice_string);
        bundle.putBoolean("enableCB", true);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static DialogSimpleNotification newInstance(String notice_string) {
        DialogSimpleNotification newFragment = new DialogSimpleNotification();
        Bundle bundle = new Bundle();
        bundle.putString("notice", notice_string);
        bundle.putBoolean("enableCB", false);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle args = getArguments();

        final int resId = args.getInt("resId", -1);
        if (resId > -1) {
            notice = ctx.getResources().getString(resId);
        } else {
            final String nstr = args.getString("notice", notice);
            notice = nstr;
        }
        final boolean enableCB = args.getBoolean("enableCB", enable_response_cb);

        builder.setMessage(notice)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (enableCB) {
                            if (cb != null) {
                                cb.oncontified(DialogSimpleNotification.this);
                            } else mListener.onDialogNeutral(DialogSimpleNotification.this);
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                });
        if (enableCB) {
            builder.setCancelable(false);
        }
        return builder.create();
    }
}
