package com.hkm.root.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hkm.U.Tool;

/**
 * Created by Hesk on 26/3/2014.
 */
public class ServiceHandler extends Handler {
    public final static int
            TOAST = 101,
            TIMEOUT = 102,
            SUCCESS = 100;
    private Context ctx;

    public ServiceHandler(Context c) {
        ctx = c;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case TOAST:
                String output = msg.obj.toString();
                Tool.trace(ctx, output);
                break;
            case TIMEOUT:
                Tool.trace(ctx, "The server is too busy, please try again later.");
                break;
            case SUCCESS:
                  /*  Intent intent = new Intent();
                    intent.setAction("com.hkm.Application.ONECALL_UPLOAD_COMPLETE");
                    sendBroadcast(intent);
                    stopSelf();*/
                //  stopSelf();
                String f = "asd";
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
