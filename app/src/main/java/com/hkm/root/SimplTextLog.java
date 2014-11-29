package com.hkm.root;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TextView;

import com.hkm.U.Tool;
import com.hkm.oc.R;

/**
 * Created by hesk on 1/21/14.
 */
public class SimplTextLog extends FragmentActivity {
    TextView log;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.blog_block);
            log = (TextView) findViewById(R.id.textView);

        } catch (Exception e) {
            e.printStackTrace();
            Tool.trace(this, e.toString());
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
           //TODO work on his..
          /*  Message msgObj = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("message", msg);
            msgObj.setData(b);
            handler.sendMessage(msgObj);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
