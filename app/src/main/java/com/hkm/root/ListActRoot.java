package com.hkm.root;

import android.app.ListActivity;
import android.support.v4.app.DialogFragment;

import com.hkm.root.Dialog.DialogCB;
import com.hkm.root.Dialog.DialogMeterReport;

/**
 * Created by Hesk ons
 */
public class ListActRoot extends ListActivity implements DialogCB {
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        try {
            DialogMeterReport dialog = new DialogMeterReport();
            //    dialog.show(getFragmentManager(), "Mette");
        } catch (Exception e) {
            // trace_msg(e.toString());
        }
        getActionBar().hide();
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the DialogCB interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        final String tag = dialog.getTag();
        // User touched the dialog's positive button
        // trace_msg(t);
        getActionBar().show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        final String tag = dialog.getTag();
        // User touched the dialog's negative button
        getActionBar().show();

    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {
        final String tag = dialog.getTag();
        getActionBar().show();
    }


}
