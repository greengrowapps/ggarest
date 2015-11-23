package com.greengrowapps.ggarest;

import android.content.Context;
import android.os.Handler;

import com.greengrowapps.ggarest.authorization.CredentialsImpl;
import com.greengrowapps.ggarest.authorization.UrlConnectionBasicAuthorizator;
import com.greengrowapps.ggarest.serialization.JsonSerializer;
import com.greengrowapps.ggarest.serialization.Serializer;
import com.greengrowapps.ggarest.streams.StreamConverter;
import com.greengrowapps.ggarest.streams.StreamConverterImpl;
import com.greengrowapps.ggarest.webservice.RequestExecutionAndroidFactory;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactory;

import java.security.InvalidParameterException;

public class GgaRest {

    private static WebserviceImpl instance;
    private static RequestExecutionAndroidFactory executionFactory;
    private static Serializer serializer = new JsonSerializer();
    private static StreamConverter streamConverter = new StreamConverterImpl("UTF-8");

    private GgaRest(){

    }

    public static void init(Context context){
        executionFactory = new RequestExecutionAndroidFactory(new Handler());
    }

    public static void setSerializer(Serializer serializer) {
        if(streamConverter==null){
            throw new InvalidParameterException("serializer must not be null");
        }
        GgaRest.serializer = serializer;
    }
    public static void setStreamConverter(StreamConverter streamConverter) {
        if(streamConverter==null){
            throw new InvalidParameterException("Stream converter must not be null");
        }
        GgaRest.streamConverter = streamConverter;
    }
    public static void useBasicAuth(String username, String pass){
        executionFactory.setAuthorizator( new UrlConnectionBasicAuthorizator(new CredentialsImpl(username,pass)));
    }
    public static void clearAuth(){
        executionFactory.setAuthorizator( null );
    }

    protected static void buildInstance() {
        instance = new WebserviceImpl(executionFactory, serializer, streamConverter);
    }

    public static Webservice ws() {
        if(executionFactory==null){
            throw new RuntimeException("Class must be initialized first");
        }
        if(instance==null || isChangedDependencies()){
            buildInstance();
        }
        return instance;
    }

    private static boolean isChangedDependencies() {
        return !instance.getSerializer().equals(serializer) || !instance.getStreamConverter().equals(streamConverter);
    }

}
