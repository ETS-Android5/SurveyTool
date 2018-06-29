package com.hkm.oc.preF.root;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.hkm.Application.appWork;
import com.hkm.U.Constant;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.root.Dialog.DialogSimpleNotification;

/**
 * Created by Hesk on 12/6/2014.
 */
public class pref extends ActionBarActivity {
    protected pref root_context;
    protected int bitwiseFeatures;
    protected appWork ac;
    protected android.app.Fragment fragmentTwo;
    protected FragmentManager fragmentManager = getFragmentManager();
    protected FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root_context = this;
        ac = (appWork) getApplicationContext();
        bitwiseFeatures = Window.FEATURE_ACTION_BAR_OVERLAY;
        final Window window = root_context.getWindow();
        window.requestFeature(windowFeature(bitwiseFeatures));
        setContentView(R.layout.mainfragment);
        //  final android.app.ActionBar ab = root_context.getActionBar();
        configActionBar(root_context.getSupportActionBar());
        Bundle extras = root_context.getIntent().getExtras();
        if (savedInstanceState == null) {
            loadingmain(extras);
        } else {
            loadingmain(savedInstanceState, extras);
        }
    }

    /**
     * with the saved states
     *
     * @param states
     * @param extras
     */
    protected void loadingmain(final Bundle states, final Bundle extras) {

    }

    /**
     * without saved states
     *
     * @param extras
     */
    protected void loadingmain(final Bundle extras) {

    }

    protected ActionBar configActionBar(final ActionBar ab) {
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
            ab.setDisplayHomeAsUpEnabled(true);
        }
        return ab;
    }

    protected int windowFeature(int options) {
        //  win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  win.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        return options;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ProgressDialog progressBar;

    public void progress_bar_start(int resId) {
        final String n = root_context.getResources().getString(resId);
        prepare_progress_bar();
        progressBar.setMessage(n);
        progressBar.show();
    }

    public void progressBar_dismiss() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    public void progressBar_dismiss(int resId) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        simple_dialog_one_button(resId, Constant.DialogCallBack.SINGLE_CLICK);
    }

    public void progressBar_dismiss(String str) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        simple_dialog_one_button(str, Constant.DialogCallBack.SINGLE_CLICK);
    }

    private void prepare_progress_bar() {

        progressBar = new ProgressDialog(root_context);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void progress_bar_start(String info) {
        prepare_progress_bar();
        progressBar.setMessage(info);
        progressBar.show();
    }

    /**
     * @param string_id
     * @param callback_tag
     */
    protected void simple_dialog_one_button(int string_id, String callback_tag) {
        try {
            final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(string_id);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), callback_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(this, e.toString());
        }
    }

    protected void simple_dialog_one_button(String message, String callback_tag) {
        try {
            final DialogSimpleNotification dialog = DialogSimpleNotification.newInstance(message);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), callback_tag); //with this tag name
        } catch (Exception e) {
            Tool.trace(this, e.toString());
        }
    }

}
