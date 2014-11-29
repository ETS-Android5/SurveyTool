package com.hkm.root.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hkm.oc.R;
import com.hkm.U.Tool;

/**
 * Created by hesk on 5/31/2014.
 */
public class DialogLabelDescription extends RootDialog implements DialogInterface.OnClickListener {


    public DialogLabelDescription() {

    }

  /*  @SuppressLint("ValidFragment")
    public DialogLabelDescription(String preEnteredInformation) {
        this.information = preEnteredInformation;
    }*/

    public static DialogLabelDescription newInstance(String preEnteredInformation, String display_string) {
        DialogLabelDescription newFragment = new DialogLabelDescription();
        Bundle bundle = new Bundle();
        bundle.putString("info", preEnteredInformation);
        bundle.putString("dstr", display_string);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private String information = "";
    private String displaytext = "";

    public View get_custom_view() {
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View DialogView = inflater.inflate(R.layout.dialog_layout_edittext, null);
        field_1 = (TextView) DialogView.findViewById(R.id.displaytext);
        field_2 = (EditText) DialogView.findViewById(R.id.editText);

        Bundle args = getArguments();
        information = args.getString("info", information);
        displaytext = args.getString("dstr", displaytext);

        field_2.setText(information);
        field_2.requestFocus();
        Tool.showKeyBoard(getActivity(), field_2);

        field_1.setText(displaytext);

        return DialogView;
    }

    private TextView field_1;
    private EditText field_2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setIcon(R.drawable.ic_ruler_icon)
                .setTitle(R.string.caption_here)
                .setPositiveButton(R.string.ok, this)
            //    .setNeutralButton(R.string.clear, this)
                .setNegativeButton(R.string.cancel, this)
                .setView(get_custom_view())
                .setCancelable(false);
        return builder.create();
    }

    public void clearField() {
        field_2.setText("");
    }

    public String getInformationEntered() {
        return field_2.getText().toString();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                // Send the positive button event back to the host activity
                mListener.onDialogPositiveClick(DialogLabelDescription.this);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // Send the positive button event back to the host activity
                // mListener.onDialogNegativeClick(DialogLabelDescription.this);
                Tool.hideKeyBoard(getActivity(), field_2);
                dismiss();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                // Send the positive button event back to the host activity
                // mListener.onDialogNeutral(DialogLabelDescription.this);
                clearField();
                break;
            default:
                break;
        }
    }
}
