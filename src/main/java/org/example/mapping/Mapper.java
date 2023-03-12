package org.example.mapping;

import com.google.gson.JsonElement;

public interface Mapper<T> {
    T deserialize(JsonElement element);
    JsonElement serialize(T value);
}
