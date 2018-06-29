package com.hkm.datamodel;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.hkm.oc.panel.corepanel.elements.ProposedTrialPit;
import com.hkm.oc.panel.corepanel.elements.SurveyBoundary;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hesk on 5/31/2014.
 */
public class SketchMapData implements Serializable {
    @SerializedName("A")
    private BigObserveDot main_point_A;
    @SerializedName("B")
    private BigObserveDot main_point_B;
    @SerializedName("saved_data")
    private String saved_file_data;
    @SerializedName("drawmap_uri")
    private Uri drawmap_uri;

    private ArrayList<String> radius_list = new ArrayList<String>();

    /**
     * assigned automatically from http json response
     * attachment ID on the sketch map list
     */
    @SerializedName("submission_number")
    private int submission_attachment_Id;
    /**
     * assigned automatically from http json response
     */
    @SerializedName("basemap_url")
    private Uri original_basemap_uri;

    @SerializedName("dpi")
    private int dpi = 0;

    @SerializedName("ratio")
    private double ratio = 0;

    @SerializedName("distance_ab")
    private float distance_ab = 0f;

    @SerializedName("routenode")
    private ArrayList<RouteNode> routenodes = new ArrayList<RouteNode>();

    @SerializedName("surveyboundary")
    private SurveyBoundary surveybound;

    @SerializedName("tp")
    private ProposedTrialPit proposetrialpit;


    public SketchMapData() {
        defaultsetting();
    }

    public SketchMapData(final int Id, final Uri originalbasemap) {
        defaultsetting();
        this.submission_attachment_Id = Id;
        this.original_basemap_uri = originalbasemap;
    }

    private void defaultsetting() {
        main_point_A = new BigObserveDot(200f, 200f, "point A");
        main_point_B = new BigObserveDot(500f, 500f, "point B");
        saved_file_data = null;
        original_basemap_uri = null;
        radius_list.clear();
    }

    public SurveyBoundary getSurveybound() {
        return surveybound;
    }

    public SketchMapData setSurveybound(SurveyBoundary d) {
        this.surveybound = d;
        return this;
    }

    public ProposedTrialPit getTpElement() {
        return proposetrialpit;
    }

    public SketchMapData setTpElement(ProposedTrialPit tp) {
        this.proposetrialpit = tp;
        return this;
    }

    public int getdpi() {
        return dpi;
    }

    public double getratio() {
        return ratio;
    }

    public float getRealDistanceAB() {
        return this.distance_ab;
    }

    public SketchMapData setRealDistanceAB(float d) {
        this.distance_ab = d;
        return this;
    }

    public SketchMapData set_dpi_ratio_attachment(int d, double r, int k) {
        this.dpi = d;
        this.ratio = r;
        this.submission_attachment_Id = k;
        return this;
    }

    public SketchMapData setDpi(int d) {
        this.dpi = d;
        return this;
    }

    public SketchMapData setRatio(double r) {
        this.ratio = r;
        return this;
    }

    /**
     * the recent saved map uri
     *
     * @return
     */
    public Uri get_draw_map_uri() {
        return drawmap_uri;
    }

    public SketchMapData set_draw_map_uri(Uri s) {
        this.drawmap_uri = s;
        return this;
    }

    /**
     * does it have the draw map device path found
     * @return
     */
    public boolean hasUri() {
        return drawmap_uri == null ? false : true;
    }

    /**
     * the basemap uri from the http path
     *
     * @return
     */
    public Uri get_base_map_url() {
        if (original_basemap_uri == null) original_basemap_uri = Uri.parse("");
        return original_basemap_uri;
    }

    public String get_data() {
        return this.saved_file_data;
    }

    /**
     * read from the original http post data for the first time / second time / third time ...
     *
     * @return
     */
    public int getAttachmentId() {
        return this.submission_attachment_Id;
    }

    public void set_save_file_data(String s) {
        this.saved_file_data = s;
    }

    public SketchMapData set_original_base_map(Uri s) {
        this.original_basemap_uri = s;
        return this;
    }


    public BigObserveDot get_point_a() {
        return main_point_A;
    }

    public BigObserveDot get_point_b() {
        return main_point_B;
    }

    public SketchMapData set_point_A(BigObserveDot d) {
        main_point_A = d;
        return this;
    }

    public SketchMapData set_point_B(BigObserveDot d) {
        main_point_B = d;
        return this;
    }

    public SketchMapData remove_routenode() {
        routenodes.remove(routenodes.size() - 1);
        return this;
    }

    public SketchMapData remove_routenode(int at) {
        if (routenodes.size() > at)
            routenodes.remove(at);
        return this;
    }

    public int find_node_index(RouteNode rn) {
        return routenodes.indexOf(rn);
    }

    public SketchMapData update_routenode(int record_plan_index, RouteNode node) {
        final RouteNode n = routenodes.get(record_plan_index);
        if (node.get_label() != null) {
            n.set_label(node.get_label());
        }
        n.set_distance_a(node.get_distance_a());
        n.set_distance_b(node.get_distance_b());
        n.set_depth(node.get_depth());
        n.set_cut(node.get_cut());
        n.set_r(node.get_cal_r_a(), node.get_cal_r_b());
        return this;
    }

    public SketchMapData update_routenode_label(int record_plan_index, Label mlabel) {
        routenodes.get(record_plan_index).set_label(mlabel);
        return this;
    }

    public SketchMapData add_routenode(RouteNode node) {
        routenodes.add(node);
        return this;
    }


    public SketchMapData add_routenode(RouteNode node, int at) {
        routenodes.add(at, node);
        return this;
    }

    public Label get_label_at(int record_plan_index) {
        final RouteNode r = routenodes.get(record_plan_index);
        return r.get_label();
    }

    public RouteNode get_route_node_at(int record_plan_index) {
        final RouteNode r = routenodes.get(record_plan_index);
        return r;
    }

    public int get_route_size() {
        return routenodes.size();
    }

    public ArrayList<RouteNode> getRouteNodeList() {
        return routenodes;
    }

    public SketchMapData setRouteNodes(ArrayList<RouteNode> e) {
        this.routenodes = e;
        return this;
    }

}
