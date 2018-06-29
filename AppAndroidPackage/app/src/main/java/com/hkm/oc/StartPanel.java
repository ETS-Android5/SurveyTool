package com.hkm.oc;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hkm.Application.OCPreference;
import com.hkm.Application.appWork;
import com.hkm.U.Tool;
import com.hkm.api.handlers.PicaAutoBudgetTransform;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.job.JobListActivity;
import com.hkm.oc.job.JobTaskActivity;
import com.hkm.root.Dialog.DialogCB;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.nvanbenschoten.motion.ParallaxImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.hkm.U.Content.current_job_task;

/**
 * Created by hesk on 3/10/14.
 */
public class StartPanel extends FragmentActivity implements View.OnClickListener,DialogCB {
    private static appWork ac;
    private final Handler mHandler = new Handler();
    private TextView online_status, version;
    private Button preference_btn, record_plan, jobtask_btn, myaccoun;
    private ParallaxImageView background;
    private int million_sec;
    private Timer timerThread;
    private FragmentActivity mRootContext;

    private String getVer() {
        PackageInfo pInfo = null;
        String version = "";

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ac = (appWork) getApplicationContext();
        mRootContext = this;
        try {
            if (timerThread != null) timerThread.purge();
            //measure_button -------------------
            setContentView(R.layout.start_panel);
            version = (TextView) findViewById(R.id.version_corner);
            online_status = (TextView) findViewById(R.id.online_status);
            preference_btn = (Button) findViewById(R.id.setting_button);
            record_plan = (Button) findViewById(R.id.measure_button);
            jobtask_btn = (Button) findViewById(R.id.job_task_button);
            background = (ParallaxImageView) findViewById(R.id.mapimg);
            //  myaccoun = (Button) findViewById(R.id.my_account);
            version.setText("v" + getVer());
            preference_btn.setOnClickListener(this);
            record_plan.setOnClickListener(this);
            // myaccoun.setOnClickListener(this);
            dynamicButton();
            // ----------------------------------
            simple_timer(5000);
            background.registerSensorManager();
            // ----------------------------------
        } catch (Exception e) {
            e.printStackTrace();
            Tool.trace(this, "onCreate problem incurred " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iReturn) {
        if (resultCode == RESULT_OK) {
            dynamicButton();
        }
        super.onActivityResult(requestCode, resultCode, iReturn);
    }

    private void dynamicButton() {
        final int id = ac.getRefInt(appWork.TASK_ID);
        final String t = ac.getRef(appWork.TASK_JSON + id);
        if (id > 0 && !t.equalsIgnoreCase("") || current_job_task != null) {
            jobtask_btn.setOnClickListener(this);
            jobtask_btn.setVisibility(View.VISIBLE);
            jobtask_btn.setText("Continue my Job ID \n [" + id + "]");
        } else {
            jobtask_btn.setVisibility(View.GONE);
        }
    }

    private void refreshBg() {
        if (current_job_task != null) {
            ArrayList<SketchMapData> mlist = current_job_task.getSketchMapList();
            if (mlist != null) {
                for (SketchMapData m : mlist) {
                    if (m.hasUri()) {
                        Picasso.with(ac).load(m.get_draw_map_uri()).transform(new PicaAutoBudgetTransform(600, true)).into(background);
                        break;
                    }
                }
            }
        }
    }

    private void run_check_online_status() {
        //setting_button
        appWork appcontrol = (appWork) getApplicationContext();
        if (appcontrol.isNetworkOnline()) {
            online_status.setText(R.string.status_online);
            online_status.setTextColor(getResources().getColor(R.color.holo_online));
        } else {
            online_status.setText(R.string.status_offline);
            online_status.setTextColor(getResources().getColor(R.color.holo_offline));
        }
        refreshBg();
    }

    /**
     * the oldest way to implement a timer with a new thread
     *
     * @param million_second
     */
    private void init_exe_interval(int million_second) {
        million_sec = million_second;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(million_sec);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Write your code here to update the UI.
                                run_check_online_status();
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    /**
     * a better way to implement the timer out of the UI thread
     */
    private void simple_timer(int period) {
        int delay = 1000; // delay for 1 sec.
        //int period = 10000; // repeat every 10 sec.
        timerThread = new Timer();
        timerThread.purge();
        timerThread.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        run_check_online_status();  // display the data
                    }
                });
            }
        }, delay, period);
    }

    @Override
    public void onClick(View v) {
        Intent slnt;
        Bundle sDnt;
        switch (v.getId()) {
            case R.id.setting_button:
                slnt = new Intent(getApplicationContext(), OCPreference.class);
                startActivity(slnt);
                return;
       /*     case R.id.my_account:
                slnt = new Intent(getApplicationContext(), webConnect.class);
                startActivity(slnt);
                return;*/
            //the job list
            case R.id.measure_button:
                if (ac.isNetworkOnline()) {
                    slnt = new Intent(getApplicationContext(), JobListActivity.class);
                    startActivityForResult(slnt, 1941);
                } else {
                    try {
                        DialogSimpleNotification dc = DialogSimpleNotification.newInstance(R.string.notice_no_network);
                        dc.show(mRootContext.getSupportFragmentManager(), null);
                    } catch (Exception e) {
                        Tool.trace(mRootContext, e.toString());
                    }
                }
                return;
            case R.id.job_task_button:
                slnt = new Intent(getApplicationContext(), JobTaskActivity.class);
                sDnt = new Bundle();
                sDnt.putInt("jobbundleid", ac.getRefInt(appWork.TASK_ID));
                slnt.putExtras(sDnt);
                startActivity(slnt);
                return;
            default:
                return;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timerThread.cancel();
        background.unregisterSensorManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        background.registerSensorManager();
        simple_timer(5000);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {

    }
}
