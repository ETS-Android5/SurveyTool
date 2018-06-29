package com.hkm.root.adapters;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;
/**
 * Created by Hesk on 30/6/2014.
 */
public class DragSortListView extends ListView {


    public DragSortListView(Context context) {
        super(context);
    }

    public interface DragListener {
        public void drag(int from, int to);
    }

    /**
     * Your implementation of this has to reorder your ListAdapter!
     * Make sure to call
     * {@link BaseAdapter#notifyDataSetChanged()} or something like it
     * in your implementation.
     *
     * @author heycosmo
     *
     */
    public interface DropListener {
        public void drop(int from, int to);
    }

    /**
     * Make sure to call
     * {@link BaseAdapter#notifyDataSetChanged()} or something like it
     * in your implementation.
     *
     * @author heycosmo
     *
     */
    public interface RemoveListener {
        public void remove(int which);
    }

    public interface DragSortListener extends DropListener, DragListener, RemoveListener {
    }
}
