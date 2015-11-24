package com.greengrowapps.ggarest;


import com.greengrowapps.ggarest.serialization.Serializer;
import com.greengrowapps.ggarest.streams.StreamConverter;
import com.greengrowapps.ggarest.webservice.RequestExecution;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactory;
import com.greengrowapps.ggarest.webservice.RequestExecutionCallbacks;

import java.util.HashMap;
import java.util.Map;

public class WebserviceImpl implements Webservice{

    private final RequestExecutionFactory requestExecutionFactory;
    private final StreamConverter streamConverter;
    private final Serializer serializer;
    private final Map<String, String> defaultHeaders = new HashMap<>();

    public WebserviceImpl( RequestExecutionFactory requestExecutionFactory, Serializer serializer, StreamConverter streamConverter){
        this.requestExecutionFactory = requestExecutionFactory;
        this.serializer = serializer;
        this.streamConverter = streamConverter;
    }

    @Override
    public RequestBuilder get(String url) {
        return new RequestBuilderImpl( ConnectionDefinition.newGet(url) , this );
    }

    @Override
    public RequestBuilder post(String url) {
        return new RequestBuilderImpl( ConnectionDefinition.newPost(url) , this );
    }

    @Override
    public RequestBuilder put(String url) {
        return new RequestBuilderImpl( ConnectionDefinition.newPut(url) , this );
    }

    @Override
    public RequestBuilder delete(String url) {
        return new RequestBuilderImpl( ConnectionDefinition.newDelete(url) , this );
    }

    public RequestExecution execute(ConnectionDefinition connectionDefinition, RequestExecutionCallbacks requestExecutionCallbacks) {
        RequestExecution requestExecution = requestExecutionFactory.newInstance(
                connectionDefinition,
                requestExecutionCallbacks,
                this);
        requestExecution.start();
        return requestExecution;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public StreamConverter getStreamConverter() {
        return streamConverter;
    }

    public void addDefaultHeader(String key, String value) {
        defaultHeaders.put(key, value);
    }
    public void addDefaultHeaders(Map<String,String> headers) {
        defaultHeaders.putAll(headers);
    }
    public void clearDefaultHeaders(){
        defaultHeaders.clear();
    }

}
