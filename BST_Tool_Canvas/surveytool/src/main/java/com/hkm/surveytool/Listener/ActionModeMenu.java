package com.hkm.surveytool.Listener;

import android.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

public interface ActionModeMenu {

    void set_status_dimension(double h, double w);

    void setUpActionModeMenu(final FragmentManager mfragmentgr, TextView th, TextView tw);

    void onClick(View w);


    /*

     public void set_status_dimension(double h, double w) {


     th.setText(String.format("%.2fm", EQPool.p2m((float) Math.abs(h))));
     tw.setText(String.format("%.2fm", EQPool.p2m((float) Math.abs(w))));


     }
     */
}
