package com.hkm.listviewhkm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hkm.U.Content;
import com.hkm.datamodel.RouteNode;
import com.hkm.listviewhkm.base.DialogIntent;
import com.hkm.listviewhkm.model.DataListFragment;
import com.hkm.listviewhkm.model.RouteNodeControl;
import com.hkm.oc.R;
import com.hkm.U.Tool;

/**
 * Created by Hesk on 30/6/2014.
 */
public class NodeList extends DialogIntent {

    //https://github.com/bauerca/drag-sort-listview/blob/master/library/src/com/mobeta/android/dslv/DragSortCursorAdapter.java#L40
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setContentView(R.layout.datalist_route);
            Content.require_update_depth = false;
            Content.require_update_pos = false;
            highlight(true);
            list = new DataListFragment();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void highlight(final boolean enter) {
        if (actintent.getExtras() != null) {
            final int inspect_point_index = actintent.getExtras().getInt("inspect", -1);
            if (inspect_point_index > -1) {
                RouteNode rn = Content.current_sketch_map.get_route_node_at(inspect_point_index);
                if (enter)
                    rn.setHighLight(true);
                else
                    rn.setHighLight(false);
            }
        }
    }

    private void homeclick() {
        try {
            if (RouteNodeControl.is_complete_list()) {
                highlight(false);
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean("route_updated", RouteNodeControl.has_changes());
                data.putExtras(bundle);
                setResult(RESULT_OK, data);
                getActionBar().setHomeButtonEnabled(false);
                finish();
            } else {
               /* DialogSimpleNotification d = DialogSimpleNotification.newInstance(R.string.notice_node_exit_error);
                d.show(fm, "gobackdialog");*/
                Tool.trace(root_content, R.string.notice_node_exit_error);
            }
        } catch (Exception e) {
            Tool.trace(root_content, e.toString());
        }
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
                break;
            case android.R.id.home:
                homeclick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.routelistmanagement, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        return true;
    }

}
