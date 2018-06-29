package com.hkm.oc.preF.f;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hkm.U.Constant;
import com.hkm.datamodel.PhotoViewData;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.R;
import com.hkm.oc.job.adapter.DrawMapAdapter;
import com.hkm.oc.job.adapter.PhotoView;
import com.hkm.oc.job.adapter.PhotoViewAdapter;
import com.hkm.oc.preF.root.UtilFragment;
import com.hkm.oc.preF.root.commonFragment;
import com.hkm.oc.preF.transitions.DepthViewPagerTransformer;
import com.hkm.U.Tool;

import org.lucasr.twowayview.TwoWayView;

import static com.hkm.U.Content.current_job_task;

/**
 * Created by Hesk on 12/6/2014.
 */
public class jobtask extends commonFragment {
    public static String TAG = "ListActivities";
    private ImageButton ib_loc, ib_wipp, ib_plan;
    private PhotoViewAdapter PVadapter;
    private DrawMapAdapter mmapadp;
    private TextView jobid, projectid, address;
    private ViewPager mhlv_basemap_fliper;
    private Button button1, button2, button3, button4;
    private TwoWayView mhlv_wip_photos2;
    private SketchMapData loaded_from_vp;
    private PageListener pageListener;
    private UtilFragment ctx;

    //http://loopj.com/android-async-http/
    public jobtask() {
        //Get references buttons
    }

    @Override
    public void onAttach(Activity mactivity) {
        super.onAttach(mactivity);
        ctx = (UtilFragment) mactivity;
    }

    @Override
    public View view_start(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.job_detail_activity, container, false);
        ib_loc = (ImageButton) rootView.findViewById(R.id.bn_pin_icon);
        ib_wipp = (ImageButton) rootView.findViewById(R.id.bn_add_photo);
        // ib_plan = (ImageButton) findViewById(R.id.bn_add_plan);
        // Get references to UI widgets
        mhlv_wip_photos2 = (TwoWayView) rootView.findViewById(R.id.twowayview_list);
        mhlv_basemap_fliper = (ViewPager) rootView.findViewById(R.id.pager);
        jobid = (TextView) rootView.findViewById(R.id.ff_jobid);
        projectid = (TextView) rootView.findViewById(R.id.ff_projectid);
        address = (TextView) rootView.findViewById(R.id.ff_address);
        button2 = (Button) rootView.findViewById(R.id.goto_last);
        button1 = (Button) rootView.findViewById(R.id.goto_first);
        button3 = (Button) rootView.findViewById(R.id.edit_map);
        button4 = (Button) rootView.findViewById(R.id.start_map);
        ib_wipp.setOnClickListener(this);
        ib_loc.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        process_start();
        return rootView;
    }

    protected void process_start() {
        Log.d(TAG, current_job_task.get_list_location_map().toString());
        Log.d(TAG, "start from the line now");
        jobid.setText(current_job_task.getJID());
        projectid.setText(current_job_task.getProjectID());
        address.setText(current_job_task.getAddress());
        PVadapter = new PhotoViewAdapter(ctx, current_job_task.getPhotoList());
        mhlv_wip_photos2.setAdapter(PVadapter);
        mmapadp = new DrawMapAdapter(getFragmentManager());
        mhlv_basemap_fliper.setAdapter(mmapadp);
        pageListener = new PageListener(button3, button4);
        mhlv_basemap_fliper.setOnPageChangeListener(pageListener);
        mhlv_basemap_fliper.setPageTransformer(true, new DepthViewPagerTransformer());
        mhlv_basemap_fliper.setCurrentItem(1, true);
        mhlv_wip_photos2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
                String t = String.format(getResources().getString(R.string.editingitemon), position);
                Tool.trace(ctx, t);
                PhotoView ph = (PhotoView) child.getTag();
                ph.onEdit();
                // return true;
            }
        });
        mhlv_wip_photos2.setItemMargin(3);
        // Watch for button clicks.
    }

    @Override
    protected Intent chosen_files(Intent d) {
        Intent ds = super.chosen_files(d);
        Log.d(TAG, "found files for uploads");
        final Uri uri = ds.getData();
        //  Content.wip_photos_uris.add(uri);
        current_job_task.addPhotoViewData(new PhotoViewData("description here", uri));
        PVadapter.notifyDataSetChanged();
        return ds;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent_return) {
        switch (requestCode) {
            case Constant.IntentKey.WPP_INTENT:
                if (resultCode == Constant.IntentAction.REMOVE) {
                    int pos = intent_return.getExtras().getInt("remove_pos", -1);
                    if (pos > 0) {
                        current_job_task.getPhotoList().remove(pos);
                        PVadapter.notifyDataSetChanged();
                    }
                } else {
                    PVadapter.notifyDataSetChanged();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent_return);
    }


    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        /*
           private ViewPager mp;
           private int currentPage;
        */
        private Button b1, b2;
        private SketchMapData t;

        public PageListener(Button b1, Button b2) {
            //edit
            this.b1 = b1;
            //start new
            this.b2 = b2;
        }

        public SketchMapData getData() {
            return t;
        }

        public void onPageSelected(int position) {
            try {
                t = current_job_task.findLocalWorkBaseMapByPager(position);
                this.b2.setVisibility(View.GONE);
                this.b1.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                this.b1.setVisibility(View.GONE);
                this.b2.setVisibility(View.VISIBLE);
                t = null;
            }
        }
    }

    @Override
    public void onClick(View view) {
        final int ID = view.getId();
        switch (ID) {
            case R.id.bn_add_photo:
                pickFileImage();
                return;
            case R.id.bn_pin_icon:
                //  intentMapView("", "");
                return;
            case R.id.goto_first:
                mhlv_basemap_fliper.setCurrentItem(0);
                return;
            case R.id.goto_last:
                int c = current_job_task.getTotalMaps() - 1 > -1 ? current_job_task.getTotalMaps() : 0;
                mhlv_basemap_fliper.setCurrentItem(c);
                return;
            case R.id.start_map:
                start_plan(new SketchMapData());
                return;
            case R.id.edit_map:
                start_plan(pageListener.getData());
                return;
            default:
                return;
        }
    }
/*
    protected void setup_display_data() {
        final int jid = getIntent().getExtras().getInt("jobbundleid");
        if (current_job_task == null) {

            progress_bar_start(R.string.load_data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //stuff that updates ui
                    new loadtask(new taskcb() {
                        @Override
                        public void cbsuccess(String d) {
                            Log.d(TAG, "success");
                            setup_view();
                            //stuff that updates ui
                            progressBar_dismiss();

                        }

                        @Override
                        public void cbfailure(String d) {
                            // progressBar_dismiss();
                        }
                    }, jid, ac).execute();
                }
            });

        } else {
            Log.d(TAG, "bad now");
            setup_view();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                getActionBar().setHomeButtonEnabled(false);
                progress_bar_start(R.string.save_data);
                new savetask(new taskcb() {
                    @Override
                    public void cbsuccess(String d) {
                        progressBar_dismiss();
                        finish();
                    }

                    @Override
                    public void cbfailure(String d) {
                        progressBar_dismiss();
                        finish();
                    }
                }, ac).execute();
                return true;
        }
        return false;
    }*/
}
