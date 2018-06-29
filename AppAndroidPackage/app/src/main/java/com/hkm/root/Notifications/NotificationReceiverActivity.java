package com.hkm.root.Notifications;

/**
 * Created by hesk on 11/20/13.
 */

import android.app.Activity;
import android.os.Bundle;

import com.hkm.oc.R;

public class NotificationReceiverActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.not);
        setContentView(R.layout.notification_view);
    }
}