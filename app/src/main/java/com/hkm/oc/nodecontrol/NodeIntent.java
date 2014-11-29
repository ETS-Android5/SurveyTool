package com.hkm.oc.nodecontrol;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hkm.U.Constant;
import com.hkm.U.Content;
import com.hkm.datamodel.RouteNode;
import com.hkm.listviewhkm.LabelIntent;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.root.Dialog.activity_control;
import com.hkm.root.adapters.Pattern;
import com.hkm.root.adapters.RouteNodeAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hesk on
 * this is the currently working list view for the testing results
 */
public class NodeIntent extends BasicNodeIntent {
    DataListFragment list;

    //the label management activity will handle the input target list index to show the labels about the selected row
    private int target_action_on_list_index = -1;

    /**
     * This is the configuration for new config
     *
     * @param c
     */
    @Override
    public void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c);
        final RouteNodeAdapter ad = list.getRAdapter();
        ad.onOrientationChange(c.orientation);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
      /*  super.onSaveInstanceState(savedInstanceState);*/
        // savedInstanceState.putInt("theme", mCurTheme);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
     /*   super.onRestoreInstanceState(savedInstanceState);*/
        // Restore state members from saved instance
      /*  mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);*/
    }

    private void update_route_list() {
        final RouteNodeAdapter m_route_ad = list.getRAdapter();
        m_route_ad.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iReturn) {
        super.onActivityResult(requestCode, resultCode, iReturn);
        if (requestCode == Constant.IntentKey.LABEL_AND_LEGEND_PICKER && resultCode == RESULT_OK) {
            update_route_list();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        final String DTag = dialog.getTag();
        if (DTag == Constant.DialogCallBack.BOOLEAN) {
        }
        root_content.getActionBar().show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        final String dtag = dialog.getTag();
        // User touched the dialog's negative button
        root_content.getActionBar().show();
    }

    @Override
    public void onDialogNeutral(DialogFragment dialog) {
        final String dtag = dialog.getTag();
        // getActionBar().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // Create the list fragment and add it as our sole content.
            Content.require_update_depth = false;
            Content.require_update_pos = false;
            highlight(true);
            list = new DataListFragment();
            root_content.getActionBar().setDisplayHomeAsUpEnabled(true);
            fm.beginTransaction().add(R.id.mlist_container, list).commit();
        } else {

        }
    }

    /**
     * when this activity is closing
     */
    @Override
    public void finish() {
        final RouteNodeAdapter ad = list.getRAdapter();
        if (ad.isCompleted()) {
            // Prepare data intent
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putBoolean("route_updated", true);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            highlight(false);
            super.finish();
        } else {
            Tool.trace(this, "please complete the fields first");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_row:
                try {
                    list.getRAdapter().addnew();
                    // list.mAdapter.addnew();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case android.R.id.home:
                getActionBar().setHomeButtonEnabled(false);
                finish();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.routelistmanagement, menu);
        try {
            final Bundle data = this.getIntent().getExtras();
            if (!data.getBoolean("enable_add_new", true)) {
                MenuItem item_new_row = menu.findItem(R.id.add_new_row);
                item_new_row.setVisible(false);
            }
        } catch (Exception e) {

        } finally {
            return true;
        }
    }

    private void highlight(final boolean enter) {
        if (root_content.getIntent().getExtras() != null) {
            final int inspect_point_index = root_content.getIntent().getExtras().getInt("inspect", -1);
            if (inspect_point_index > -1) {
                RouteNode rn = Content.current_sketch_map.get_route_node_at(inspect_point_index);
                if (enter)
                    rn.setHighLight(true);
                else
                    rn.setHighLight(false);
            }
        }

    }

    public static class DataListLoader extends AsyncTaskLoader<List<Pattern>> {
        List<Pattern> mPatterns;

        public DataListLoader(Context context) {
            super(context);
        }

        @Override
        public List<Pattern> loadInBackground() {
            System.out.println("DataListLoader.loadInBackground");

            // You should perform the heavy task of getting data from
            // Internet or database or other source
            // Here, we are generating some Sample data

            // Create corresponding array of entries and load with data.
            List<Pattern> entries = new ArrayList<Pattern>(5);


            return entries;
        }

        /**
         * Called when there is new data to deliver to the client.  The
         * super class will take care of delivering it; the implementation
         * here just adds a little more logic.
         */
        @Override
        public void deliverResult(List<Pattern> listOfData) {
            if (isReset()) {
                // An async query came in while the loader is stopped.  We
                // don't need the result.
                if (listOfData != null) {
                    onReleaseResources(listOfData);
                }
            }
            List<Pattern> oldApps = listOfData;
            mPatterns = listOfData;

            if (isStarted()) {
                // If the Loader is currently started, we can immediately
                // deliver its results.
                super.deliverResult(listOfData);
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
            if (mPatterns != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(mPatterns);
            }

            if (takeContentChanged() || mPatterns == null) {
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
        public void onCanceled(List<Pattern> apps) {
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
            if (mPatterns != null) {
                onReleaseResources(mPatterns);
                mPatterns = null;
            }
        }

        /**
         * Helper function to take care of releasing resources associated
         * with an actively loaded data set.
         */
        protected void onReleaseResources(List<Pattern> apps) {
        }

    }

    @SuppressLint("ValidFragment")
    public class DataListFragment extends ListFragment implements activity_control, LoaderManager.LoaderCallbacks<List<Pattern>> {


        private RouteNodeAdapter mRouteAd;

        /*       private DotAdapter mAdapter;
          public DotAdapter getAdapter() {
                   return mAdapter;
               }
       */
        public RouteNodeAdapter getRAdapter() {
            return mRouteAd;
        }

        /**
         * this is the implementation from the Pattern Class
         *
         * @param type
         */
        @Override
        public void onListChanged(int type) {

        }

        /**
         * this is the implementation from the Pattern Class
         *
         * @param action_on_position_radius_list
         */
        @Override
        public void onPickLegend(int action_on_position_radius_list) {
            //  NodeIntent.this.legend_picker(action_on_position_radius_list);
            Intent i = new Intent(NodeIntent.this, LabelIntent.class);
            Bundle b = new Bundle();
            NodeIntent.this.target_action_on_list_index = action_on_position_radius_list;
            b.putInt("listid", action_on_position_radius_list);
            i.putExtras(b);
            NodeIntent.this.startActivityForResult(i, Constant.IntentKey.LABEL_AND_LEGEND_PICKER);
            // Log.d("START", "Get Pos at:" + "filter_letter " + " , NodeIntent.this.legend_picker(\"\", \"\");");
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // Initially there is no data
            setEmptyText("No Data Here");
            // Create an empty adapter we will use to display the loaded data.


            mRouteAd = new RouteNodeAdapter(getActivity(), Content.current_sketch_map.getRouteNodeList());
            setListAdapter(mRouteAd);
            mRouteAd.setAdapterListener(this);
            //the first off line version will use the data to be saved.
            final boolean hasToLoadData = false;
            if (!hasToLoadData) {
                setListShown(true);
            } else {
                // Start out with a progress indicator.
                setListShown(false);
                // Prepare the loader.  Either re-connect with an existing one,
                getLoaderManager().initLoader(0, null, this);

            }
            if (Content.changed_to_new_ab_points) {
                Content.changed_to_new_ab_points = false;
            }
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // Insert desired behavior here.
            Log.i("DataListFragment", "Item clicked: " + id);
        }

        @Override
        public Loader<List<Pattern>> onCreateLoader(int arg0, Bundle arg1) {
            System.out.println("DataListFragment.onCreateLoader");
            return new DataListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<Pattern>> arg0, List<Pattern> data) {
            //  mAdapter.setData(data);
            System.out.println("DataListFragment.onLoadFinished");
            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Pattern>> arg0) {
            //  mAdapter.setData(null);
        }

        private class itemClickListener implements AdapterView.OnItemLongClickListener {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return true;
            }
        }
    }
}
