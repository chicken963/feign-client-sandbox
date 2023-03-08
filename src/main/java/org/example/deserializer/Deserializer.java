package org.example.deserializer;

import com.google.gson.JsonElement;

public interface Deserializer<T>  {
    T deserialize(JsonElement e);
}
