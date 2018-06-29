package com.hkm.Application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.hkm.U.Tool;
import com.hkm.api.OCHttpClient;
import com.hkm.oc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Hesk on --
 */
public class appWork extends application_volley_layer {
    /**
     * application key section
     */
    public static final String
            TAG = "APP_CONTROLLER",
            USER_NAME = "app_login",
            PASS = "app_pass",
            NONCE = "appnonce",
            TOKEN = "userloginauthentoken",
            TOKENW = "userwebkitcookie",
            PROJECT_CODE = "userworkingonsiteprojectid",
            CURRENT_DISTANCE_AB = "currentdistanceab",
            POINT_A = "pta",
            POINT_B = "ptb",
    //the job id for the job task
    TASK_ID = "job_task_id",
    //Job Task Activity Data
    TASK_JSON = "json_task_";
    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static SharedPreferences sprefence;
    private static SharedPreferences preference_panel;

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sprefence = getSharedPreferences("oc_detail", MODE_PRIVATE);
        preference_panel = PreferenceManager.getDefaultSharedPreferences(this);
        setupcookiemanager();
        // Tool.trace(this, getRf(USER_NAME));
        // Tool.trace(this, getRf(PASS));
        OCHttpClient.login(this);
        // showpackagesigning();
    }

    private void showpackagesigning() {
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo("com.hkm.oc.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Hash key", sha1Hash(key));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    private String sha1Hash(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public void save_web_cookie(String token) {
        SharedPreferences.Editor edit = sprefence.edit();
        edit.putString(TOKENW, token);
        edit.commit();
    }

    public String get_web_cookie() {
        return sprefence.getString(TOKENW, "");
    }

    public void save_token_cookie(String token_cookie) {
        SharedPreferences.Editor edit = sprefence.edit();
        edit.putString(TOKEN, token_cookie);
        edit.commit();
    }

    public String get_token_cookie() {
        return sprefence.getString(TOKEN, "");
    }

    public void save_nonce(String st) {
        SharedPreferences.Editor edit = sprefence.edit();
        edit.putString(NONCE, st);
        edit.commit();
    }

    public void save_Ref(String tag, int t) {
        SharedPreferences.Editor edit = sprefence.edit();
        edit.putInt(tag, t);
        edit.commit();
    }

    public void save_Ref(String tag, String t) {
        SharedPreferences.Editor edit = sprefence.edit();
        edit.putString(tag, t);
        edit.commit();
    }

    private boolean getPrefBool(String key) {
        return preference_panel.getBoolean(key, false);
    }

    private int getPrefInt(String key) {
        return preference_panel.getInt(key, -1);
    }

    public boolean IsDebug() {
        return getPrefBool("onDebugAdmin");
    }

    public boolean IsDrawMapExitNotification() {
        return getPrefBool("notification_save_draw_map_exit");
    }

    public String getRf(String tag) {
        return preference_panel.getString(tag, "");
    }

    public String getRef(String tag) {
        return sprefence.getString(tag, "");
    }

    public int getRefInt(String tag) {
        return sprefence.getInt(tag, -1);
    }

    private void clear_save() {
        SharedPreferences.Editor edit = sprefence.edit();
        edit.clear();
        edit.commit();
        Tool.trace(this, "Info saved..");
    }

    private String getPreference(int res_id) {
        final String value = sprefence.getString(getResources().getString(res_id), "");
        return value;
    }

    //used by the base map uploading
    public String getPreferenceUploadProjectInfo() {
        Gson json = new Gson();
        String r = json.toJson(new SendUploadData());
        Log.i(TAG, "getPreferenceUploadProjectInfo");
        Log.i(TAG, r);
        return r;
    }

    public boolean readSuccessFailure(String returnJson) {
        try {
            JSONObject obj = new JSONObject(returnJson);
            String result = obj.getString("result");
            Log.d(TAG, obj.toString());
            if (result.equalsIgnoreCase("success")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Tool.trace(getApplicationContext(), e.toString());
            return false;
        }
        return status;
    }

    protected void OpenAppInPlay() {
        Uri uri = Uri.parse("market://details?id="/** put package name of app here. */);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    private class SendUploadData {
        private String cpname, cplicense, locationsite, projectid, cplogin;

        public SendUploadData() {
            cpname = getPreference(R.string.cp_name);
            cplicense = getPreference(R.string.cp_no);
            cplogin = getPreference(R.string.cp_login);
            locationsite = getPreference(R.string.jb_location);
            projectid = getPreference(R.string.jp_id);
        }
    }
}
