package com.hkm.root.json_io_model;

import android.net.Uri;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hkm.datamodel.PhotoViewData;

import java.lang.reflect.Type;

/**
 * Created by Hesk on 11/6/2014.
 */
public class PhotoViewDataModel implements JsonSerializer<PhotoViewData>, JsonDeserializer<PhotoViewData> {


    @Override
    public PhotoViewData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject j = json.getAsJsonObject();
        final String str = j.get("description").getAsString();
        final String uri = j.get("local_uri").getAsString();
        final String tag = j.get("tag").getAsString();
        final Uri mUri = Uri.parse(uri);
        final PhotoViewData ph = new PhotoViewData(str, mUri);
        ph.tag = tag;
        //  context.<Uri>deserialize(jsonObject.get("local_uri"), Uri.class)
        return ph;
    }

    @Override
    public JsonElement serialize(PhotoViewData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject o = new JsonObject();
        o.addProperty("description", src.description);
        o.addProperty("local_uri", src.uri.toString());
        o.addProperty("tag", src.tag);
        return o;
    }
}
