package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.serialization.JsonSerializer;
import com.greengrowapps.ggarest.streams.StreamConverterImpl;
import com.greengrowapps.ggarest.webservice.RequestCallbackCaller;
import com.greengrowapps.ggarest.webservice.RequestExecution;
import com.greengrowapps.ggarest.webservice.RequestExecutionCallbacks;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactory;
import com.greengrowapps.ggarest.webservice.RequestExecutionImpl;

public class GgaRestTest extends RealConnectionsTest{
    @Override
    protected Webservice getWebserviceInstance() {

        RequestExecutionFactory factory = new RequestExecutionFactory() {
            @Override
            public RequestExecution newInstance(ConnectionDefinition connectionDefinition, final RequestExecutionCallbacks requestExecutionCallbacks, WebserviceImpl webservice) {
                return new RequestExecutionImpl(connectionDefinition, new RequestCallbackCaller() {
                    @Override
                    public void callRequestCompleted(ResponseImpl response) {
                        requestExecutionCallbacks.onRequestCompleted(response);
                    }

                    @Override
                    public void callError(Exception e) {
                        requestExecutionCallbacks.onException(e);
                    }
                }, webservice );
            }
        };

        return new WebserviceImpl(factory, new JsonSerializer(), new StreamConverterImpl("UTF-8"));
    }

    @Override
    public void testSimpleGet() throws Exception {
        super.testSimpleGet();
    }
}
