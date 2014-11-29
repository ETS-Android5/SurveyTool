package com.hkm.root.json_io_model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hkm.datamodel.Label;
import com.hkm.datamodel.RouteNode;

import java.lang.reflect.Type;

/**
 * Created by Hesk on 17/6/2014.
 */

public class RouteNodeModel implements JsonSerializer<RouteNode>, JsonDeserializer<RouteNode> {


    @Override
    public RouteNode deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject j = src.getAsJsonObject();
        final RouteNode nodeMap = new RouteNode(j.get("datastorage_index").getAsInt());

        // final ArrayList<RouteNode> contextarray = context.deserialize(j.getAsJsonArray("routenode"), RouteNode.class);

        nodeMap
                .set_cable_r(j.get("cable_radius").getAsFloat())
                .set_cut(j.get("is_node_cut").getAsBoolean())
                .set_depth(j.get("depth").getAsFloat())
                .set_r(j.get("cal_r_a").getAsInt(), j.get("cal_r_b").getAsInt())
                .set_distance_a(j.get("measure_a").getAsFloat())
                .set_distance_b(j.get("measure_b").getAsFloat())
                .set_has_result(j.get("has_result").getAsBoolean())
                .set_label(context.<Label>deserialize(j.get("label_object").getAsJsonObject(), Label.class))
                .choose_possible_point(j.get("selected_dot_result").getAsInt())

        ;
        return nodeMap;
    }


    @Override
    public JsonElement serialize(RouteNode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject o = new JsonObject();
        o.addProperty("datastorage_index", src.get_index());
        o.addProperty("cal_r_a", src.get_cal_r_a());
        o.addProperty("cal_r_b", src.get_cal_r_b());
        o.addProperty("measure_a", src.get_distance_a());
        o.addProperty("measure_b", src.get_distance_b());
        o.addProperty("depth", src.get_depth());
        o.addProperty("cable_radius", src.get_cable_radius());
        o.addProperty("is_node_cut", src.get_cut());
        o.addProperty("has_result", src.has_result());
        o.addProperty("selected_dot_result", src.get_decision());
        o.add("label_object", context.serialize(src.get_label(), Label.class));

        return o;
    }
}
