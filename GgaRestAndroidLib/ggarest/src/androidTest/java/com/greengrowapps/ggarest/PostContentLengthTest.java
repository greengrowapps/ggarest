package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.GgaRestTest;
import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.tools.AsyncBlockRunner;
import com.greengrowapps.ggarest.tools.AsyncTimeoutHelper;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.IOException;
import java.util.Random;

import static com.greengrowapps.ggarest.tools.AsyncTimeoutHelper.runAndWaitForEnd;

public class PostContentLengthTest extends GgaRestTest {


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EchoPostResponse{
        public String data;
        public String json;
        public Headers headers;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Headers{

        @JsonProperty("Content-Length")
        public String contentLenth;
    }

    public void testPostContentLength() throws Exception {

        checkPostWithStringLength(1);
        checkPostWithStringLength(4);
        checkPostWithStringLength(256);
        checkPostWithStringLength(1024);
        checkPostWithStringLength(2048);
        checkPostWithStringLength(4194304);
    }

    private void checkPostWithStringLength(int length) throws IOException {
        final String content=random(length);
        final String contentJson = getWebserviceInstance().getSerializer().fromObject(content);
        final int realLength=contentJson.length();
        getWebserviceInstance()
                .post("http://httpbin.org/post")
                .withBody(content)
                .onSuccess(EchoPostResponse.class, new OnObjResponseListener<EchoPostResponse>() {
                    @Override
                    public void onResponse(int code, final EchoPostResponse message, final Response fullResponse) {
                        assertEquals(contentJson, message.data);
                        assertEquals(content, message.json);
                        assertEquals(realLength+"",message.headers.contentLenth);
                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        fail();
                    }
                })
                .executeAndWait();
    }

    public static String random(int randomLength) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
