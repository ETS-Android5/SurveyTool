package com.hkm.datamodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import com.google.gson.annotations.SerializedName;
import com.hkm.U.Constant;
import com.hkm.U.Tool;

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

    /**
     * This is the final step to find all other data in the data binding.
     * The text color will be generated
     * everything should be able to called after this
     */
    public void refresh_data(Context ctx) {
        final Typeface font = Typeface.create("Arial", Typeface.NORMAL);
        final String color_name = DataHandler.get_label_color_instrinsic(lineLabelInt);
        line_label_text = DataHandler.get_line_label_by_instrinsic(lineLabelInt);
        paint_color = Tool.getPaintColorCodeByName(ctx, color_name);
        textColor = new Paint();
        textColor.setColor(paint_color);
        textColor.setTypeface(font);
        textColor.setTextSize(15f);
        textColor.setAntiAlias(true);
        // tex textPaint = textView.getPaint();
        textColor.getTextBounds(line_label_text, 0, line_label_text.length(), bounds);

        strokeColor = new Paint();
        // strokeColor.setColor(Color.BLACK);
        strokeColor.setColor(paint_color);
        strokeColor.setStyle(Paint.Style.STROKE);
        strokeColor.setAntiAlias(true);
        strokeColor.setStrokeWidth(1.1f);


        if (is_sharp()) {
            final String png_filename = DataHandler.get_1_item(Constant.MapData.InternalIconList[letterIntrinsic]);
            final int g = ctx.getResources().getIdentifier(png_filename, "drawable", ctx.getPackageName());
            final BitmapDrawable feature_icon_drawable = (BitmapDrawable) ctx.getResources().getDrawable(g);
            final int h = feature_icon_drawable.getIntrinsicHeight();
            final int w = feature_icon_drawable.getIntrinsicWidth();
            if (h == w) {
                iSize = size_sq;
            } else {
                iSize = size_rect;
            }
            feature_icon_bitmap = BitmapFactory.decodeResource(ctx.getResources(), g);
            feature_icon_bitmap = Bitmap.createScaledBitmap(feature_icon_bitmap, iSize.x, iSize.y, false);
            bitmap_ResId = g;
        } else {
            final String png_filename = DataHandler.get_1_item(Constant.MapData.InternalIconList[lineLabelInt]);
            final int g = ctx.getResources().getIdentifier(png_filename, "drawable", ctx.getPackageName());
            bitmap_ResId = g;
            feature_icon_bitmap = null;
        }
    }

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
