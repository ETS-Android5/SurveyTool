package com.hkm.surveytool.Element.DataPoints;

import android.graphics.Point;
import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BigObserveDot extends PointF implements Serializable {
    @SerializedName("label")
    private String label;

    private long set_label_time;

    public BigObserveDot(float x, float y) {
        super(x, y);
    }

    public BigObserveDot(float x, float y, String label) {
        super(x, y);
        this.label = label;
    }

    public BigObserveDot setlabel(String x) {

        final long curr = System.currentTimeMillis();
        label = x;
        set_label_time = curr;
        return this;
    }

   /* @Override
    public final void set(PointF pos) {
        super.set(pos);
    }*/

    public String getlabel() {
        return label == null ? "" : label;
    }

    public PointF asPointF() {
        return new PointF(this.x, this.y);
    }

    public Point asPoint() {
        return new Point((int) this.x, (int) this.y);
    }
}
