package com.hkm.surveytool.Element.DataPoints;

import android.graphics.Paint;
import android.graphics.Path;

public class Paths {
    private Paint path_paint, text_paint;
    private Path cable_path;
    private String path_label_text;
    private Label label;

    public Paths(Label Path_Label, Path c_path) {
        cable_path = c_path;
        path_label_text = Path_Label.get_line_label_text();
        text_paint = Path_Label.get_line_label_paint();
        path_paint = Path_Label.get_stroke_paint();
        label = Path_Label;
    }

    public Paint getTextPaint() {
        return text_paint;
    }

    public Paint getPaint() {
        return path_paint;
    }

    public Path getPath() {
        return cable_path;
    }

    public String getString() {
        return path_label_text;
    }

    public Label getLabel() {
        return label;
    }
}
