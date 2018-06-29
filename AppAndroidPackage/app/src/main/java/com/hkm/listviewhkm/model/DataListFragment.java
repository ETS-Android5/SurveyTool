package com.hkm.listviewhkm.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
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

import com.hkm.U.Constant;
import com.hkm.datamodel.BigObserveDot;
import com.hkm.datamodel.RouteNode;
import com.hkm.listviewhkm.LabelIntent;
import com.hkm.listviewhkm.NodeList;
import com.hkm.listviewhkm.RNList.EditPlainAdapter;
import com.hkm.listviewhkm.RNList.RNAdapter;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;
import com.hkm.root.Tasks.taskcb;

import static com.hkm.U.Constant.update_existing_index;
import static com.hkm.U.Content.current_sketch_map;
import static com.hkm.U.Content.require_update_pos;
import static com.hkm.U.Content.saved_route;
import static com.hkm.U.Content.screensizeX;
import static com.hkm.U.Tool.trace;

/**
 * Created by Hesk on 30/6/2014.
 */

@SuppressLint("ValidFragment")
public class DataListFragment extends ListFragment implements AbsListView.OnScrollListener, LoaderManager.LoaderCallbacks<Cursor>, EditPlainAdapter.RowListener {
    public static int LOADER_ID = 854511;
    protected RNAdapter mRouteAd;
    protected boolean mIsScrollingUp = false;
    protected int mLastFirstVisibleItem = 0;
    protected NodeList root_context;
    protected LoaderManager LMgm;

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
        screensizeX = getScreensize();
        mRouteAd.onOrientationChange(screensizeX, c.orientation);
    }

    private int getScreensize() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root_context = (NodeList) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initially there is no data
        setEmptyText("No Data Here");
        screensizeX = getScreensize();
        // Create an empty adapter we will use to display the loaded data.
        mRouteAd = new RNAdapter(R.layout.datalist_row, this.getActivity());
        mRouteAd.setRowListener(this);
        setListAdapter(mRouteAd);
        getListView().setOnScrollListener(this);
        //the first off line version will use the data to be saved.
        setListShown(false);
        LMgm = getLoaderManager();
        LMgm.initLoader(LOADER_ID, null, this);
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
        mRouteAd.bindDataSource(data, current_sketch_map.getRouteNodeList());
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
        final Intent i = new Intent(root_context, LabelIntent.class);
        final Bundle b = new Bundle();
        //final int k = Content.current_sketch_map.get_label_at(pos) == null ? -99 : pos;
        b.putInt("node_index", pos);
        i.putExtras(b);
        //do not put getActivity in here
        startActivityForResult(i, Constant.IntentKey.LABEL_AND_LEGEND_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.IntentKey.LABEL_AND_LEGEND_PICKER && resultCode == Activity.RESULT_OK) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
            Tool.trace(root_context, "in fragment come back from the label intent");
        }
    }

    @Override
    public boolean radius_validation(String report_measure1, String report_measure2, int pos) {
        final BigObserveDot a = current_sketch_map.get_point_a();
        final BigObserveDot b = current_sketch_map.get_point_b();
        final int r1 = EQPool.m2p(Float.parseFloat(report_measure1));
        final int r2 = EQPool.m2p(Float.parseFloat(report_measure2));
        final PointF[] found_pair = EQPool.getLayerIntersectPairES(a, b, r1, r2);
        final boolean valid = found_pair != null;
        final RouteNode rn = current_sketch_map.get_route_node_at(pos);
        rn.set_r(r1, r2);
        if (valid) {
            if (saved_route.size() > 0) {
                /**
                 * adding this update position into the list
                 */
                final int result = update_existing_index.indexOf(pos);
                if (result == -1) {
                    update_existing_index.add(pos);
                    trace(root_context, R.string.set_results);
                } else {
                    trace(root_context, R.string.set_results_updated);
                }
                rn.setIntdicate(C.indicator.CHANGED);
                require_update_pos = true;
            } else {
                rn.setIntdicate(C.indicator.FINAL);
                trace(root_context, R.string.set_result_first_time);
            }
            rn.set_has_result(true);
        } else {
            /** checking of the pair is not found **/
            rn.setIntdicate(C.indicator.INVALID);
            trace(root_context, R.string.no_result_found);
            rn.set_has_result(false);
        }
        return valid;
    }

    @Override
    public boolean add_position(int pos) {
        new ListWorker(pos, LOADER_ID, this, new taskcb() {
            @Override
            public void cbsuccess(String d) {


            }

            @Override
            public void cbfailure(String d) {

            }
        }).execute();
        return false;
    }

    /* Add to the last one */
    public boolean add_position() {
        new ListWorker(-1, LOADER_ID, this, new taskcb() {
            @Override
            public void cbsuccess(String d) {


            }

            @Override
            public void cbfailure(String d) {

            }
        }).execute();
        return false;
    }

    @Override
    public boolean remove_position(int pos) {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return false;
    }

    public RNAdapter getAdapter() {
        return mRouteAd;
    }

    private class itemClickListener implements AdapterView.OnItemLongClickListener {
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            return true;
        }
    }

    public class ListWorker extends AsyncTask<Void, Void, String> {

        private taskcb cb;
        private int pos, Loader_ID;
        private LoaderManager.LoaderCallbacks loadmgmcb;

        public ListWorker(int pos, int Loader_ID, LoaderManager.LoaderCallbacks loadcb, taskcb cb) {
            this.cb = cb;
            this.pos = pos;
            this.Loader_ID = Loader_ID;
            this.loadmgmcb = loadcb;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "done";
            if (pos > 0) {
                RouteNodeControl.addNewSingle(pos, root_context);
            } else {
                RouteNodeControl.addNewSingle(root_context);
            }
            if (mLastFirstVisibleItem > 0) mLastFirstVisibleItem--;
            LMgm.restartLoader(Loader_ID, null, loadmgmcb);
            return result;
        }

        @Override
        protected void onPreExecute() {
            root_context.progress_bar_start(R.string.notice_progress_list);
        }

        @Override
        protected void onPostExecute(String result) {
            root_context.progressBar_dismiss();
            if (result.equals("done")) {
                cb.cbsuccess(result);
            } else {
                cb.cbfailure(result);
            }
        }

    }

}
