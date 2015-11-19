package com.greengrowapps.ggarest;

import android.content.Context;
import android.os.Handler;

import com.greengrowapps.ggarest.serialization.JsonSerializer;
import com.greengrowapps.ggarest.streams.StreamConverterImpl;
import com.greengrowapps.ggarest.webservice.RequestExecutionAndroidFactory;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactory;

public class GgaRest {

    private static Webservice instance;

    private GgaRest(){

    }

    public static void init(Context context){
        instance = buildInstance();
    }

    protected static Webservice buildInstance() {

        RequestExecutionFactory executionFactory = new RequestExecutionAndroidFactory(new Handler());

        return new WebserviceImpl(executionFactory, new JsonSerializer(), new StreamConverterImpl("UTF-8"));
    }

    public static Webservice ws() {
        if(instance==null){
            throw new RuntimeException("Class must be initialized first");
        }
        return instance;
    }

}
