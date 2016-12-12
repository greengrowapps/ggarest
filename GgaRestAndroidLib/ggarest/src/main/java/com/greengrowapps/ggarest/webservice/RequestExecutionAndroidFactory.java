package com.greengrowapps.ggarest.webservice;

import android.os.Handler;

import com.greengrowapps.ggarest.ConnectionDefinition;
import com.greengrowapps.ggarest.WebserviceImpl;
import com.greengrowapps.ggarest.authorization.UrlConnectionAuthorizator;
import com.greengrowapps.ggarest.mock.MockRequest;

import java.util.List;


public class RequestExecutionAndroidFactory implements RequestExecutionFactory {

    private final Handler mainThreadHandler;
    private UrlConnectionAuthorizator authorizator;

    public RequestExecutionAndroidFactory(Handler mainThreadHandler){
        this.mainThreadHandler = mainThreadHandler;
    }

    public void setAuthorizator(UrlConnectionAuthorizator authorizator){
        this.authorizator = authorizator;
    }

    @Override
    public RequestExecution newInstance(ConnectionDefinition connectionDefinition, RequestExecutionCallbacks callbacks, List<MockRequest> mockRequests, WebserviceImpl webservice) {

        return new RequestExecutionImpl(connectionDefinition, new HandleredCallbackCallerImpl(mainThreadHandler,callbacks), webservice, authorizator, mockRequests);

    }
}
