package com.hkm.root.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;

import com.hkm.U.Content;
import com.hkm.datamodel.RouteNode;

import java.util.ArrayList;

/**
 * Created by Hesk on 30/6/2014.
 */
public class NAdapter extends CursorAdapter implements Filterable, DragSortListView.DragSortListener {
    /**
     * @param context
     * @param c
     * @deprecated
     */
    public static final int REMOVED = -1;
    public static String TAG = "customecusoradapter";
    /**
     * Key is ListView position, value is Cursor position
     */
    private SparseIntArray mListMapping = new SparseIntArray();
    private ArrayList<Integer> mRemovedCursorPositions = new ArrayList<Integer>();
    private Context context;
    private int layout;

    public NAdapter(int layout, Context context, Cursor c) {
        super(context, c);
        this.layout = layout;
        this.context = context;
    }

    public NAdapter(int layout, Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.layout = layout;
        this.context = context;
    }

    public NAdapter(int layout, Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.layout = layout;
        this.context = context;
    }


    /**
     * Changes Cursor and clears list-Cursor mapping.
     *
     * @see android.widget.CursorAdapter#changeCursor(android.database.Cursor)
     */
    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        resetMappings();
    }

    private void resetMappings() {
        mListMapping.clear();
        mRemovedCursorPositions.clear();
    }

    /**
     * Swaps Cursor and clears list-Cursor mapping.
     *
     * @see android.widget.CursorAdapter#swapCursor(android.database.Cursor)
     */
    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor old = super.swapCursor(newCursor);
        resetMappings();
        return old;
    }

    /**
     * Resets list-cursor mapping.
     */
    public void reset() {
        resetMappings();
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(mListMapping.get(position, position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(mListMapping.get(position, position));
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(mListMapping.get(position, position), convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(mListMapping.get(position, position), convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Cursor c = getCursor();
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, viewGroup, false);
        int index = c.getInt(0);
        Pattern p = new Pattern(index, context);
        p.addConvertViewToElements(v).bind_data2display(Content.current_sketch_map.get_route_node_at(index));
        p.startEngine();
        /**
         * Next set the name of the entry.
         */
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        //int nameCol = c.getColumnIndex(Contacts.People.NAME);
        //String name = c.getString(nameCol);
        int index = c.getInt(0);
        RouteNode rn = Content.current_sketch_map.get_route_node_at(index);
        Log.d(TAG, rn.get_label().get_display_big_button_label());
        Pattern p = new Pattern(index, context);
        p.addConvertViewToElements(view).bind_data2display(rn);
        p.startEngine();
    }

    /**
     * On drop, this updates the mapping between Cursor positions
     * and ListView positions. The Cursor is unchanged. Retrieve
     * the current mapping with {@link getCursorPositions()}.
     *
     * @see DragSortListView.DropListener#drop(int, int)
     */
    @Override
    public void drop(int from, int to) {
        if (from != to) {
            int cursorFrom = mListMapping.get(from, from);

            if (from > to) {
                for (int i = from; i > to; --i) {
                    mListMapping.put(i, mListMapping.get(i - 1, i - 1));
                }
            } else {
                for (int i = from; i < to; ++i) {
                    mListMapping.put(i, mListMapping.get(i + 1, i + 1));
                }
            }
            mListMapping.put(to, cursorFrom);

            cleanMapping();
            notifyDataSetChanged();
        }
    }

    /**
     * On remove, this updates the mapping between Cursor positions
     * and ListView positions. The Cursor is unchanged. Retrieve
     * the current mapping with {@link getCursorPositions()}.
     *
     * @see DragSortListView.RemoveListener#remove(int)
     */
    @Override
    public void remove(int which) {
        int cursorPos = mListMapping.get(which, which);
        if (!mRemovedCursorPositions.contains(cursorPos)) {
            mRemovedCursorPositions.add(cursorPos);
        }

        int newCount = getCount();
        for (int i = which; i < newCount; ++i) {
            mListMapping.put(i, mListMapping.get(i + 1, i + 1));
        }

        mListMapping.delete(newCount);

        cleanMapping();
        notifyDataSetChanged();
    }


    /**
     * Does nothing. Just completes DragSortListener interface.
     */
    @Override
    public void drag(int from, int to) {
        // do nothing
    }

    /**
     * Remove unnecessary mappings from sparse array.
     */
    private void cleanMapping() {
        ArrayList<Integer> toRemove = new ArrayList<Integer>();

        int size = mListMapping.size();
        for (int i = 0; i < size; ++i) {
            if (mListMapping.keyAt(i) == mListMapping.valueAt(i)) {
                toRemove.add(mListMapping.keyAt(i));
            }
        }

        size = toRemove.size();
        for (int i = 0; i < size; ++i) {
            mListMapping.delete(toRemove.get(i));
        }
    }

    @Override
    public int getCount() {
        return super.getCount() - mRemovedCursorPositions.size();
    }


    /**
     * Get the Cursor position mapped to by the provided list position
     * (given all previously handled drag-sort
     * operations).
     *
     * @param position List position
     * @return The mapped-to Cursor position
     */
    public int getCursorPosition(int position) {
        return mListMapping.get(position, position);
    }

    /**
     * Get the current order of Cursor positions presented by the
     * list.
     */
    public ArrayList<Integer> getCursorPositions() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < getCount(); ++i) {
            result.add(mListMapping.get(i, i));
        }
        return result;
    }

    /**
     * Get the list position mapped to by the provided Cursor position.
     * If the provided Cursor position has been removed by a drag-sort,
     * this returns {@link #REMOVED}.
     *
     * @param cursorPosition A Cursor position
     * @return The mapped-to list position or REMOVED
     */
    public int getListPosition(int cursorPosition) {
        if (mRemovedCursorPositions.contains(cursorPosition)) {
            return REMOVED;
        }

        int index = mListMapping.indexOfValue(cursorPosition);
        if (index < 0) {
            return cursorPosition;
        } else {
            return mListMapping.keyAt(index);
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) {
            return getFilterQueryProvider().runQuery(constraint);
        }

        StringBuilder buffer = null;
        String[] args = null;
        if (constraint != null) {
            buffer = new StringBuilder();
            buffer.append("UPPER(");
            buffer.append(Contacts.People.NAME);
            buffer.append(") GLOB ?");
            args = new String[]{constraint.toString().toUpperCase() + "*"};
        }

        return context.getContentResolver().query(Contacts.People.CONTENT_URI, null,
                buffer == null ? null : buffer.toString(), args, Contacts.People.NAME + " ASC");
    }
}
