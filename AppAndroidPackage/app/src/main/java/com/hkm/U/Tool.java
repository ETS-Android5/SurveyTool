package com.hkm.U;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.datamodel.DataHandler;
import com.hkm.oc.R;
import com.hkm.oc.panel.corepanel.elements.Route;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static android.provider.DocumentsContract.getDocumentId;
import static android.provider.DocumentsContract.isDocumentUri;

/**
 * Developed By Hesk Working on the Toolset of the Plot and Chart System 2013
 * All Rights Reserved.
 * Created by hesk on 7/31/13.
 */
public class Tool {
    /**
     * @param jspath the path to attempt to read
     * @return
     */
    private Activity c;

    public Tool(Activity act_input) {
        this.c = act_input;
    }

    public static boolean createDirIfNotExists(String pathd) {
        boolean ret = true;
        // final String pathd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CP_BaseMap/";
        final File file = new File(pathd);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    private static boolean isExternalStoragePresent(Context ctx) {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        if (!((mExternalStorageAvailable) && (mExternalStorageWriteable))) {
            Toast.makeText(ctx, "SD card not present", Toast.LENGTH_LONG).show();
        }
        return (mExternalStorageAvailable) && (mExternalStorageWriteable);
    }

    public static void takeScreenShot(View surface_view) throws Exception {

        // create bitmap screen capture
        Bitmap bitmap;
        View v1 = surface_view;
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] imageData = bos.toByteArray();

//http://www.myexception.cn/android/360069.html
        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CP_BaseMap/";
        FileOutputStream ostream = null;
        if (Tool.createDirIfNotExists(path)) {
            final File file = new File(path + "i-" + System.currentTimeMillis() + ".png");
            //canvas.drawBitmap(toDiskBitmap, width, height, PaintsetBase.k_paint());
            // Save Bitmap to File
            //http://www.techques.com/question/1-9225622/Android:save-image-from-canvas-(SurfaceView)
            try {
                ostream = new FileOutputStream(file);
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                bos.writeTo(ostream);
                ostream.flush();
                ostream.close();
                ostream = null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception(e);
            } finally {
                if (ostream != null) {
                    try {
                        ostream.close();
                        ostream = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //canvas.restore();
        }


    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        final int width = bm.getWidth();
        final int height = bm.getHeight();
        final float scaleWidth = ((float) newWidth) / width;
        final float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        final Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        final Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] pro_j = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, pro_j, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (CursorIndexOutOfBoundsException e) {
            return "";
        } catch (NullPointerException e) {
            return "";
        } catch (ExceptionInInitializerError e) {
            return "";
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static float sizeResample(Point actual, Point optim, Point size) {
        if (actual.y > optim.y || actual.x > optim.x) {
            float r = 0;
            if (actual.y > actual.x) {
                r = (float) optim.y / (float) actual.y;
                size.y = optim.y;
                size.x = (int) (actual.x * r);
            } else {
                r = (float) optim.x / (float) actual.x;
                size.x = optim.x;
                size.y = (int) (actual.y * r);
            }
            return r;
        } else {
            size = new Point(actual);
            return 0f;
        }
    }
//merge
    //http://stackoverflow.com/questions/1540272/android-how-to-overlay-a-bitmap-draw-over-a-bitmap

    public static int calculateInSampleSize(
            BitmapFactory.Options options,
            int reqWidth,
            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Bitmap decodeScaledBitmapFromSdCard(
            String filePath,
            int reqWidth,
            int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static BitmapFactory.Options decodeFromResOptions() {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //   BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = 2;
        return options;
    }

    public static String getviewscreenshot(Bitmap bitmap, Route data, boolean also_survery_boundary) {
        String path_local = Constant.AppBasePath;
        boolean creatpath = Tool.createDirIfNotExists(path_local);
        List<Integer> b = DataHandler.getlegendindex(data);
        if (also_survery_boundary) {
            b.add(99);
        }
        String c = Tool.glue("_", b);
        String filename = String.format("%si%s_%s.png", path_local, System.currentTimeMillis(), c);
        // String filename =path_local + "i" + System.currentTimeMillis() + ".png";
        if (creatpath) {
            final File file = new File(filename);
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    public static Bitmap getImageFromPath(String imageInSD) {
        Bitmap b = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inJustDecodeBounds = true;
        try {
            b = BitmapFactory.decodeFile(imageInSD, options);
            return b;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
            try {
                b = BitmapFactory.decodeFile(imageInSD, options);
                return b;
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
                // handle gracefully.
            }
        }
        //=========
        return b;
    }

    public static Bitmap decodeAndResizeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            /*while (true) {
                //if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                if (width_tmp < REQUIRED_SIZE || height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }*/
            Log.d("scaled size is this", scale + "");
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    public static Bitmap LoadScaledBitmap(final ContentResolver cr, final Uri location, final int IMAGE_MAX_SIZE_MP) {
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = IMAGE_MAX_SIZE_MP == -1 ? 1200000 : IMAGE_MAX_SIZE_MP; // 1.2MP
            in = cr.openInputStream(location);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            // Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ",orig-height: " + o.outHeight);

            Bitmap b = null;
            in = cr.openInputStream(location);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                // Log.d(TAG, "1th scale operation dimenions - width: " + width + ", height:" + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            //  Log.d(TAG, "bitmap size - width: " + b.getWidth() + ", height: " +    b.getHeight());
            return b;
        } catch (IOException e) {
            //  Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Bitmap decodeFile(String mFilePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        try {
            BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (mFilePath != null) {
            bitmap = BitmapFactory.decodeFile(mFilePath, options);
        }
        return bitmap;
    }

    public static boolean Assert(EditText t, String expected) {
        try {
            if (t.getText().toString().trim().equals(expected)) {
                if (t.getText().toString().length() == 0) {
                    return false; //same if the field is empty and it is incomplete.
                } else {
                    return true; //same from the original field, no change
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean Assert(EditText t, String expected, Context ctx) {
        try {
            if (t.getText().toString().trim().equals(expected)) {
                return true;
            } else {
                Toast.makeText(ctx, R.string.warning02, Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * JAVA equivalent of PHP's implode(',' , array_filter( array () ))
     *
     * @param separator
     * @param data
     * @return examples:
     * System.out.println(implode(", ", "ab", " ", "abs"));
     * <p/>
     * or
     * <p/>
     * System.out.println(implode(", ", new String[] { "ab", " ", "abs" }));
     * <p/>
     * Output ab, abs
     * <p/>
     * For more regular expression technique please visit:
     * http://myprogrammingblog.com/2012/02/20/java-how-to-split-java-string-with-delimeter-comma-space-new-line-tab-solved/
     */
    public static String implode(String separator, String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(separator);
            }
        }
        sb.append(data[data.length - 1]);
        return sb.toString();
    }

    public static String glue(String separator, List<Integer> data) {
        StringBuilder sb = new StringBuilder();
        int len = data.size();
        for (int i = 0; i < len; i++) {
            int addname_code = data.get(i);
            sb.append(addname_code);
            if (len > 1 && i < len - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String glue(String separator, String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (data.length > 1 && i < data.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }


    public static int getPaintColorCodeByName(Context ctx, String name) {
        final int color_code = ctx.getResources().getIdentifier(name, "color", ctx.getPackageName());
        return ctx.getResources().getColor(color_code);
    }

    public static boolean isEmpty(TextView t) {
        try {
            final String contenttext = t.getText().toString();
            Log.d("TAG STRING CONTENT", contenttext);
            if (contenttext.equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("TAG STRING CONTENT", "NO CONTEST");
            e.printStackTrace();
            return false;
        }
    }


    public static void trace(Context ctx, int resId, Object... param) {
        String f = "resId not found";
        try {
            f = ctx.getResources().getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(ctx, f, Toast.LENGTH_LONG).show();
        }
        Toast.makeText(ctx, String.format(f, param), Toast.LENGTH_LONG).show();
    }

    public static void trace(Context ctx, String format, Object... param) {
        Toast.makeText(ctx, String.format(format, param), Toast.LENGTH_LONG).show();
    }

    public static void trace(Context ctx, String str) {
        Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
    }

    public static void trace(Context ctx, int resId) {
        String f = "resId not found";
        try {
            f = ctx.getResources().getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(ctx, f, Toast.LENGTH_LONG).show();
        }
    }

    public static void showKeyBoard(Context ctx, EditText focused_textfield) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focused_textfield, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyBoard(Context ctx, EditText focused_textfield) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focused_textfield.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**Security Code **/
    public static String getDeviceId(Context context) {
        String id = getUniqueID(context);
        if (id == null)
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return id;
    }

    private static String getUniqueID(Context context) {

        String telephonyDeviceId = "NoTelephonyId";
        String androidDeviceId = "NoAndroidId";

        // get telephony id
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyDeviceId = tm.getDeviceId();
            if (telephonyDeviceId == null) {
                telephonyDeviceId = "NoTelephonyId";
            }
        } catch (Exception e) {
        }

        // get internal android device id
        try {
            androidDeviceId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            if (androidDeviceId == null) {
                androidDeviceId = "NoAndroidId";
            }
        } catch (Exception e) {

        }

        // build up the uuid
        try {
            String id = getStringIntegerHexBlocks(androidDeviceId.hashCode())
                    + "-"
                    + getStringIntegerHexBlocks(telephonyDeviceId.hashCode());

            return id;
        } catch (Exception e) {
            return "0000-0000-1111-1111";
        }
    }

    public static String getStringIntegerHexBlocks(int value) {
        String result = "";
        String string = Integer.toHexString(value);

        int remain = 8 - string.length();
        char[] chars = new char[remain];
        Arrays.fill(chars, '0');
        string = new String(chars) + string;

        int count = 0;
        for (int i = string.length() - 1; i >= 0; i--) {
            count++;
            result = string.substring(i, i + 1) + result;
            if (count == 4) {
                result = "-" + result;
                count = 0;
            }
        }

        if (result.startsWith("-")) {
            result = result.substring(1, result.length());
        }

        return result;
    }
}
