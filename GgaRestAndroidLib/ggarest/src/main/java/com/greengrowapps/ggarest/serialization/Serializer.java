package com.greengrowapps.ggarest.serialization;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;

import java.io.IOException;

public interface Serializer {

    <T> T fromString(String string, Class<T> clazz) throws IOException;
    String fromObject(Object object) throws IOException;

}
