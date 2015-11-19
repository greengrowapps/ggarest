package com.greengrowapps.ggarest.notifiers;

import com.greengrowapps.ggarest.ResponseImpl;
import com.greengrowapps.ggarest.listeners.OnListResponseListener;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListNotifier<T> extends AbstractNotifier{


    private final Class<T> clazz;
    private final OnListResponseListener<T> listener;

    public ListNotifier(Class<T> clazz, OnListResponseListener<T> listener){
        this.clazz = clazz;
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void processResponseAndNotify(ResponseImpl response) throws IOException {

        Class listClass = Array.newInstance(clazz,0).getClass();

        T[] array = (T[]) response.deserializeBody(listClass);

        List<T> list = Arrays.asList(array);
        listener.onResponse(response.getStatusCode(), list, response);
    }
}
