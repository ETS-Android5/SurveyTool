package com.hkm.oc.preF.root;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.hkm.U.Constant;
import com.hkm.U.Tool;

/**
 * Created by Hesk on 12/6/2014.
 */
public class UtilFragment extends pref {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iReturn) {
        switch (requestCode) {
            case Constant.IntentKey.CHOOSE_FILES_PATHS:
                if (resultCode == RESULT_OK) {
                    chosen_files(iReturn);
                } else {
                    Tool.trace(this, "Cancelled.");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, iReturn);
    }

    /**
     * ALL RECEIVING INTENTS ARE COLLECTED IN HERE
     *
     * @param data
     * @return
     */

    protected Intent chosen_files(Intent data) {
        final Uri uri = data.getData();
        return data;
    }

    /**
     * ALL OUT GOING INTENTS ARE TRIGGERED IN HERE
     */
    protected void pickPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);//one can be replced with any action code
    }

    protected void pickPicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replced with any action code
    }

    public void pickFileImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select an image to Upload"), Constant.IntentKey.CHOOSE_FILES_PATHS);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }


    protected void intentDisplayLocation(String latitude, String longitude) {
        //  String uri = "geo:"+ latitude + "," + longitude;
        //  startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
        // You can also choose to place a point like so:
        //Hong Kong
        if (latitude.equalsIgnoreCase("")) {
            latitude = "114.1731891";
        }
        if (longitude.equalsIgnoreCase("")) {
            longitude = "22.3319402";
        }
        String t = "geo:%1$s,%2$s?q=%3$s";
        //        String uri = "geo:" + latitude + "," + longitude + "?q=my+street+address";
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(String.format(t, latitude, longitude, ""))));
    }

  /*  protected void intentMapView(String latitude, String longitude) {

        if (latitude.equalsIgnoreCase("")) {
            latitude = "114.1731891";
        }
        if (longitude.equalsIgnoreCase("")) {
            longitude = "22.3319402";
        }
        final Intent sIntent = new Intent(getApplicationContext(), MapViewActivity.class);
        Bundle bData = new Bundle();
        bData.putString("action", "getloc");
        bData.putString("latitude", latitude);
        bData.putString("longitude", longitude);
        sIntent.putExtras(bData);
        startActivity(sIntent);
    }
*/
    /**
     * PRESS SOME SPECIAL BUTTONS WILL TRIGGER THESE FUNCTIONS
     */
    protected void go_back_to_main_screen() {
        super.finish();
    }
}
