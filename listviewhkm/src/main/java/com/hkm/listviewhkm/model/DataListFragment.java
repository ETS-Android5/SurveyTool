package com.hkm.listviewhkm.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hkm.listviewhkm.R;
import com.hkm.listviewhkm.RNList.EditPlainAdapter;
import com.hkm.listviewhkm.RNList.RNAdapter;

/**
 * Created by Hesk on 30/6/2014.
 */

@SuppressLint("ValidFragment")
public class DataListFragment extends ListFragment implements AbsListView.OnScrollListener, LoaderManager.LoaderCallbacks<Cursor>, EditPlainAdapter.RowListener {
    protected RNAdapter mRouteAd;

    protected boolean mIsScrollingUp = false;
    protected int mLastFirstVisibleItem = 0;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        final ListView lw = getListView();

        if (scrollState == 0)
            Log.i("a", "scrolling stopped...");


        if (view.getId() == lw.getId()) {
            final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                mIsScrollingUp = false;
                Log.i("a", "scrolling down...");
            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                mIsScrollingUp = true;
                Log.i("a", "scrolling up...");
            }

            mLastFirstVisibleItem = currentFirstVisibleItem;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c);
        Content.screensizeX = getScreensize();
        mRouteAd.onOrientationChange(Content.screensizeX, c.orientation);
    }

    private int getScreensize() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initially there is no data
        setEmptyText("No Data Here");
        Content.screensizeX = getScreensize();
        // Create an empty adapter we will use to display the loaded data.
        mRouteAd = new RNAdapter(R.layout.datalist_row, this.getActivity());
        mRouteAd.setRowListener(this);
        setListAdapter(mRouteAd);
        getListView().setOnScrollListener(this);
        //the first off line version will use the data to be saved.
        setListShown(false);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        Log.i("DataListFragment", "Item clicked: " + id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
        System.out.println("DataListFragment.onCreateLoader");
        return new DataListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //  mAdapter.setData(data);
        System.out.println("DataListFragment.onLoadFinished");
        // The list should now be shown.
        mRouteAd.bindDataSource(data, Content.current_sketch_map.getRouteNodeList());
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        getListView().setSelection(mLastFirstVisibleItem);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRouteAd.resetDataSource();
        if (loader.getId() == 0) {
            //perhaps do swapCursor(null) on an adapter using this loader
        } else if (loader.getId() == 1) {
            //perhaps do swapCursor(null) on an adapter using this loader
        }
        //getListView().setSelection(0);
    }

    @Override
    public void startLabelIntent(int pos) {

    }

    @Override
    public boolean radius_validation(String report_measure1, String report_measure2, int pos) {
        return true;
    }

    @Override
    public boolean add_position(int pos) {
        RouteNodeControl.addNewSingle(pos);
        if (mLastFirstVisibleItem > 0) mLastFirstVisibleItem--;
        getLoaderManager().restartLoader(0, null, this);
        return false;
    }

    /*add to the last one*/
    public boolean add_position() {
        RouteNodeControl.addNewSingle();
        if (mLastFirstVisibleItem > 0) mLastFirstVisibleItem++;
        getLoaderManager().restartLoader(0, null, this);
        return false;
    }

    @Override
    public boolean remove_position(int pos) {
        getLoaderManager().restartLoader(0, null, this);
        return false;
    }

    private class itemClickListener implements AdapterView.OnItemLongClickListener {
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            return true;
        }
    }

}
