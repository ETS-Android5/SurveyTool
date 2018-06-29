package com.hkm.root.Tasks;

import android.os.AsyncTask;

import com.hkm.Application.appWork;
import com.hkm.root.handler.CursorDataConversion;


public class loadtask extends AsyncTask<Void, Void, String> {
    public static String TAG = "task_to_load";
    private int n;
    private taskcb cb;
    private appWork ac;

    public loadtask(taskcb cb, int job_id, appWork ac) {
        this.cb = cb;
        this.ac = ac;
        this.n = job_id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return CursorDataConversion.load_task(ac, n);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("done")) {
            cb.cbsuccess(result);
        } else {
            cb.cbfailure(result);
        }
    }

}
