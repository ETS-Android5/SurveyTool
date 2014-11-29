package com.hkm.oc.signature;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hkm.U.Tool;
import com.hkm.oc.R;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pen.Spen;
import com.samsung.android.sdk.pen.SpenSettingPenInfo;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.engine.SpenColorPickerListener;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
import com.samsung.android.sdk.pen.settingui.SpenSettingPenLayout;

import java.io.IOException;

/**
 * Created by Hesk on 5/6/2014.
 */
public class Sign extends Process {

    private SpenColorPickerListener mColorPickerListener =
            new SpenColorPickerListener() {
                @Override
                public void onChanged(int color, int x, int y) {
                    // Set the color from the Color Picker to the setting view.
                    if (mPenSettingView != null) {
                        SpenSettingPenInfo penInfo = mPenSettingView.getInfo();
                        penInfo.color = color;
                        mPenSettingView.setInfo(penInfo);
                    }
                }
            };

    @Override
    protected int windowFeature(int options) {
        options = Window.FEATURE_ACTION_BAR;
        return options;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                mSpenPageDoc.clearHistory();
                mSpenPageDoc.removeAllObject();
                mSpenSurfaceView.update();
                return true;
            case R.id.pensetting:
                // If PenSettingView is open, close it.
                if (mPenSettingView.isShown()) {
                    mPenSettingView.setVisibility(View.GONE);
                    // If PenSettingView is not open, open it.
                } else {
                    mPenSettingView.setViewMode(SpenSettingPenLayout.VIEW_MODE_EXTENSION);
                    mPenSettingView.setVisibility(View.VISIBLE);
                }
                return true;
            case R.id.done:
                savesign(true);
                return true;
            case android.R.id.home:
                final Bundle b = new Bundle();
                final Intent ires = new Intent();
                ires.putExtras(b);
                setResult(RESULT_CANCELED, ires);
                finish();
                return true;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    protected void setTitle() {
        final String s = mFragmentActivity.getResources().getString(getIntent().getExtras().getInt("title"));
        final ActionBar c = getSupportActionBar();
        c.setTitle(s);
    }

    @Override
    protected void postOnCreate() {
        setTitle();
        setContentView(R.layout.sign_panel);
        // Initialize Spen
        boolean isSpenFeatureEnabled = false;
        final Spen spenPackage = new Spen();
        try {
            spenPackage.initialize(mFragmentActivity);
            isSpenFeatureEnabled = spenPackage.isFeatureEnabled(Spen.DEVICE_PEN);
        } catch (SsdkUnsupportedException e) {
            if (SDKUtils.processUnsupportedException(mFragmentActivity, e) == true) {
                return;
            }
        } catch (Exception e1) {
            Tool.trace(mFragmentActivity, "Cannot initialize Spen");
            e1.printStackTrace();
            finish();
        }

        FrameLayout spenViewContainer = (FrameLayout) findViewById(R.id.spenViewContainer);
        RelativeLayout spenViewLayout = (RelativeLayout) findViewById(R.id.spenViewLayout);

        // Create PenSettingView
        mPenSettingView = new SpenSettingPenLayout(mFragmentActivity, new String(), spenViewLayout);
        if (mPenSettingView == null) {
            Tool.trace(mFragmentActivity, "Cannot create new PenSettingView.");
            finish();
        }

        spenViewContainer.addView(mPenSettingView);
        // Create SpenView
        mSpenSurfaceView = new SpenSurfaceView(mFragmentActivity);
        if (mSpenSurfaceView == null) {
            Tool.trace(mFragmentActivity, "Cannot create new SpenView.");
            finish();
        }
        spenViewLayout.addView(mSpenSurfaceView);
        mPenSettingView.setCanvasView(mSpenSurfaceView);

        // Get the dimension of the device screen
        Display display = getWindowManager().getDefaultDisplay();
        Rect rect = new Rect();
        display.getRectSize(rect);
        try {
            mSpenNoteDoc = new SpenNoteDoc(mFragmentActivity, rect.width(), rect.height());
        } catch (IOException e) {
            Tool.trace(mFragmentActivity, "Cannot create new NoteDoc");
            e.printStackTrace();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        // Add a Page to NoteDoc, get an instance, and set it to the member variable.
        mSpenPageDoc = mSpenNoteDoc.appendPage();
        mSpenPageDoc.setBackgroundColor(getResources().getColor(R.color.keyguard_avatar_frame_color));
        //   mSpenPageDoc.setBackgroundColor(0);
        mSpenPageDoc.clearHistory();
        // Set PageDoc to View
        mSpenSurfaceView.setPageDoc(mSpenPageDoc, true);
        mSpenSurfaceView.setZoomable(false);

        initPenSettingInfo();
        mSpenSurfaceView.setColorPickerListener(mColorPickerListener);

        if (isSpenFeatureEnabled == false) {
            mSpenSurfaceView.setToolTypeAction(SpenSurfaceView.TOOL_FINGER, SpenSurfaceView.ACTION_STROKE);
            Tool.trace(mFragmentActivity, "Device does not support Spen. \n You can draw stroke by finger");
        }
    }

    private void initPenSettingInfo() {
        // Initialize Pen settings
        SpenSettingPenInfo penInfo = new SpenSettingPenInfo();
        penInfo.color = Color.BLUE;
        penInfo.size = 10;
        mSpenSurfaceView.setPenSettingInfo(penInfo);
        mPenSettingView.setInfo(penInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPenSettingView != null) {
            mPenSettingView.close();
        }
        if (mSpenSurfaceView != null) {
            mSpenSurfaceView.close();
            mSpenSurfaceView = null;
        }

        if (mSpenNoteDoc != null) {
            try {
                mSpenNoteDoc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSpenNoteDoc = null;
        }
    }
}
