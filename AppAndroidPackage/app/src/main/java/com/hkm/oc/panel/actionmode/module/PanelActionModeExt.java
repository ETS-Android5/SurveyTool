package com.hkm.oc.panel.actionmode.module;

import android.support.v7.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hkm.oc.panel.basic_panel_support;
import com.hkm.oc.panel.corepanel.MapPanel;
import com.hkm.oc.preF.f.WorkPanelFragment;

/**
 * Created by hesk on 6/1/2014.
 */
public class PanelActionModeExt implements ActionMode.Callback, View.OnKeyListener, View.OnClickListener, TextView.OnEditorActionListener {
    protected WorkPanelFragment rootfragment;
    protected MapPanel panel;
    protected basic_panel_support root_context;
    protected ActionMode _model;

    protected String getTxt(int ResId) {
        return root_context.getResources().getString(ResId);
    }

    public PanelActionModeExt(final WorkPanelFragment f, final basic_panel_support r) {
        this.root_context = r;
        this.rootfragment = f;
        this.panel = f.getPanel();
    }

    public WorkPanelFragment getRootfragment() {
        return this.rootfragment;
    }

    public basic_panel_support getRootContext() {
        return this.root_context;
    }

    protected void initTag(ActionMode mode, ModeTag.Tag tag, boolean closeready) {
        _model = mode;
        final ModeTag t = new ModeTag();
        t.closeReady = closeready;
        t.now = tag;
        mode.setTag(t);
    }

    protected void changeClose(ActionMode mode, boolean b) {
        _model = mode;
        final ModeTag t = (ModeTag) mode.getTag();
        t.closeReady = b;
        mode.setTag(t);
    }

    protected void changeModeTag(ActionMode mode, String t) {
        _model = mode;
        final ModeTag y = (ModeTag) _model.getTag();
        y.submode = t;
        mode.setTag(y);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        _model = mode;
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        _model = mode;
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        _model = mode;
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        _model = mode;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                onSearch();
                break;
            case EditorInfo.IME_ACTION_NEXT:
                onNext();
                break;
            case EditorInfo.IME_ACTION_SEND:
                onSend();
                break;
            case EditorInfo.IME_ACTION_DONE:
                onDone();
                break;
            default:
                break;
        }
        return false;
    }

    protected void onSearch() {
    }

    protected void onNext() {
    }

    protected void onSend() {
    }

    protected void onDone() {
    }
}
