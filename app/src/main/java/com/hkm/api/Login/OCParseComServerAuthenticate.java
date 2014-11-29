package com.hkm.api.Login;

import android.content.Context;

import com.hkm.api.OCHttpClient;

import java.io.Serializable;

/**
 * Created by Hesk on 3/6/2014.
 */
public class OCParseComServerAuthenticate implements ServerAuthenticate {
    private static OCParseComServerAuthenticate ourInstance = new OCParseComServerAuthenticate();

    public static OCParseComServerAuthenticate getInstance() {
        return ourInstance;
    }

    public OCParseComServerAuthenticate() {

    }

    private class ParseComError implements Serializable {
        int code;
        String error;
    }

    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {

        return null;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        return null;
    }

    public String userSignIn(Context ctx) throws Exception {
        OCHttpClient.login(ctx);
        return null;
    }
}
