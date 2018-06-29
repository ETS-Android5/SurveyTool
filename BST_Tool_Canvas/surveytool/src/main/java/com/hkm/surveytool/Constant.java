package com.hkm.surveytool;

import android.R;
import android.graphics.Point;
import android.os.Environment;

import java.util.ArrayList;

/**
 * Created by hesk on 7/26/13.
 */
public class Constant {
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final int NormalBackGroundColor = R.color.holo_blue_dark;
    public static final String DETAG = "_hkm_debugger";
    public static final int MODE_EDIT_MAIN_POINT = 0;
    public static final int MODE_EDIT_BASEMAP_POSITION = 1;
    public static final int MODE_EDIT_BASEMAP_SCALE_SIZE = 2;
    public static final int MODE_EDIT_LINK_CHILDREN_TESTING_RESULT = 3;
    public static final Point output_hardcode_size = new Point(1455, 945);
    public static final String ROW_NOT_COMPLETE_TEXT = "----";
    public static final String view_final_web_review_maps = "http://onecallapp.imusictech.net/printinggui/";
    public static final ArrayList<Integer> update_existing_index = new ArrayList<Integer>();

    /**
     * a very useful constant for calculating the size ratio
     */
    public static final double inch_meter_ratio = 0.0254;
    public final static String APPFOLDER = "/OneCall/";
    public final static String BASEMAPFOLDER = APPFOLDER + "assignedbasemap/";
    public final static String AppBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + APPFOLDER;
    public final static String BASEMAPPath = AppBasePath + "assignedbasemap/";
    public final static String EXPORTbASEMAPPath = AppBasePath + "exportedbasemap/";
    public final static String AppBasePathPicture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + APPFOLDER;

    public Constant() {
    }

    public static class EndPoint {
        public static final String WP_UPLOAD_RESULT_GATEWAY = "http://onecallapp.imusictech.net/api/appaccess/submission_basemap/";
        //production use only
        public static final String WP_DATA_ENDPOINT = "http://onecallapp.imusictech.net/api/appaccess/submission_report/";
        public static final String WP_BASE_MAP_ENDPOINT = "http://onecallapp.imusictech.net/api/appaccess/submission_return_base_maps/";
        public static final String WP_WORK_PHOTO_ENDPOINT = "http://onecallapp.imusictech.net/api/appaccess/submission_site_pictures/";
        public static final String WP_SIGNATURES_ENDPOINT = "http://onecallapp.imusictech.net/api/appaccess/submission_signatures/";
    }

    public static class IntentKey {
        public final static int INTENT_ROUTE_MANAGEMENT = 653;
        public final static int SELECT_PICTURE = 950;
        public final static int LEGEND_ICON_PICK = 992;
        public final static int SELECT_FINALIZE_CHILDREN = 951;
        public final static int LABEL_AND_LEGEND_PICKER = 999;
        public final static int REQUEST_CODE_CHOOSE_FILE_TO_UPLOAD = 312;
        public final static int CHOOSE_FILES_PATHS = 313;
        public final static int WPP_INTENT = 932;
        public final static int MAP_PANEL_ACTIVITY = 9382;
        public final static int WLR_CLIENT_SIGN = 10241;
        public final static int WCS_CLIENT_SIGN = 10242;
        public final static int WLR_TEAM_SIGN = 10243;
        public final static int CAMERA_REQUEST = 22011;
        public final static int OPEN_RETRIEVED_IMAGES = 22013;
        public final static int OPEN_TASK_FORM = 22014;

        /**
         * triggers the action intent for upload picture
         */
        public final static int UPLOAD_PICTURE = 1023;
    }

    public static class IntentAction {
        public final static int REMOVE = 9125;
        public final static int UPDATE = 9051;
    }

    public static class DialogCallBack {
        public final static String BOOLEAN = "simpleBoolean";
        public final static String SINGLE_CLICK = "one_click_no_additional_functions_or_effect";
        public final static String SINGLE_CLICK_HIDE_ACTIONBAR = "hideActionBar";
        public final static String SINGLE_CLICK_SHOW_ACTIONBAR = "showActionBar";
        public final static String SET_OR_CANCEL = "setOrCancel";
        public final static String SINGLE_DISTANCE_NOTICE = "set_ab_point_notice";
        public final static String SINGLE_DISTANCE_RATIO_NOTICE = "set_ab_pointer_notice";
        public final static String DO_UPLOAD = "do_upload_map";
        public final static String DO_SB = "do_sb";
        public final static String DO_AB_NEW_DATAPOINTS = "do_ab";
    }

    public static class DialogInt {
        public final static int FRESH_CONNECT_NODE_START_DIALOG = 0;
        public final static int FRESH_CONNECT_NODE_DONE_DIALOG = 1;
        public final static int ROUTE_EDIT_MODE_START_DIALOG = 56;
        public final static int ROUTE_EDIT_MODE_DONE_DIALOG = 66;
        public final static int NO_DATA_TO_START_CONNECTING = 2;
        public final static int NO_DATA_TO_START_MEASUREMENT_ERROR = 42;
        public final static int SET_AB_DISTANCE_DONE = 3;
        public final static int SET_AB_DISTANCE_NOTICE = 3035;
        public final static int SET_AB_DISTANCE_RATIO_NOTICE = 3042;
        public final static int DO_NEED_TO_SET_AB_DISTANCE_FOR_RATIO = 3012;
        public final static int DO_NEED_TO_UPLOAD = 3232;
        public final static int DO_NEED_TO_QUIT_SB_INCOMPLETE = 3512;
        public final static int DO_NEED_TO_SET_AB_POSITION = 3013;
    }


    public static class MapData {
        /**
         * list type detail:
         * <p/>
         * 0. legend icon id name
         * 1. description text
         * 2. legend type
         * 3. type
         * 4. color code id name
         * <p/>
         * 5. THE MARKER LINE LABEL
         */
        public final static String[] InternalIconList = {

                "legend_30_ff0000,Electric Cable Oil Pit,feature,P,oc_red",
                "legend_01_ff0000,Electric Cable Pit,feature,P,oc_red",
                "legend_31_ff0000,ATC Cable Pit,feature,M,oc_red",
                "legend_32_ff0000,Traffic Light,feature,M,oc_red",
                "legend_02_ff0000,Lamppost,feature,L,oc_red",

                "legend_33_ff0000,Illuminating Bollard,feature,M,oc_red",
                "legend_35_ff0000,TCSS Cable Pit,feature,M,oc_red",
                "legend_03_ff0000,Control Box,feature,P,oc_red",
                "legend_34_ff0000,Public Lighting Pit,feature,L,oc_red",
                "legend_36_000000,Earth Pit,feature,U,oc_dark",

                "legend_37_ffbf00,Cable TV Pit,feature,C,oc_yellow_1",
                "legend_38_dda500,PCCW Pit,feature,T,oc_yellow_2",
                "legend_39_dda500,Hutchison Pit,feature,T,oc_yellow_2",
                "legend_40_dda500,NT&T Pit,feature,T,oc_yellow_2",
                "legend_41_dda500,New World Telecom Pit,feature,T,oc_yellow_2",
                "legend_42_dda500,HKBN Pit,feature,T,oc_yellow_2",
                "legend_43_dda500,Easterstar Pit,feature,T,oc_yellow_2",
                "legend_44_dda500,WT&T Pit,feature,T,oc_yellow_2",
                "legend_45_dda500,TGT Pit,feature,T,oc_yellow_2",
                "legend_46_dda500,TRAX Pit,feature,T,oc_yellow_2",
                "legend_47_dda500,Telephone Kiosk,feature,T,oc_yellow_2",

                "legend_49_ff7f00,Gas Value,feature,G,oc_yellow_3",
                "legend_48_ff7f00,Gas Pit,feature,G,oc_yellow_3",

                "legend_50_0000ff,Fire Hydrant Pit,feature,A,oc_blue_1",
                "legend_51_0000ff,Irrigation Water,feature,A,oc_blue_1",


                "legend_04_0000ff,Meter,feature,A,oc_blue_1",
                "legend_05_0000ff,Water Valve,feature,A,oc_blue_1",
                "legend_52_0000ff,Water Valve Pit,feature,A,oc_blue_1",

                "legend_50_0000ff,Fire Hydrant Pit,feature,B,oc_blue_2",
                "legend_51_0000ff,Irrigation Water,feature,B,oc_blue_2",

                "legend_52_0007fff,Water Valve Pit,feature,B,oc_blue_2",
                "legend_04_007fff,Meter,feature,B,oc_blue_2",
                "legend_59_007fff,Water Valve,feature,B,oc_blue_2",

                "legend_53_4a9500,Storm Manhole,feature,S,oc_green_2",
                "legend_54_a5dd00,Foul Manhole,feature,F,oc_green_1",
                "legend_53_4a9500,Catch-Pit,feature,S,oc_green_2",
                "legend_56_4a9500,Gully,feature,S,oc_green_2",
                "legend_60_000000,Cooling Main Value,feature,U,oc_dark",
                "legend_57_000000,Cooling Main Value Pit/Manhole,feature,U,oc_dark",
                "legend_58_000000,Unclsasified Utility manhole,feature,U,oc_dark",

                "legend_12_ff0000,Electric Cable,line,P,oc_red,ELEC",
                "legend_13_ff0000,E&M/ATC Cable,line,M,oc_red,ATC",
                "legend_14_ff0000,Public Lighting Cable,line,L,oc_red,PL",
                "legend_15_ff0000,TCSS Cable,line,M,oc_red,TCSS",

                "legend_06_ffbf00,Cable TV Cable,line,C,oc_yellow_1,CATV",

                "legend_07_dda500,PCCW Cable,line,T,oc_yellow_2,PCCW",
                "legend_08_dda500,Hutchison Cable,line,T,oc_yellow_2,HGC",
                "legend_09_dda500,NT&T Cable,line,T,oc_yellow_2,NT&T",
                "legend_16_dda500,New World Telecom Cable,line,T,oc_yellow_2,NWT",
                "legend_17_dda500,HKBN Cable,line,T,oc_yellow_2,HKBN",
                "legend_18_dda500,Eaststar Cable,line,T,oc_yellow_2,EASTERSTAR",
                "legend_19_dda500,WT&T Cable,line,T,oc_yellow_2,WT&T",
                "legend_20_dda500,TGT Cable,line,T,oc_yellow_2,TGT",
                "legend_21_dda500,TRAX Cable,line,T,oc_yellow_2,TRAX",

                "legend_22_ff7f00,Gas Pipe,line,G,oc_yellow_3,GAS",

                "legend_10_0000ff,Fresh Water Pipe,line,A,oc_blue_1,F WAT",
                "legend_11_007fff,Salt Water Pipe,line,B,oc_blue_2,S WAT",
                "legend_23_0000ff,Irrigation Water Pipe,line,A,oc_blue_1,IR",

                "legend_24_4a9500,Storm Water Pipe,line,S,oc_green_2,STORM",
                "legend_25_a5dd00,Foul Water Pipe,line,F,oc_green_1,FOUL",

                "legend_26_000000,Cooling Main Pipe,line,U,oc_dark,COOLING MAIN",
                "legend_27_000000,Unclassified Utility Line,line,U,oc_dark,UN",
                "legend_28_4a9500,U-Channel,line,S,oc_green_2,U-C",
                "legend_29_4a9500,S-Channel,line,S,oc_green_2,S-C"
        };
    }
}
