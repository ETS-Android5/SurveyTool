package com.hkm.bst_tool_canvas.NodeManagement;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.hkm.bst_tool_canvas.R;
import com.hkm.bst_tool_canvas.base.Mainase;
import com.hkm.surveytool.AppDataRetain;

import butterknife.BindView;

public class NodeMgm extends Mainase {
    @BindView(R.id.mlist_container)
    protected RecyclerView listview;

    @Override
    protected int layout() {
        return R.layout.c_list_node_classic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppDataRetain.require_update_depth = false;
            AppDataRetain.require_update_pos = false;
        }

    }
}
