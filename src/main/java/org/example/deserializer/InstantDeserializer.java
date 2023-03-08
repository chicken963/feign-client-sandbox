package org.example.deserializer;

import com.google.gson.JsonElement;
import org.threeten.bp.Instant;


public class InstantDeserializer implements Deserializer<Instant> {
    @Override
    public Instant deserialize(JsonElement jsonElement) {
        long unixMillis = (long) (jsonElement.getAsJsonPrimitive().getAsDouble() * 1000);
        return Instant.ofEpochMilli(unixMillis);
    }
}
