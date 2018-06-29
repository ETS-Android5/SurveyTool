package com.hkm.oc.preF.root;

import android.os.Bundle;

import com.hkm.oc.preF.f.NewLegendListFragment;

import java.util.ArrayList;

/**
 * Created by Hesk ons
 */
public interface CrossReceiver {
    NewLegendListFragment take_list_render(ArrayList<Bundle> arb, int select);
}
