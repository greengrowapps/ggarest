package com.greengrowapps.ggarest.webservice;

import com.greengrowapps.ggarest.ResponseImpl;

import java.io.IOException;

public class NormalCallbackCaller implements RequestCallbackCaller{

    private final RequestExecutionCallbacks requestExecutionCallbacks;

    public NormalCallbackCaller(RequestExecutionCallbacks requestExecutionCallbacks){
        this.requestExecutionCallbacks = requestExecutionCallbacks;
    }

    @Override
    public void callRequestCompleted(final ResponseImpl response){
        requestExecutionCallbacks.onRequestCompleted(response);
    }

    @Override
    public void callError(final Exception e) {
        requestExecutionCallbacks.onException(e);
    }

    @Override
    public void callTimeout() {
        requestExecutionCallbacks.onTimeout();
    }
}
