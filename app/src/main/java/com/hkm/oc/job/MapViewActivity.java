package com.hkm.oc.job;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.hkm.oc.R;
import com.hkm.oc.pre.MapApiLayer;

/**
 * Created by Hesk on 30/5/2014.
 */
public class MapViewActivity extends MapApiLayer {
    public static String TAG = "MapViewFragment";

    //Error inflating class fragment
    @Override
    protected void postOnCreate() {
        //  Bundle b = this.getArguments();//activity_maps
        //act_googlemap
        setContentView(R.layout.activity_maps);

        final Bundle b = mFragmentActivity.getIntent().getExtras();
        try {
            float Lat = Float.parseFloat(b.getString("latitude", ""));
            float Lng = Float.parseFloat(b.getString("longitude", ""));
            currentLatLng = new LatLng(Lat, Lng);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        setUpMapIfNeeded();
       /* Lng = Float.parseFloat(b.getString("action", ""));*/
        /*
        bData.putString("action", "getloc");
        bData.putString("latitude", latitude);
        bData.putString("longitude", longitude);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        new MenuInflater(ac).inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_location:
                confirm_current_location();
                return true;
            case R.id.apply_marker_at_my_location:
                applyMyLocation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
