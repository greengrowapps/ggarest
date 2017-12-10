package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnExceptionListener;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.listeners.OnTimeoutListener;

import java.util.Collection;

public interface RequestBuilder {

    RequestBuilder withBody(Object body);
    RequestBuilder withPlainBody(String body);

    RequestBuilder addHeader(String key, String value);
    RequestBuilder addHeaders(Collection<? extends RequestHeader> headers);

    RequestBuilder withTimeout(long millis);

    RequestBuilder onResponse(int statusCode, OnResponseListener listener);
    <T> RequestBuilder onResponse(Class<T> responseClass, int statusCode, OnObjResponseListener<T> listener);
    <T> RequestBuilder onResponse(Class<T> responseClass, int statusCode, OnListResponseListener<T> listener);

    RequestBuilder onSuccess(OnResponseListener listener);
    <T> RequestBuilder onSuccess(Class<T> responseClass, OnObjResponseListener<T> listener);
    <T> RequestBuilder onSuccess(Class<T> responseClass, OnListResponseListener<T> listener);

    RequestBuilder onOther(OnResponseListener listener);
    RequestBuilder onTimeout(OnTimeoutListener onTimeoutListener);

    RestRequest execute() throws AlreadyExecutingException;
    void executeAndWait() throws AlreadyExecutingException;

    RestRequest build();

    RequestBuilder onException(OnExceptionListener onExceptionListener);

}
