package com.hkm.root.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import com.hkm.U.Tool;
import com.hkm.datamodel.DataHandler;

/**
 * Created by hesk on 10/19/13.
 */
public class DialogListPick extends RootDialog {
    final String
            notice = "Please define the actual distance between Point A and Point B.",
            title = "User Define Distance",
            label = "Measure in meter (M)";
    private EditText field_input_text;
   // private String[] provinces = new String[]{"上海", "北京", "湖南", "湖北", "海南"};

    public DialogListPick() {
        // final View DialogView = getActivity().findViewById(R.layout.dialog_layout_ab_distance);
        //  final EditText field_1 = (EditText) DialogView.findViewById(R.id.edmeter);
        //   final TextView tx = (TextView) DialogView.findViewById(R.id.text_content_scale);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        //R.array.colors_array
        builder.setItems(
                DataHandler.get_previous_labels(),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Tool.trace(ctx, "this is choosen:"+which);
                    }
                });
        builder.setCancelable(false);
        return builder.create();
    }
}
