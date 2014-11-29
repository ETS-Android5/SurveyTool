package com.hkm.listviewhkm.model;

import android.content.Context;

import com.hkm.datamodel.Label;
import com.hkm.datamodel.RouteNode;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;

import java.util.ArrayList;
import java.util.Collections;

import static com.hkm.U.Content.current_sketch_map;
import static com.hkm.listviewhkm.model.C.indicator.CHANGED;
import static com.hkm.listviewhkm.model.C.indicator.INVALID;

/**
 * Created by Hesk on 3/7/2014.
 */
public class RouteNodeControl {

    public static void create_sample_list_items(Context ctx) {
        for (int i = 0; i < 30; i++) {
            addNewSingle(ctx);
        }
    }

    public static void addNewSingle(Context ctx) {
        final int size = current_sketch_map.get_route_size();
        if (size > 0) {
            current_sketch_map.add_routenode(generate_logical_new_route(size, ctx));
        } else {
            current_sketch_map.add_routenode(generate_new_route_node_structure());
        }
    }

    public static void addNewSingle(int pos, Context ctx) {
        current_sketch_map.add_routenode(generate_new_node_at(pos, ctx), pos + 1);
    }

    private static RouteNode generate_new_route_node_structure() {
        final RouteNode new_route_node = new RouteNode(EQPool.generate_random_id());
      /*  final Label L = new Label();
        L.set_display_big_button_label("L13");
        L.set_dLetter("L");
        L.set_dNumeric("13");
        L.set_letterIntrinsic(10);
        L.set_lineLabelInt(10);
           //   .set_label(L)
        */
        return new_route_node.setIntdicate(INVALID)
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);
    }

    private static RouteNode generate_logical_new_route(final int size, final Context ctx) {
        final int last_index = size - 1;
        final Label lab = new Label();
        final Label previous_label = current_sketch_map.get_label_at(last_index);
        final int NextNum = getNextAddNumericLabel(previous_label);
        final RouteNode new_route_node = new RouteNode(EQPool.generate_random_id());
        final String display_label_string = previous_label.get_dLetter() + NextNum + "";

        lab.set_display_big_button_label(display_label_string);
        lab.set_dLetter(previous_label.get_dLetter());
        lab.set_dNumeric(NextNum + "");
        lab.set_letterIntrinsic(previous_label.get_letterIntrinsic());
        lab.set_lineLabelInt(previous_label.get_line_label_int());
        lab.refresh_data(ctx);

       /* return new_route_node.setIntdicate(INVALID)
                .set_label(lab)
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);*/
        return new_route_node.setIntdicate(INVALID)
                .set_label(lab).set_cable_r(0.0f)
                .set_distance_a(0.00f).set_distance_b(0.00f)
                .set_depth(0.00f).set_r(0, 0).set_cut(false);
    }

    private static int getNextAddNumericLabel(final Label previous_label) {

        final int line_intrinsic = previous_label.get_line_label_int();
        final ArrayList<Integer> k = new ArrayList<Integer>();

        for (RouteNode RN : current_sketch_map.getRouteNodeList()) {
            if (line_intrinsic == RN.get_label().get_line_label_int()) {
                k.add(Integer.parseInt(RN.get_label().get_dNumeric()));
            }
        }

        final int tMax = Collections.max(k);
        return tMax + 1;
    }

    private static RouteNode generate_new_node_at(final int position, final Context ctxx) {
        final Label previous_label = current_sketch_map.get_label_at(position);
        final int NextNum = getNextAddNumericLabel(previous_label);
        final RouteNode new_route_node = new RouteNode(EQPool.generate_random_id());
        final String display_label_string = previous_label.get_dLetter() + NextNum + "";
        final Label lab = new Label();
        lab.set_display_big_button_label(display_label_string);
        lab.set_dLetter(previous_label.get_dLetter());
        lab.set_dNumeric(NextNum + "");
        lab.set_letterIntrinsic(previous_label.get_letterIntrinsic());
        lab.set_lineLabelInt(previous_label.get_line_label_int());
        lab.refresh_data(ctxx);
        return new_route_node.setIntdicate(INVALID)
               .set_label(lab).set_cable_r(0.0f)
               .set_distance_a(0.00f).set_distance_b(0.00f)
               .set_depth(0.00f).set_r(0, 0).set_cut(false);
    }

    public static boolean is_complete_list() {
        final ArrayList<RouteNode> l = current_sketch_map.getRouteNodeList();
        for (RouteNode r : l) {
            boolean c = r.getIndicate() == INVALID;
            if (c) return false;
        }
        return true;
    }

    public static boolean has_changes() {
        final ArrayList<RouteNode> l = current_sketch_map.getRouteNodeList();
        for (RouteNode r : l) {
            boolean c = r.getIndicate() == CHANGED;
            if (c) return true;
        }
        return false;
    }

}
