package com.greengrowapps.ggarest;


import com.greengrowapps.ggarest.mock.MockRequest;
import com.greengrowapps.ggarest.mock.MockRequestBuilder;
import com.greengrowapps.ggarest.mock.MockRequestBuilderImpl;
import com.greengrowapps.ggarest.notifiers.ListNotifier;
import com.greengrowapps.ggarest.serialization.Serializer;
import com.greengrowapps.ggarest.streams.StreamConverter;
import com.greengrowapps.ggarest.webservice.RequestExecution;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactory;
import com.greengrowapps.ggarest.webservice.RequestExecutionCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebserviceImpl implements Webservice{

    private final RequestExecutionFactory requestExecutionFactory;
    private final StreamConverter streamConverter;
    private final Serializer serializer;
    private final Map<String, String> defaultHeaders = new HashMap<>();
    private final List<MockRequest> mockRequests = new ArrayList<>();

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

    @Override
    public MockRequestBuilder mockGet(String url) {
        return MockRequestBuilderImpl.newGet(this,url);
    }

    @Override
    public MockRequestBuilder mockPost(String url) {
        return MockRequestBuilderImpl.newPost(this,url);
    }

    @Override
    public MockRequestBuilder mockPut(String url) {
        return MockRequestBuilderImpl.newPut(this,url);
    }

    @Override
    public MockRequestBuilder mockDelete(String url) {
        return MockRequestBuilderImpl.newDelete(this,url);
    }

    public RequestExecution execute(ConnectionDefinition connectionDefinition, RequestExecutionCallbacks requestExecutionCallbacks) {
        RequestExecution requestExecution = requestExecutionFactory.newInstance(
                connectionDefinition,
                requestExecutionCallbacks,
                mockRequests,
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

    public void addMockedRequest(MockRequest mockRequest) {
        mockRequests.add(mockRequest);
    }

    public Map<String, String> getDefaultHeaders() {
        return new HashMap<>(defaultHeaders);
    }
}
