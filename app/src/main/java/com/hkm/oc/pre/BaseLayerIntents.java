package com.hkm.oc.pre;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.hkm.U.Content;
import com.hkm.U.Tool;
import com.hkm.datamodel.LocationMap;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.R;
import com.hkm.oc.job.MapViewActivity;
import com.hkm.oc.panel.WorkPanel;
import com.hkm.oc.wv.offline_form_line_record;
import com.hkm.oc.wv.offline_form_survey;

import static com.hkm.U.Constant.IntentKey.CAMERA_REQUEST;
import static com.hkm.U.Constant.IntentKey.CHOOSE_FILES_PATHS;
import static com.hkm.U.Constant.IntentKey.MAP_PANEL_ACTIVITY;
import static com.hkm.U.Constant.IntentKey.OPEN_TASK_FORM;

/**
 * SIMPLE BOTTOM UP LAYOUT
 */
public class BaseLayerIntents extends BaseLayerActionMode {
    private Uri imageUri;

    /**
     * PRESS SOME SPECIAL BUTTONS WILL TRIGGER THESE FUNCTIONS
     */
    protected void go_back_to_main_screen() {
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_FILES_PATHS:
                if (resultCode == RESULT_OK) {
                    chosen_files(data);
                } else {
                    Tool.trace(this, "Gallery Action Cancelled");
                }
                break;
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    chosen_return_camera_image(imageUri);
                    // retrieve the bitmap from the intent
                    break;
                } else if (resultCode == RESULT_CANCELED) {
                    Tool.trace(this, "Camera Action Cancelled");
                }
            case OPEN_TASK_FORM:
                on_check_form_return();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void on_check_form_return() {
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

    protected void chosen_return_camera_image(Uri dataUri) {
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

    protected void pickFileImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String title = getResources().getString(R.string.title_add_photo);
        try {
            mFragmentActivity.startActivityForResult(Intent.createChooser(intent, title), CHOOSE_FILES_PATHS);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    protected void FromCamera() {
        String title = getResources().getString(R.string.content_value_title);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        //values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //  File f = new File(Constant.AppBasePathPicture + EQPool.shortUUID() + ".jpg");
        Intent pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //  pictureActionIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(pictureActionIntent, CAMERA_REQUEST);
    }

    /**
     * starting the new Map Plan in here
     *
     * @param locamap_info
     */
    protected void start_plan(final LocationMap locamap_info) {
        Content.current_job_task.addSketchMap(locamap_info);
        Intent slnt = new Intent(mFragmentActivity, WorkPanel.class);
        mFragmentActivity.startActivityForResult(slnt, MAP_PANEL_ACTIVITY);
    }

    /**
     * continue with the previous sketching map
     *
     * @param skmap
     */
    protected void start_plan(final SketchMapData skmap) {
        Content.current_sketch_map = skmap;
        Intent slnt = new Intent(mFragmentActivity, WorkPanel.class);
        mFragmentActivity.startActivityForResult(slnt, MAP_PANEL_ACTIVITY);
    }

    protected void start_form(int formnum) {
        Intent slnt = null;
        if (formnum == 1) {
            slnt = new Intent(mFragmentActivity, offline_form_line_record.class);
        }
        if (formnum == 2) {
            slnt = new Intent(mFragmentActivity, offline_form_survey.class);
        }
        if (slnt != null)
            mFragmentActivity.startActivityForResult(slnt, OPEN_TASK_FORM);
        else {
            Tool.trace(mFragmentActivity, "This form is not supported.");
        }
    }

    protected void intentMapView(PointF marker) {
        //String latitude, String longitude
        final Intent sIntent = new Intent(getApplicationContext(), MapViewActivity.class);
        Bundle bData = new Bundle();
        bData.putString("action", "getloc");
        bData.putString("latitude", String.valueOf(marker.x));
        bData.putString("longitude", String.valueOf(marker.y));
        sIntent.putExtras(bData);
        mFragmentActivity.startActivityForResult(sIntent, MAP_PANEL_ACTIVITY, bData);
    }

    protected void intentMapView() {
        //String latitude, String longitude
        final Intent sIntent = new Intent(getApplicationContext(), MapViewActivity.class);
        Bundle bData = new Bundle();
        bData.putString("action", "getloc");
        bData.putString("latitude", "22.3319402");
        bData.putString("longitude", "114.1731891");
        sIntent.putExtras(bData);
        mFragmentActivity.startActivityForResult(sIntent, MAP_PANEL_ACTIVITY, bData);
    }
}
