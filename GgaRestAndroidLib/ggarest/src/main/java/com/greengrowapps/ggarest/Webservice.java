package com.greengrowapps.ggarest;


import com.greengrowapps.ggarest.mock.MockRequestBuilder;

public interface Webservice {

    RequestBuilder get(String url);
    RequestBuilder post(String url);
    RequestBuilder put(String url);
    RequestBuilder delete(String url);

    MockRequestBuilder mockGet(String url);
    MockRequestBuilder mockPost(String url);
    MockRequestBuilder mockPut(String url);
    MockRequestBuilder mockDelete(String url);


}
