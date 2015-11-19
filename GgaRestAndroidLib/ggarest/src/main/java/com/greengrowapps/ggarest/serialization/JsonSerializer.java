package com.greengrowapps.ggarest.serialization;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonSerializer implements Serializer {

    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T fromString(String string, Class<T> clazz) throws IOException {
        return mapper.readValue(string, clazz);
    }

    @Override
    public String fromObject(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }
}
