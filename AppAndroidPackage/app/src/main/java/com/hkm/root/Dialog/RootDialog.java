package com.hkm.root.Dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;

/**
 * Created by Hesk. This is the BaseBottom part of the dialog function that needs to be extended for complete release of the Dialog Activity
 */
public class RootDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    protected DialogCB mListener;
    protected Context ctx;
  
    // Override the Fragment.onAttach() method to instantiate the DialogCB
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            ctx = getActivity();
            // Instantiate the DialogCB so we can send events to the host
            mListener = (DialogCB) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogCB");
        }
    }


}
