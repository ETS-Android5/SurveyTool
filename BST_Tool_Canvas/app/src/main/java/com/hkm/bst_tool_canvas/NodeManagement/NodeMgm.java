package com.hkm.bst_tool_canvas.NodeManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hkm.bst_tool_canvas.R;
import com.hkm.bst_tool_canvas.base.Mainase;
import com.hkm.surveytool.AppDataRetain;

import butterknife.BindView;

public final class NodeMgm extends Mainase {
    @BindView(R.id.mlist_container)
    protected RecyclerView listview;
    public static final int requestCode = 9312;

    @Override
    protected int layout() {
        return R.layout.c_list_node_classic;
    }

    public static final void startInit(AppCompatActivity c) {
        Intent in = new Intent(c, NodeMgm.class);
        Bundle work = new Bundle();
        work.putString("msg", "sasaas");
        in.putExtras(work);
        c.startActivityForResult(in, requestCode);
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
