package com.greengrowapps.ggarest.webservice;

import com.greengrowapps.ggarest.ResponseImpl;


public interface RequestCallbackCaller {

    void callRequestCompleted(ResponseImpl response);
    void callError(final Exception e);
    void callTimeout();
}
