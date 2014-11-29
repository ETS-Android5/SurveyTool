package com.hkm.root.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Hesk on 26/3/2014.
 */
public class ProgressBar {
    private Context ctx;
    private String message_in_dialog;

    public ProgressBar(Context context_content, String message) {
        ctx = context_content;
        message_in_dialog = message;
    }

    private ProgressDialog progressBar;
    private int progressBarStatus;

    protected void startProgressDialog(View v) {
        // prepare for a progress bar dialog
        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage(message_in_dialog);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        //reset progress bar status
        progressBarStatus = 0;

        //reset filesize
        // fileSize = 0;

       /* new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    // process some tasks
                    progressBarStatus = fileDownloadStatus();
                    //  sleep 1 second to show the progress
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                // when, file is downloaded 100%,
                if (progressBarStatus >= 100) {
                    // sleep 2 seconds, so that you can see the 100% of file download
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();*/

    }
}
