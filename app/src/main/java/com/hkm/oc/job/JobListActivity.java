package com.hkm.oc.job;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hkm.Application.appWork;
import com.hkm.U.Content;
import com.hkm.U.Tool;
import com.hkm.api.OCHttpClient;
import com.hkm.api.handlers.hkm_oc_standard_handler;
import com.hkm.datamodel.JobTaskData;
import com.hkm.oc.R;
import com.hkm.oc.job.adapter.jobListAdapter;
import com.hkm.oc.job.adapter.job_row;
import com.hkm.oc.pre.BaseLayerAppearance;
import com.hkm.root.Dialog.DialogCB;
import com.hkm.root.Dialog.DialogDoUWant;
import com.hkm.root.Dialog.DialogSimpleNotification;
import com.hkm.root.Dialog.DsingleCB;
import com.hkm.root.Tasks.loadtask;
import com.hkm.root.Tasks.taskcb;
import com.hkm.root.json_io_model.Exclude;

import org.apache.http.Header;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hesk on 3/6/2014.
 */
public class JobListActivity extends BaseLayerAppearance {
    public static String TAG = "ListJOBActivities";
    final DialogInterface.OnClickListener negative_button = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };
    private jobListAdapter madapter;

    public JobListActivity() {

    }

    @Override
    protected int windowFeature(int opt) {
        return Window.FEATURE_ACTION_BAR;
    }

//    private JsonArray raw_items_loaded;

    @Override
    protected void postOnCreate() {
        progress_bar_start(R.string.load_data);
        setContentView(R.layout.datalist_myjob);
        //Get references buttons
        loadDataFromServer();
        // Get references to UI widgets
    }

    private void loadDataFromServer() {
        final HashMap<String, String> paramMap = new HashMap<String, String>();
        OCHttpClient.apiget(getBaseContext(), "appaccess/get_my_jobs_progress", paramMap, new hkm_oc_standard_handler() {
            @Override
            protected void onSuccess(int statusCode, Header[] headers, JsonArray items) {
                //  raw_items_loaded = items;
                Exclude ex = new Exclude();
                Gson gs = new GsonBuilder()
                        .addDeserializationExclusionStrategy(ex)
                        .addSerializationExclusionStrategy(ex)
                        .serializeNulls()
                        .create();
                try {
                    Type apply_object = new TypeToken<ArrayList<JobTaskData>>() {
                    }.getType();
                    Log.d(TAG, apply_object.toString());
                    ArrayList<JobTaskData> wrapper = gs.fromJson(items, apply_object);
                    Content.myjob_data = wrapper;
                } catch (JsonSyntaxException e) {
                    Log.d(TAG, e.toString());
                } catch (JsonParseException e) {
                    Log.d(TAG, e.toString());
                } catch (Exception e) {
                    // e.printStackTrace();
                    Content.myjob_data = new ArrayList<JobTaskData>();
                    Log.d(TAG, e.toString());
                }

                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().add(R.id.mlist_container, new DataListFragment()).commit();
                // super.onSuccess(statusCode, headers, items);
            }

            @Override
            protected void onFailure(Exception e) {
                progressBar_dismiss(e.getMessage());
            }

        });
    }

    private void startNewTask(JobTaskData start_job_data) {
        Content.current_job_task = start_job_data;

    }

    private void ReturnMainScreen(int jobid) {
        Bundle b = new Bundle();
        b.putInt("jobbundleid", jobid);
        ac.save_Ref(appWork.TASK_ID, jobid);
        final Intent ires = new Intent();
        ires.putExtras(b);
        setResult(RESULT_OK, ires);
        // this cannot be the integer

        finish();
    }

    private void load_job_plan(final JobTaskData start_job_data) {
        progress_bar_start(R.string.load_data);
        new loadtask(new taskcb() {
            @Override
            public void cbsuccess(String d) {
                progressBar_dismiss();
                Tool.trace(getBaseContext(), "Job " + start_job_data.getJID() + " is loaded successfully.");
                ReturnMainScreen(start_job_data.getID());
            }

            @Override
            public void cbfailure(String d) {
                progressBar_dismiss();
                final DialogSimpleNotification dialogDoUWant = DialogSimpleNotification.newInstance(R.string.job_task_load_failure, new DsingleCB() {
                    @Override
                    public void oncontified(DialogFragment dialog) {
                        Content.current_job_task = start_job_data;
                        ReturnMainScreen(start_job_data.getID());
                    }
                });
                dialogDoUWant.show(getSupportFragmentManager(), "single_click");
            }
        }, start_job_data.getID(), ac).execute();
        //int index_local_job_data = Content.local_job_data.indexOf(start_job_data);
    }

    @SuppressLint("ValidFragment")
    public class DataListFragment extends ListFragment implements DialogCB {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // Initially there is no data
            setEmptyText("No Data Here");
            // Create an empty adapter we will use to display the loaded data.
            madapter = new jobListAdapter(getActivity(), Content.myjob_data, getFragmentManager());
            setListAdapter(madapter);
            progressBar_dismiss();
        }

        private void newtsk(final JobTaskData row_job_data) {
            int existing_plan_ID = ac.getRefInt(appWork.TASK_ID);
            final String question_1 = String.format(getResources().getString(R.string.job_task_question_1), existing_plan_ID, row_job_data.getID());
            final String question_2 = String.format(getResources().getString(R.string.job_task_question_2), row_job_data.getID());
            final DialogInterface.OnClickListener positive_cb = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Content.current_job_task = row_job_data;
                    ReturnMainScreen(row_job_data.getID());
                    Tool.trace(getBaseContext(), "New job plan is created.");
                    dialogInterface.dismiss();
                }
            };

            if (existing_plan_ID > 0) {
                final DialogDoUWant dialogDoUWant = new DialogDoUWant(question_1, positive_cb, negative_button);
                dialogDoUWant.show(getSupportFragmentManager(), "new_task_notice");
            } else if (existing_plan_ID == row_job_data.getID()) {
                final DialogSimpleNotification dialogDoUWant = DialogSimpleNotification.newInstance(R.string.job_task_question_3);
                dialogDoUWant.show(getSupportFragmentManager(), "single_click");
            } else {
                final DialogDoUWant dialogDoUWant = new DialogDoUWant(question_2, positive_cb, negative_button);
                dialogDoUWant.show(getSupportFragmentManager(), "new_task_brand_new");
            }
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            final job_row job = (job_row) v.getTag();
            switch (job.getStatus()) {
                case NEW:
                    newtsk(job.getBindedJobTaskData());
                    break;
                case STARTED:
                    JobTaskData row_job_data = job.getBindedJobTaskData();
                    final int selectedID = row_job_data.getID();
                    final boolean existing_working_plan = selectedID == ac.getRefInt(appWork.TASK_ID);
                    if (!existing_working_plan) load_job_plan(row_job_data);
                    break;
            }
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

}
