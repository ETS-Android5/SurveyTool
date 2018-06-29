package com.hkm.root.handler;

import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.hkm.Application.appWork;
import com.hkm.datamodel.JobTaskData;
import com.hkm.datamodel.LocationMap;
import com.hkm.datamodel.PhotoViewData;
import com.hkm.datamodel.RouteNode;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.panel.corepanel.elements.ProposedTrialPit;
import com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox;
import com.hkm.oc.panel.corepanel.elements.mathmodels.line_eq;
import com.hkm.root.json_io_model.Exclude;
import com.hkm.root.json_io_model.LocationMapModel;
import com.hkm.root.json_io_model.PhotoViewDataModel;
import com.hkm.root.json_io_model.RouteNodeModel;
import com.hkm.root.json_io_model.SketchMapDataModel;
import com.hkm.root.json_io_model.TPBoxModel;
import com.hkm.root.json_io_model.TPModel;
import com.hkm.root.json_io_model.UriModel;
import com.hkm.root.json_io_model.lineEQmodel;

import java.util.ArrayList;
import java.util.UnknownFormatFlagsException;

import static com.hkm.U.Content.current_job_task;

/**
 * Created by Hesk on 30/6/2014.
 */
public class CursorDataConversion {
    public CursorDataConversion() {
    }

    public static MatrixCursor fromArrayList(ArrayList<RouteNode> nodelist) {
        String[] columnNames = {"_id", "rn_id"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        // String[] array = getResources().getStringArray(R.array.allStrings); //if strings are in resources
        String[] temp = new String[2];
        int id = 0;
        for (RouteNode item : nodelist) {
            id++;
            temp[0] = Integer.toString(id);
            temp[1] = Integer.toString(item.get_index());
            cursor.addRow(temp);
        }
        // String[] from = {"text"};
        // int[] to = {R.id.name_entry};
        return cursor;
    }

    public static String load_task(appWork ac, int n) {
        final String TAG = "saveTask";
        String result;
        try {
            Exclude ex = new Exclude();
            GsonBuilder gb = new GsonBuilder();
            gb
                    .registerTypeAdapter(Uri.class, new UriModel())
                    .registerTypeAdapter(PhotoViewData.class, new PhotoViewDataModel())
                    .registerTypeAdapter(SketchMapData.class, new SketchMapDataModel())
                    .registerTypeAdapter(LocationMap.class, new LocationMapModel())
                    .registerTypeAdapter(RouteNode.class, new RouteNodeModel())
                    .registerTypeAdapter(TPBox.class, new TPBoxModel())
                    .registerTypeAdapter(ProposedTrialPit.class, new TPModel())
                    .registerTypeAdapter(line_eq.class, new lineEQmodel())

                            //.addDeserializationExclusionStrategy(ex)
                    .serializeNulls()
                    .serializeSpecialFloatingPointValues()
            ;
            Gson g = gb.create();
            String junk = ac.getRef(appWork.TASK_JSON + n);
            Log.d(TAG, junk);
            current_job_task = g.fromJson(junk, JobTaskData.class);
            //Log.d(TAG, junk);
            result = "done";
        } catch (JsonIOException e1) {
            Log.d(TAG, e1.toString());
            result = e1.toString();
        } catch (JsonSyntaxException e3) {
            Log.d(TAG, e3.toString());
            result = e3.toString();
        } catch (JsonParseException e2) {
            Log.d(TAG, e2.toString());
            result = e2.toString();
        } catch (UnknownError e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (UnknownFormatFlagsException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (ClassCastException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (UnsupportedOperationException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (NullPointerException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (RuntimeException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (Exception e_) {
            Log.d(TAG, e_.toString());
            result = e_.toString();
        }
        return result;
    }

    public static String save_task(appWork ac) {
        final String TAG = "saveTask";
        String result;
        try {
            Exclude ex = new Exclude();
            GsonBuilder gb = new GsonBuilder();
            gb
                    .registerTypeAdapter(Uri.class, new UriModel())
                    .registerTypeAdapter(PhotoViewData.class, new PhotoViewDataModel())
                    .registerTypeAdapter(SketchMapData.class, new SketchMapDataModel())
                    .registerTypeAdapter(LocationMap.class, new LocationMapModel())
                    .registerTypeAdapter(RouteNode.class, new RouteNodeModel())
                    .registerTypeAdapter(TPBox.class, new TPBoxModel())
                    .registerTypeAdapter(ProposedTrialPit.class, new TPModel())
                    .registerTypeAdapter(line_eq.class, new lineEQmodel())
                    .serializeNulls()
                            //   .addSerializationExclusionStrategy(ex)
                    .enableComplexMapKeySerialization()
            ;
            //gb.setPrettyPrinting();
            Gson g = gb.create();
            if (current_job_task == null) throw new Exception("cannot save data");
            String f = g.toJson(current_job_task);
            Log.d(TAG, f);
            ac.save_Ref(appWork.TASK_JSON + current_job_task.getJID(), f);
            result = "done";
        } catch (JsonIOException e1) {
            Log.d(TAG, e1.toString());
            result = e1.toString();
        } catch (JsonSyntaxException e3) {
            Log.d(TAG, e3.toString());
            result = e3.toString();
        } catch (JsonParseException e2) {
            Log.d(TAG, e2.toString());
            result = e2.toString();
        } catch (UnknownError e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (UnknownFormatFlagsException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (ClassCastException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (UnsupportedOperationException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (NullPointerException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (RuntimeException e0) {
            Log.d(TAG, e0.toString());
            result = e0.toString();
        } catch (Exception e_) {
            Log.d(TAG, e_.toString());
            result = e_.toString();
        }
        return result;
    }
}
