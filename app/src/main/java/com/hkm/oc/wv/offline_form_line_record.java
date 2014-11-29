package com.hkm.oc.wv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;

import com.hkm.Application.appWork;
import com.hkm.oc.R;
import com.hkm.oc.wv.supportwebview.JSInterfaceSupport;
import com.hkm.oc.wv.supportwebview.OneCallClient;
import com.hkm.oc.wv.supportwebview.webviewSupports;

/**
 * Created by Hesk on 10/6/2014.
 */
public class offline_form_line_record extends webviewSupports {
    private static String file_local = "file:///android_asset/html_app/formlinerecord.html";
    private String TAG = "onecall offline_form_line_record";
    private String token_cookie = "";

    @Override
    public void onResume() {
        CookieSyncManager.getInstance().stopSync();
        super.onResume();
    }

    @Override
    public void onPause() {
        CookieSyncManager.getInstance().sync();
        super.onPause();
    }

    private void view_settings(WebView view, appWork control) {
        token_cookie = control.get_web_cookie();
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
        cookieSyncManager.startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();

        cookieSyncManager.sync();

        // Enable Javascript
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void postOnCreate() {
        extra_action_bar_settings();
        setContentView(R.layout.act_access_webview);
        WebViewDatabase.getInstance(this).clearHttpAuthUsernamePassword();
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        view_settings(mWebView, ac);
        /*
        String url = Auth.getUrl(SyncStateContract.Constants.API_ID, Auth.getSettings());
        webview.loadUrl(url);
        */
        //  mWebView.setHttpAuthUsernamePassword(HOST, REALM, USERNAME, PASSWORD);
        mWebView.setWebViewClient(new OneCallClient(this, mWebView));
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(new JSInterfaceSupport(this, ac), "onecall_data_support");
        mWebView.loadUrl(file_local);
    }

    @Override
    public void finish() {
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.bntoggletoolbar:
                toolbartoggle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.finish();
            // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_wlr, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
