package com.hkm.root.json_io_model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Reads a model in JSON format.
 *
 * @param in    Where to read the model from.
 * @param clazz The class of the object we expect to read.
 * @return The LogisticModelParameters object that we read.
 */
public class PolymorphicTypeAdapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {
    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject x = jsonElement.getAsJsonObject();
        try {
            //noinspection RedundantTypeArguments
            return jsonDeserializationContext.<T>deserialize(x.get("value"), Class.forName(x.get("class").getAsString()));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't understand serialized data, found bad type: " + x.get("class").getAsString());
        }
    }

    @Override
    public JsonElement serialize(T x, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject r = new JsonObject();
        r.add("class", new JsonPrimitive(x.getClass().getName()));
        r.add("value", jsonSerializationContext.serialize(x));
        return r;
    }
}