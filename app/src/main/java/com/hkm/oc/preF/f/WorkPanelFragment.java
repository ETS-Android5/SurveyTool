package com.hkm.oc.preF.f;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hkm.oc.R;
import com.hkm.oc.preF.root.commonFragment;
import com.hkm.oc.panel.WorkPanel;
import com.hkm.oc.panel.worker;
import com.hkm.oc.panel.corepanel.MapPanel;

/**
 * Created by Hesk on 13/6/2014.
 */
@SuppressLint("ValidFragment")
public class WorkPanelFragment extends commonFragment {

    private Bitmap loadBitmap;
    private MapPanel panel;
    private TextView statusText;
    private worker.PanelListener maction_bar_control;
    private WorkPanel wp;

    public WorkPanelFragment() {

    }

    public static WorkPanelFragment newInstance(Bitmap b, worker.PanelListener actionbar) {
        WorkPanelFragment newFragment = new WorkPanelFragment();
        newFragment.addPanelListener(actionbar, b);
        Bundle bundle = new Bundle();

        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onAttach(Activity mactivity) {
        super.onAttach(mactivity);
        wp = (WorkPanel) mactivity;
    }

    public void addPanelListener(worker.PanelListener actionbar, Bitmap b) {
        this.maction_bar_control = actionbar;
        this.loadBitmap = b;
    }


    public MapPanel getPanel() {
        return panel;
    }

    @Override
    protected View view_start(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_map_panel, null);

        statusText = (TextView) rv.findViewById(R.id.statustext);
        panel = (MapPanel) rv.findViewById(R.id.rendering_panel);
        panel
                .setPanelListener(maction_bar_control)
                .setBaseMap(loadBitmap, new Point(loadBitmap.getWidth(), loadBitmap.getHeight()))
                .startPanel();
        panel.setMode(MapPanel.CMode.ADJUSTMENT);
        return rv;
    }
}
