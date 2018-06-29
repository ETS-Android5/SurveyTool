package com.hkm.root.handler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.hkm.Application.appWork;
import com.hkm.oc.panel.basic_panel_support;
import com.hkm.U.Tool;
import com.hkm.oc.R;
import com.hkm.root.Tasks.upload_pic_at;

public class AycnInfo extends Service {
    //private static final String TAG = "ServerCommunication";
    public final static int UPLOAD_PICTURE = 1023;
    public final static int TOAST = 101, TIMEOUT = 102, SUCCESS = 100, FAILURE = 104, START = 106;
    public String uriStr;
    public Handler uiHandler;
    /* private ProgressDialog mprogressbar;*/
    private basic_panel_support intent_class;
    private boolean bound = false;

    public class HandleBinder extends Binder {
        public AycnInfo getService() {
            return AycnInfo.this;
        }

        public void bindActivity(basic_panel_support in) {
            if (intent_class == null) {
                intent_class = in;
                bound = true;
            }
        }
    }

    public class IHandler extends Handler {
        public void handleMessage(Message msg) {
            final appWork ac = (appWork) getApplicationContext();
            final String output = msg.obj.toString();
            switch (msg.what) {
                case START:
                    if (bound) {
                        intent_class.progress_bar_upload_data();
                    }
                    break;
                case TOAST:
                    Tool.trace(AycnInfo.this, output);
                    break;
                case TIMEOUT:
                    if (bound) {
                        intent_class.progressBar_dismiss(R.string.dialog_msg_failure_timeout_upload);
                        bound = false;
                        stopSelf();
                    }
                    break;
                case SUCCESS:
                    /*  Intent intent = new Intent();
                    intent.setAction("com.hkm.Application.ONECALL_UPLOAD_COMPLETE");
                    sendBroadcast(intent);
                    stopSelf();*/
                    //      Tool.trace(AycnInfo.this, output);
                    //if (bound) {
                    if (ac.readSuccessFailure(output)) {
                        intent_class.progressBar_dismiss(R.string.complete_upload);
                    } else {
                        Tool.trace(AycnInfo.this, output);
                        intent_class.progressBar_dismiss(R.string.failure_upload);
                    }
                    // bound = false;
                    stopSelf();
                    //   }
                    break;
                case FAILURE:
                    Tool.trace(AycnInfo.this, output);
                    if (bound) {
                        intent_class.progressBar_dismiss(output);
                        bound = false;
                        stopSelf();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private HandleBinder mBinder = new HandleBinder();

    private Messenger mMessenger;

    public Messenger getMessenger() {
        return mMessenger;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uiHandler = new IHandler();
        mMessenger = new Messenger(uiHandler);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        int action = intent.getIntExtra("action", -1);
        if (action == UPLOAD_PICTURE) {
            uriStr = intent.getStringExtra("uri");
            final upload_pic_at up = new upload_pic_at(getBaseContext(), uiHandler, uriStr);
            //Toast.makeText(this, "uri in service: " + uriStr, Toast.LENGTH_LONG).show();
            up.execute(getApplicationContext(), null, null);
        }
        super.onStart(intent, startId);
    }

  /*  protected String convertMediaUriToPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();

        return path;
    }*/


    @Override
    public IBinder onBind(Intent intent) {
        //   Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        //return mMessenger.getBinder();
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}