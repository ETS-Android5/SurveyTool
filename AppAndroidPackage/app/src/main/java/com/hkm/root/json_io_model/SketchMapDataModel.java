package com.hkm.root.json_io_model;

import android.net.Uri;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.hkm.datamodel.BigObserveDot;
import com.hkm.datamodel.RouteNode;
import com.hkm.datamodel.SketchMapData;
import com.hkm.oc.panel.corepanel.elements.ProposedTrialPit;
import com.hkm.oc.panel.corepanel.elements.SurveyBoundary;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesk on 11/6/2014.
 */
public class SketchMapDataModel implements JsonSerializer<SketchMapData>, JsonDeserializer<SketchMapData> {
    public static String DRAWMAP = "drawmap_uri";

    @Override
    public SketchMapData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext c) throws JsonParseException {
        final JsonObject j = json.getAsJsonObject();
        final SketchMapData loaded_map_data = new SketchMapData();

      /*  final Gson gson = new GsonBuilder()
                .registerTypeAdapter(RouteNode.class, new RouteNodeModel())
                .create();*/

        final Type typeOfrouteNode = new TypeToken<List<RouteNode>>() {
        }.getType();

        //final ArrayList<RouteNode> cnode_r = gson.fromJson(j.get("routenode"), typeOfrouteNode);
        final ArrayList<RouteNode> contextnode_r = c.deserialize(j.get("routenode").getAsJsonArray(), typeOfrouteNode);

        loaded_map_data


                .set_original_base_map(Uri.parse(j.get("basemap_url").getAsString()))
                .set_dpi_ratio_attachment(
                        j.get("dpi").getAsInt(),
                        j.get("ratio").getAsDouble(),
                        j.get("submission_number").getAsInt()
                )
                .setRealDistanceAB(j.get("distance_ab").getAsFloat())

                .set_point_A(c.<BigObserveDot>deserialize(j.get("A").getAsJsonObject(), BigObserveDot.class))
                .set_point_B(c.<BigObserveDot>deserialize(j.get("B").getAsJsonObject(), BigObserveDot.class))
                .setRouteNodes(contextnode_r)

                .setSurveybound(c.<SurveyBoundary>deserialize(j.get("surveyboundary").getAsJsonObject(), SurveyBoundary.class))
                .setTpElement(c.<ProposedTrialPit>deserialize(j.get("tp").getAsJsonObject(), ProposedTrialPit.class));

        if (j.has(DRAWMAP)) {
            loaded_map_data.set_draw_map_uri(Uri.parse(j.get(DRAWMAP).getAsString()));
        }

        return loaded_map_data;
    }

    @Override
    public JsonElement serialize(SketchMapData m, Type typeOfSrc, JsonSerializationContext c) {
        JsonObject o = new JsonObject();
        if (m.hasUri()) {
            o.addProperty(DRAWMAP, m.get_draw_map_uri().toString());
        }
        o.addProperty("basemap_url", m.get_base_map_url().toString());
        o.addProperty("dpi", m.getdpi());
        o.addProperty("ratio", m.getratio());
        o.addProperty("data", m.get_data());
        o.addProperty("distance_ab", m.getRealDistanceAB());
        o.addProperty("submission_number", m.getAttachmentId());
        o.add("A", c.serialize(m.get_point_a(), BigObserveDot.class));
        o.add("B", c.serialize(m.get_point_b(), BigObserveDot.class));
        o.add("routenode", c.serialize(m.getRouteNodeList()));
        o.add("surveyboundary", c.serialize(m.getSurveybound(), SurveyBoundary.class));
        o.add("tp", c.serialize(m.getTpElement(), ProposedTrialPit.class));

        //  o.add("route_path_render", c.serialize(m.getRoute()));
        //  o.addProperty("routenode", m.getRouteNodeList());
        return o;
    }
}
