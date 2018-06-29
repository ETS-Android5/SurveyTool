package com.hkm.listviewhkm.model;

/**
 * Created by Hesk on 3/7/2014.
 */
public class RouteNodeControl {

    public static void create_sample_list_items() {
        for (int i = 0; i < 30; i++) {
            addNewSingle();
        }
    }

    public static void addNewSingle() {
        final int size = Content.current_sketch_map.get_route_size();
        if (size > 0) {
            Content.current_sketch_map.add_routenode(generate_logical_new_route(size));
        } else {
            Content.current_sketch_map.add_routenode(generate_new_route_node_structure());
        }
    }

    public static void addNewSingle(int pos) {
        Content.current_sketch_map.add_routenode(generate_new_node_at(pos), pos+1);
    }

    private static RouteNode generate_new_route_node_structure() {
        final RouteNode new_route_node = new RouteNode(ToolBox.generate_random_id());
        final Label L = new Label();
        L.set_display_big_button_label("L13");
        L.set_dLetter("L");
        L.set_dNumeric("13");
        L.set_letterIntrinsic(10);
        L.set_lineLabelInt(10);
        return new_route_node.setIntdicate(C.indicator.INVALID)
                .set_label(L)
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);
    }

    private static RouteNode generate_logical_new_route(final int size) {
        final int last_index = size - 1;
        final Label previous_label = Content.current_sketch_map.get_label_at(last_index);
        final int newDNumeric = Integer.parseInt(previous_label.get_dNumeric()) + 1;
        final RouteNode new_route_node = new RouteNode(ToolBox.generate_random_id());
        final String display_label_string = previous_label.get_dLetter() + newDNumeric + "";
        final Label lab = new Label();
        lab.set_display_big_button_label(display_label_string);
        lab.set_dLetter(previous_label.get_dLetter());
        lab.set_dNumeric(newDNumeric + "");
        lab.set_letterIntrinsic(previous_label.get_letterIntrinsic());
        lab.set_lineLabelInt(previous_label.get_line_label_int());
        // lab.refresh_data(this.context);
        return new_route_node.setIntdicate(C.indicator.INVALID)
                .set_label(lab)
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);
    }
    private static RouteNode generate_new_node_at(final int position){
        final Label previous_label = Content.current_sketch_map.get_label_at(position);
        final int newDNumeric = Integer.parseInt(previous_label.get_dNumeric()) + 1;
        final RouteNode new_route_node = new RouteNode(ToolBox.generate_random_id());
        final String display_label_string = previous_label.get_dLetter() + newDNumeric + "";
        final Label lab = new Label();
        lab.set_display_big_button_label(display_label_string);
        lab.set_dLetter(previous_label.get_dLetter());
        lab.set_dNumeric(newDNumeric + "");
        lab.set_letterIntrinsic(previous_label.get_letterIntrinsic());
        lab.set_lineLabelInt(previous_label.get_line_label_int());
        // lab.refresh_data(this.context);
        return new_route_node.setIntdicate(C.indicator.INVALID)
                .set_label(lab)
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);
    }
}
