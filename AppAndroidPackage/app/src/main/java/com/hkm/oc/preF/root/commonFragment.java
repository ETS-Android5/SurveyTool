package com.hkm.oc.preF.root;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.Application.appWork;
import com.hkm.U.Constant;
import com.hkm.datamodel.SketchMapData;

/**
 * Created by Hesk on 12/6/2014.
 */
public class commonFragment extends Fragment implements View.OnClickListener {
    protected View view;
    protected appWork ac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view_start(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity mactivity) {
        super.onAttach(mactivity);
        ac = (appWork) getActivity().getApplicationContext();
    }

    /**
     * have to override this
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected View view_start(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onClick(View view) {

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


    protected void pickFileImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select an image to Upload"), Constant.IntentKey.CHOOSE_FILES_PATHS);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    protected void start_plan(final SketchMapData skmap) {

    }
}
