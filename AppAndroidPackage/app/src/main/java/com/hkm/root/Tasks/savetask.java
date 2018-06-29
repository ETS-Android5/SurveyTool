package com.hkm.root.Tasks;

import android.os.AsyncTask;

import com.hkm.Application.appWork;
import com.hkm.root.handler.CursorDataConversion;

/**
 * Created by Hesk on 10/6/2014.
 */
public class savetask extends AsyncTask<Void, Void, String> {
    public static String TAG = "task_to_save";
    private taskcb cb;
    private appWork ac;

    public savetask(taskcb cb, appWork ac) {
        this.cb = cb;
        this.ac = ac;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return CursorDataConversion.save_task(ac);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equalsIgnoreCase("done"))
            cb.cbsuccess(result);
        else
            cb.cbfailure(result);
    }
}
