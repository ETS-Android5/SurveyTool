package com.hkm.root.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hkm.oc.R;

/**
 * Created by Hesk on //
 */
public class not_in_use_dialog extends DialogFragment {

    final String
            notice = "Please define the actual distance between Point A and Point B.",
            title = "User Define Distance";

    public not_in_use_dialog() {
    }

    public void on_click_pos() {

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

        return new AlertDialog.Builder(getActivity())
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
                })
                /*.setNeutralButton(R.string.goToGitHub, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://github.com/47deg/android-swipelistview";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    }
                })*/
                .create();

    }
}
