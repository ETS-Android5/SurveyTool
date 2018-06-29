package com.hkm.oc.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hkm.datamodel.PhotoViewData;
import com.hkm.oc.R;

import java.util.List;


/**
 * Created by Hesk on 29/5/2014.
 */
public class PhotoViewAdapter extends ArrayAdapter<PhotoViewData> {
    public String TAG = "photoAA";
    private LayoutInflater mInflater;
    private Context ctx;
    private List<PhotoViewData> list;
    private int LayoutResId;

    //R.layout.single_photoview_layout
    public PhotoViewAdapter(Context context, List<PhotoViewData> values, int LayoutResId) {
        super(context, LayoutResId, values);
        this.LayoutResId = LayoutResId;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = values;
        this.ctx = context;
    }
    public PhotoViewAdapter(Context context, List<PhotoViewData> values) {
        super(context, R.layout.single_photoview_layout, values);
        this.LayoutResId = R.layout.single_photoview_layout;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = values;
        this.ctx = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(LayoutResId, parent, false);
            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            PhotoView holder = new PhotoView(ctx, convertView, position);
            holder.renderView();
            convertView.setTag(holder);
        } else {
            PhotoView recall = (PhotoView) convertView.getTag();
            recall.update(position).renderView();
            // recall.textView.setText(holder.getText());
            // convertView.setBackgroundColor(getItem(position).getBackgroundColor());
        }
        // Populate the text

        // Set the color

        return convertView;
    }

}
