package com.hkm.oc.panel;

import android.content.res.Configuration;
import android.os.Bundle;


/**
 * Created by Hesk in 2013.
 * Developed and Debugged by Hesk
 * All rights reserved
 * <p/>
 * This is the core activity on the one call client application
 * The purpose is used for constructing the graphic for the reporting basemap.
 */

public class WorkPanel extends loading {
    public static String TAG = "basemapurlworker";
    public static String TAG_REQUEST = "BASEMAPIMAGESYNC";

    @Override
    public void onDestroy() {
        ac.cancelPendingRequests(TAG_REQUEST);
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // Sync the toggle state after onRestoreInstanceState has occurred.
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }


}