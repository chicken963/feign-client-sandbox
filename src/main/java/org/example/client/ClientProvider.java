package org.example.client;

import com.google.gson.*;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.example.deserializer.Deserializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

public class ClientProvider<C> {

    private static final Properties properties;
    private final Class<C> type;

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientProvider(Class<C> type) {
        this.type = type;
    }

    private final GsonBuilder mappingsAccumulator = new GsonBuilder();

    public <T> ClientProvider<C> withMapping(Class<T> clazz, final Deserializer<T> rule) {
        mappingsAccumulator
                .registerTypeAdapter(clazz, new JsonDeserializer<T>() {
                    @Override
                    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return rule.deserialize(jsonElement);
                    }
                });
        return this;
    }

    public C provide() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder(mappingsAccumulator.create()))
                .logger(new Logger.NoOpLogger())
                .logLevel(Logger.Level.FULL)
                .target(type, properties.getProperty("api.url"));
    }
}
