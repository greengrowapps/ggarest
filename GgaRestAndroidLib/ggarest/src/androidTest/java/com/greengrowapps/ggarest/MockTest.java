package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.listeners.OnTimeoutListener;
import com.greengrowapps.ggarest.tools.AsyncBlockRunner;
import com.greengrowapps.ggarest.tools.AsyncTimeoutHelper;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

import static com.greengrowapps.ggarest.tools.AsyncTimeoutHelper.runAndWaitForEnd;

public class MockTest extends GgaRestTest {

    private static final int CONNECTION_TIMEOUT = 30*1000;

    public void testMockGet() throws Exception {

        final Webservice ws = getWebserviceInstance();
        ws.mockGet("http://www.google.com")
                .responseCode(204)
                .responseContent("{\"text\":\"hello\"}")
                .save();


        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                ws
                        .get("http://www.google.com")
                        .onResponse(SimpleObj.class, 204, new OnObjResponseListener<SimpleObj>() {
                            @Override
                            public void onResponse(int code, SimpleObj object, Response fullResponse) {
                                assertEquals(204,code);
                                assertEquals("hello",object.text);
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SimpleObj{
        public String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


}
