package com.hkm.root.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.hkm.U.Content;
import com.hkm.datamodel.DataHandler;
import com.hkm.datamodel.Label;
import com.hkm.datamodel.RouteNode;
import com.hkm.oc.R;
import com.hkm.U.Tool;
import com.hkm.oc.panel.corepanel.elements.mathmodels.EQPool;
import com.hkm.root.Dialog.activity_control;

import java.util.Collections;
import java.util.List;

/**
 * Created by Hesk on
 */
public class RouteNodeAdapter extends BaseAdapter {
    public final String TAG = "RouteNodeAdapter Notification";
    private final Activity context;
    public activity_control adapterListener;
    private int orientation, totalWidth = 0, lock_open_position = -1;
    private boolean isOnStateAddNew, isLockedForAddingNew, list_changed;
    private List<RouteNode> data = Collections.emptyList();
    // a field in the adapter
    private int mSelectedPosition = -1;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public RouteNodeAdapter(Activity ctx, List<RouteNode> active_data_bind) {
        context = ctx;
        data = active_data_bind;

        totalWidth = getScreensize();
        Log.d("tag total width", ": " + totalWidth);
        isLockedForAddingNew = false;
        isOnStateAddNew = false;
        list_changed = false;
        //  setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adapterListener = new activity_control() {
            @Override
            public void onPickLegend(int target_list_i) {
            }

            @Override
            public void onListChanged(int type) {
            }
        };

    }

    public void updateBananas(List<RouteNode> bananaPhones) {
        ThreadPreconditions.checkOnMainThread();
        this.data = bananaPhones;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public RouteNode getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getScreensize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public void onOrientationChange(int orientation) {
        this.orientation = orientation;
        notifyDataSetChanged();
        totalWidth = getScreensize();
    }

    /**
     * the mechanical update and renew the data changed from the list
     *
     * @param Ox
     * @param route_object
     */
    private void update_mechanical_route_node_bar(Pattern Ox, RouteNode route_object) {

        /** update each field */
        boolean complete = true;
        if (route_object.get_label() == null) {
            complete = false;
        }
        /** object controllor here */
        if (complete) {
            Log.d("CHECK START", "Fields complete not first rez: " + Ox.pos);
            isLockedForAddingNew = false;
            Ox.setStatus(Pattern.status.COMPLETE);
            // Ox.updateGlobalListItem()
            //   Ox.refresh();
        } else {
            //  Ox.setStatus(Pattern.status.INCOMPLETE);
            Log.d("CHECK START", "Fields incomplete not first rez: " + Ox.pos);
            isLockedForAddingNew = true;
        }

        if (isOnStateAddNew && !complete && lock_open_position == Ox.pos) {
            Log.d("START INCOMPLETE update", "TRIGGER AT THE POSITION:" + Ox.pos);
            isLockedForAddingNew = true;
            Ox.new_rezzed_row = true;
            isOnStateAddNew = false;
            lock_open_position = -1;
            Ox.setStatus(Pattern.status.INCOMPLETE);
        }


        Log.d("CHECK START", "at tag position of " + Ox.pos + "");
    }

    private Pattern newInstancePattern(int position, View rv) {
        final Pattern h = new Pattern(position, context);
        h.setAdapter(this);
        h.set_interactive_screen_width(totalWidth);
        h.addConvertViewToElements(rv).setStatus(Pattern.status.INITIAL).startEngine();
        return h;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    // getter and setter methods for the field above
    public void setSelectedPositionChanged(int selectedPosition) {
        mSelectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        View rv = cv;
        if (rv == null) {
            LayoutInflater in = context.getLayoutInflater();
            rv = in.inflate(R.layout.datalist_row, null);
            final Pattern r = newInstancePattern(position, rv);
            rv.setTag(r);
            Log.d(TAG, "the list is triggered to update at the view position of: " + position + ". Pos data: new data");
        }
        final Pattern pattern = (Pattern) rv.getTag();
        final int found_reuse_view_int = pattern.pos;


        try {
            final RouteNode node = Content.current_sketch_map.get_route_node_at(position);
            if (node != null) {
                Log.d(TAG, node.get_label().toString());
                Log.d(TAG, "the list is triggered to update at the view position of: " + position + ". Pos data:" + found_reuse_view_int);
                pattern.bind_data2display(node);
                update_mechanical_route_node_bar(pattern, node);
            }
        } catch (Exception e) {
        }
        if (mSelectedPosition == position) {

        }
        pattern.set_interactive_screen_width(totalWidth);
        rv.setTag(pattern);

        return rv;
    }

    public boolean isCompleted() {
        return !isLockedForAddingNew;
    }

    private RouteNode generate_new_route_node_structure() {
        final RouteNode new_route_node = new RouteNode(EQPool.generate_random_id());
        return new_route_node
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);
    }

    private RouteNode generate_logical_new_route(final int size) {
        final int last_index = size - 1;
        final Label previous_label = Content.current_sketch_map.get_label_at(last_index);
        final int newDNumeric = Integer.parseInt(previous_label.get_dNumeric()) + 1;
        final RouteNode new_route_node = new RouteNode(EQPool.generate_random_id());
        final String display_label_string = previous_label.get_dLetter() + newDNumeric + DataHandler.sharpOut(previous_label.get_letterIntrinsic());
        final Label lab = new Label();
        lab.set_display_big_button_label(display_label_string);
        lab.set_dLetter(previous_label.get_dLetter());
        lab.set_dNumeric(newDNumeric + "");
        lab.set_letterIntrinsic(previous_label.get_letterIntrinsic());
        lab.set_lineLabelInt(previous_label.get_line_label_int());
        lab.refresh_data(this.context);
        return new_route_node
                .set_label(lab)
                .set_cable_r(0.0f)
                .set_distance_a(0.00f)
                .set_distance_b(0.00f)
                .set_depth(0.00f)
                .set_r(0, 0)
                .set_cut(false);
    }

    /**
     * insert from the very last item
     *
     * @return
     */
    public boolean addnew() {
        if (isLockedForAddingNew) {
            Tool.trace(context, R.string.warning01);
            return false;
        }
        //this is the logic of getting the previous data type into the new row and have it fill out the first one
        final int size = Content.current_sketch_map.get_route_size();
        if (size > 0) {
            Content.current_sketch_map.add_routenode(generate_logical_new_route(size));
        } else {
            Content.current_sketch_map.add_routenode(generate_new_route_node_structure());
        }
        //--------------------------------------------------------
        //   Content.radius_list.add(DataHandler.RezNewDataRow(rowtype));
        //--------------------------------------------------------
        //   Content.radius_a_slot.add(1);
        //   Content.radius_b_slot.add(1);
        notifyDataSetChanged();
        isLockedForAddingNew = true;
        isOnStateAddNew = true;
        Tool.trace(context, R.string.notice_new_row);
        adapterListener.onListChanged(0);
        return true;

    }

    /**
     * insert from between the rows
     *
     * @param current_pos
     * @return
     */
    public boolean addnew(int current_pos) {
        if (current_pos >= getCount() - 1) {
            Tool.trace(context, R.string.warn_new_row);
            return false;
        }

        if (isLockedForAddingNew) {
            Tool.trace(context, R.string.warning01);
            return false;
        }

        Content.current_sketch_map.add_routenode(generate_new_route_node_structure(), current_pos);
        notifyDataSetChanged();
        isLockedForAddingNew = true;
        Tool.trace(context, R.string.notice_new_row_insert);
        lock_open_position = current_pos;
        adapterListener.onListChanged(0);
        return true;
    }

    public void removeItem() {
        Content.current_sketch_map.remove_routenode();
        isLockedForAddingNew = false;
        adapterListener.onListChanged(0);
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        Content.current_sketch_map.remove_routenode(index);
        isLockedForAddingNew = false;
        adapterListener.onListChanged(0);
        notifyDataSetChanged();
    }

    public void updateListItem(Pattern H) {
        //will able to add another new row....
        if (H.analyze_distances_possible_and_update()) {
            notifyDataSetChanged();
            isLockedForAddingNew = false;
            Tool.trace(context, R.string.notice_done);
        } else {
            isLockedForAddingNew = true;
            Tool.trace(context, R.string.warning03);
        }
    }

    public void setAdapterListener(activity_control c) {
        adapterListener = c;
    }


}