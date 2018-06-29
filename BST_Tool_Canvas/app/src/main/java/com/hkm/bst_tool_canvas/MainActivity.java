package com.hkm.bst_tool_canvas;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private final Handler mHandler = new Handler();
    private TextView online_status, version;
    private Button preference_btn, record_plan, jobtask_btn, myaccoun;
    private int million_sec;
    private Timer timerThread;
    private FragmentActivity mRootContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
