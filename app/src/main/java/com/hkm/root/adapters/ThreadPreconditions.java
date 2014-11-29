package com.hkm.root.adapters;

import android.os.Looper;

import com.hkm.oc.BuildConfig;

/**
 * Created by Hesk on 27/6/2014.
 */
public class ThreadPreconditions {
    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }
}
