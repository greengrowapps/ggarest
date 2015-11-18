package com.greengrowapps.ggarest.listeners;


import com.greengrowapps.ggarest.Response;

public interface OnObjResponseListener<T> {

    void onResponse(int code, T object, Response fullResponse);
}
