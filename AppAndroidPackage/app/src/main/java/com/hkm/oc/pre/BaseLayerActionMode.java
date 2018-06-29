package com.hkm.oc.pre;

import android.os.Environment;

import com.hkm.U.Tool;

import java.io.File;

/**
 * Created by hesk on 5/27/2014.
 */
public class BaseLayerActionMode extends BaseBottom {
    protected File getAppPath() {
        // Set the save directory for the file.
        File filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OneCall/");
        if (!filePath.exists()) {
            if (!filePath.mkdirs()) {
                Tool.trace(this, "Save Path Creation Error");
                return null;
            }
        }
        return filePath;
    }
}