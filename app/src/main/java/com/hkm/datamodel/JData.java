package com.hkm.datamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import static com.hkm.U.Content.active_data;

/**
 * JSON data handling the application level
 * Created by Hesk on 6th march 2014
 * imusictech ltd
 */
public class JData implements Serializable{
    public final static String
            ROUTE_KEY = "route",
            SURVEY_KEY = "survey",
            TP_KEY = "tp";



    /**
     * @param labelobject
     * @param refA
     * @param refB
     * @param EstDepth
     */
    public static RouteNode make_route_measurement(Label labelobject, float refA, float refB, float EstDepth) {
        RouteNode route_node = new RouteNode();
        route_node.set_distance_a(refA);
        route_node.set_distance_b(refB);
        route_node.set_depth(EstDepth);
        return route_node;
    }


    public static void add_tp_node(TrailPitNode new_route_node) {
        if (!active_data.containsKey(TP_KEY)) {
            // active_data.put(ROUTE_KEY,);
            ArrayList<TrailPitNode> new_array = new ArrayList<TrailPitNode>();
            new_array.add(new_route_node);
            active_data.put(TP_KEY, new_array);
        } else {
            final ArrayList<TrailPitNode> list = (ArrayList<TrailPitNode>) active_data.get(SURVEY_KEY);
            list.add(new_route_node);
        }
    }

    public static void add_survery_node(SurveyNode new_route_node) {
        if (!active_data.containsKey(SURVEY_KEY)) {
            // active_data.put(ROUTE_KEY,);
            ArrayList<SurveyNode> new_array = new ArrayList<SurveyNode>();
            new_array.add(new_route_node);
            active_data.put(SURVEY_KEY, new_array);
        } else {
            final ArrayList<SurveyNode> list = (ArrayList<SurveyNode>) active_data.get(SURVEY_KEY);
            list.add(new_route_node);
        }
    }

    /**
     * this is the part from the Spinner selection for the spinner UI
     */
    public static String[] get_spinner_options() throws Exception {
        if (!active_data.containsKey(ROUTE_KEY)) {
            throw new Exception("no listing in here");
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final String[] spinner_data = new String[list.size() + 1];
            final Iterator<RouteNode> iterator = list.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                spinner_data[i] = iterator.next().get_label().get_display_big_button_label();
                i++;
            }
            spinner_data[0] = "Replace by ref. point..";
            return spinner_data;
        }
    }

    /**
     * to find the record_plan_index by using the tag name
     *
     * @param tag_name
     * @return
     */
    public static int find_record_index_by_tag_name(String tag_name) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            System.out.print("no listing in here for get matching previous listing");
            return -1;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final Iterator<RouteNode> iterator = list.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                final String listTagName = iterator.next().get_label().get_display_big_button_label();
                if (listTagName.equalsIgnoreCase(tag_name)) {
                    return i;
                }
                i++;
            }
            return -1;
        }
    }

    /**
     * use in the label setting
     * get matching previous listing labels
     *
     * @return
     */
    public static boolean is_matched_from_the_previous_tag_record(String tag_name) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            System.out.print("no listing in here for get matching previous listing");
            return false;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final Iterator<RouteNode> iterator = list.iterator();
            while (iterator.hasNext()) {
                final String listTagName = iterator.next().get_label().get_display_big_button_label();
                if (listTagName.equalsIgnoreCase(tag_name)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static int remove_route_node(int record_n) {
        final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
        //int f = list.size() >record_n ;
        if (list.size() > record_n) {
            list.remove(record_n);
            return record_n;
        } else {
            return -1;
        }
    }

    public static int remove_last_route_node() {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return -1;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            int f = list.size() - 1;
            list.remove(f);
            return f;
        }
    }

    /**
     * only to update the route node label object
     *
     * @param record_plan_index
     * @param mlabel
     */
    public static void update_route_node_label(int record_plan_index, Label mlabel) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final RouteNode n = list.get(record_plan_index);
            n.set_label(mlabel);
        }
    }

    /**
     * handle both updating label object and the row data object
     *
     * @param record_plan_index
     * @param node
     */
    public static void update_route_node(int record_plan_index, RouteNode node) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final RouteNode n = list.get(record_plan_index);
            if (node.get_label() != null) {
                n.set_label(node.get_label());
            }
            n.set_distance_a(node.get_distance_a());
            n.set_distance_b(node.get_distance_b());
            n.set_depth(node.get_depth());
            n.set_cut(node.get_cut());
            n.set_r(node.get_cal_r_a(), node.get_cal_r_b());
        }
    }

    /**
     * adding a new route node data to the last index
     *
     * @param new_route_node
     */
    public static void add_route_node(RouteNode new_route_node) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            // active_data.put(ROUTE_KEY,);
            ArrayList<RouteNode> new_array = new ArrayList<RouteNode>();
            new_array.add(new_route_node);
            active_data.put(ROUTE_KEY, new_array);
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            list.add(new_route_node);
        }
    }

    /**
     * insert the data into the list index all elements will shift after the index
     *
     * @param new_route_node
     */
    public static void add_route_node(RouteNode new_route_node, int record_plan_index) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            // active_data.put(ROUTE_KEY,);
            ArrayList<RouteNode> new_array = new ArrayList<RouteNode>();
            new_array.add(new_route_node);
            active_data.put(ROUTE_KEY, new_array);
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            list.add(record_plan_index, new_route_node);
        }
    }

    /**
     * call up the source of the array list adapter
     *
     * @return
     */
    public static ArrayList<RouteNode> init_adapter_list_src() {
        if (!active_data.containsKey(ROUTE_KEY)) {
            ArrayList<RouteNode> new_array = new ArrayList<RouteNode>();
            active_data.put(ROUTE_KEY, new_array);
            return new_array;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            return list;
        }
    }

    /**
     * update the existing label object
     *
     * @param record_plan_index
     * @param mlabel
     */
    public static void update_label_at(int record_plan_index, Label mlabel) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final RouteNode r = list.get(record_plan_index);
            r.set_label(mlabel);
        }
    }

    /**
     * get the existing label object at a specific index int
     *
     * @param record_plan_index
     * @return
     */
    public static Label get_label_at(int record_plan_index) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return null;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final RouteNode r = list.get(record_plan_index);
            return r.get_label();
        }
    }

    public static RouteNode get_route_node_at(int record_plan_index) {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return null;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            final RouteNode r = list.get(record_plan_index);
            return r;
        }
    }

    public static int get_route_size() {
        if (!active_data.containsKey(ROUTE_KEY)) {
            return 0;
        } else {
            final ArrayList<RouteNode> list = (ArrayList<RouteNode>) active_data.get(ROUTE_KEY);
            return list.size();
        }
    }

}
