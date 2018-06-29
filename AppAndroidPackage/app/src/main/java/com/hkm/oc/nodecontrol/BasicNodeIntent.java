package com.hkm.oc.nodecontrol;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.root.Dialog.DialogCB;

/**
 * Created by Hesk on 19/6/2014.
 */
public class BasicNodeIntent extends ActionBarActivity implements DialogCB {
    protected BasicNodeIntent root_content;
    protected FragmentManager fm;
    protected FragmentTransaction ft;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Tool.trace(root_content, "memory is now low.. please restart the app");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.app_start_new, R.anim.app_start_old);
        setContentView(R.layout.datalist_route);
        root_content = this;
        fm = getSupportFragmentManager();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.app_end_new, R.anim.app_end_old);

    }
}
