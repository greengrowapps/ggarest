package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnExceptionListener;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.listeners.OnTimeoutListener;

import java.util.Collection;


public class RequestBuilderImpl implements RequestBuilder {

    private final WebserviceImpl webservice;
    private final ConnectionDefinition connectionDefinition;

    public RequestBuilderImpl(ConnectionDefinition connectionDefinition, WebserviceImpl webservice){
        this.connectionDefinition = connectionDefinition;
        this.webservice = webservice;
    }

    @Override
    public RequestBuilder withBody(Object body) {
        connectionDefinition.setBody(body);
        return this;
    }
    @Override
    public RequestBuilder withPlainBody(String body) {
        connectionDefinition.setPlainBody(body);
        return this;
    }

    @Override
    public RequestBuilder addHeader(String key, String value) {
        connectionDefinition.addHeader(key,value);
        return this;
    }

    @Override
    public RequestBuilder addHeaders(Collection<? extends RequestHeader> headers) {
        for(RequestHeader header : headers) {
            connectionDefinition.addHeader(header.getKey(), header.getValue());
        }
        return this;
    }

    @Override
    public RequestBuilder withTimeout(long millis) {
        connectionDefinition.setTimeout(millis);
        return this;
    }

    @Override
    public RequestBuilder onResponse(int statusCode, OnResponseListener listener) {
        connectionDefinition.registerListener(statusCode,listener);
        return this;
    }

    @Override
    public <T> RequestBuilder onResponse(Class<T> responseClass, int statusCode, OnObjResponseListener<T> listener) {
        connectionDefinition.registerListener(responseClass,statusCode,listener);
        return this;
    }

    @Override
    public <T> RequestBuilder onResponse(Class<T> responseClass, int statusCode, OnListResponseListener<T> listener) {
        connectionDefinition.registerListener(responseClass,statusCode,listener);
        return this;
    }

    @Override
    public RequestBuilder onSuccess(OnResponseListener listener) {
        connectionDefinition.registerListener(200,listener);
        return this;
    }

    @Override
    public <T> RequestBuilder onSuccess(Class<T> responseClass, OnObjResponseListener<T> listener) {
        connectionDefinition.registerListener(responseClass,200,listener);
        return this;
    }

    @Override
    public <T> RequestBuilder onSuccess(Class<T> responseClass, OnListResponseListener<T> listener) {
        connectionDefinition.registerListener(responseClass,200,listener);
        return this;
    }

    @Override
    public RequestBuilder onOther(OnResponseListener listener) {
        connectionDefinition.setDefaultListener(listener);
        return this;
    }

    @Override
    public RequestBuilder onTimeout(OnTimeoutListener onTimeoutListener) {
        connectionDefinition.setTimeoutListener(onTimeoutListener);
        return this;
    }

    @Override
    public RestRequest execute() throws AlreadyExecutingException {
        RestRequest restRequest = build();
        restRequest.execute();
        return restRequest;
    }

    @Override
    public void executeAndWait() throws AlreadyExecutingException {
        RestRequest restRequest = build();
        restRequest.executeAndWait();
    }

    @Override
    public RestRequest build() {
        return new RestRequestImpl(connectionDefinition, webservice);
    }

    @Override
    public RequestBuilder onException(OnExceptionListener onExceptionListener) {
        connectionDefinition.setOnExceptionListener(onExceptionListener);
        return this;
    }
}
