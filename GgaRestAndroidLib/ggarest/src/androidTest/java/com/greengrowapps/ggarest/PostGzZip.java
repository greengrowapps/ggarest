package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.listeners.OnObjResponseListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;

import junit.framework.Assert;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class PostGzZip extends GgaRestTest {
    private static final String url="http://httpbin.org/gzip";


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EchoPostResponse{
        public String data;
        public String json;
        public Headers headers;
        public Files files;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Files{
        public String file;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Headers{

        @JsonProperty("Content-Length")
        public String contentLenth;
        public String Enctype;
        public String File;


    }
    public class Hello{
        public String content="CompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePleaseCompresMePlease";
    }

    public void testInitial() throws IOException {
        getWebserviceInstance()
                //.post(url)
                .get(url)
                 .addHeader("Accept-Encoding","gzip")
                // .withBody(new Hello())
                .onResponse(200, new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        assertNotNull(fullResponse);

                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        fail();
                    }
                })
                .withTimeout(60*1000*60)
                .executeAndWait();
    }





    public void testSendGzip() throws IOException {
        getWebserviceInstance()
                .post("http://httpbin.org/anything")
                .useGzip()
                .addHeader("Accept-Encoding","gzip")
                 .withBody(new Hello())
                .onResponse(200, new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        assertNotNull(fullResponse);

                    }
                })
                .onOther(new OnResponseListener() {
                    @Override
                    public void onResponse(int code, Response fullResponse, Exception e) {
                        fail();
                    }
                })
                .withTimeout(60*1000*60)
                .executeAndWait();
    }





}
