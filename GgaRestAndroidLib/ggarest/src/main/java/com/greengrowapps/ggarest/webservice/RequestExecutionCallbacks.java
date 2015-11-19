package com.greengrowapps.ggarest.webservice;

import com.greengrowapps.ggarest.ResponseImpl;

import java.io.IOException;

public interface RequestExecutionCallbacks {
    void onRequestCompleted(ResponseImpl response);

    void onException(Exception exception);
}
