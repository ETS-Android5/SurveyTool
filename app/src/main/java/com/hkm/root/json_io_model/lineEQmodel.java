package com.hkm.root.json_io_model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hkm.oc.panel.corepanel.elements.mathmodels.line_eq;

import java.lang.reflect.Type;

/**
 * Created by Hesk on 23/6/2014.
 */
public class lineEQmodel implements JsonSerializer<line_eq>, JsonDeserializer<line_eq> {

    @Override
    public line_eq deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject j = src.getAsJsonObject();
        double a = j.get("m").getAsDouble();
        double b = j.get("t").getAsDouble();
        return new line_eq(a, b);
    }

    @Override
    public JsonElement serialize(line_eq src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject o = new JsonObject();
        o.addProperty("m", src.m);
        o.addProperty("t", src.t);
        return o;
    }

}
