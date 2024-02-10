package com.example.tournamentmanager.adapter;

import com.google.gson.*;
import org.bson.types.ObjectId;

public class ObjectIdAdapter implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {
    @Override
    public JsonElement serialize(ObjectId src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toHexString());
    }

    @Override
    public ObjectId deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            try {
                return new ObjectId(json.getAsString());
            } catch (IllegalArgumentException e) {
                Gson fallbackGson = new Gson();
                return fallbackGson.fromJson(json, ObjectId.class);
            }
        } else {
            Gson fallbackGson = new Gson();
            return fallbackGson.fromJson(json, ObjectId.class);
        }
    }
}
