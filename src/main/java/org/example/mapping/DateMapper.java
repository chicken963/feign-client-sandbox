package org.example.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Date;

public class DateMapper implements Mapper<Date>{
    @Override
    public Date deserialize(JsonElement element) {
        long unixMillis = (long) (element.getAsJsonPrimitive().getAsDouble());
        return new Date(unixMillis);
    }

    @Override
    public JsonElement serialize(Date value) {
        return new JsonPrimitive(String.valueOf(value.getTime()));
    }
}
