package com.hkm.listviewhkm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hkm.listviewhkm.model.DataListFragment;
import com.hkm.listviewhkm.model.Tool;

/**
 * Created by Hesk on 30/6/2014.
 */
public class TestIntent extends ActionBarActivity {
    protected Context root_content;
    protected DataListFragment list;
    protected FragmentManager fm;

    //https://github.com/bauerca/drag-sort-listview/blob/master/library/src/com/mobeta/android/dslv/DragSortCursorAdapter.java#L40
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root_content = this;
        fm = getSupportFragmentManager();
        if (savedInstanceState == null) {

            setContentView(R.layout.datalist_route);
            //  C.require_update_depth = false;
            //  Constant.require_update_pos = false;
            list = new DataListFragment();
        }
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.mlist_container, list).commit();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Tool.trace(root_content, "memory is now low.. please restart the app");
    }

    @Override
    public void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // savedInstanceState.putInt("theme", mCurTheme);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
      /*  mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);*/
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iReturn) {
        super.onActivityResult(requestCode, resultCode, iReturn);
       /* if (requestCode == Constant.IntentKey.LABEL_AND_LEGEND_PICKER && resultCode == RESULT_OK) {
            update_route_list();
        }*/
    }

    @Override
    public void finish() {
      /*  final RouteNodeAdapter ad = list.getRAdapter();
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
        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_row:
                try {

                    list.add_position();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case android.R.id.home:
                getActionBar().setHomeButtonEnabled(false);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

                getMenuInflater().inflate(R.menu.routelistmanagement, menu);



        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
       /* try {
            final Bundle data = this.getIntent().getExtras();
            if (data != null) {
                if (!data.getBoolean("enable_add_new", true)) {
                    MenuItem item_new_row = menu.findItem(R.id.add_new_row);
                    item_new_row.setVisible(false);
                }
            }
            return true;
        } catch (Exception e) {

        } finally {
            return true;
        }*/
        return true;
    }

}
