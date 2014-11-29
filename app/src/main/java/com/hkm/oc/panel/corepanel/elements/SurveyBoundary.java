package com.hkm.oc.panel.corepanel.elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;
import com.hkm.U.Tool;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hesk on 2013/10/4
 */
public class SurveyBoundary implements Serializable {


    @SerializedName("pointlist")
    private ArrayList<PointF> containDots = new ArrayList<PointF>();

    transient private Canvas main;
    transient private boolean closegap, onSBmode;
    transient private Context ctx;
    transient private boolean enabled_survey_boundary = false;
    transient private Pen depen;
    transient private Paint p_default;

    public SurveyBoundary(Canvas mainCanvas, Context mctx, Pen mpen) {
        this.initialize(mainCanvas, mctx, mpen);
    }

    public SurveyBoundary initialize(Canvas mainCanvas, Context mctx, Pen mpen) {
        main = mainCanvas;
        ctx = mctx;
        depen = mpen;
        closegap = onSBmode = enabled_survey_boundary = false;
        p_default = mpen.getSurveyBoundaryPaint();
        return this;
    }

    public SurveyBoundary start() {
        if (containDots.size() > 0)
            closegap = true;
        return this;
    }

    public boolean has_survey_boundary_legend() {
        return enabled_survey_boundary;
    }

    public void onMode(boolean b) {
        onSBmode = b;
    }

    public void clear() {
        containDots.clear();
        closegap = false;
        enabled_survey_boundary = false;
    }

    public void add(PointF point) {
        if (!closegap) {
            containDots.add(point);
            enabled_survey_boundary = true;
        } else {
            Tool.trace(ctx, "You have completed the survey boundary.");
        }
    }

    public void goback() {
        closegap = false;
        if (containDots.size() > 0) {
            containDots.remove(containDots.size() - 1);
        } else {
            enabled_survey_boundary = false;
        }
    }

    public boolean isDone() {
        return closegap;
    }

    // trigger action of DONE and the return back if this action is successful or not
    public boolean done() {
        if (containDots.size() > 0) {
            if (!closegap) {
                add(containDots.get(0));
                closegap = true;
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private void rendering() {
        final int size = containDots.size();
        final Path pan = new Path();
        if (size > 0 && (closegap || onSBmode)) {
            for (int i = 0; i < size; i++) {
                final PointF position_object = containDots.get(i);
                if (i == 0) {
                    pan.moveTo(position_object.x, position_object.y);
                }
                if (i > 0) {
                    pan.lineTo(position_object.x, position_object.y);
                }
            }
            main.drawPath(pan, p_default);
        }
    }

    public void updateCanvas(Canvas cv) {
        main = cv;
    }

    public void renderPath() {
        rendering();
    }

    public void renderPath(Canvas pointer_reference) {
        final Canvas cache_pointer = main;
        main = pointer_reference;
        rendering();
        main = cache_pointer;
    }
}
