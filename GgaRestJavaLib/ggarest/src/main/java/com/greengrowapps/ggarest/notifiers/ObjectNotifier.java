package com.greengrowapps.ggarest.notifiers;

import com.greengrowapps.ggarest.ResponseImpl;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;

import java.io.IOException;

public class ObjectNotifier<T> extends AbstractNotifier{


    private final Class<T> clazz;
    private final OnObjResponseListener<T> listener;

    public ObjectNotifier(Class<T> clazz, OnObjResponseListener<T> listener){
        this.clazz = clazz;
        this.listener = listener;
    }

    @Override
    public void processResponseAndNotify(ResponseImpl response) throws IOException {
        T object = response.deserializeBody(clazz);
        listener.onResponse(response.getStatusCode(), object, response);
    }

}
