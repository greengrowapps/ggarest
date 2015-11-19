package com.greengrowapps.ggarest;

import android.test.AndroidTestCase;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnExceptionListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.tools.AsyncBlockRunner;
import com.greengrowapps.ggarest.tools.AsyncTimeoutHelper;

import java.util.List;

import static com.greengrowapps.ggarest.tools.AsyncTimeoutHelper.*;

public abstract class RealConnectionsTest extends AndroidTestCase {

    //private static final int CONNECTION_TIMEOUT = 5*60*1000;
    private static final int CONNECTION_TIMEOUT = 5*1000;

    protected abstract Webservice getWebserviceInstance();

    public void testSimpleGet() throws Exception {

        runAndWaitForEnd(CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                getWebserviceInstance()
                        .get("http://www.google.com")
                        .onSuccess(new OnResponseListener() {
                            @Override
                            public void onResponse(int code, Response fullResponse) {
                                asyncTimeoutHelper.end();
                            }
                        }).execute();
            }

            @Override
            public void onEndCalled() {
                //Success
            }

            @Override
            public void onTimeout() {
                fail("Connection timed out");
            }
        });


    }

    public static class UnauthorizedMessage{
        public String error_message;
        public List<String> results;
        public String status;
    }

    public void testSimpleObject() throws Exception {

        runAndWaitForEnd(CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                getWebserviceInstance()
                        .get("https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY")
                        .onSuccess(UnauthorizedMessage.class, new OnObjResponseListener<UnauthorizedMessage>() {
                            @Override
                            public void onResponse(int code, UnauthorizedMessage message, Response fullResponse) {
                                assertEquals("REQUEST_DENIED",message.status);
                                asyncTimeoutHelper.end();
                            }
                        })
                        .execute();
            }

            @Override
            public void onEndCalled() {
                //Success
            }

            @Override
            public void onTimeout() {
                fail("Connection timed out");
            }
        });


    }

    //https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
}
