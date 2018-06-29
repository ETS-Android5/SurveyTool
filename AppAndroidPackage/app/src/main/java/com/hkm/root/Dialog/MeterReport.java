package com.hkm.root.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hkm.U.Content;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;

/**
 * Created by Hesk on
 */

/**
 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
 */
@SuppressLint("ValidFragment")
public class MeterReport extends DialogFragment {

    final String
            notice = "Please define the actual distance between Point A and Point B.",
            title = "User Define Distance";
    Context ctx;

    @SuppressLint("ValidFragment")
    public MeterReport(Context x) {
        ctx = x;
    }

    public static MeterReport newInstance(Context x) {
        // fm.saveFragmentInstanceState(this);
        MeterReport f = new MeterReport(x);
        Bundle args = new Bundle();
        args.putString("title", "");
        f.setArguments(args);
        //  this.onCreateDialog(getArguments());
        return f;
    }

    public void display_dialog() {
        Dialog h = this.onCreateDialog(getArguments());
        h.show();
    }

    public void on_click_pos() {
        final View DialogView = getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        final EditText field_1 = (EditText) DialogView.findViewById(R.id.edmeter);

        try {
            final float c = EQPool.measure_distance_ratio(
                    Content.current_sketch_map.get_point_a(),
                    Content.current_sketch_map.get_point_b(),
                    Float.parseFloat(field_1.getText().toString()));
            if (c > 1) {
                Content.current_sketch_map.setRatio(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void on_click_neg() {

    }

    public void on_click_mid() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View DialogView = getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        final EditText field_1 = (EditText) DialogView.findViewById(R.id.edmeter);
        final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
        tx.setText(notice);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            tx.setTextColor(Color.WHITE);
        }

        AlertDialog.Builder df = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                        //.setMessage(notice)
                .setView(DialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // PreferencesManager.getInstance(getActivity()).setShowAbout(!checkBox.isChecked());
                        on_click_pos();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      /*  String url = "http://47deg.com";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);*/
                        on_click_neg();
                    }
                });
                /*.setNeutralButton(R.string.goToGitHub, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://github.com/47deg/android-swipelistview";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    }
                })*/
        return df.create();

    }


}
