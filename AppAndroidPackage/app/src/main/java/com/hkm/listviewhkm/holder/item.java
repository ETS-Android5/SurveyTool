package com.hkm.listviewhkm.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.hkm.listviewhkm.RNList.RNAdapter;
import com.hkm.listviewhkm.model.C;

/**
 * Created by hesk on 6/30/2014.
 */
public abstract class item implements
        View.OnTouchListener,
        TextView.OnEditorActionListener,
        View.OnClickListener,
        NumberPickerDialogFragment.NumberPickerDialogHandler {
    protected RNAdapter adapter_pointer;
    protected boolean useCacheView;
    protected NumberPickerBuilder mNumberPickerBuilder;
    protected Context ctx;
    protected int pos, totalWidth, orientation;

    public item(int p, Context c, RNAdapter pointer) {
        pos = p;
        ctx = c;
        adapter_pointer = pointer;
    }

    protected abstract item addConvertViewToElements(View cv);

    protected abstract item getCacheView(View random_view);

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
    }

    @Override
    public void onClick(View view) {
    }

    @SuppressLint("NewApi")
    public abstract void getLayerProcess() throws Exception;

    @SuppressLint("NewApi")
    public abstract void getLayerProcessNoAnimation() throws Exception;

    protected abstract boolean isCompleteAllFields();

    public abstract boolean action_submit_data();

    protected abstract void update_stack_list(C.tag n, double newNum);
}
