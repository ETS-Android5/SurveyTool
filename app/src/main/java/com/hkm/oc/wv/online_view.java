package com.hkm.oc.wv;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;

import com.hkm.Application.appWork;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.oc.wv.supportwebview.OneCallClient;
import com.hkm.oc.wv.supportwebview.webviewSupports;


/**
 * Created by Hesk on 30/5/2014.
 */
public class online_view extends webviewSupports {
    /**
     * 「URL」にはBasic認証のあるサイトのURLを設定します。
     * 「USERNAME」にはBasic認証のあるサイトのユーザーネームを設定します。
     * 「PASSWORD」にはBasic認証のあるサイトのパスワードを設定します。
     * 「HOST」にはBasic認証のあるサイトのドメインを設定します。
     * 「REALM」にはBasic認証のあるサイトで設定してある文字列を設定します。
     */
    private final static String URLLOGOUT = "http://onecallapp.imusictech.net/logout";
    private final static String URL = "http://onecallapp.imusictech.net/wp-admin";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    /*

        @Override
        public void onCreate(Bundle b) {
            super.onCreate(b);
            postOnCreate();
        }
    */
    private final static String HOST = "onecallapp.imusictech.net";
    private final static String REALM = "Users Only";
    private static String file_local = "file:///android_asset/html_app/";
    private String TAG = "onecall web access";
    private String token_cookie = "";

    //http://blog.winfieldpeterson.com/2013/01/17/cookies-in-hybrid-android-apps/
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
        cookieManager.setCookie(URL, token_cookie);
        cookieSyncManager.sync();

        // Enable Javascript
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }


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
        mWebView.setHttpAuthUsernamePassword(HOST, REALM, USERNAME, PASSWORD);
        mWebView.setWebViewClient(new OneCallClient(this, mWebView));
        mWebView.loadUrl(URL);

        if (ac.isNetworkOnline()) {
            //      mWebView.loadUrl("http://onecallapp.imusictech.net/wp-admin");
        } else {
            Tool.trace(this, "Please enable WIFI or Internet Connection.");
        }
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
            case R.id.bnlogout:
                mWebView.loadUrl(URLLOGOUT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
