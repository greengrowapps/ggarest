package com.greengrowapps.ggarest;


import java.util.Map;

public interface Response {

    int getStatusCode();
    Map<String,String> getHeaders();
    String getBody();

}
