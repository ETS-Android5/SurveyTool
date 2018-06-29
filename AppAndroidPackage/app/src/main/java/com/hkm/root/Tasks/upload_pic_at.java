package com.hkm.root.Tasks;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.hkm.Application.appWork;
import com.hkm.U.Constant;
import com.hkm.root.handler.AycnInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesk on 26/3/2014.
 */
public class upload_pic_at extends AsyncTask<Context, Void, Void> {
    private HttpParams httpParams;
    private String uriStr;
    private Handler uiHandler;
    private Context ctx;

    public upload_pic_at(Context c, Handler handle, String data_string) {
        uiHandler = handle;
        ctx = c;
        uriStr = data_string;
        httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 50000);
        HttpConnectionParams.setSoTimeout(httpParams, 50000);
    }

    protected static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    protected String convertMediaUriToPath(Uri uri) {
        final String[] proj = {MediaStore.Images.Media.DATA};
        final Cursor cursor = ctx.getContentResolver().query(uri, proj, null, null, null);
        final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        final String path = cursor.getString(column_index);
        cursor.close();

        return path;
    }

    @Override
    protected void onPreExecute() {
        Log.i("Async-Example", "onPreExecute Called");

        final Message msg = new Message();
        msg.what = AycnInfo.START;
        msg.obj = "";
        uiHandler.sendMessage(msg);
    }

    //AycnInfo.this
    @Override
    protected Void doInBackground(Context... params) {
        final appWork ac = (appWork) ctx.getApplicationContext();
        Context context = params[0];
        //main
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        //HttpPost httpPost = new HttpPost("http://192.168.1.80/database_test.php");
        HttpPost httpPost = new HttpPost(Constant.EndPoint.WP_UPLOAD_RESULT_GATEWAY);
        String path;
        final Message msg = new Message();
        try {
            String encodedImage = "";
            String content_col = uriStr.substring(0, 8);
            final boolean NotPath = content_col.equalsIgnoreCase("content:");
            if (NotPath) {
                //chooser return a uri, not path
                Uri uri = Uri.parse(uriStr);
                path = convertMediaUriToPath(uri);
               /* mprogressbar.setMessage(uriStr);*/
                msg.obj = uri.toString();
                uiHandler.sendMessage(msg);
            } else {
                path = uriStr;
            }

            String localFilename = path.split("/")[((path.split("/")).length) - 1];
            localFilename = localFilename.substring(0, (localFilename.length() - 4));
            String name_arr[] = localFilename.split("_");
            ArrayList<Integer> legend = new ArrayList<Integer>();
            for (int i = 0; i < name_arr.length; i++) {
                try {
                    int legendNum = Integer.parseInt(name_arr[i]);
                    legend.add(legendNum);
                } catch (Exception ex) {

                }
            }

            JSONArray JsonlegendArray = new JSONArray(legend);
            String Jsonlegend = JsonlegendArray.toString();
            //    JSONArray JsonEntries = new JSONArray(Content.radius_list);
            //    String jserialstring = JsonEntries.toString();
            String jserialstring = "under development";
            //convert image to byte
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            //encode byteImage to Base64 String

            try {
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                //msg.obj = encodedImage;
                //uiHandler.sendMessage(msg);
            } catch (Exception ex) {
                msg.obj = ex.toString();
                msg.what = AycnInfo.FAILURE;
                uiHandler.sendMessage(msg);
            }

            // Add data
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("action", "print"));
            nameValuePairs.add(new BasicNameValuePair("image", encodedImage));
            nameValuePairs.add(new BasicNameValuePair("legend", Jsonlegend));
            //this is the temp solution
            if (!NotPath && jserialstring.length() > 0) {
                nameValuePairs.add(new BasicNameValuePair("map_data", jserialstring));
            } else {
                nameValuePairs.add(new BasicNameValuePair("map_data", "no data files"));
            }
            nameValuePairs.add(new BasicNameValuePair("basicdata", ac.getPreferenceUploadProjectInfo()));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);

            String returnString = convertStreamToString(response.getEntity().getContent());

            //PhpServiceTester.this.uiHandler.sendMessage(msg);
            //WorkPanel.this.uiHandler.sendMessage(msg);
            //OCFoundation.this.uiHandler.sendMessage(msg);

            msg.what = AycnInfo.SUCCESS;
            msg.obj = returnString;
            uiHandler.sendMessage(msg);

        } catch (ClientProtocolException e) {
            Log.d("ServerConnection", e.toString());
            msg.what = AycnInfo.TIMEOUT;
            msg.obj = e.toString();
            uiHandler.sendMessage(msg);
        } catch (IOException e) {
            Log.d("ServerConnection", e.toString());
            msg.what = AycnInfo.FAILURE;
            msg.obj = e.toString();
            uiHandler.sendMessage(msg);
        } catch (Exception e) {
            Log.d("ServerConnection", e.toString());
            msg.what = AycnInfo.FAILURE;
            msg.obj = e.toString();
            uiHandler.sendMessage(msg);
        }
        return null;
    }
}
