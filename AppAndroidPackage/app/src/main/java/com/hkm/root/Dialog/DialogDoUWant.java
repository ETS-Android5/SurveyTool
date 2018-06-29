package com.hkm.root.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.hkm.oc.R;

/**
 * Created by Hesk on 5/6/2014.
 */
public class DialogDoUWant extends RootDialog {
    protected String notice;

    private DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    private DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };



    public DialogDoUWant() {
        this.notice = "no message error";
    }


    @SuppressLint("ValidFragment")
    public DialogDoUWant(int resString, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        if (isAdded()) {
            this.notice = getActivity().getResources().getString(resString);
            this.positive = positive;
            this.negative = negative;

        }
    }


    @SuppressLint("ValidFragment")
    public DialogDoUWant(String string, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        this.notice = string;
        this.positive = positive;
        this.negative = negative;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.notice)
                .setPositiveButton(R.string.myes, positive)
                .setNegativeButton(R.string.mno, negative)
                .setCancelable(false);
        return builder.create();
    }


}
