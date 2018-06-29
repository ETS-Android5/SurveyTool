package com.hkm.root.Tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.api.handlers.PicaAutoBudgetTransform;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.panel.corepanel.elements.SurveyBoundary;
import com.hkm.U.Tool;
import com.hkm.root.handler.MainHandlerCallBack;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by Hesk on
 */
public class SaveReportImage extends AsyncTask<String, Void, String> {
    private MapPanel panel;
    private Context ctx;
    private String dialog_msg;
    private String os, model, product;
    private MainHandlerCallBack mhandler;
    public static String TAG = "SaveReportImage Class";
    public Bitmap saved_bitmap_for_notification;
    public final static int
            START = 101,
            TIMEOUT = 102,
            SUCCESS = 100,
            FAILURE = 103;

    public SaveReportImage(MapPanel mpanel, Context mcontext, MainHandlerCallBack mainHandlerCallBack) {
        panel = mpanel;
        ctx = mcontext;

        os = android.os.Build.DEVICE;
        model = android.os.Build.MODEL;
        product = android.os.Build.PRODUCT;
        mhandler = mainHandlerCallBack;

    }

    @Override
    protected String doInBackground(final String... params) {
        String result = "";
        try {

            final Point size = panel.getBaseMapSize();
            final Point fixed = Constant.output_hardcode_size;
            //TODO: this is hardcoded for DEMO ONLY
            // expected the retangle ratio to be 34.3 / 22.4
            final Bitmap b = Bitmap.createBitmap(fixed.x, fixed.y, Bitmap.Config.ARGB_8888);
            // original
            // final Bitmap b = Bitmap.createBitmap(fixed.x, fixed.y, Bitmap.Config.ARGB_8888);
            //-- -- -- -- -- -
            final Canvas c = new Canvas(b);
            final Matrix m = new Matrix();
            final boolean isTablet = ctx.getResources().getBoolean(R.bool.is_tablet);
            //final float ratio_float_dimen = ctx.getResources().getDimension(R.dimen.ratio_output);
            if (model.equalsIgnoreCase("N7100") && !isTablet) {
                m.setScale(0.5305f, 0.5305f, 0, 0);
            }
            if (model.equalsIgnoreCase("GT-N8000") || isTablet) {
                m.setScale(1f, 1f, 0, 0);
            }
            if (model.equalsIgnoreCase("GT-P3100") && !isTablet) {
                m.setScale(1f, 1f, 0, 0);
            }
            Log.d(TAG, "Export texture on machine model:" + model + ", Is Tablet:" + isTablet);
            c.setMatrix(m);
            panel.finalDraw(c, m);
            //also save the legend data into the reporting map
            SurveyBoundary msb = panel.getSB();
            final String file_path = Tool.getviewscreenshot(b, panel.getRoute(), msb.has_survey_boundary_legend());
            Uri filepath_uri = Uri.parse("file://" + file_path);
            saved_bitmap_for_notification = Picasso.with(ctx).load(filepath_uri).transform(new PicaAutoBudgetTransform(600, true)).get();
            Content.current_sketch_map.set_draw_map_uri(filepath_uri);


            Log.d("save_report", os + " - " + model + " - " + product);
            b.recycle();
            result = file_path;
        } catch (UnsupportedOperationException e) {
            result = "failure";
        } catch (NullPointerException e) {
            result = "failure";
        } catch (RuntimeException e) {
            result = "failure";
        } catch (ExceptionInInitializerError e) {
            result = "failure";
        } catch (OutOfMemoryError e) {
            Log.i("Async-Example", "OutOfMemoryError" + e.toString());
            result = "failure";
        } catch (Exception e) {
            Log.i("Async-Example", "Exception Called" + e.toString());
            result = "failure";
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        Log.i("Async-Example", "onPreExecute Called");
        handler_cb(START);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("Async-Example", "onPostExecute Called");
        //downloadedImg.setImageBitmap(result);
        if (result.equalsIgnoreCase("failure")) {
            handler_cb(FAILURE);
        } else {
            createNotification(Uri.parse("file://" + result));
        }
    }

    private void handler_cb(int signal) {
        Message n = Message.obtain();
        n.what = signal;
        mhandler.handleMessage(n);
    }

    protected void createNotification(final Uri filepath_uri) {
        try {
            //final Bitmap bigpictureforInput = BitmapFactory.decodeResource(getResources(), R.drawable.static_testing2);
            // Prepare intent which is triggered if the
            // notification is selected
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(filepath_uri, "image/*");
            final PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
            // Build notification
            // Actions are just fake
            final Notification.Builder notification_display = new Notification.Builder(ctx)
                    .setContentTitle("Captured a new basemap")
                    .setContentText("New CP Reporting Map has been exported.")
                    .setSmallIcon(R.drawable.ic_ruler_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_ruler_icon))
                    .setContentIntent(pIntent)
                    .setWhen(new Date().getTime() + 2000)
                    .addAction(R.drawable.ic_launcher, "Review", pIntent);
            /*
                .addAction(R.drawable.ic_launcher, "More", pIntent)
                .addAction(R.drawable.ic_launcher, "And more", pIntent);
            */
            final Notification show_dis = new Notification.BigPictureStyle(notification_display).bigPicture(saved_bitmap_for_notification).build();
            final NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            // hide the notification after its selected
            show_dis.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, show_dis);
            saved_bitmap_for_notification.recycle();
            handler_cb(SUCCESS);
        } catch (UnsupportedOperationException e) {
            handler_cb(FAILURE);
        } catch (NullPointerException e) {
            handler_cb(FAILURE);
        } catch (RuntimeException e) {
            handler_cb(FAILURE);
        } catch (ExceptionInInitializerError e) {
            handler_cb(FAILURE);
        } catch (OutOfMemoryError e) {
            handler_cb(FAILURE);
        } catch (Exception e) {
            handler_cb(FAILURE);
        }
    }


}