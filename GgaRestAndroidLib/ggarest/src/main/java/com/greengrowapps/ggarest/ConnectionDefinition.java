package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.listeners.OnExceptionListener;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.listeners.OnTimeoutListener;
import com.greengrowapps.ggarest.notifiers.AbstractNotifier;
import com.greengrowapps.ggarest.notifiers.ListNotifier;
import com.greengrowapps.ggarest.notifiers.ObjectNotifier;
import com.greengrowapps.ggarest.notifiers.PlainNotifier;

import java.util.HashMap;
import java.util.Map;

public class ConnectionDefinition {

    private static final long DEFAULT_TIMEOUT = 10 * 1000;
    private OnExceptionListener exceptionListener;
    private final RestMethod method;
    private final String url;
    private final Map<String,String> headers = new HashMap<>();
    private Object body = null;
    private final Map<Integer,AbstractNotifier> statusCodeListeners = new HashMap<>();
    private OnResponseListener defaultListener;
    private OnTimeoutListener timeoutListener;
    private long timeout = DEFAULT_TIMEOUT;

    private ConnectionDefinition(RestMethod method, String url){
        this.method = method;
        this.url = url;
    }

    public static ConnectionDefinition newGet(String url){
        return new ConnectionDefinition(RestMethod.GET, url);
    }
    public static ConnectionDefinition newPost(String url){
        return new ConnectionDefinition(RestMethod.POST, url);
    }
    public static ConnectionDefinition newPut(String url){
        return new ConnectionDefinition(RestMethod.PUT, url);
    }
    public static ConnectionDefinition newDelete(String url){
        return new ConnectionDefinition(RestMethod.DELETE, url);
    }

    public void addHeader(String key, String value){
        headers.put(key,value);
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void registerListener(int statusCode, OnResponseListener listener){
        PlainNotifier plainNotifier = new PlainNotifier(listener);
        statusCodeListeners.put(statusCode, plainNotifier);
    }
    public <T> void registerListener(Class<T> responseClass, int statusCode, OnObjResponseListener<T> listener){
        ObjectNotifier<T> objectNotifier = new ObjectNotifier<>(responseClass,listener);
        statusCodeListeners.put(statusCode, objectNotifier);
    }
    public <T> void registerListener(Class<T> responseClass, int statusCode, OnListResponseListener<T> listener){
        ListNotifier<T> listNotifier = new ListNotifier<>(responseClass, listener);
        statusCodeListeners.put(statusCode, listNotifier);
    }
    public void setDefaultListener(OnResponseListener defaultListener) {
        this.defaultListener = defaultListener;
    }

    public void setTimeoutListener(OnTimeoutListener timeoutListener) {
        this.timeoutListener = timeoutListener;
    }

    public boolean isPostPut(){
        return method==RestMethod.POST || method==RestMethod.PUT;
    }

    public RestMethod getMethod() {
        return method;
    }
    public boolean hasBody() {
        return body!=null;
    }
    public Object getBody() {
        return body;
    }
    public OnExceptionListener getExceptionListener() {
        return exceptionListener;
    }
    public String getUrl() {
        return url;
    }
    public OnResponseListener getDefaultListener() {
        return defaultListener;
    }
    public boolean hasListenerForStatusCode(int statusCode) {
        return statusCodeListeners.containsKey(statusCode);
    }
    public AbstractNotifier getNotifier(int statusCode){
        return statusCodeListeners.get(statusCode);
    }

    public void setOnExceptionListener(OnExceptionListener onExceptionListener) {
        this.exceptionListener = onExceptionListener;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public long getTimeout() {
        return timeout;
    }

    public OnTimeoutListener getTimeoutListener() {
        return timeoutListener;
    }

    public void setTimeout(long timeoutMillis) {
        this.timeout = timeoutMillis;
    }
}
