package com.hkm.oc.pre;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hkm.U.Content;
import com.hkm.U.Tool;
import com.hkm.oc.R;

/**
 * HKM All Rights reserved.
 * Created by Hesk on 24/6/2014.
 */
public class MapApiLayer extends BaseLayerAppearance
        implements GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationChangeListener {
    protected static String TAG = "dragposition";
    protected GoogleMap mMap;
    protected LatLng currentLatLng, mylocation;
    protected int mapType = GoogleMap.MAP_TYPE_NORMAL;
    protected float cameraZoomDefault = 18, cameraZoom;
    protected MarkerOptions mo;
    private Marker markerP;

    protected int windowFeature(int options) {
        //  win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  win.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        options = Window.FEATURE_ACTION_BAR | Window.FEATURE_LEFT_ICON;
        return options;
    }

    protected void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            //getExtendedMap();
            //.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        final BitmapDescriptor Bdescriptor = BitmapDescriptorFactory.fromResource(R.drawable.i_marker_inside_azure);
        mo = new MarkerOptions()
                .position(currentLatLng)
                .snippet("Job" + Content.current_job_task.getID())
                        //.title("Job" + Content.current_job_task.getID())

                .icon(Bdescriptor)
                .draggable(true);
        // .flat(true);
        //.rotation(3f);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.setMapType(mapType);
        mMap.setTrafficEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationChangeListener(this);
        //  mMap.setPadding(0, 10, 0, 0);
        mMap.setOnMarkerDragListener(this);

        if (hasSavedInstance()) {
            mapType = saved_instance.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);
            double savedLat = saved_instance.getDouble("lat");
            double savedLng = saved_instance.getDouble("lng");
            currentLatLng = new LatLng(savedLat, savedLng);
            cameraZoom = saved_instance.getFloat("zoom", cameraZoomDefault);
        } else {
            cameraZoom = cameraZoomDefault;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, cameraZoom));
        markerP = mMap.addMarker(mo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the map type so when we change orientation, the mape type can be restored
        final LatLng cameraLatLng = mMap.getCameraPosition().target;
        final float cameraZoom = mMap.getCameraPosition().zoom;
        outState.putInt("map_type", mapType);
        outState.putDouble("lat", cameraLatLng.latitude);
        outState.putDouble("lng", cameraLatLng.longitude);
        outState.putFloat("zoom", cameraZoom);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "drag start");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "on Marker start Drag");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "drag start");
        final LatLng pos = marker.getPosition();
        markerP = marker;
        Content.current_job_task.setLatLng(String.valueOf(pos.latitude), String.valueOf(pos.longitude));
        Tool.trace(this, "position marker set");
    }


    protected void confirm_current_location() {
        // LatLng pos = mMap.getCameraPosition().target;
        //  LatLng cameraLatLng = mMap.getMyLocation().
        if (markerP != null) {
            final LatLng pos = markerP.getPosition();
            Tool.trace(mFragmentActivity, "Position : " + pos.toString());
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //https://developers.google.com/maps/documentation/business/mobile/android/reference/com/google/android/m4b/maps/GoogleMap.OnMyLocationButtonClickListener
        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        mylocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    protected void applyMyLocation() {
        markerP.setPosition(mylocation);
        final LatLng pos = markerP.getPosition();
        Content.current_job_task.setLatLng(String.valueOf(pos.latitude), String.valueOf(pos.longitude));
        Tool.trace(mFragmentActivity, "position marker set");
    }
   /* @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 4);
        mMap.animateCamera(cameraUpdate);

        Marker myMarkerthirtyfour = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        locationManager.removeUpdates(this);

    }*/
}
