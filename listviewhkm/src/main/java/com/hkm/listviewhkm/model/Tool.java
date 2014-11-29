package com.hkm.listviewhkm.model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
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

    public static String getStr(EditText a) {
        return a.getText().toString();
    }

    public static String getStr(TextView a) {
        return a.getText().toString();
    }



    public static int LabelFieldStatus(TextView t) {
        try {
            final String contentfield = t.getText().toString();
            boolean hasSharp = false, hasNumber = false, hasLetter = isAlpha(contentfield);
            if (contentfield.isEmpty()) {
                return 0;
            }
            if (contentfield.indexOf("#") > 0)
                hasSharp = true;

            if (contentfield.matches(".*\\d.*"))
                hasNumber = true;

            if (hasLetter && hasNumber && hasSharp) {
                return 2;
            }

            if (hasLetter && hasNumber) {
                return 1;
            }

        } catch (Exception e) {
            return -1;
        }

        return 0;
    }

    public static boolean isEmpty(EditText t) {
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
}
