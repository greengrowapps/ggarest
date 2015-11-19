package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

public class ResponseImpl implements Response{

    private final int statusCode;
    private final Map<String, String> headers;
    private final String body;
    private final Serializer serializer;

    public ResponseImpl(int statusCode, Map<String, String> headers, String body, Serializer serializer) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.serializer = serializer;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getBody() {
        return body;
    }

    public <T> T deserializeBody(Class<T> clazz) throws IOException {
        return serializer.fromString(body, clazz);
    }
}
