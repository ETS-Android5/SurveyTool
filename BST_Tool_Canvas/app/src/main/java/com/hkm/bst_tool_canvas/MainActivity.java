package com.hkm.bst_tool_canvas;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hkm.bst_tool_canvas.NodeManagement.NodeMgm;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private final Handler mHandler = new Handler();
    private TextView online_status, version;
    private Button preference_btn, record_plan, jobtask_btn, myaccoun;
    private int million_sec;
    private Timer timerThread;
    private AppCompatButton mAppCompatButton;
    private FragmentActivity mRootContext;
    private final String TAG = "fonef";

    protected void setUpMapIfNeeded() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        online_status = (TextView) findViewById(R.id.statustext);
        mAppCompatButton = (AppCompatButton) findViewById(R.id.big_small);
        final Bundle b = getIntent().getExtras();
        try {
            float Lat = Float.parseFloat(b.getString("latitude", ""));
            float Lng = Float.parseFloat(b.getString("longitude", ""));
            //     currentLatLng = new LatLng(Lat, Lng);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        mAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NodeMgm.startInit(MainActivity.this);
            }
        });
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


}
