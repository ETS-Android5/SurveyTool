package com.hkm.Application;

import android.app.Application;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hesk on 30/5/2014.
 */
public class application_volley_layer extends Application {
    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";
    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static application_volley_layer sInstance;
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    private static void setMultiPartBody(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) throws AuthFailureError {
        /*

        if (request instanceof MultiPartRequest == false) {
            return;
        }

        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        Map<String, File> fileUpload = ((MultiPartRequest) request).getFileUploads();
        for (Map.Entry<String, File> entry : fileUpload.entrySet()) {
            multipartEntity.addPart(((String) entry.getKey()), new FileBody((File) entry.getValue()));
        }


        Map<String, String> stringUpload = ((MultiPartRequest) request).getStringUploads();
        for (Map.Entry<String, String> entry : stringUpload.entrySet()) {
            try {
                multipartEntity.addPart(((String) entry.getKey()), new StringBody((String) entry.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        httpRequest.setEntity(multipartEntity);

        */
    }

    protected static HashMap<String, Object> parse(String json) {
        //JsonParser parser = new com.google.gson.JsonParser();
        JsonObject object = (JsonObject) new JsonParser().parse(json);
        Set<Map.Entry<String, JsonElement>> set = object.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
        HashMap<String, Object> map = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (!value.isJsonPrimitive()) {
                map.put(key, parse(value.toString()));
            } else {
                map.put(key, value.getAsString());
            }
        }
        return map;
    }

    /**
     * @return appWork singleton instance
     */
    public static synchronized application_volley_layer getInstance() {
        return sInstance;
    }

    protected void setupcookiemanager() {
        //Setup Cookie Manager and Persistence to disk
        CookieSyncManager.createInstance(this);
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the singleton
        sInstance = this;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
