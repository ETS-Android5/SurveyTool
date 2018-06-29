package com.hkm.listviewhkm.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hesk on
 */
public class Label implements Serializable {
    @SerializedName("dLetter")
    private String dLetter;
    @SerializedName("dNumeric")
    private String dNumeric;
    @SerializedName("display_big_button_label")
    private String display_big_button_label;
    @SerializedName("line_label_text")
    private String line_label_text;
    @SerializedName("letterIntrinsic")
    private int letterIntrinsic;
    @SerializedName("lineLabelInt")
    private int lineLabelInt;
    @SerializedName("paint_color")
    private int paint_color;

    transient private Paint textColor, strokeColor;
    transient private final Rect bounds = new Rect();
    transient private Matrix rotation;

    public Label() {
        textColor = new Paint();
        strokeColor = new Paint();
    }

    public int get_letterIntrinsic() {
        return letterIntrinsic;
    }

    public String get_dLetter() {
        return dLetter;
    }

    public String get_dNumeric() {
        return dNumeric;
    }


    /**
     * can be both feature type of line type
     *
     * @param index
     */
    public void set_letterIntrinsic(int index) {
        letterIntrinsic = index;
    }

    public void set_lineLabelInt(int index) {
        lineLabelInt = index;
    }

    public void set_dLetter(String index) {
        dLetter = index;
    }

    public void set_dNumeric(String index) {
        dNumeric = index;
    }

    public void set_display_big_button_label(String index) {
        display_big_button_label = index;
    }

    public void set_matrix(Matrix mmatrix) {
        rotation = mmatrix;
    }

    public boolean is_sharp() {
        //only for 0 - 39
        if (letterIntrinsic >= 0 && letterIntrinsic <= 39) {
            return true;
        } else {
            return false;
        }
    }

    private static Point size_sq = new Point(50, 50), size_rect = new Point(80, 50);


    transient private int bitmap_ResId;

    public String get_display_big_button_label() {
        return display_big_button_label;
    }

    public Point get_iSize() {
        return iSize;
    }

    private Point iSize;
    private Bitmap feature_icon_bitmap;

    public int getBitmap_ResId() {
        return bitmap_ResId;
    }

    public Bitmap get_icon_sharp_bitmap() {
        return feature_icon_bitmap;
    }

    /**
     * the paint will be returned
     *
     * @return
     */
    public Paint get_line_label_paint() {
        return textColor;
    }

    public Paint get_stroke_paint() {
        assert strokeColor != null;
        return strokeColor;
    }

    public int get_paint_color() {
        assert paint_color > 0;
        return paint_color;
    }

    public Matrix get_maxtrix() {
        return rotation;
    }

    /**
     * the text label will be returned.
     *
     * @return
     */
    public Point get_text_bound() {
        final int texheight = bounds.height() >> 1; //the half of the height   bounds.height() >> 1  equivalent to bounds.height() /2
        final int texwidth = bounds.width();
        return new Point(texwidth, texheight);
    }

    public String get_line_label_text() {
        return line_label_text;
    }

    public int get_line_label_int() {
        return lineLabelInt;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** LABEL DETAIL *****\n");
        sb.append("distance=" + line_label_text + "," + display_big_button_label + "\n");
        sb.append("*****************************");
        return sb.toString();
    }
}
