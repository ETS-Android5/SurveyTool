package com.hkm.oc.signature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.hkm.U.Constant;
import com.hkm.oc.R;
import com.hkm.oc.pre.BaseLayerAppearance;
import com.hkm.root.Dialog.DsingleCB;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.document.SpenPageDoc;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
import com.samsung.android.sdk.pen.settingui.SpenSettingPenLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Hesk on 5/6/2014.
 */
public class Process extends BaseLayerAppearance {
    protected SpenNoteDoc mSpenNoteDoc;
    protected SpenPageDoc mSpenPageDoc;
    protected SpenSurfaceView mSpenSurfaceView;
    protected SpenSettingPenLayout mPenSettingView;

    protected boolean savesign(final boolean isClose) {
        //  final String str = getAppPath().getPath() + '/';
        final double d = Math.random() * 1000000;
        final String fileName = "signed" + Double.toString(d);
        final String saveFilePath = Constant.AppBasePath + fileName + ".png";
        progress_bar_start(R.string.save_data);
        saveTask(saveFilePath, isClose);
        return true;
    }

    private void saveTask(final String saved_file_path, final boolean isClose) {

        final AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            private String error = "";

            @Override
            protected Boolean doInBackground(String... filename) {
                final String strFileName = filename[0]; //Your write code

                // Capture the view
                Bitmap imgBitmap = mSpenSurfaceView.captureCurrentView(false);
                if (imgBitmap == null) {
                    error = "Capture failed." + strFileName;
                    return false;
                }

                OutputStream out = null;
                try {
                    // Create FileOutputStream and save the captured image.
                    out = new FileOutputStream(strFileName);
                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    // Save the note information.
                    mSpenNoteDoc.save(out);
                    out.close();
                    error = "Captured images were stored in the file";
                } catch (IOException e) {
                    File tmpFile = new File(strFileName);
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }
                    error = "Failed to save the file.";
                    e.printStackTrace();
                    return false;
                } catch (Exception e) {
                    File tmpFile = new File(strFileName);
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }

                    error = "Failed to save the file.";
                    e.printStackTrace();
                    return false;
                }
                imgBitmap.recycle();

                return true;
            }

            @Override
            protected void onPostExecute(Boolean saved) {
                if (saved) {
                    progressBar_dismiss("signature confirmed", new DsingleCB() {
                                @Override
                                public void oncontified(DialogFragment dialog) {
                                    if (isClose) {
                                        Bundle b = new Bundle();
                                        b.putString("filename", saved_file_path);
                                        final Intent ires = new Intent();
                                        ires.putExtras(b);
                                        setResult(RESULT_OK, ires);
                                        finish();
                                    }
                                }
                            }
                    );
                    //HANDLE SUCCESS

                } else {
                    //HANDLE ERROR
                    progressBar_dismiss("not saved");
                }

            }
        };
        task.execute(saved_file_path);
    }
}
