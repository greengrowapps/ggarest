package com.greengrowapps.ggarest.mock;

public interface MockRequestBuilder {

    MockRequestBuilder responseCode(int code);
    MockRequestBuilder responseContent(String content);
    void save();

}
