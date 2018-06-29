package com.hkm.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hkm.Application.appWork;
import com.hkm.api.handlers.hkm_oc_standard_handler;
import com.hkm.U.Tool;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.hkm.Application.appWork.NONCE;
import static com.hkm.Application.appWork.PASS;
import static com.hkm.Application.appWork.TOKEN;
import static com.hkm.Application.appWork.USER_NAME;
import static com.hkm.api.Login.AccountGeneral.X_APPLICATION_ID;
import static com.hkm.api.Login.AccountGeneral.X_REST_API_KEY;

/**
 * Created by hesk on 5/27/2014.
 */
public class OCHttpClient {
    public static String TAG = "OClistClient";
    public static String TAG_JSON = "json_handler_OClistClient_status_code";
    private static String get_nounce_api = "get_nonce";
    private static String login_api = "auth/generate_auth_cookie";
    public static final String WP_API_BASE_URL = "http://onecallapp.imusictech.net/api/";
    //private static final String BASE_URL = "http://api.twitter.com/1/";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return WP_API_BASE_URL + relativeUrl;
    }

    public static void apiget(Context ctx, String controllermethod, HashMap<String, String> paramMap, hkm_oc_standard_handler handle) {
        final appWork ac = (appWork) ctx.getApplicationContext();
        paramMap.put("cookie", ac.getRef(TOKEN));
        final RequestParams params = new RequestParams(paramMap);
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Parse-Application-Id", X_APPLICATION_ID);
        client.addHeader("X-Parse-REST-API-Key", X_REST_API_KEY);
        client.addHeader("Content-Type", "application/json");
        final String full_request = WP_API_BASE_URL + controllermethod;
        client.get(full_request, params, handle);
    }

    public static void login(final Context ctx) {
        getnounce(ctx);
    }

    private static void login_now(final Context ctx) {
        final appWork ac = (appWork) ctx.getApplicationContext();
        final HashMap<String, String> paramMap = new HashMap<String, String>();
        final AsyncHttpClient client = new AsyncHttpClient();
        paramMap.put("nonce", ac.getRef(NONCE));
        paramMap.put("username", ac.getRf(USER_NAME));
        paramMap.put("password", ac.getRf(PASS));
        final RequestParams params = new RequestParams(paramMap);
        client.addHeader("X-Parse-Application-Id", X_APPLICATION_ID);
        client.addHeader("X-Parse-REST-API-Key", X_REST_API_KEY);
        client.addHeader("Content-Type", "application/json");
        client.get(WP_API_BASE_URL + login_api, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d(TAG_JSON, "" + params.toString());
                    Log.d(TAG, response.toString());
                    if (response.has("error")) {
                        Log.d(TAG, response.getString("error"));
                        Tool.trace(ctx, response.getString("error"));
                    } else {
                        String cookie = response.getString("cookie");
                        if (!cookie.equals(ac.get_token_cookie()))
                            ac.save_token_cookie(cookie);
                        Tool.trace(ctx, "login success");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                    Tool.trace(ctx, "server error:" + statusCode);
                }
            }
        });

    }

    private static void getnounce(final Context ctx) {
        final appWork ac = (appWork) ctx.getApplicationContext();
        HashMap<String, String> paramMap = new HashMap<String, String>();
        final AsyncHttpClient client = new AsyncHttpClient();
        paramMap.put("controller", "auth");
        paramMap.put("method", "generate_auth_cookie");
        RequestParams params = new RequestParams(paramMap);
        client.addHeader("X-Parse-Application-Id", X_APPLICATION_ID);
        client.addHeader("X-Parse-REST-API-Key", X_REST_API_KEY);
        client.addHeader("Content-Type", "application/json");
        Tool.trace(ctx, "start login process");
        client.get(WP_API_BASE_URL + get_nounce_api, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    Log.d(TAG, response.toString());

                    if (statusCode == HttpStatus.SC_NO_CONTENT)
                        throw new Exception("emtpy content");


                    final JsonParser parser = new JsonParser();
                    //          Gson g = new Gson();
                    final JsonObject jsob = parser.parse(response).getAsJsonObject();

                    if (jsob.has("error")) {
                        final String r = jsob.getAsJsonPrimitive("error").getAsString();
                        throw new Exception("Error message: " + r);
                    }


                    if (jsob.has("nonce"))
                        ac.save_nonce(jsob.getAsJsonPrimitive("nonce").getAsString());

                    OCHttpClient.login_now(ctx);
                } catch (Exception e) {
                    onFailure(e);
                }
            }

            public void onFailure(Exception e) {
                Tool.trace(ctx, e.toString());
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                Tool.trace(ctx, e.toString());
            }
        });
    }
}
