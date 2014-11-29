package com.hkm.oc.panel;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hkm.api.handlers.PicaAutoBudgetTransform;
import com.hkm.oc.R;
import com.hkm.oc.preF.f.WorkPanelFragment;
import com.hkm.root.Dialog.DsingleCB;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.hkm.U.Constant.BASEMAPPath;
import static com.hkm.U.Content.current_sketch_map;

/**
 * Created by Hesk in 2013.
 * Developed and Debugged by Hesk
 * All rights reserved
 * <p/>
 * This is the core activity on the one call client application
 * The purpose is used for constructing the graphic for the reporting basemap.
 */
public class loading extends worker {

    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;

    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    protected int windowFeature(int options) {
        return Window.FEATURE_ACTION_BAR;
    }

    @Override
    protected ActionBar configActionBar(final ActionBar ab) {
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        return ab;
    }

    /**
     * Swaps fragments in the main content view
     */

    private void selectItem(int position) {
        // create a new fragment and specify the planet to show based on position
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        //   root_context.getActionBar().setTitle((mPlanetTitles[position]));
        mDrawerLayout.closeDrawer(mDrawerList);
        //   Tool.trace(root_context, position + "");
        switch (position) {
            case 1:
                Take_action_point_inspection();
                break;
            case 2:
                Take_action_tp();
                break;
            case 3:
                Take_action_survey();
                break;
            case 4:
                Take_action_enter_measurement();
                break;
            case 5:
                Take_action_panel_zoom();
                break;
            case 6:
                Take_action_edit_AB_points();
                break;
            case 7:
                Take_action_save_jpg();
                break;
            case 0:
                exit_current_sketch_map();
                break;
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        try {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.edit_map).setVisible(!drawerOpen);
        } catch (Exception e) {
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        try {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        } catch (Exception e) {
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    protected void negviation_drawerCreate() {
        mDrawerLayout = (DrawerLayout) root_context.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(root_context,    /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View drawerView) {
                Log.d(TAG, "x - close it");
                root_context.getActionBar().setTitle("x - close it");
                root_context.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "x - open it");
                root_context.getActionBar().setTitle("x - open it");
                root_context.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerList = (ListView) root_context.findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new menudraweradapter(root_context, setup_menu_display_items(root_context)));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
/*
        new custom_action_bar(this, ab);


*/
        mDrawerLayout.invalidate();

    }

    protected void create_fragment(Bitmap d) {
        final Fragment frag = WorkPanelFragment.newInstance(d, new PanelListener());
        wpf = (WorkPanelFragment) frag;
        // Insert the fragment by replacing any existing fragment
        root_context.getFragmentManager().beginTransaction().replace(R.id.content_frame, frag).commit();
    }


    protected static List<RowItem> setup_menu_display_items(Context context) {
        final String[] menutitles = context.getResources().getStringArray(R.array.map_control_items);
        final TypedArray menuIcons = context.getResources().obtainTypedArray(R.array.icons);
        final List<RowItem> menulist = new ArrayList<RowItem>();
        menulist.add(new RowItem("mainlogo", R.drawable.exitstrategy, RowItem.RowItemType.BIG));
        for (int i = 0; i < menutitles.length; i++) {
            RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(i, -1), RowItem.RowItemType.NORMAL);
            menulist.add(items);
        }
        menuIcons.recycle();
        return menulist;
    }


    public class cbFailureDialogCB implements DsingleCB {
        @Override
        public void oncontified(DialogFragment dialog) {
            root_context.finish();
        }
    }

    @Override
    protected void loadingmain(final Bundle states, final Bundle extras) {
        progress_bar_start(R.string.load_image);
        final File cacheDir = new File(BASEMAPPath);
        //todo there are bugs need to solve from loading image problem
        final Picasso loader = new Picasso.Builder(root_context).downloader(new OkHttpDownloader(cacheDir)).build();
        Uri map_official = current_sketch_map.get_base_map_url();
        loader.with(root_context).load(map_official).transform(new PicaAutoBudgetTransform(2000, true)).into(target);
        /*final taskbitmap task_cb = new taskbitmap() {
            @Override
            public void cbsuccess(final Bitmap d) {
                root_context.setContentView(R.layout.native_plot_map);
                negviation_drawerCreate();
                create_fragment(d);
                root_context.progressBar_dismiss();
            }
            @Override
            public void cbfailure(Exception d) {
                Log.d(TAG, d.toString());
                root_context.progressBar_dismiss(d.toString(), new cbFailureDialogCB());
            }
        };
        new loadbasemap(task_cb, ac, root_context).execute();*/
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            root_context.setContentView(R.layout.native_plot_map);
            negviation_drawerCreate();
            create_fragment(bitmap);
            root_context.progressBar_dismiss();
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            root_context.progressBar_dismiss("cannot load the image.", new cbFailureDialogCB());
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {

        }
    };

}
