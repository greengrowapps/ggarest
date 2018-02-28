package com.greengrowapps.ggarest.webservice;


import com.greengrowapps.ggarest.ConnectionDefinition;
import com.greengrowapps.ggarest.ResponseImpl;
import com.greengrowapps.ggarest.WebserviceImpl;
import com.greengrowapps.ggarest.authorization.UrlConnectionAuthorizator;
import com.greengrowapps.ggarest.mock.MockRequest;
import com.greengrowapps.ggarest.serialization.Serializer;
import com.greengrowapps.ggarest.streams.StreamConverter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RequestExecutionImpl extends Thread implements RequestExecution{

    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private final Object mutex = new Object();

    private final ConnectionDefinition connectionDefinition;
    private final RequestCallbackCaller requestCallbackCaller;
    private final WebserviceImpl webservice;
    private final UrlConnectionAuthorizator authorizator;
    private final List<MockRequest> mockRequests;
    private HttpURLConnection urlConnection;
    private boolean cancelled = false;

    private final Thread timeoutThread;
    private boolean timeout = false;

    public RequestExecutionImpl(
            final ConnectionDefinition connectionDefinition,
            RequestCallbackCaller requestCallbackCaller,
            WebserviceImpl webservice,
            UrlConnectionAuthorizator authorizator, List<MockRequest> mockRequests) {

        this.connectionDefinition = connectionDefinition;
        this.requestCallbackCaller = requestCallbackCaller;
        this.webservice = webservice;
        this.authorizator = authorizator;
        this.mockRequests = mockRequests;

        timeoutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(connectionDefinition.getTimeout());
                    synchronized (mutex){
                        if(!Thread.interrupted()) {
                            timeout = true;
                        }
                    }
                }
                catch (InterruptedException e){
                    //Do nothing
                }
            }
        });
    }

    @Override
    public void run() {

        for(MockRequest mockRequest : mockRequests){
            if(mockRequest.isMe(connectionDefinition.getUrl(),connectionDefinition.getMethod())){
                requestCallbackCaller.callRequestCompleted(mockRequest.buildResponse(webservice.getSerializer()));
                return;
            }
        }

        URL url;
        urlConnection = null;
        try {
            url = new URL(connectionDefinition.getUrl());
            synchronized (mutex) {
                urlConnection = (HttpURLConnection) url.openConnection();
            }
            urlConnection.setRequestMethod( getRequestMethod() );

            urlConnection = fillWithHeaders( urlConnection, webservice.getDefaultHeaders() );
            urlConnection = fillWithHeaders( urlConnection, connectionDefinition.getHeaders() );

            if(connectionDefinition.isPostPut()) {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                if(connectionDefinition.hasPlainBody()){
                    StreamConverter streamConverter = webservice.getStreamConverter();
                    String stringBody = connectionDefinition.getPlainBody();
                    streamConverter.writeToOutputStream( stringBody, urlConnection.getOutputStream() );
                }else if(connectionDefinition.hasBody()){
                    StreamConverter streamConverter = webservice.getStreamConverter();
                    Serializer serializer = webservice.getSerializer();
                    String stringBody = serializer.fromObject(connectionDefinition.getBody());
                    streamConverter.writeToOutputStream( stringBody, urlConnection.getOutputStream() );
                }
            }

            if(authorizator!=null){
                urlConnection = authorizator.authorize(urlConnection);
            }

            if(connectionDefinition.isPostPut()){
                urlConnection.setRequestProperty(CONTENT_LENGTH_HEADER,""+urlConnection.getContentLength());
            }

            if(!cancelled) {
                timeoutThread.start();
                int statusCode = urlConnection.getResponseCode();
                synchronized (mutex) {
                    if(!timeout) {
                        timeoutThread.interrupt();
                    }
                    else{
                        throw new TimeoutException();
                    }
                }
                InputStream in = new BufferedInputStream( isErrorCode(statusCode) ?
                        urlConnection.getErrorStream() :
                        urlConnection.getInputStream()
                );
                processResponse(in, urlConnection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            requestCallbackCaller.callError(e);
        } catch (TimeoutException e){
            requestCallbackCaller.callTimeout();
        } finally {
            synchronized (mutex) {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

    private HttpURLConnection fillWithHeaders(HttpURLConnection urlConnection, Map<String, String> headers) {
        for(String key : headers.keySet()){
            urlConnection.setRequestProperty(key, headers.get(key));
        }
        return urlConnection;
    }

    private boolean isErrorCode(int statusCode) {
        return statusCode>=400;
    }

    private void processResponse(InputStream in, HttpURLConnection urlConnection) throws IOException {

        String body = webservice.getStreamConverter().readFromInputStream(in);

        int statusCode = urlConnection.getResponseCode();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        ResponseImpl response = new ResponseImpl(statusCode, translateHeaders(headers), body, webservice.getSerializer());

        if(!cancelled) {
            requestCallbackCaller.callRequestCompleted(response);
        }
    }

    private Map<String,String> translateHeaders(Map<String, List<String>> headers) {

        Map<String,String> headersNewMap = new HashMap<>();

        for(String key : headers.keySet()){

            String commaSeparated = toCommaSeparated( headers.get(key) );
            headersNewMap.put(key,commaSeparated);
        }

        return headersNewMap;
    }

    private String toCommaSeparated(List<String> values) {

        StringBuilder concatenated = new StringBuilder();
        for(String value : values ){

            concatenated.append( concatenated.length()>0 ? " , " : "");

            concatenated.append(value);
        }
        return concatenated.toString();
    }

    public void cancel() {
         synchronized (mutex) {
             cancelled = true;
             if (urlConnection != null) {
                 urlConnection.disconnect();
             }
         }
    }

    public String getRequestMethod() {
        switch (connectionDefinition.getMethod()){

            case GET:
                return "GET";
            case POST:
                return "POST";
            case PUT:
                return "PUT";
            case DELETE:
                return "DELETE";
        }
        throw new RuntimeException("Not implemented for "+connectionDefinition.getMethod().name());
    }
}
