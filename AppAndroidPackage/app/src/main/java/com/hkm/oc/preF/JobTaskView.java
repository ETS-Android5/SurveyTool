package com.hkm.oc.preF;

import android.os.Bundle;
import android.util.Log;

import com.hkm.oc.R;
import com.hkm.oc.preF.f.jobtask;
import com.hkm.oc.preF.root.UtilFragment;
import com.hkm.root.Tasks.loadtask;
import com.hkm.root.Tasks.taskcb;

import static com.hkm.U.Content.current_job_task;

/**
 * Created by Hesk on 12/6/2014.
 */
public class JobTaskView extends UtilFragment {
    public static String TAG = "jobtaskview";

    private void loading(final int jid) {
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
                            //stuff that updates ui
                            progressBar_dismiss();
                            fragmentTransaction.add(R.id.container, new jobtask()).commit();
                        }

                        @Override
                        public void cbfailure(String d) {
                             progressBar_dismiss();
                        }
                    }, jid, ac).execute();
                }
            });
        } else {
            Log.d(TAG, "bad now");
            fragmentTransaction.add(R.id.container, new jobtask()).commit();
        }
    }

    @Override
    protected void loadingmain(final Bundle states, final Bundle extras) {
        loading(extras.getInt("jobbundleid"));
    }

    @Override
    protected void loadingmain(final Bundle extras) {
        loading(extras.getInt("jobbundleid"));
    }
}
