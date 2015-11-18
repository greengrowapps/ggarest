package com.greengrowapps.ggarest.listeners;


import com.greengrowapps.ggarest.Response;

import java.util.List;

public interface OnListResponseListener<T> {

    void onResponse(int code, List<T> object, Response fullResponse);
}
