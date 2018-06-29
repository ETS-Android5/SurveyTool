package com.hkm.listviewhkm.RNList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.listviewhkm.holder.Pattern;
import com.hkm.listviewhkm.model.C;
import com.hkm.listviewhkm.model.Content;
import com.hkm.listviewhkm.model.RouteNode;

import java.util.ArrayList;

/**
 * Created by Hesk on 30/6/2014.
 */
public class RNAdapter extends EditPlainAdapter {
    public final String TAG = "RNAInspector";

    public RNAdapter(int layout, Context context, ArrayList<RouteNode> c) {
        super(layout, context, c);
    }

    public RNAdapter(int layout, Context context) {
        super(layout, context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Cursor c = getCursor();
        final int n = c.getPosition();
        Log.d(TAG, n + " - create view");
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(layout, null);

        return newPattern(n, context, v);
    }

    protected C.status getStatus() {
        //C.status.INITIAL
        return C.status.COMPLETE;
    }

    protected View newPattern(int n, Context context, View v) {
        final Pattern p = new Pattern(n, context, this);
        p.addConvertViewToElements(v);
        p.set_interactive_screen_width(Content.screensizeX);
        p.setStatus(getStatus());
        p.startEngine();
        patternlist.put(n, p);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Cursor c = getCursor();
        final int n = c.getPosition();
        Log.d(TAG, n + " - bind view");

        //   LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    View v = mInflater.inflate(layout, null);

        RouteNode rn = Content.current_sketch_map.get_route_node_at(n);
        Pattern p = patternlist.get(n);
        if (p == null) {
            newPattern(n, context, view);
            p = patternlist.get(n);
        } else {
            p.getCacheView(view).startEngine();
            p.setStatusNoAnimation();
        }

        //p.addConvertViewToElements(view);

        p.bind_data2display(rn);

    }

}
