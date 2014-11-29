package com.hkm.listviewhkm.model;

import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hesk on
 */
public class RouteNode implements Serializable {

    public transient static int FIRST_POINT_CHOICE = 0;
    public transient static int SECOND_POINT_CHOICE = 1;
    transient protected C.indicator mindicator = C.indicator.FINAL;
    @SerializedName("datastorage_index")
    private int datastorage_index;
    @SerializedName("cal_r_a")
    private int cal_r_a;
    @SerializedName("cal_r_b")
    private int cal_r_b;
    @SerializedName("selected_dot_result")
    private int selected_dot_result = -1;
    @SerializedName("is_node_cut")
    private boolean is_node_cut;
    @SerializedName("has_result")
    private boolean has_result;
    @SerializedName("measure_a")
    private float measure_a;
    @SerializedName("measure_b")
    private float measure_b;
    @SerializedName("depth")
    private float depth;
    @SerializedName("cable_radius")
    private float cable_radius;
    @SerializedName("label_object")
    private Label label_object;
    /* transient private C.indicator mIndicator;*/
    transient private boolean highlight;
    /**
     * control interactions for the map panel
     */
    private transient PointF[] raw_testing_result = new PointF[]{
            new PointF(), new PointF()
    };

    public RouteNode() {
    }

    public RouteNode(int order_int) {
        this.datastorage_index = order_int;
    }

    public Label get_label() {
        return this.label_object;
    }

    public RouteNode set_label(Label mlabel) {
        this.label_object = mlabel;
        return this;
    }

    public boolean has_result() {
        return this.has_result;
    }

    public C.indicator getIndicate() {
        return mindicator;
    }

    public RouteNode setIntdicate(C.indicator i) {
        mindicator = i;
        return this;
    }


    public RouteNode set_datastorage_index(int index) {
        this.datastorage_index = index;
        return this;
    }

    public int get_index() {
        return datastorage_index;
    }

    public float get_distance_a() {
        return this.measure_a;
    }

    public RouteNode set_distance_a(float measure) {
        this.measure_a = measure;
        return this;
    }

    public float get_distance_b() {
        return this.measure_b;
    }

    public RouteNode set_distance_b(float measure) {
        this.measure_b = measure;
        return this;
    }

    public float get_depth() {
        return this.depth;
    }

    public RouteNode set_depth(float measure) {
        this.depth = measure;
        return this;
    }

    public float get_cable_radius() {
        return this.cable_radius;
    }

    public boolean get_cut() {
        return this.is_node_cut;
    }

    public RouteNode set_cut(boolean boo) {
        this.is_node_cut = boo;
        return this;
    }

    public RouteNode set_has_result(boolean boo) {
        this.has_result = boo;
        return this;
    }


    public RouteNode set_cable_r(float r) {
        this.cable_radius = r;
        return this;
    }

    public RouteNode set_r(int r1, int r2) {
        this.cal_r_a = r1;
        this.cal_r_b = r2;
        return this;
    }

    public String get_tag_depth() {
        if (this.depth <= 0.0f) {
            return "UN";
        } else {
            return depth + "d";
        }
    }

    public RouteNode setHighLight(boolean b) {
        highlight = b;
        return this;
    }

    public boolean highLighted() {
        return highlight;
    }

    public int get_cal_r_a() {
        return this.cal_r_a;
    }

    public int get_cal_r_b() {
        return this.cal_r_b;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** Node *****\n");
        sb.append("ID=" + datastorage_index + "\n");
        sb.append("distance=" + get_distance_a() + "," + get_distance_b() + "\n");
        sb.append("set depth=" + get_tag_depth() + "\n");
        sb.append("calculations=[" + get_cal_r_a() + "," + get_cal_r_b() + "]\n");
        sb.append("is cut: " + this.is_node_cut);
        sb.append("*****************************");
        return sb.toString();
    }


    public boolean is_this_pair_undecided() {
        return selected_dot_result == -1;
    }

    public int get_decision() {
        return selected_dot_result;
    }

    public RouteNode reset_possible_points() {
        selected_dot_result = -1;
        return this;
    }

    public RouteNode choose_possible_point(int e) {
        //e only can be 0 or 1
        selected_dot_result = e;
        return this;
    }

}
