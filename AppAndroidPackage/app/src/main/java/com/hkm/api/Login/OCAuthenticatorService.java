package com.hkm.api.Login;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by hesk on 5/27/2014.
 */
public class OCAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        OCAuthenticator authenticator = new OCAuthenticator(this);
        return authenticator.getIBinder();
    }
}
