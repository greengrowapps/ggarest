package com.greengrowapps.ggarest.webservice;

import android.os.Handler;

import com.greengrowapps.ggarest.ResponseImpl;

import java.io.IOException;

public class HandleredCallbackCallerImpl implements RequestCallbackCaller{

    private final Handler handler;
    private final RequestExecutionCallbacks requestExecutionCallbacks;

    public HandleredCallbackCallerImpl(Handler handler, RequestExecutionCallbacks requestExecutionCallbacks){
        this.handler = handler;
        this.requestExecutionCallbacks = requestExecutionCallbacks;
    }

    @Override
    public void callRequestCompleted(final ResponseImpl response){
        handler.post(new Runnable() {
            @Override
            public void run() {
                requestExecutionCallbacks.onRequestCompleted(response);
            }
        });
    }

    @Override
    public void callError(final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                requestExecutionCallbacks.onException(e);
            }
        });
    }
}
