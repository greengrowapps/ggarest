package com.greengrowapps.ggarest.mock;

import com.greengrowapps.ggarest.ResponseImpl;
import com.greengrowapps.ggarest.RestMethod;
import com.greengrowapps.ggarest.serialization.Serializer;

import java.util.HashMap;

public class MockRequest {
    private final String url;
    private final RestMethod method;
    private final String content;
    private final int resultCode;

    public MockRequest(String url, RestMethod method, int resultCode, String content){
        this.url = url;
        this.method = method;
        this.resultCode = resultCode;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public RestMethod getMethod() {
        return method;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getContent() {
        return content;
    }

    public boolean isMe(String url, RestMethod method){
        return this.url.equals(url) && this.method == method;
    }

    public ResponseImpl buildResponse(Serializer serializer) {
        return new ResponseImpl(resultCode,new HashMap<String, String>(),content,serializer);
    }
}
