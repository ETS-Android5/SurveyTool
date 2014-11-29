package com.hkm.root.json_io_model;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Hesk on 11/6/2014.
 */
public class UriModel implements JsonSerializer<Uri>, JsonDeserializer<Uri> {

    @Override
    public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
        String uri_path = src.toString();
        return new JsonPrimitive(uri_path);
    }

    @Override
    public Uri deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final String jsonObject = json.getAsJsonPrimitive().getAsString();
        Log.d("get here", jsonObject);
        //final Uri uri = Uri.parse(jsonObject.get("local_uri").getAsString());
        final Uri uri = Uri.parse(jsonObject);
        return uri;
    }
}
