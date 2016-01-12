package com.greengrowapps.ggarest.listeners;

import com.greengrowapps.ggarest.Response;

public interface OnResponseListener {

    void onResponse(int code, Response fullResponse, Exception e);

}
