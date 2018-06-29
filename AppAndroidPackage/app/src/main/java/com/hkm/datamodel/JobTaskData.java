package com.hkm.datamodel;

import android.graphics.PointF;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import static com.hkm.U.Content.current_sketch_map;

/**
 * Data model each job data
 * Created by Hesk on 3/6/2014.
 */
public class JobTaskData implements Serializable {

    @SerializedName("working_customer_survey_complete")
    public boolean working_customer_survey_complete = false;
    @SerializedName("working_form_line_record_complete")
    public boolean working_form_line_record_complete = false;
    // the job post ID
    @SerializedName("post_id")
    private String post_id;
    // the project post ID
    @SerializedName("project_id")
    private String project_id;
    // the status of job data
    @SerializedName("status")
    private String status;
    //photos from work in progress
    @SerializedName("photos")
    private ArrayList<PhotoViewData> photolist;
    //download from the db
    //@SerializedName("basemaps")
    //private ArrayList<PhotoViewData> wipdata_list;
    //saved and achieved in the machine
    @SerializedName("workmaps")
    private ArrayList<SketchMapData> work_snapshots = new ArrayList<SketchMapData>();
    //this is the basemap from the detail
    @SerializedName("basemap_detail")
    private ArrayList<LocationMap> loc;
    //this is the address tag retrieved from the db
    @SerializedName("address_tag")
    private String address_location;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("working_form_line_record")
    private String working_form_line_record = "";
    @SerializedName("wflr_team_sign")
    private Uri wflr_team_sign;
    @SerializedName("wflr_client_sign")
    private Uri wflr_client_sign;
    @SerializedName("working_customer_survey")
    private String working_customer_survey = "";
    @SerializedName("wcs_client_sign")
    private Uri wcs_client_sign;
    @SerializedName("device_id")
    private String device_android_id;

    public JobTaskData() {

    }

    public void getLRSignatures(final ArrayList<Uri> pointer) {
        if (working_form_line_record_complete) {
            pointer.add(wflr_client_sign);
            pointer.add(wflr_team_sign);
        }
    }

    public void getCSSignatures(final ArrayList<Uri> pointer) {
        if (working_customer_survey_complete) {
            pointer.add(wcs_client_sign);
        }
    }

    public void saveSignWLRClient(Uri d) {
        wflr_client_sign = d;
        checkWLR();
    }

    public void saveSignWLRTeam(Uri d) {
        wflr_team_sign = d;
        checkWLR();
    }

    public void saveSignWCSClient(Uri d) {
        wcs_client_sign = d;
        checkWCS();
    }

    public void saveWLRForm(String d) {
        working_form_line_record = d;
        checkWLR();
    }

    public void saveWCSForm(String d) {
        working_customer_survey = d;
        checkWCS();
    }

    private void checkWCS() {
        boolean fact1 = wcs_client_sign != null;
        boolean fact2 = working_customer_survey != null ? !working_customer_survey.isEmpty() : false;
        working_customer_survey_complete = fact1 && fact2;
    }

    private void checkWLR() {
        boolean fact1 = wflr_team_sign != null && wflr_client_sign != null;
        boolean fact2 = working_form_line_record != null ? !working_form_line_record.isEmpty() : false;
        working_form_line_record_complete = fact1 && fact2;
    }

    public JobTaskData setLatLng(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
        return this;
    }

    public ArrayList<PhotoViewData> getPhotoList() {
        if (photolist == null) photolist = new ArrayList<PhotoViewData>();
        return photolist;
    }

    public int getID() {
        return Integer.parseInt(post_id);
    }

    public void setID(int id) {
        this.post_id = String.valueOf(id);
    }

    public String getJID() {
        return post_id;
    }

    public String getProjectID() {
        return project_id;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address_location;
    }

    public void addPhotoViewData(final PhotoViewData new_data) {
        photolist.add(new_data);
    }

    public ArrayList<LocationMap> get_list_location_map() {
        if (loc == null) loc = new ArrayList<LocationMap>();
        return loc;
    }

    public boolean hasSketchMapDrawn(final int attachment_id) {
        if (this.work_snapshots.size() == 0) return false;
        for (final SketchMapData item : this.work_snapshots) {
            if (item.getAttachmentId() == attachment_id) {
                return true;
            }
        }
        return false;
    }

    public LocationMap findLocationMapByAttachmentID(final int attachment_id) throws Exception {
        if (this.loc.size() == 0)
            throw new Exception("item not found from findLocationMapByAttachmentID ");
        for (final LocationMap item : this.loc) {
            if (item.get_attachment_id() == attachment_id) {
                return item;
            }
        }
        throw new Exception("item not found from findLocationMapByAttachmentID attachment ID");
    }

    public SketchMapData findLocalWorkBaseMapByPager(int pager_position) throws Exception {
        int attachment_id = this.loc.get(pager_position).get_attachment_id();
        for (SketchMapData item : this.work_snapshots) {
            if (item.getAttachmentId() == attachment_id) {
                return item;
            }
        }
        throw new Exception("item not found from findLocalWorkBaseMapByPager attachment ID did not get matched with the existing Working Map");
    }

    public int getTotalMaps() {
        if (loc != null) {
            return loc.size();
        } else return 0;
    }

    public boolean GPS_ready() {
        return lat != null && lng != null;
    }

    public PointF getGPS() {
        return new PointF(Float.parseFloat(lat), Float.parseFloat(lng));
    }

    public void setBasemapList(final ArrayList<SketchMapData> list) {
        work_snapshots = (ArrayList<SketchMapData>) list.clone();
    }

    public void set_android_id(String k) {
        device_android_id = k;
    }

    public void addSketchMap(final LocationMap server_map) {
        if (work_snapshots == null) work_snapshots = new ArrayList<SketchMapData>();
        final SketchMapData new_sketch_map = new SketchMapData(server_map.get_attachment_id(), Uri.parse(server_map.get_url()));
        current_sketch_map = new_sketch_map;
        work_snapshots.add(new_sketch_map);
    }

    public void setPhotoViewDataList(final ArrayList<PhotoViewData> list) {
        photolist = (ArrayList<PhotoViewData>) list.clone();
    }

    public ArrayList<SketchMapData> getSketchMapList() {
        return work_snapshots;
    }

    public void setProject_id(final String project_id) {
        this.project_id = project_id;
    }

    public void setJStatus(String status) {
        this.status = status;
    }

    public static enum JOBSTATUS {
        NEW, STARTED, SUBMITTED, PENDING, APPROVED, COMPLELE
    }
}