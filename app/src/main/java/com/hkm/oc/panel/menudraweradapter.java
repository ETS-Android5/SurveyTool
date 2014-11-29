package com.hkm.oc.panel;

/**
 * Created by Hesk on 13/6/2014.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class menudraweradapter extends BaseAdapter {

    Context context;
    List<RowItem> rowItems = new ArrayList<RowItem>();

    public menudraweradapter(Context context, final List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowItem data = rowItems.get(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(data.getLayout(), null);
            RowItem display = new RowItem(data.getTitle(), data.getIcon(), data.getType());
            display.setView(convertView).renderView();
            convertView.setTag(data);
        } else {
            RowItem recall = (RowItem) convertView.getTag();
            recall.setView(convertView).renderView();
        }
        Log.d(TAG, convertView.toString());
        return convertView;
    }

    public static String TAG = "MENUDRAWERDEBUG";

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

}