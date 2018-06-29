package com.hkm.root.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.hkm.oc.R;


/**
 * Created by Hesk ons
 * to show one field input from a dialog
 * need to implement
 * onDialogPositiveClick,
 * onDialogNegativeClick
 * from the listener event
 */
public class DialogDoYouWantTo extends RootDialog {
    protected String notice = "demo message is here";
    private DCBBool doublecallback;

    public DialogDoYouWantTo() {
    }

    @SuppressLint("ValidFragment")
    public DialogDoYouWantTo(String string) {
        notice = string;
    }

    public void setNotice(String n) {
        this.notice = n;
    }

    @SuppressLint("ValidFragment")
    public DialogDoYouWantTo(DCBBool cb) {
        doublecallback = cb;
    }

    public static DialogDoYouWantTo newInstance(int resId) {
        DialogDoYouWantTo newFragment = new DialogDoYouWantTo();
        Bundle bundle = new Bundle();
        bundle.putInt("resId", resId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static DialogDoYouWantTo newInstance(int resId, DCBBool cb) {
        DialogDoYouWantTo newFragment = new DialogDoYouWantTo(cb);
        Bundle bundle = new Bundle();
        bundle.putInt("resId", resId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static DialogDoYouWantTo newInstance(String resIdString, DCBBool cb) {
        DialogDoYouWantTo newFragment = new DialogDoYouWantTo(cb);
        Bundle bundle = new Bundle();
        bundle.putInt("resId", -1);
        newFragment.setNotice(resIdString);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        int resId = args.getInt("resId", -1);
        if (resId > -1) {
            notice = ctx.getResources().getString(resId);
        }

        builder.setMessage(notice);
        if (doublecallback != null) {
            builder.setPositiveButton(R.string.myes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doublecallback.onyes(dialogInterface);
                }
            }).setNegativeButton(R.string.mno, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doublecallback.onno(dialogInterface);
                }
            });
        } else {
            builder.setPositiveButton(R.string.myes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onDialogPositiveClick(DialogDoYouWantTo.this);
                }
            }).setNegativeButton(R.string.mno, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onDialogNegativeClick(DialogDoYouWantTo.this);
                }
            });
        }
        return builder.create();
    }

}
