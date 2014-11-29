package com.hkm.root.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hkm.datamodel.DataHandler;
import com.hkm.oc.R;


/**
 * Created by Hesk ons
 * to show one field input from a dialog
 * need to implement
 * onDialogPositiveClick,
 * onDialogNegativeClick
 * from the listener event
 */
@SuppressLint("ValidFragment")
public class DialogSetLabelNumber extends RootDialog implements View.OnClickListener {
    final String
            notice = "Please label this testing row on",
            title = "You may change the label on this field",
            label = "adding # with this button",
            remove = "remove #",
            add = "add #";
    private EditText field_input_text;
    private String button_field, letter, numeric;
    private boolean hasSharp = false;
    private activity_control activity_control_listener;
    private int current_list_i;

    @SuppressLint("ValidFragment")
    public DialogSetLabelNumber(int data_index) {
        current_list_i = data_index;
        letter = DataHandler.get_label_letter(data_index);
        numeric = DataHandler.get_label_numeric(data_index);
        //   final View DialogView = getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        //   final EditText field_1 = (EditText) DialogView.findViewById(R.id.edmeter);
        //   final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
        activity_control_listener = new activity_control() {
            @Override
            public void onPickLegend(int i) {

            }

            @Override
            public void onListChanged(int type) {

            }

        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_sharp_button && view instanceof Button) {
            Button v = (Button) view;
            if (!hasSharp) {
                hasSharp = true;
                v.setText(remove);
            } else {
                hasSharp = false;
                v.setText(add);
            }
        }
        if (view.getId() == R.id.setting_letter_button && view instanceof Button) {

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(notice)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        process_data();
                        mListener.onDialogPositiveClick(DialogSetLabelNumber.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(DialogSetLabelNumber.this);
                    }
                });
        builder.setView(get_custom_view());
        builder.setCancelable(false);
        return builder.create();
    }

    public void setLetter(String c) {
        letter = c;
    }

    public void setListChoiceListener() {

    }

    public void process_data() {
        //  field_input_text.setText();
        if (button_field.contains("#")) {
            button_field = letter + "#";
        } else {
            button_field = button_field + "#";
        }
    }

    public View get_custom_view() {
        final String currentratio = "";
        // - Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_label_setting, null);
        // - getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        field_input_text = (EditText) DialogView.findViewById(R.id.edmeter);
        //------------------------------------------------------------------------
        //
        // - final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
        //
        //------------------------------------------------------------------------
        final Button but = (Button) DialogView.findViewById(R.id.setting_sharp_button);
        but.setOnClickListener(this);
        final Button but2 = (Button) DialogView.findViewById(R.id.setting_letter_button);
        but2.setOnClickListener(this);
        final TextView bigtoptextfield = (TextView) DialogView.findViewById(R.id.text_content_dialog);
        bigtoptextfield.setText(currentratio);

        /**
         *
         * Developing custom fonts
         *
         */
        try {
            // AssetManager am = getActivity().getAssets();
            //  Typeface font = Typeface.createFromAsset(am, "ffftusj.ttf");
            // field_input_text.setTypeface(font);
        } catch (Exception e) {
            Log.d("FONT ERROR", "FONT IS NOT LOADED::" + e.toString());
        }


        return DialogView;
    }

}


