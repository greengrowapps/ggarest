package com.greengrowapps.ggarest.webservice;

import com.greengrowapps.ggarest.ConnectionDefinition;
import com.greengrowapps.ggarest.WebserviceImpl;
import com.greengrowapps.ggarest.mock.MockRequest;

import java.util.List;

public class RequestExecutionFactoryJava implements RequestExecutionFactory {

    @Override
    public RequestExecution newInstance(ConnectionDefinition connectionDefinition, RequestExecutionCallbacks requestExecutionCallbacks, List<MockRequest> mockRequests, WebserviceImpl webservice) {
        return new RequestExecutionImplJava(connectionDefinition, new NormalCallbackCaller(requestExecutionCallbacks), webservice, mockRequests);
    }
}
