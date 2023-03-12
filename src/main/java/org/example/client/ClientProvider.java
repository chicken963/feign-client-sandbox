package org.example.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.example.mapping.Adapter;
import org.example.mapping.Mapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ClientProvider<C> {

    private static final Properties properties;
    private final Class<C> type;
    private final GsonBuilder mappingsBuilder = new GsonBuilder();

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

    public <T> ClientProvider<C> withMapping(Class<T> clazz, Mapper<T> mapper) {
        Adapter<T> typeAdapter = new Adapter<T>(mapper);
        mappingsBuilder.registerTypeAdapter(clazz, typeAdapter);
        return this;
    }

    public C provide() {
        Gson mappings = mappingsBuilder.create();
        return Feign.builder()
                .encoder(new GsonEncoder(mappings))
                .decoder(new GsonDecoder(mappings))
                .logger(new Logger.NoOpLogger())
                .logLevel(Logger.Level.FULL)
                .target(type, properties.getProperty("api.url"));
    }
}
