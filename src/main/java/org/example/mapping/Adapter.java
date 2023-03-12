package org.example.mapping;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Adapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {
    private final Mapper<T> mapper;

    public Adapter(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public T deserialize(JsonElement jsonElement,
                         Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return mapper.deserialize(jsonElement);
    }


    @Override
    public JsonElement serialize(T value, Type type, JsonSerializationContext jsonSerializationContext) {
        return mapper.serialize(value);
    }

}
