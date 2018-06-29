package com.hkm.root.json_io_model;

import android.graphics.PointF;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox;
import com.hkm.oc.panel.corepanel.elements.mathmodels.line_eq;

import java.lang.reflect.Type;

/**
 * Created by Hesk on 23/6/2014.
 */
public class TPBoxModel implements JsonSerializer<TPBox>, JsonDeserializer<TPBox> {

    @Override
    public TPBox deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext c) throws JsonParseException {
        final JsonObject j = src.getAsJsonObject();
        final TPBox nodeMap = new TPBox();
        double a = j.get("box_width").getAsDouble();
        double b = j.get("box_height").getAsDouble();
        String s = j.get("box_label").getAsString();
        PointF p = c.deserialize(j.get("box_center").getAsJsonObject(), PointF.class);
        float[] g1 = c.deserialize(j.get("group_start").getAsJsonArray(), float[].class);
        float[] g2 = c.deserialize(j.get("group_end").getAsJsonArray(), float[].class);
        line_eq lo = c.<line_eq>deserialize(j.get("ori_l").getAsJsonObject(), line_eq.class);
        line_eq l2 = c.<line_eq>deserialize(j.get("l2").getAsJsonObject(), line_eq.class);
        nodeMap
                .loadWH(a, b, p)
                .setLabel(s)
                .setEditingLabel(false)
                .setSelected(false)
                .setLabelPositionOffsetPreview(c.<PointF>deserialize(j.get("position_offset").getAsJsonObject(), PointF.class))
                .setBaseOnLine(lo)
                .setL2(l2)
                .setPointsGroup(g1, g2);
        ;

        return nodeMap;
    }

    @Override
    public JsonElement serialize(TPBox m, Type typeOfSrc, JsonSerializationContext c) {
        JsonObject o = new JsonObject();
        o.addProperty("box_width", m.getWidth());
        o.addProperty("box_height", m.getHeight());
        o.addProperty("box_label", m.getLabel());
        o.add("position_offset", c.serialize(m.getPosition_offset(), PointF.class));
        o.add("l2", c.serialize(m.getL2(), line_eq.class));
        o.add("ori_l", c.serialize(m.getLOriginal(), line_eq.class));
        o.add("group_start", c.serialize(m.exportFloatGroup1()));
        o.add("group_end", c.serialize(m.exportFloatGroup2()));
        o.add("box_center", c.serialize(m.getPairCenters()[1]));
        return o;
    }
}