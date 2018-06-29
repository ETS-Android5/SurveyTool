package com.hkm.surveytool.Element.DataPoints;

import java.util.EnumSet;

import static com.hkm.surveytool.Element.DataPoints.SymbolC.tag.ROW_NUMBER_SET_A;
import static com.hkm.surveytool.Element.DataPoints.SymbolC.tag.ROW_NUMBER_SET_B;
import static com.hkm.surveytool.Element.DataPoints.SymbolC.tag.ROW_NUMBER_SET_C;
import static com.hkm.surveytool.Element.DataPoints.SymbolC.tag.ROW_NUMBER_SET_D;

public class SymbolC {
    public static final String ROW_NOT_COMPLETE_TEXT = "----";
    public static final String DEFAULT_NOTHING = "----";
    public static final double inch_meter_ratio = 0.0254;
    public static final EnumSet<tag> inputtags = EnumSet.of(ROW_NUMBER_SET_A, ROW_NUMBER_SET_B, ROW_NUMBER_SET_C, ROW_NUMBER_SET_D);

    public static enum tag {
        TAG_NONE,
        LABEL_CONTROL, ROW_NUMBER_SET_A, ROW_NUMBER_SET_B, ROW_NUMBER_SET_C, ROW_NUMBER_SET_D,
        CANCEL, YES, EDIT,
        C_EDIT, C_REMOVE, C_INSERT, C_OK, C_LINECUT;
    }


    public static enum status {
        INITIAL, FOCUS, COMPLETE, INCOMPLETE, MODIFY, DEFAULT;
    }

    public static enum breaktype {
        // mMarkType= 0 that is LINE
        // mMarkType= 1 that is BREAK POINT
        // mMarkType= 2 that is FEATURE there is no single marker now
        CONTINUE, BREAK_POINT, WITH_ICON;
    }

    public static enum indicator {
        NA, FINAL, CHANGED, INVALID
    }


}
