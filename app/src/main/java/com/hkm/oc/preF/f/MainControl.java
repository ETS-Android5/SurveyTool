package com.hkm.oc.preF.f;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hkm.U.Constant;
import com.hkm.oc.R;

/**
 * Created by hesk on 7/26/13.
 */
@SuppressLint("ValidFragment")
public class MainControl extends Fragment {

    public static  int xml_layout;

    @SuppressLint("ValidFragment")
    public MainControl(int xml_layout_id) {
        xml_layout = xml_layout_id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(xml_layout, container, false);
        TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        dummyTextView.setText(Integer.toString(getArguments().getInt(Constant.ARG_SECTION_NUMBER)));
        return rootView;
    }
}
