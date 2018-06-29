package com.hkm.listviewhkm.RNList;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.hkm.listviewhkm.holder.Pattern;
import com.hkm.listviewhkm.model.RouteNode;
import com.hkm.listviewhkm.model.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Hesk on 30/6/2014.
 */
public class EditPlainAdapter extends CursorAdapter {
    protected int layout;
    protected ArrayList<RouteNode> rawdata;
    protected boolean isOnStateAddNew, isLockedForAddingNew, list_changed;
    protected int orientation, lock_open_position = -1, mSelectedPosition = -1, mRemoved = -1;
    protected HashMap<Integer, Pattern> patternlist = new HashMap<Integer, Pattern>();
    protected RowListener mRowListener;
    protected Context ctx;

    public EditPlainAdapter(int layout, Context context) {
        super(context, newCursor());
        this.layout = layout;
        ctx = context;
        isLockedForAddingNew = false;
        isOnStateAddNew = false;
        list_changed = false;
    }

    public EditPlainAdapter(int layout, Context context, ArrayList<RouteNode> c) {
        super(context, fromArrayList(c));
        this.layout = layout;
        this.rawdata = c;
        ctx = context;
        isLockedForAddingNew = false;
        isOnStateAddNew = false;
        list_changed = false;
    }

    public EditPlainAdapter(int layout, Context context, ArrayList<RouteNode> c, boolean autoRequery) {
        super(context, fromArrayList(c), autoRequery);
        this.layout = layout;
        this.rawdata = c;
        ctx = context;
        isLockedForAddingNew = false;
        isOnStateAddNew = false;
        list_changed = false;

    }

    public EditPlainAdapter(int layout, Context context, ArrayList<RouteNode> c, int flags) {
        super(context, fromArrayList(c), flags);
        this.layout = layout;
        this.rawdata = c;
        ctx = context;
        isLockedForAddingNew = false;
        isOnStateAddNew = false;
        list_changed = false;
    }

    public static MatrixCursor newCursor() {
        String[] columnNames = {"_id", "rn_id", "Label"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        return cursor;
    }

    public static MatrixCursor fromArrayList(ArrayList<RouteNode> nodelist) {
        MatrixCursor cursor = newCursor();
        //    String[] array = getResources().getStringArray(R.array.allStrings); //if strings are in resources
        String[] tempRow = new String[3];
        int id = 0;
        for (RouteNode item : nodelist) {
            id++;
            tempRow[0] = Integer.toString(id);
            tempRow[1] = Integer.toString(item.get_index());
            tempRow[2] = item.get_label().get_display_big_button_label();
            cursor.addRow(tempRow);
        }
        // String[] from = {"text"};
        // int[] to = {R.id.name_entry};
        return cursor;
    }

    public void mark(int pos, boolean mark) {
        rawdata.get(pos).set_cut(mark);
    }

    public void addItem(int pos) {
        mRowListener.add_position(pos);
        patternlist.clear();
    }

    public void removeItem(int position) {
        rawdata.remove(position);
        mRowListener.remove_position(position);
        patternlist.clear();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    public void setRowListener(RowListener m) {
        this.mRowListener = m;
    }

    public void startLabel(int pos) {
        if (mRowListener != null) mRowListener.startLabelIntent(pos);
        else
            Tool.trace(ctx, "listener did not set now");
    }

    public boolean analyze_distances(String r1, String r2, int pos) {
        if (mRowListener == null) {
            Tool.trace(ctx, "listener did not set now");
            return true;
        } else
            return mRowListener.radius_validation(r1, r2, pos);
    }

    public void onOrientationChange(int w, int orientation) {
        Iterator it = patternlist.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            //   System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //  it.remove(); // avoids a ConcurrentModificationException
            final Pattern e = (Pattern) pairs.getValue();
            e.set_interactive_screen_width(w);
        }
    }

    public EditPlainAdapter bindDataSource(Cursor c, ArrayList<RouteNode> src) {
        changeCursor(c);
        rawdata = src;
        //  notifyDataSetInvalidated();
        return this;
    }

    public EditPlainAdapter resetDataSource() {
        changeCursor(null);
        rawdata = null;
        patternlist.clear();
        return this;
    }

    public interface RowListener {
        public void startLabelIntent(int pos);

        public boolean radius_validation(String report_measure1, String report_measure2, int pos);

        public boolean remove_position(int pos);

        public boolean add_position(int pos);
    }
}
