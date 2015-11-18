package com.greengrowapps.ggarest;


public interface Webservice {

    RequestBuilder get(String url);
    RequestBuilder post(String url);
    RequestBuilder put(String url);
    RequestBuilder delete(String url);

}
