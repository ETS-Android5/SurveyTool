package com.hkm.root.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hkm.U.Content;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.elements.AbPointRuler;

/**
 * Created by Hesk ons
 * to show one field input from a dialog
 * need to implement
 * onDialogPositiveClick,
 * onDialogNegativeClick
 * from the listener event
 */
public class DialogMeterReport extends RootDialog {
    final String
            notice = "Please define the actual distance between Point A and Point B.",
            title = "User Define Distance",
            label = "Measure in meter (M)";
    private EditText field_input_text;

    public DialogMeterReport() {
        // final View DialogView = getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        //  final EditText field_1 = (EditText) DialogView.findViewById(R.id.edmeter);
        //   final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(notice)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(DialogMeterReport.this);
                        setRatio();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(DialogMeterReport.this);
                    }
                });
        builder.setView(get_custom_view());
        builder.setCancelable(false);
        return builder.create();
    }

    private void setRatio() {
        float meter = 0;
        try {
            String trimed = field_input_text.getText().toString().trim();
            meter = Float.valueOf(trimed);
            System.out.print(meter);
            if (meter > 10f) {
                AbPointRuler.ratio_calculate(meter, Content.current_sketch_map.get_point_a(), Content.current_sketch_map.get_point_b());
            } else {
                System.out.print("cannot output a new ratio");
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public View get_custom_view() {
        final String currentratio = getResources().getString(R.string.notice_meter_dialog_part)
                + Content.current_sketch_map.getratio();
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_layout_ab_distance, null);
        //   /getActivity().findViewById(R.layout.dialog_layout_ab_distance);

        field_input_text = (EditText) DialogView.findViewById(R.id.edmeter);
        // final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
        final TextView tx2 = (TextView) DialogView.findViewById(R.id.label2text);
        final TextView tx3 = (TextView) DialogView.findViewById(R.id.text_content_dialog);
        /**
         *
         * Developing custom fonts
         *
         */
        try {
            AssetManager am = getActivity().getAssets();
            Typeface font = Typeface.createFromAsset(am, "ffftusj.ttf");
            field_input_text.setTypeface(font);
        } catch (Exception e) {
            Log.d("FONT ERROR", "FONT IS NOT LOADED::" + e.toString());
        }
        tx3.setText(currentratio);
        tx2.setText(label);
        return DialogView;
    }

}
