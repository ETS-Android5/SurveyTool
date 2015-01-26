package com.hkm.oc.panel.corepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Hesk on 13/10/08
 */
public class SimpleSurfacePanel extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    CanvasThread canvasThread;
    SurfaceHolder thePanelHolder;
    boolean is_autoStart = true;

    public SimpleSurfacePanel(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        init();
    }

    public SimpleSurfacePanel(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }

    public SimpleSurfacePanel(Context ctx) {
        super(ctx);
        init();
    }

    @Override
    public SurfaceHolder getHolder() {
        return super.getHolder();
    }

    public void startPanel() throws Exception {
        // loadResources();
        //init_Paint();
        canvasThread.setRunning(true);
        // if (panelListenr == null) {
        //     throw new Exception("panel listener is not defined yet");
        // }
        canvasThread.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("hello");
        // Save=(Button)findViewById(R.id.Save);
        System.out.println("hello2");
    }

    protected void init() {
        thePanelHolder = getHolder();
        thePanelHolder.addCallback(this);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        thePanelHolder.setFixedSize(getWidth(), getHeight());
        canvasThread = new CanvasThread(thePanelHolder);
        this.setFocusable(false);

        if (is_autoStart) {
            try {
                startPanel();
            } catch (Exception e) {

            }
        }
    }

    protected void onMove(PointF cursor, MotionEvent me) {

    }

    private void onDown(PointF cursor, MotionEvent me) {

    }

    protected void onUp(PointF cursor) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        final PointF tch = new PointF(me.getX(), me.getY());
        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onDown(tch, me);
                break;
            case MotionEvent.ACTION_UP:
                onUp(tch);
                return false;
            //  break;
            case MotionEvent.ACTION_MOVE:
                onMove(tch, me);
                break;
        }
        return true; //true to make it continue to loop over and over again
    }

    protected void load_points() {

    }

    protected void save_points() {

    }

    protected void trace_msg(CharSequence t) {
        Toast.makeText(getContext(), "Panel: " + t, Toast.LENGTH_SHORT).show();
    }

    protected void trace_msg(int id) {
        String t = getResources().getString(id);
        Toast.makeText(getContext(), "Panel: " + t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        load_points();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
        save_points();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //  trace_msg("surfaceDestroyed");
        // canvasThread.setRunning(false);
        /*if (!canvasThread.isInterrupted())
            canvasThread.interrupt();*/
    }

    public void pause() {
        canvasThread.onPause();
    }

    public void resumeHandsUp() {
        canvasThread.onPause();
    }

    public void resume() {
        canvasThread.onResume();
    }

    @Override
    protected void onDraw(Canvas inputcanvas) {

    }

    private class CanvasThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean isRun;
        private Object mPauseLock;
        private boolean mPaused;

        public CanvasThread(SurfaceHolder h) {
            this.surfaceHolder = h;
            mPauseLock = new Object();
            mPaused = false;
            isRun = false;
        }

        public boolean isRun() {
            return isRun;
        }

        /**
         * Call this on pause.
         */
        public void onPause() {
            synchronized (mPauseLock) {
                mPaused = true;
            }
        }

        /**
         * Call this on resume.
         */
        public void onResume() {
            synchronized (mPauseLock) {
                mPaused = false;
                mPauseLock.notifyAll();
            }
        }

        public void setRunning(boolean run) {
            this.isRun = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (isRun) {
                c = null;
                if (!surfaceHolder.getSurface().isValid()) {
                    continue;
                } else {
                    try {
                        c = this.surfaceHolder.lockCanvas(null);
                        synchronized (this.surfaceHolder) {
                           // SimpleSurfacePanel.this.onDraw(c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (surfaceHolder.getSurface().isValid()) {
                            surfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
                //do stuffs
                // structure for cutting the pause
                // http://stackoverflow.com/questions/6776327/how-to-pause-resume-thread-in-android
                synchronized (mPauseLock) {
                    while (mPaused) {
                        try {
                            mPauseLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
