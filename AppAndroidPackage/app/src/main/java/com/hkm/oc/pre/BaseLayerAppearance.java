package com.hkm.oc.pre;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.hkm.Application.appWork;
import com.hkm.oc.R;

/**
 * Created by hesk on 5/27/2014.
 */
public class BaseLayerAppearance extends BaseLayerIntents {
    protected int bitwiseFeatures;
    protected appWork ac;
    protected Bundle saved_instance;

    protected void postOnCreate() {
    }

    protected boolean hasSavedInstance() {
        return saved_instance != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.app_start_new, R.anim.app_start_old);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mFragmentActivity = this;
        bitwiseFeatures = Window.FEATURE_ACTION_BAR_OVERLAY;
        final Window window = mFragmentActivity.getWindow();
        window.requestFeature(windowFeature(bitwiseFeatures));
        final ActionBar ab = mFragmentActivity.getActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            ab.setDisplayHomeAsUpEnabled(true);
        }
        ac = (appWork) mFragmentActivity.getApplicationContext();
        if (savedInstanceState != null) saved_instance = savedInstanceState;
        postOnCreate();
    }

    protected int windowFeature(int options) {
        //  win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  win.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        return options;
    }


    private void show_full_custom_action_bar(ActionBar ab) {
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.NAVIGATION_MODE_LIST);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //   panel.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore state members from saved instance
        //   panel.restoreInstanceState(savedInstanceState);
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        go_back_to_main_screen();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // stop the goback up press
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            // handle your back button code here
            return true;

            // consumes the back key event - ActionMode is not finished
        }

        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                getActionBar().setHomeButtonEnabled(false);
                finish();
                return true;
        }
        return false;
    }
}
