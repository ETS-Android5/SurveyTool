package com.hkm.datamodel;

import android.graphics.Matrix;

/**
 * Created by Hesk on 27/3/2014.
 */
public class LabelRenderObject {
    private Matrix matrix_rotation;
    private Label lbl;

    public LabelRenderObject(Label labelm, Matrix m) {
        final Matrix ma = new Matrix();
        ma.set(m);
        lbl = labelm;
        matrix_rotation = ma;
    }

    public Label getLbl() {
        return lbl;
    }

    public Matrix getMatrix() {
        return matrix_rotation;
    }
}
