package com.hkm.root.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hkm.oc.R;

/**
 * Created by Hesk on
 */
public class DialogEnterPairDistance extends RootDialog {


    final String
            notice = "Please enter measurements for distance of Point A and the distance of Point B.",
            title = "User Define Distance";

    public DialogEnterPairDistance() {
        // final View DialogView = getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        //  final EditText field_1 = (EditText) DialogView.findViewById(R.id.edmeter);
        //   final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
    }

    public View get_custom_view() {
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_layout_new_test, null);
        final EditText field_1 = (EditText) DialogView.findViewById(R.id.cc1);
        final EditText field_2 = (EditText) DialogView.findViewById(R.id.cc2);
        return DialogView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(notice)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(DialogEnterPairDistance.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogPositiveClick(DialogEnterPairDistance.this);
                    }
                });
        builder.setView(get_custom_view());
        builder.setCancelable(false);
        return builder.create();
    }

}
