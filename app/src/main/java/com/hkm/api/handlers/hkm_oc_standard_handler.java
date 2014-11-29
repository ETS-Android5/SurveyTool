package com.hkm.api.handlers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

/**
 * Created by Hesk on 3/6/2014.
 */
public class hkm_oc_standard_handler extends TextHttpResponseHandler {
    public static String TAG = "handlerJson";
    public static String TAG2 = "handlerJstatuscode";
    private static Gson gson = new Gson();

    public void onSuccessTime(int statusCode, Header[] headers, String item) {

    }

    protected void onSuccess(int statusCode, Header[] headers, JsonArray array_item) {
        Log.d(TAG2, "" + statusCode);
        Log.d(TAG, array_item.toString());
        try {

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }


    @Override
    public void onFailure(String responseBody, Throwable error) {
        Log.d(TAG2, "" + responseBody);
        Log.d(TAG, "header" + error.toString() + ".  ");
        onFailure(new Exception(error.toString()));
    }

    protected void onFailure(Exception e) {
        Log.d(TAG, e.toString());
    }


    @Override
    public void onSuccess(final int statusCode, final Header[] headers, final String responseBody) {

        Log.d(TAG2, "" + statusCode);
        Log.d(TAG, responseBody);

        if (statusCode != HttpStatus.SC_NO_CONTENT) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //  return this.getGson().fromJson(jsonObject, UploadResponse.class);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {

                                final JsonParser parser = new JsonParser();
                                final JsonObject jsob = parser.parse(responseBody).getAsJsonObject();
                                if (jsob.has("status")) {
                                    if (jsob.getAsJsonPrimitive("status").getAsString().equalsIgnoreCase("error")) {
                                        onFailure(new Exception(jsob.getAsJsonPrimitive("error").getAsString()));
                                        return;
                                    }
                                }
                                if (jsob.has("code")) {
                                    int return_code = jsob.getAsJsonPrimitive("code").getAsInt();
                                    if (return_code > 1) {
                                        onFailure(new Exception("the result code is :" + return_code + ", is that the expected code?"));
                                    } else if (jsob.has("result")) {
                                        String result = jsob.getAsJsonPrimitive("result").getAsString();
                                        if (result.equals("success")) {
                                            if (jsob.has("data")) {
                                                onSuccess(statusCode, headers, jsob.getAsJsonArray("data"));
                                            } else if (jsob.has("obtain")) {
                                                onSuccess(statusCode, headers, jsob.getAsJsonArray("obtain"));
                                            } else if (jsob.has("timestamp")) {
                                                onSuccessTime(statusCode, headers, jsob.getAsJsonPrimitive("timestamp").getAsString());
                                            } else {
                                                onFailure(new Exception("nothing can be done with the returning json."));
                                            }
                                        } else {
                                            onFailure(new Exception("the return result is failure"));
                                        }
                                    } else {
                                        onFailure(new Exception("the return JSON does not have result"));
                                    }
                                } else {
                                    onFailure(new Exception("the return JSON does not have code"));
                                }
                                //     TypeDTO[] myTypes = gson.fromJson(jsob, TypeDTO[].class);
                            }
                        });
                    } catch (final Exception ex) {
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(ex);
                            }
                        });
                    }
                }
            }).start();
        } else {
            onFailure(new Exception("please connect to the internet"));
        }
    }
}
