package com.greengrowapps.ggarest.mock;


import android.bluetooth.BluetoothAssignedNumbers;

import com.greengrowapps.ggarest.RestMethod;
import com.greengrowapps.ggarest.Webservice;
import com.greengrowapps.ggarest.WebserviceImpl;

public class MockRequestBuilderImpl implements MockRequestBuilder{

    private final WebserviceImpl webservice;
    private final RestMethod method;
    private final String url;

    private int resultCode = 200;
    private String content = null;

    private MockRequestBuilderImpl(WebserviceImpl webservice, RestMethod method, String url){
        this.webservice = webservice;
        this.method = method;
        this.url = url;
    }

    public static MockRequestBuilderImpl newGet(WebserviceImpl ws, String url){
        return new MockRequestBuilderImpl(ws, RestMethod.GET, url);
    }
    public static MockRequestBuilderImpl newPost(WebserviceImpl ws, String url){
        return new MockRequestBuilderImpl(ws, RestMethod.POST, url);
    }
    public static MockRequestBuilderImpl newPut(WebserviceImpl ws, String url){
        return new MockRequestBuilderImpl(ws, RestMethod.PUT, url);
    }
    public static MockRequestBuilderImpl newDelete(WebserviceImpl ws, String url){
        return new MockRequestBuilderImpl(ws, RestMethod.DELETE, url);
    }

    @Override
    public MockRequestBuilder responseCode(int code) {
        this.resultCode = code;
        return this;
    }

    @Override
    public MockRequestBuilder responseContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public void save() {
        webservice.addMockedRequest(new MockRequest(url,method,resultCode,content));
    }
}
