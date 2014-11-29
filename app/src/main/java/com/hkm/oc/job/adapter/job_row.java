package com.hkm.oc.job.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.Application.appWork;
import com.hkm.datamodel.JobTaskData;
import com.hkm.oc.R;
import com.hkm.root.Dialog.DCBBool;
import com.hkm.root.Dialog.DialogDoYouWantTo;

import static com.hkm.U.Content.myjob_data;
import static com.hkm.datamodel.JobTaskData.JOBSTATUS;

/**
 * Created by Hesk on 3/6/2014.
 * my job list view
 */
public class job_row implements View.OnClickListener, View.OnTouchListener {

    public static String TAG = "MY_JOB_VIEW";
    public TextView view_project_id, view_post_id, view_status, view_progress;
    public ImageView image;
    public ImageButton btn_trash;
    private Context ctx;
    private int pos;
    private JOBSTATUS status;
    private appWork ac;
    private FragmentManager fm;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trash_btn:
                final JobTaskData d = myjob_data.get(pos);
                final int job_post_id = d.getID();
                String s = ctx.getResources().getString(R.string.job_task_question_5);
                String txt = String.format(s, job_post_id);
                DialogDoYouWantTo doUWant = DialogDoYouWantTo.newInstance(txt, new DCBBool() {
                    @Override
                    public void onyes(DialogInterface dialog) {
                        ac.save_Ref(appWork.TASK_JSON + job_post_id, "");
                        btn_trash.setVisibility(View.GONE);
                        dialog.dismiss();
                    }

                    @Override
                    public void onno(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                doUWant.show(fm, null);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    //LINKED TO THE LAYOUT XML
    public job_row(Context ctx, View Convertedview, int position, FragmentManager fm) {
        this.fm = fm;
        this.ctx = ctx;
        this.ac = (appWork) ctx.getApplicationContext();
        this.view_project_id = (TextView) Convertedview.findViewById(R.id.project_id);
        this.view_post_id = (TextView) Convertedview.findViewById(R.id.post_id);
        this.view_status = (TextView) Convertedview.findViewById(R.id.status);
        this.view_progress = (TextView) Convertedview.findViewById(R.id.progressbar);
        this.btn_trash = (ImageButton) Convertedview.findViewById(R.id.trash_btn);
        this.btn_trash.setOnClickListener(this);
        this.update(position);
    }

    /*
    private void dynamicButton() {
         int id = ac.getRefInt(appWork.TASK_ID);
         String t = ac.getRef(appWork.TASK_JSON + id);
      if (id > 0 && !t.equalsIgnoreCase("") || Content.current_job_task != null) {
      } else {
      }
    }
    */

    public void update(int pos) {
        this.pos = pos;
        final JobTaskData d = myjob_data.get(pos);
        final int job_post_id = d.getID();
        final String t = this.ac.getRef(appWork.TASK_JSON + job_post_id);
        final int ID = ac.getRefInt(appWork.TASK_ID);

        final boolean existing_working_plan = job_post_id == ID;

        if (t.equalsIgnoreCase("")) {
            this.status = JOBSTATUS.NEW;
            this.btn_trash.setVisibility(View.GONE);
        } else {
            this.status = JOBSTATUS.STARTED;
            if (!existing_working_plan) {
                this.btn_trash.setVisibility(View.GONE);
            } else {
                this.btn_trash.setVisibility(View.VISIBLE);
            }
        }

        try {
            this.view_project_id.setText(d.getProjectID());
            this.view_post_id.setText(d.getJID() + "");
            this.view_status.setText(d.getStatus() + "");
            this.view_progress.setText(this.status.toString());
        } catch (Exception e) {
            Log.d(TAG, "pos @ " + pos + " is not working. Data# " + d.toString());
        }
    }

    public JobTaskData getBindedJobTaskData() {
        return myjob_data.get(this.pos);
    }

    public JOBSTATUS getStatus() {
        return this.status;
    }
}
