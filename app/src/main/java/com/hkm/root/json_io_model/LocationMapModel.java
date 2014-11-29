package com.hkm.root.json_io_model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hkm.datamodel.LocationMap;

import java.lang.reflect.Type;

/**
 * Created by Hesk on 11/6/2014.
 */
public class LocationMapModel implements JsonSerializer<LocationMap>
        , JsonDeserializer<LocationMap> {

    @Override
    public LocationMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject j = json.getAsJsonObject();
        final LocationMap lm = new LocationMap(j.get("ID").getAsInt(), j.get("guid").getAsString());
        return lm;
    }

    @Override
    public JsonElement serialize(LocationMap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject o = new JsonObject();
        o.addProperty("guid", src.get_url());
        o.addProperty("ID", String.valueOf(src.get_attachment_id()));
        return o;
    }


}
