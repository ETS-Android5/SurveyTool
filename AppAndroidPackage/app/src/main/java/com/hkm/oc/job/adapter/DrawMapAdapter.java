package com.hkm.oc.job.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.hkm.datamodel.LocationMap;
import com.hkm.datamodel.SketchMapData;

import static com.hkm.U.Content.current_job_task;

/**
 * Created by Hesk on 9/6/2014.
 */
public class DrawMapAdapter extends FragmentPagerAdapter {
    public DrawMapAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return current_job_task.getSketchMapList().size();
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        DrawMapFragment fragment;
        try {
            //  SketchMapData sk_map = current_job_task.findLocalWorkBaseMapByPager(position);
            SketchMapData sk_map = current_job_task.getSketchMapList().get(position);
            LocationMap loc_map = current_job_task.findLocationMapByAttachmentID(sk_map.getAttachmentId());
            fragment = DrawMapFragment.newInstance(position, loc_map, sk_map);
        } catch (Exception e) {
            e.printStackTrace();
            fragment = null;
        }
        return fragment;
    }
}