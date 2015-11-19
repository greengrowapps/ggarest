package com.greengrowapps.ggarest.webservice;

import android.os.Handler;

import com.greengrowapps.ggarest.ConnectionDefinition;
import com.greengrowapps.ggarest.WebserviceImpl;


public class RequestExecutionAndroidFactory implements RequestExecutionFactory {

    private final Handler mainThreadHandler;

    public RequestExecutionAndroidFactory(Handler mainThreadHandler){
        this.mainThreadHandler = mainThreadHandler;
    }


    @Override
    public RequestExecution newInstance(ConnectionDefinition connectionDefinition, RequestExecutionCallbacks callbacks, WebserviceImpl webservice) {

        return new RequestExecutionImpl(connectionDefinition, new HandleredCallbackCallerImpl(mainThreadHandler,callbacks), webservice);

    }
}
