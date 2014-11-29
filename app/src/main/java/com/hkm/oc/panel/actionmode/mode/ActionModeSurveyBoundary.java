package com.hkm.oc.panel.actionmode.mode;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hkm.oc.R;
import com.hkm.oc.panel.actionmode.module.PanelActionModeExt;
import com.hkm.oc.panel.actionmode.module.ModeTag;
import com.hkm.oc.panel.basic_panel_support;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.panel.corepanel.elements.SurveyBoundary;
import com.hkm.U.Tool;
import com.hkm.oc.preF.f.WorkPanelFragment;

/**
 * Created by Hesk on 26/6/2014.
 */
public class ActionModeSurveyBoundary extends PanelActionModeExt {
    private MapPanel.CMode currentstoredmode;

    public ActionModeSurveyBoundary(WorkPanelFragment f, basic_panel_support r) {
        super(f, r);
    }

    // Called when the action mode is created; startActionMode() was called
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        final MenuInflater inf = mode.getMenuInflater();
        // Assumes that you have "contexual.xml" menu resources
        inf.inflate(R.menu.sb_menu, menu);
        currentstoredmode = panel.getMode();
        panel.setMode(MapPanel.CMode.SB_MODE);
        final SurveyBoundary sb = panel.getSB();
        sb.onMode(true);
        initTag(mode, ModeTag.Tag.SB_MODE, sb.isDone());
        return true;
    }

    /**
     * Called each time the action mode is shown. Always called after
     * onCreateActionMode, but
     * may be called multiple times if the mode is invalidated.
     */
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        panel.setMode(MapPanel.CMode.SB_MODE);
        return false; // Return false if nothing is done
    }

    /**
     * public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
     * // mode.setTitle(selCount + " selected");
     * // mode.invalidate();  // Add this to Invalidate CAB
     * }
     */

    // Called when the user selects a contextual menu item
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        final SurveyBoundary sb = panel.getSB();
        switch (item.getItemId()) {
            case R.id.act_accept:
                if (!sb.done()) {
                    Tool.trace(root_context, R.string.surveypathdone);
                } else {
                    Tool.trace(root_context, R.string.surveypath);
                    panel.resume();

                    changeClose(mode, true);
                }
                if (sb.isDone()) {
                    mode.finish();
                }
                return true;
            case R.id.act_cancel:
                sb.goback();
                return true;
            case R.id.act_reset:
                sb.clear();
                //    mActionModeCloseReady = true;
                changeClose(mode, true);
                Tool.trace(root_context, R.string.sbreset);
                return true;
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    public void onDestroyActionMode(ActionMode mode) {
        panel.setMode(currentstoredmode);
        final SurveyBoundary sb = panel.getSB();
        sb.onMode(false);
    }
}

