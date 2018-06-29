package com.hkm.oc.job.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hkm.datamodel.JobTaskData;
import com.hkm.oc.R;

import java.util.List;

/**
 * Created by Hesk on 3/6/2014.
 */
public class jobListAdapter extends ArrayAdapter<JobTaskData> {
    public String TAG = "jobsAdapterAA";
    private LayoutInflater mInflater;
    private Context ctx;
    private List<JobTaskData> list;
    private FragmentManager fm;

    public jobListAdapter(Context context, List<JobTaskData> values, FragmentManager fm) {
        super(context, R.layout.single_photoview_layout, values);
        this.mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = values;
        this.ctx = context;
        this.fm = fm;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.datalist_row_myjob, parent, false);
            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            job_row holder = new job_row(ctx, convertView, position, fm);
            convertView.setTag(holder);
        } else {
            job_row recall = (job_row) convertView.getTag();
            recall.update(position);
            // recall.textView.setText(holder.getText());
            // convertView.setBackgroundColor(getItem(position).getBackgroundColor());
        }
        // Populate the text
        // Set the color
        return convertView;
    }

}

