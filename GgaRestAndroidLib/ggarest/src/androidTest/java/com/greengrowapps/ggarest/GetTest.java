package com.greengrowapps.ggarest;

import android.test.AndroidTestCase;
import android.util.Log;

import com.greengrowapps.ggarest.authorization.CredentialsImpl;
import com.greengrowapps.ggarest.authorization.UrlConnectionBasicAuthorizator;
import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnExceptionListener;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.listeners.OnTimeoutListener;
import com.greengrowapps.ggarest.tools.AsyncBlockRunner;
import com.greengrowapps.ggarest.tools.AsyncTimeoutHelper;
import com.greengrowapps.ggarest.webservice.RequestExecutionAndroidFactory;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.List;

import static com.greengrowapps.ggarest.tools.AsyncTimeoutHelper.*;

public class GetTest extends GgaRestTest {

    private static final int CONNECTION_TIMEOUT = 30*1000;

    public void testSimpleGet() throws Exception {

        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                getWebserviceInstance()
                        .get("http://www.google.com")
                        .onSuccess(new OnResponseListener() {
                            @Override
                            public void onResponse(int code, Response fullResponse, Exception e) {
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

    public static class Args {

    }

    public static class Headers {
        public String Accept;

        @JsonProperty("Accept-Encoding")
        public String AcceptEncoding;
        @JsonProperty("Accept-Langueage")
        public String AcceptLanguage;

        public String Cookie;
        public String Host;
        public String Referer;

        @JsonProperty("Upgrade-Insecure-Requests")
        public String UpdateInsecureRequests;
        @JsonProperty("User-Agent")

        public String UserAgent;

    }

    public static class ResponseGet {
        public Args args;
        public Headers headers;
        public String origin;
        public String url;
    }

    public void testSimpleObject() throws Exception {

        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                getWebserviceInstance()
                        .get("http://httpbin.org/get")
                        .onSuccess(ResponseGet.class, new OnObjResponseListener<ResponseGet>() {
                            @Override
                            public void onResponse(int code, final ResponseGet message, Response fullResponse) {
                                asyncTimeoutHelper.performAssertions(new Runnable() {
                                    @Override
                                    public void run() {
                                        assertEquals("http://httpbin.org/get",message.url);
                                    }
                                });
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EchoPostResponse{
        public String data;
    }

    public void testBasicAuth() throws Exception {

        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                final Webservice webservice = getWebserviceInstance();

                setLogin("user","passwd");

                webservice
                        .get("http://httpbin.org/basic-auth/user/passwd")
                        .onSuccess(new OnResponseListener() {
                            @Override
                            public void onResponse(int code, Response fullResponse, Exception e) {
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

    public void testOAuth() throws Exception {

        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                final Webservice webservice = getWebserviceInstance();

                setLogin("user","passwd");

                webservice
                        .get("http://httpbin.org/basic-auth/user/passwd")
                        .onSuccess(new OnResponseListener() {
                            @Override
                            public void onResponse(int code, Response fullResponse, Exception e) {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User{
        int id;
        String name;
        String username;
    }

    public void testSimpleList() throws Exception {

        runAndWaitForEnd(this,CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                final Webservice webservice = getWebserviceInstance();

                webservice
                        .get("http://jsonplaceholder.typicode.com/users")
                        .onSuccess(User.class,new OnListResponseListener<User>() {
                            @Override
                            public void onResponse(int code, List<User> users, Response fullResponse) {
                                assertFalse(users.isEmpty());
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

    public void testTimeout() throws Exception {

        final boolean[] otherCalled = new boolean[1];
        final boolean[] timeoutCalled = new boolean[1];

        otherCalled[0]=false;
        timeoutCalled[0]=false;

        runAndWaitForEnd(this, CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                final Webservice webservice = getWebserviceInstance();

                webservice
                        .get("http://httpbin.org/delay/10")
                        .withTimeout(5*1000)
                        .onOther(new OnResponseListener() {
                            @Override
                            public void onResponse(int code, Response fullResponse, Exception e) {
                                otherCalled[0] = true;
                            }
                        })
                        .onTimeout(new OnTimeoutListener() {
                            @Override
                            public void onTimeout() {
                                timeoutCalled[0] = true;
                                asyncTimeoutHelper.end();
                            }
                        })
                        .execute();
            }

            @Override
            public void onEndCalled() {
                try {
                    Thread.sleep(5*1000);
                    assertFalse(otherCalled[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    fail("Ex");
                }
            }

            @Override
            public void onTimeout() {
                fail("Connection timed out");
            }
        });
    }

    public void testTimeoutWithoutListener() throws Exception {

        final boolean[] otherCalled = new boolean[1];

        otherCalled[0]=false;

        runAndWaitForEnd(this, CONNECTION_TIMEOUT, new AsyncBlockRunner() {

            @Override
            public void run(final AsyncTimeoutHelper asyncTimeoutHelper) throws AlreadyExecutingException {

                final Webservice webservice = getWebserviceInstance();

                webservice
                        .get("http://httpbin.org/delay/10")
                        .withTimeout(5*1000)
                        .onOther(new OnResponseListener() {
                            @Override
                            public void onResponse(int code, Response fullResponse, Exception e) {
                                otherCalled[0] = true;
                                asyncTimeoutHelper.end();
                            }
                        })
                        .execute();
            }

            @Override
            public void onEndCalled() {
                try {
                    Thread.sleep(5*1000);
                    assertTrue(otherCalled[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    fail("Ex");
                }
            }

            @Override
            public void onTimeout() {
                fail("Connection timed out");
            }
        });
    }

    public void testExecuteAndWait() throws Exception {

        final boolean[] success = new boolean[1];
        success[0] = false;

        getWebserviceInstance()
                .get("http://httpbin.org/get")
                .onSuccess(ResponseGet.class, new OnObjResponseListener<ResponseGet>() {
                    @Override
                    public void onResponse(int code, final ResponseGet message, Response fullResponse) {
                        success[0]=true;
                    }
                })
                .executeAndWait();

        assertTrue(success[0]);
    }
}
