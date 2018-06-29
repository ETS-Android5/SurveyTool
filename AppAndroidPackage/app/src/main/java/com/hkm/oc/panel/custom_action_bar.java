package com.hkm.oc.panel;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hkm.oc.R;

/**
 * Created by Hesk on 12/6/2014.
 */
public class custom_action_bar implements View.OnClickListener {
    private worker ctx;

    public custom_action_bar(final worker ctx, final ActionBar ab) {
        this.ctx = ctx;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (!ctx.getResources().getBoolean(R.bool.is_tablet)) {
            View v = inflater.inflate(R.layout.top_bar, null);
            ImageView mtextureview_1 = (ImageView) v.findViewById(R.id.tView1);
            ImageButton button_main_points_interactions = (ImageButton) v.findViewById(R.id.button_01);
            ImageButton button_children_linkings = (ImageButton) v.findViewById(R.id.button_02);
            ImageButton button_move_map = (ImageButton) v.findViewById(R.id.button_03);
            /**
             * this is the main points for changing the point A and the point B
             */
            button_main_points_interactions.setOnClickListener(this);
            button_children_linkings.setOnClickListener(this);
            button_move_map.setOnClickListener(this);
            ab.setCustomView(v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_01:
                break;
            case R.id.button_02:
                break;
            case R.id.button_03:
                break;

        }
    }
}
