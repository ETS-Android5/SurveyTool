package com.hkm.listviewhkm.model;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.hkm.listviewhkm.RNList.EditPlainAdapter;

import java.util.ArrayList;

/**
 * Created by Hesk on 30/6/2014.
 */
public class DataListLoader extends AsyncTaskLoader<Cursor> {
    protected Cursor cursorSet;
    protected ArrayList<RouteNode> list;

    public DataListLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        System.out.println("DataListLoader.loadInBackground");
        // You should perform the heavy task of getting data from
        // Internet or database or other source
        // Here, we are generating some Sample data
        // if (Content.current_sketch_map.get_route_size() == 0)
         // Content.create_sample_list_items();
        // Create corresponding array of entries and load with data.
        list = Content.current_sketch_map.getRouteNodeList();
        Cursor cursor_output = EditPlainAdapter.fromArrayList(list);
        return cursor_output;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(Cursor cursor_output) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (cursor_output != null) {
                onReleaseResources(cursor_output);
            }
        }
        Cursor oldApps = cursor_output;
        cursorSet = cursor_output;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(cursor_output);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (cursorSet != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(cursorSet);
        }

        if (takeContentChanged() || cursorSet == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(Cursor apps) {
        super.onCanceled(apps);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(apps);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (cursorSet != null) {
            onReleaseResources(cursorSet);
            cursorSet = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(Cursor apps) {
    }

}