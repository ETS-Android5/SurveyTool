package com.hkm.U;

import android.graphics.Point;
import android.net.Uri;
import android.os.Message;

import com.hkm.datamodel.JobTaskData;
import com.hkm.datamodel.LabelRenderObject;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.panel.corepanel.elements.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hesk on 7/26/13.
 */
public class Content {
    /**
     * This is the main data backbone to contain all the application data
     */
    public final static HashMap<String, Object> active_data = new HashMap<String, Object>();
    /**
     * contain a list of testing result data. This is the total result for all the testing data
     */
    public final static ArrayList<String> radius_list = new ArrayList<String>();
    public final static Point basemap_input_size = new Point();
    /**
     * internal use only data model
     */
    // public static ArrayList<HashMap<String, PointF>> route_list = new ArrayList<HashMap<String, PointF>>();
    public static List<Integer> nresult = new ArrayList<Integer>();
    public static List<LabelRenderObject> label_list = new ArrayList<LabelRenderObject>();
    public static Route saved_route;
    /**
     * contains a list of single integer as the position from the panel
     * <p/>
     * public static List<Integer> radius_a_slot = new ArrayList<Integer>();
     * public static List<Integer> radius_b_slot = new ArrayList<Integer>();
     */
    public static int dpi = 0, screensizeX;
    public static double ratio = 0;

    /**
     * the main point position from A and B
     * <p/>
     * public static BigObserveDot main_point_A = new BigObserveDot(200f, 200f, "point A");
     * public static BigObserveDot main_point_B = new BigObserveDot(500f, 500f, "point B");
     */

    public static Uri recent_uploaded_basemap_file_path;
    public static Message upload_event_log;
    public static boolean ratio_changed = false, allow_demo_list_gen = false;
    //from the download list
    public static ArrayList<JobTaskData> myjob_data = new ArrayList<JobTaskData>();
    //from the local list
    public static JobTaskData current_job_task;
    public static SketchMapData current_sketch_map;
    //  public static ArrayList<Uri> wip_photos_uris = new ArrayList<Uri>();
    public static ArrayList<Integer> update_existing_index = new ArrayList<Integer>();
    public static boolean require_update_pos, require_update_depth, changed_to_new_ab_points;
}
