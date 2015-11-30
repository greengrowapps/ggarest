package com.greengrowapps.ggarest;

import android.test.AndroidTestCase;

import com.greengrowapps.ggarest.authorization.CredentialsImpl;
import com.greengrowapps.ggarest.authorization.UrlConnectionAuthorizator;
import com.greengrowapps.ggarest.authorization.UrlConnectionBasicAuthorizator;
import com.greengrowapps.ggarest.serialization.JsonSerializer;
import com.greengrowapps.ggarest.streams.StreamConverterImpl;
import com.greengrowapps.ggarest.webservice.RequestCallbackCaller;
import com.greengrowapps.ggarest.webservice.RequestExecution;
import com.greengrowapps.ggarest.webservice.RequestExecutionCallbacks;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactory;
import com.greengrowapps.ggarest.webservice.RequestExecutionImpl;

public class GgaRestTest extends AndroidTestCase{

    UrlConnectionAuthorizator authorizator;

    protected WebserviceImpl getWebserviceInstance() {

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

                    @Override
                    public void callTimeout() {
                        requestExecutionCallbacks.onTimeout();
                    }
                }, webservice, authorizator );
            }
        };

        return new WebserviceImpl(factory, new JsonSerializer(), new StreamConverterImpl("UTF-8"));
    }

    protected void setLogin(String user, String pass) {
        authorizator = new UrlConnectionBasicAuthorizator(new CredentialsImpl(user,pass));
    }
}
