package com.hkm.oc.job;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hkm.Application.appWork;
import com.hkm.U.Constant;
import com.hkm.U.Tool;
import com.hkm.datamodel.PhotoViewData;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.R;
import com.hkm.oc.job.adapter.DrawMapAdapter;
import com.hkm.oc.job.adapter.DrawMapFragment;
import com.hkm.oc.job.adapter.PhotoView;
import com.hkm.oc.job.adapter.PhotoViewAdapter;
import com.hkm.oc.job.adapter.RetrieveView;
import com.hkm.oc.job.adapter.RetrieveViewAdapter;
import com.hkm.oc.pre.BaseLayerAppearance;
import com.hkm.oc.preF.transitions.DepthViewPagerTransformer;
import com.hkm.root.Dialog.DCBBool;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.hkm.root.Dialog.DsingleCB;
import com.hkm.root.Tasks.completecb;
import com.hkm.root.Tasks.loadtask;
import com.hkm.root.Tasks.savetask;
import com.hkm.root.Tasks.taskcb;
import com.hkm.root.Tasks.upload_data;
import com.hkm.root.Tasks.upload_verification;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import static com.hkm.U.Constant.IntentKey.MAP_PANEL_ACTIVITY;
import static com.hkm.U.Constant.IntentKey.WPP_INTENT;
import static com.hkm.U.Content.current_job_task;

/**
 * Created by hesk on 5/27/2014.
 */
public class JobTaskActivity extends BaseLayerAppearance implements View.OnClickListener {
    public static String TAG = "ListActivities";
    protected ActionBar mactionbar;
    private ImageButton ib_camera, ib_loc, ib_wipp, ib_plan, submit_area, form1, form2;
    private PhotoViewAdapter PVadapter;
    private RetrieveViewAdapter PVRetrieveAdapter;
    private DrawMapAdapter mmapadp;
    private TextView jobid, projectid, address;
    private ViewPager mhlv_basemap_fliper;
    private Button button1, button2, button3, button4, button5, button6;
    private TwoWayView mhlv_wip_photos2, twowaylistview_retrieved_images;
    private SketchMapData loaded_from_vp;
    private PageListener pageListener;
    private View empty_draw_map, empty_related_images, base_map_viewpager, twowaylistview_frame, site_photo_frame, empty_site_photo;

    //http://loopj.com/android-async-http/
    public JobTaskActivity() {

    }

    @Override
    protected void postOnCreate() {
        setup_display_data();
    }

    private void setup_view() {

        mFragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String mtitle = mFragmentActivity.getResources().getString(R.string.app_name_jobtask);
                getSupportActionBar().setTitle(mtitle + " " + current_job_task.getJID());
                mFragmentActivity.setContentView(R.layout.job_detail_activity);
                //Get references buttons
                ib_loc = (ImageButton) findViewById(R.id.bn_pin_icon);
                ib_wipp = (ImageButton) findViewById(R.id.bn_add_photo);
                // ib_plan = (ImageButton) findViewById(R.id.bn_add_plan);
                // Get references to UI widgets
                mhlv_wip_photos2 = (TwoWayView) findViewById(R.id.twowayview_list);
                twowaylistview_retrieved_images = (TwoWayView) findViewById(R.id.twowaylistview_retrieved_images);
                mhlv_basemap_fliper = (ViewPager) findViewById(R.id.pager);
                jobid = (TextView) findViewById(R.id.ff_jobid);
                projectid = (TextView) findViewById(R.id.ff_projectid);
                address = (TextView) findViewById(R.id.ff_address);
                button2 = (Button) findViewById(R.id.goto_last);
                button1 = (Button) findViewById(R.id.goto_first);
                button3 = (Button) findViewById(R.id.edit_map);
                button4 = (Button) findViewById(R.id.start_map);
                // button5 = (Button) findViewById(R.id.empty_related_images_button);
                button6 = (Button) findViewById(R.id.submit_bottom);
                address = (TextView) findViewById(R.id.ff_address);
                base_map_viewpager = findViewById(R.id.base_map_viewpager);
                empty_related_images = findViewById(R.id.empty_related_images);
                empty_draw_map = findViewById(R.id.empty_draw_map);
                twowaylistview_frame = findViewById(R.id.twowaylistview_frame);

                site_photo_frame = findViewById(R.id.site_photo_frame);
                empty_site_photo = findViewById(R.id.empty_site_photo);
                submit_area = (ImageButton) findViewById(R.id.submit_area);
                form1 = (ImageButton) findViewById(R.id.form_1);
                form2 = (ImageButton) findViewById(R.id.form_2);
                ib_camera = (ImageButton) findViewById(R.id.bn_add_camera);
                form1.setOnClickListener((JobTaskActivity) mFragmentActivity);
                form2.setOnClickListener((JobTaskActivity) mFragmentActivity);
                ib_wipp.setOnClickListener((JobTaskActivity) mFragmentActivity);
                ib_loc.setOnClickListener((JobTaskActivity) mFragmentActivity);
                button1.setOnClickListener((JobTaskActivity) mFragmentActivity);
                button2.setOnClickListener((JobTaskActivity) mFragmentActivity);
                button3.setOnClickListener((JobTaskActivity) mFragmentActivity);
                button4.setOnClickListener((JobTaskActivity) mFragmentActivity);
                //  button5.setOnClickListener((JobTaskActivity) mFragmentActivity);
                submit_area.setOnClickListener((JobTaskActivity) mFragmentActivity);
                ib_camera.setOnClickListener((JobTaskActivity) mFragmentActivity);
                button6.setOnClickListener((JobTaskActivity) mFragmentActivity);
                process_start();
                on_check_form_return();
            }
        });
    }

    protected void on_check_form_return() {
        if (current_job_task.working_customer_survey_complete) {
            form2.setEnabled(false);
            Picasso.with(mFragmentActivity).load(R.drawable.ic_check_dark).into(form2);
        } else {
            form2.setEnabled(true);
        }

        if (current_job_task.working_form_line_record_complete) {
            form1.setEnabled(false);
            Picasso.with(mFragmentActivity).load(R.drawable.ic_check_dark).into(form1);
        } else {
            form1.setEnabled(true);
        }

    }

    protected void process_start() {
        Log.d(TAG, current_job_task.get_list_location_map().toString());
        Log.d(TAG, "start from the line now");
        jobid.setText(current_job_task.getJID());
        projectid.setText(current_job_task.getProjectID());
        address.setText(current_job_task.getAddress());

        // discover retrieved image listing
        if (current_job_task.get_list_location_map().size() > 0) {
            PVRetrieveAdapter = new RetrieveViewAdapter(JobTaskActivity.this, current_job_task.get_list_location_map());
            twowaylistview_retrieved_images.setAdapter(PVRetrieveAdapter);
            twowaylistview_retrieved_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
                    RetrieveView ph = (RetrieveView) child.getTag();
                    ph.onEdit();
                }
            });
            empty_related_images.setVisibility(View.GONE);
            twowaylistview_frame.setVisibility(View.VISIBLE);
        } else {
            empty_related_images.setVisibility(View.VISIBLE);
            twowaylistview_frame.setVisibility(View.GONE);
        }

        // discover draw map listing
        if (current_job_task.getSketchMapList().size() > 0) {
            mmapadp = new DrawMapAdapter(mFragmentActivity.getFragmentManager());
            mhlv_basemap_fliper.setAdapter(mmapadp);
            pageListener = new PageListener(button3, button4, mmapadp);
            mhlv_basemap_fliper.setOnPageChangeListener(pageListener);
            mhlv_basemap_fliper.setPageTransformer(true, new DepthViewPagerTransformer());
            mhlv_basemap_fliper.setCurrentItem(0, true);
            base_map_viewpager.setVisibility(View.VISIBLE);
            empty_draw_map.setVisibility(View.GONE);
        } else {
            base_map_viewpager.setVisibility(View.GONE);
            empty_draw_map.setVisibility(View.VISIBLE);
        }

        // discover the work in progress photos
        if (current_job_task.getPhotoList().size() > 0) {
            PVadapter = new PhotoViewAdapter(JobTaskActivity.this, current_job_task.getPhotoList());
            mhlv_wip_photos2.setAdapter(PVadapter);
            mhlv_wip_photos2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
                    String t = String.format(getResources().getString(R.string.editingitemon), position);
                    Tool.trace(mFragmentActivity, t);
                    PhotoView ph = (PhotoView) child.getTag();
                    ph.onEdit();
                    // return true;
                }
            });
            mhlv_wip_photos2.setItemMargin(3);
            empty_site_photo.setVisibility(View.GONE);
            site_photo_frame.setVisibility(View.VISIBLE);
        } else {
            empty_site_photo.setVisibility(View.VISIBLE);
            site_photo_frame.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view) {
        final int ID = view.getId();
        switch (ID) {
            case R.id.bn_add_camera:
                FromCamera();
                return;
            case R.id.bn_add_photo:
                pickFileImage();
                return;
            case R.id.bn_pin_icon:
                if (current_job_task.GPS_ready()) {
                    intentMapView(current_job_task.getGPS());
                } else {
                    intentMapView();
                }
                return;
            case R.id.goto_first:
                mhlv_basemap_fliper.setCurrentItem(0);
                return;
            case R.id.goto_last:
                int c = current_job_task.getTotalMaps() - 1 > -1 ? current_job_task.getTotalMaps() : 0;
                mhlv_basemap_fliper.setCurrentItem(c);
                return;
            case R.id.start_map:
                start_plan(current_job_task.get_list_location_map().get(mhlv_basemap_fliper.getCurrentItem()));
                return;
            case R.id.edit_map:
                start_plan(pageListener.getData());
                return;
            case R.id.submit_bottom:
                verify_completion();
                return;
            case R.id.submit_area:
                verify_completion();
                return;
            case R.id.form_2:
                start_form(2);
                return;
            case R.id.form_1:
                start_form(1);
                return;
            default:
                return;
        }
    }

    protected void verify_completion() {
        new upload_verification(new completecb() {
            @Override
            public void pre() {
                progress_bar_start(R.string.check_data_for_submission);
            }

            @Override
            public void cbsuccess(String d) {
                progressBar_dismiss(R.string.check_cb_submission_success, new DCBBool() {
                    @Override
                    public void onyes(DialogInterface dialog) {
                        new upload_data(new completecb() {
                            @Override
                            public void pre() {
                                progress_bar_start("uploading now...");
                            }

                            @Override
                            public void cbsuccess(final String d) {
                                //todo when the upload is success
                                DialogSimpleNotification dh = DialogSimpleNotification.newInstance("upload is success", new DsingleCB() {
                                    @Override
                                    public void oncontified(DialogFragment dialog) {
                                        DialogSimpleNotification dhd = DialogSimpleNotification.newInstance(d);
                                        dhd.show(mFragmentActivity.getSupportFragmentManager(), null);
                                    }
                                });
                                dh.show(mFragmentActivity.getSupportFragmentManager(), null);

                                progressBar_dismiss();
                            }

                            @Override
                            public void cbfailure(final String d) {
                                //todo when the upload is not success

                                DialogSimpleNotification dh = DialogSimpleNotification.newInstance("upload is not success", new DsingleCB() {
                                    @Override
                                    public void oncontified(DialogFragment dialog) {
                                        DialogSimpleNotification dhd = DialogSimpleNotification.newInstance(d);
                                        dhd.show(mFragmentActivity.getSupportFragmentManager(), null);
                                    }
                                });
                                dh.show(mFragmentActivity.getSupportFragmentManager(), null);

                                progressBar_dismiss();
                            }
                        }, ac).execute();
                    }

                    @Override
                    public void onno(DialogInterface dialog) {

                    }
                });
            }

            @Override
            public void cbfailure(String d) {
                progressBar_dismiss(d);
            }
        }, ac).execute();
    }

    @Override
    protected Intent chosen_files(Intent d) {
        Intent ds = super.chosen_files(d);
        Log.d(TAG, "found files for uploads");
        final Uri uri = ds.getData();
        //  Content.wip_photos_uris.add(uri);
        current_job_task.addPhotoViewData(new PhotoViewData("description here", uri));
        if (PVadapter != null)
            PVadapter.notifyDataSetChanged();
        else {
            process_start();
        }
        return ds;
    }

    @Override
    protected void chosen_return_camera_image(Uri dataUri) {
        try {
            // Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), dataUri);
            //imgView.setImageBitmap(thumbnail);
            //imageurl = getRealPathFromURI(imageUri);
            current_job_task.addPhotoViewData(new PhotoViewData("new camera photo", dataUri));
            if (PVadapter != null)
                PVadapter.notifyDataSetChanged();
            else {
                process_start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent_return) {
        switch (requestCode) {
            case WPP_INTENT:
                if (resultCode == RESULT_CANCELED) {
                    PVadapter.notifyDataSetChanged();
                }
                if (resultCode == Constant.IntentAction.REMOVE) {
                    int pos = intent_return.getExtras().getInt("remove_pos", -1);
                    if (pos > -1) {
                        current_job_task.getPhotoList().remove(pos);
                        PVadapter.notifyDataSetChanged();
                    }
                }
                break;
            case MAP_PANEL_ACTIVITY:
                if (resultCode == RESULT_CANCELED) {

                }
                if (resultCode == Constant.IntentAction.UPDATE) {
                    int pos = intent_return.getExtras().getInt("update_attachment_Id", -1);
                    if (pos > -1) {
                        pageListener.onPageSelected(mhlv_basemap_fliper.getCurrentItem());
                    }
                }
                break;
            case Constant.IntentKey.OPEN_RETRIEVED_IMAGES:
                if (resultCode == RESULT_CANCELED) {

                }
                if (resultCode == Constant.IntentAction.UPDATE) {
                    if (mmapadp != null)
                        mmapadp.notifyDataSetChanged();
                    else {
                        process_start();
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, intent_return);
    }


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
                            progressBar_dismiss(d);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_task, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    private void savedatatask(final boolean with_exit) {
        progress_bar_start(R.string.save_data);
        new savetask(new taskcb() {
            @Override
            public void cbsuccess(String d) {
                progressBar_dismiss();
                if (ac.IsDebug()) {
                    String df = ac.getRef(appWork.TASK_JSON + current_job_task.getJID());
                    DialogSimpleNotification dm = DialogSimpleNotification.newInstance(df, new DsingleCB() {
                        @Override
                        public void oncontified(DialogFragment dialog) {
                            if (with_exit) {
                                go_back_to_main_screen();
                            }
                        }
                    });
                    dm.show(mFragmentActivity.getSupportFragmentManager(), null);
                } else {
                    if (with_exit) {
                        go_back_to_main_screen();
                    }
                }
            }

            @Override
            public void cbfailure(String d) {
                progressBar_dismiss();
                DialogSimpleNotification dm = DialogSimpleNotification.newInstance(d, new DsingleCB() {
                    @Override
                    public void oncontified(DialogFragment dialog) {
                        if (with_exit) {
                            go_back_to_main_screen();
                        }
                    }
                });
                dm.show(mFragmentActivity.getSupportFragmentManager(), null);
            }
        }, ac).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                getActionBar().setHomeButtonEnabled(false);
                savedatatask(true);
                return true;
            case R.id.action_save_data:
                savedatatask(false);
                return true;
        }
        return false;
    }

    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        /*
           private ViewPager mp;
           private int currentPage;
        */
        private Button b1, b2;
        private SketchMapData smap;
        private DrawMapFragment fmap;
        private DrawMapAdapter adapter;
        private boolean doToggle;

        public PageListener(Button b1, Button b2, DrawMapAdapter adapter) {
            //edit
            this.b1 = b1;
            //start new
            this.b2 = b2;
            this.adapter = adapter;

            this.doToggle = false;
            //this setting is for non jumping buttons toggle
            if (!doToggle) {
                this.b2.setVisibility(View.GONE);
                this.b1.setVisibility(View.VISIBLE);
            }
            this.smap = current_job_task.getSketchMapList().get(0);
            this.fmap = (DrawMapFragment) adapter.getItem(0);
        }

        public SketchMapData getData() {
            return smap;
        }

        public void onPageSelected(final int position) {
            if (doToggle) {
                try {
                    smap = current_job_task.findLocalWorkBaseMapByPager(position);
                    this.b2.setVisibility(View.GONE);
                    this.b1.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    this.b1.setVisibility(View.GONE);
                    this.b2.setVisibility(View.VISIBLE);
                    smap = null;
                    e.printStackTrace();
                }
            } else
                smap = current_job_task.getSketchMapList().get(position);
            fmap = (DrawMapFragment) adapter.getItem(position);
        }
    }

}
