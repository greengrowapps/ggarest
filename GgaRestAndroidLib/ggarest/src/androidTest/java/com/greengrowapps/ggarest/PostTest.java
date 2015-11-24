package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.tools.AsyncBlockRunner;
import com.greengrowapps.ggarest.tools.AsyncTimeoutHelper;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

import static com.greengrowapps.ggarest.tools.AsyncTimeoutHelper.runAndWaitForEnd;

public class PostTest extends GgaRestTest {

    private static final int CONNECTION_TIMEOUT = 30*1000;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EchoPostResponse{
        public String data;
    }

//    public void testSimplePost() throws Exception {
//
//        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {
//
//            @Override
//            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {
//
//                getWebserviceInstance()
//                        .post("http://httpbin.org/post")
//                        .withBody("Hello")
//                        .onSuccess(EchoPostResponse.class, new OnObjResponseListener<EchoPostResponse>() {
//                            @Override
//                            public void onResponse(int code, final EchoPostResponse message, final Response fullResponse) {
//
//                                asyncTimeoutHelper.performAssertions(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        assertEquals("\"Hello\"", message.data);
//                                    }
//                                });
//                                asyncTimeoutHelper.end();
//                            }
//                        })
//                        .execute();
//            }
//
//            @Override
//            public void onEndCalled() {
//                //Success
//            }
//
//            @Override
//            public void onTimeout() {
//                fail("Connection timed out");
//            }
//        });
//
//    }
}
