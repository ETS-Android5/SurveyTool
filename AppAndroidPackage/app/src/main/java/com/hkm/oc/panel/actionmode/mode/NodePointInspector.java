package com.hkm.oc.panel.actionmode.mode;

import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.U.Content;
import com.hkm.datamodel.RouteNode;
import com.hkm.oc.R;
import com.hkm.oc.panel.actionmode.module.ModeTag;
import com.hkm.oc.panel.actionmode.module.PanelActionModeExt;
import com.hkm.oc.panel.basic_panel_support;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.panel.corepanel.elements.Dot;
import com.hkm.oc.panel.corepanel.elements.Route;
import com.hkm.oc.preF.f.WorkPanelFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Hesk on 25/6/2014.
 */
public class NodePointInspector extends PanelActionModeExt {

    private MapPanel.CMode currentstoredmode;
    private Route mroute;
    private String title_text_display, label_format_string, nothing_title;
    private RouteNode selected_rn;

    private View custom_view_layout;
    private EditText inputbra;
    private TextView title;
    private ImageView src;
    public static String TAG = "nodePointer";

    public NodePointInspector(WorkPanelFragment WPF, basic_panel_support c) {
        super(WPF, c);
        this.mroute = panel.getRoute();
    }


    /**
     * enable and disable dots on the route
     *
     * @param b
     * @return
     */
    public void route_dot_interaction(final boolean b) {
        for (Dot d : mroute.getContainer()) {
            if (b) {
                d.set_action_mode_listener(new Dot.actionmode() {
                    @Override
                    public void select(final RouteNode rn) {
                        root_context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                selected_rn = rn;
                                _model.invalidate();
                            }
                        });
                    }
                });
                d.SelectPoint(Dot.selection.INSPECT);
                d.setLock(false);
            } else {
                d.SelectPoint(Dot.selection.CONFIRM_IS);
                d.setLock(true);
            }
        }
    }

    private void init_input_tp_label(ActionMode mode) {
        try {
            custom_view_layout = LayoutInflater.from(root_context).inflate(R.layout.actionmode_bar, null);
            mode.setCustomView(custom_view_layout);
            src = (ImageView) root_context.findViewById(R.id.icon_legend);
            title = (TextView) root_context.findViewById(R.id.text_bar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Called when the action mode is created; startActionMode() was called
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        currentstoredmode = this.panel.getMode();
        panel.setMode(MapPanel.CMode.NODE_INSPECTOR);
        label_format_string = getTxt(R.string.point_inspector);
        nothing_title = getTxt(R.string.intro_point);
        route_dot_interaction(true);
        initTag(mode, ModeTag.Tag.NODE_INSPECTOR, true);
        title_text_display = nothing_title;
        init_input_tp_label(mode);
        return true;
    }

    private void IntMenu(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        final MenuInflater inf = mode.getMenuInflater();
        // Assumes that you have "contexual.xml" menu resources
        inf.inflate(R.menu.point_inspector, menu);
    }

    private void display_dot_info(ActionMode mode) {
        if (selected_rn != null) {
            final String label = selected_rn.get_label().get_display_big_button_label();
            title_text_display = String.format(label_format_string, label, selected_rn.get_distance_a(), selected_rn.get_distance_b());
            try {
                Picasso.with(root_context).load(selected_rn.get_label().getBitmap_ResId()).centerCrop().fit().into(src);
            } catch (UnsupportedOperationException e) {
                Log.d(TAG, e.toString());
            } catch (RuntimeException e) {
                Log.d(TAG, e.toString());
            } catch (ExceptionInInitializerError e) {
                Log.d(TAG, e.toString());
            } catch (OutOfMemoryError e) {
                Log.d(TAG, e.toString());
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            src.setVisibility(View.VISIBLE);
        } else {
            title_text_display = nothing_title;
            src.setVisibility(View.GONE);
        }

        title.setText(title_text_display);
    }

    /**
     * Called each time the action mode is shown. Always called after
     * onCreateActionMode, but
     * may be called multiple times if the mode is invalidated.
     */
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mode.getMenu().clear();
        IntMenu(mode, menu);
        display_dot_info(mode);
        return true; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_detail:
                if (selected_rn != null) {
                    int select = Content.current_sketch_map.find_node_index(selected_rn);
                    root_context.view_node_list(select);
                }
                break;
        }
        return super.onActionItemClicked(mode, item);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.route_dot_interaction(false);
    }
}