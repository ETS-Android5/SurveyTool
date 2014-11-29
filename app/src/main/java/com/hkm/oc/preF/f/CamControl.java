package com.hkm.oc.preF.f;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hkm.Application.CameraPreview;
import com.hkm.U.Constant;
import com.hkm.oc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by hesk on 7/27/13.
 */
public class CamControl extends Fragment {
    private static final String TAG = "Cam Preview";
    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;
    private static final int xml_layout = R.layout.camera_main;
    private CameraPreview preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private boolean inPreview = false;
    private static boolean cameraConfigured = false;
    private Button snappic;
    private int cameraId;

    public void CamControl() {
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }

    private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (newArea < resultArea) {
                    result = size;
                }
            }
        }

        return (result);
    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()

        }

        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width,
                                   int height) {
            Log.d(Constant.DETAG, "Camera - size set width:" + width + "");
            Log.d(Constant.DETAG, "Camera - size set height:" + height + "");
            initPreview(width, height);
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {

            // no-op
        }

    };

    private void initPreview(int width, int height) {
        if (camera != null && previewHolder.getSurface() != null) {
            try {
                camera.setPreviewDisplay(previewHolder);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
                alert(t.getMessage());
            }


            Camera.Parameters parameters = camera.getParameters();
            determineDisplayOrientation();
            if (!cameraConfigured) {
                Camera.Size bestPreviewSize = determineBestPreviewSize(parameters);
                Camera.Size bestPictureSize = determineBestPictureSize(parameters);
                parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
                parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);
                parameters.setPictureFormat(PixelFormat.JPEG);
                cameraConfigured = true;
                camera.setParameters(parameters);
            }
        }
    }

    private Camera.PictureCallback photoCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
          /*  new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            new SavePhotoTask().execute(data);
                        }
                    }
            ).start();*/


            new SavePhotoTask().execute(data);
            camera.startPreview();
            inPreview = true;
        }
    };

    class SavePhotoTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... jpeg) {

            String newFileName = String.format("mama-%d.jpg", System.currentTimeMillis());
            File photo = new File(Environment.DIRECTORY_PICTURES, newFileName);

            if (photo.exists()) {
                //alert("replaced the old one");
                Log.d(Constant.DETAG,"replaced the old one");
                photo.delete();

            }

            try {
                FileOutputStream fos = new FileOutputStream(photo.getPath());
              //  alert("new file name - " + newFileName);
                Log.d(Constant.DETAG,"new file name - " + newFileName);
                fos.write(jpeg[0]);
                fos.close();
            } catch (IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }

            return null;
        }
    }

    public void stopCamera() {
        System.out.println("stopCamera method");
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            previewHolder.removeCallback(surfaceCallback);
            previewHolder = null;
        }
    }



    public void draw_PREVIEW(Canvas canvas) {
        getView().draw(canvas);
        Paint p = new Paint(Color.RED);
        Log.d(TAG, "draw");
        canvas.drawText("PREVIEW", canvas.getWidth() / 2, canvas.getHeight() / 2, p);
    }

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            camera.startPreview();
            inPreview = true;
        }
    }

    private void alert(String t) {
        Toast.makeText(getActivity(), t, Toast.LENGTH_LONG).show();
    }

    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly.
     */
    public void determineDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation;

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(displayOrientation);
    }

    private Camera.Size determineBestPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
    }

    private Camera.Size determineBestPictureSize(Camera.Parameters parameters) {
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

        return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
    }

    protected Camera.Size determineBestSize(List<Camera.Size> sizes, int widthThreshold) {
        Camera.Size bestSize = null;

        for (Camera.Size currentSize : sizes) {
            boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 3);
            boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
            boolean isInBounds = currentSize.width <= PICTURE_SIZE_MAX_WIDTH;

            if (isDesiredRatio && isInBounds && isBetterSize) {
                bestSize = currentSize;
            }
        }

        if (bestSize == null) {
            //   listener.onCameraError();
            alert("the best size is not there");
            return sizes.get(0);
        }

        return bestSize;
    }

    @Override
    public void onPause() {
        if (camera != null) {
            if (inPreview) {
                camera.stopPreview();
            }
            camera.release();
            camera = null;
        }
        inPreview = false;
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Camera.CameraInfo info = new Camera.CameraInfo();

            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, info);
                //CAMERA_FACING_FRONT
                //CAMERA_FACING_BACK
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camera = Camera.open(i);
                    cameraId = i;
                }
            }
        }

        if (camera == null) {
            camera = Camera.open();
        }

        startPreview();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStat) {
        View rootView = inflater.inflate(xml_layout, container, false);
        // startCamera(rootView);
        CameraPreview preview = (CameraPreview) rootView.findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        //previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        snappic = (Button) rootView.findViewById(R.id.button);
        snappic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    camera.takePicture(null, null, photoCallback);
                    inPreview = false;
                } catch (Exception e) {
                    alert(e.toString());
                }
            }
        });
        snappic.bringToFront();
        return rootView;
    }


}
