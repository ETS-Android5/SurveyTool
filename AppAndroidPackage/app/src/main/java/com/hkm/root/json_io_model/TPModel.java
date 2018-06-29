package com.hkm.root.json_io_model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.hkm.oc.panel.corepanel.elements.ProposedTrialPit;
import com.hkm.oc.panel.corepanel.elements.mathmodels.TPBox;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesk on 23/6/2014.
 */
public class TPModel implements JsonSerializer<ProposedTrialPit>, JsonDeserializer<ProposedTrialPit> {

    @Override
    public ProposedTrialPit deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext c) throws JsonParseException {
        final JsonObject j = src.getAsJsonObject();
        final Type typeOfrouteNode = new TypeToken<List<TPBox>>() {
        }.getType();
        final ArrayList<TPBox> cc = c.deserialize(j.get("boxcontainer").getAsJsonArray(), typeOfrouteNode);
        final ProposedTrialPit tps = new ProposedTrialPit(cc);
        return tps;
    }


    @Override
    public JsonElement serialize(ProposedTrialPit m, Type typeOfSrc, JsonSerializationContext c) {
        JsonObject o = new JsonObject();
        o.add("boxcontainer", c.serialize(m.getList()));
        return o;
    }

}