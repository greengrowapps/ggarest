package com.greengrowapps.ggarest.webservice;

import com.greengrowapps.ggarest.ConnectionDefinition;
import com.greengrowapps.ggarest.WebserviceImpl;


public interface RequestExecutionFactory {

    RequestExecution newInstance(ConnectionDefinition connectionDefinition, RequestExecutionCallbacks requestExecutionCallbacks, WebserviceImpl webservice);

}
