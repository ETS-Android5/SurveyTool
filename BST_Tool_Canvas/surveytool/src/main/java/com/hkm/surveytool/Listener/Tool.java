package com.hkm.surveytool.Listener;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Tool {
    private Activity c;

    public Tool(Activity act_input) {
        this.c = act_input;
    }

    public static void trace(Context ctx, int resId, Object... param) {
        String f = "resId not found";
        try {
            f = ctx.getResources().getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(ctx, f, Toast.LENGTH_LONG).show();
        }
        Toast.makeText(ctx, String.format(f, param), Toast.LENGTH_LONG).show();
    }

    public static void trace(Context ctx, String format, Object... param) {
        Toast.makeText(ctx, String.format(format, param), Toast.LENGTH_LONG).show();
    }

    public static void trace(Context ctx, String str) {
        Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
    }

    public static void trace(Context ctx, int resId) {
        String f = "resId not found";
        try {
            f = ctx.getResources().getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(ctx, f, Toast.LENGTH_LONG).show();
        }
    }

    public static int getPaintColorCodeByName(Context ctx, String name) {
        final int color_code = ctx.getResources().getIdentifier(name, "color", ctx.getPackageName());
        return ctx.getResources().getColor(color_code);
    }

}
