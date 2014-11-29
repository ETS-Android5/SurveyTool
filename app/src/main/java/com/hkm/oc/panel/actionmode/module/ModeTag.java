package com.hkm.oc.panel.actionmode.module;

/**
 * Created by hesk on 6/1/2014.
 */
public class ModeTag {
    public String submode;
    public boolean closeReady;
    public Tag now;

    public ModeTag() {

    }

    public static enum Tag {
        AB_1_MODE,
        AB_2_MODE,
        TP_MODE,
        SB_MODE,
        AB_LABEL,
        NODE_INSPECTOR
    }
}
