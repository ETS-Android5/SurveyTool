package com.hkm.Application;

import android.app.IntentService;
import android.os.Handler;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hkm.oc.R;

/**
 * Created by Hesk on
 */
public class CoreService extends IntentService {
    int count = 10;
    private Handler handler = new Handler();

    public CoreService() {
        super("SmallSrv");

    }

    @Override
    protected void onHandleIntent(Intent arg0) {
        do {
            handler.post(new Runnable() {
                @Override
                public void run() {
          /*
           * this will jump toast in UI thread, 11 times, numbers from 10 to 0.
           * To make changes to UI, use handler.post(...) function.
           */
                    Toast.makeText(getApplication(), "" + count, 300).show();
                    Log.i("test", "in service");
                }
            });

            try {
                synchronized (this) {
          /*
           * for 1sec delay, use this "synchronized!" block,otherwise you will got error.
           */
                    wait(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count--;
        }
        while (count >= 0);
        try {
            synchronized (this) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopSelf(); //after finishing the service call this for self destroy.
    }

    @Override
    public void onDestroy() {
    /*
     * this will run before closing service by the system
     */
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), R.string.action_make, 500).show();
            }

        });
        super.onDestroy();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.i("SERVICE", "starting");
    }


}
