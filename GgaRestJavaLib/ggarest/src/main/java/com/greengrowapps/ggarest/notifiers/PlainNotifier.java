package com.greengrowapps.ggarest.notifiers;

import com.greengrowapps.ggarest.ResponseImpl;
import com.greengrowapps.ggarest.listeners.OnResponseListener;


public class PlainNotifier extends AbstractNotifier {

    private final OnResponseListener listener;

    public PlainNotifier(OnResponseListener listener){
        this.listener = listener;
    }

    @Override
    public void processResponseAndNotify(ResponseImpl response) {
        listener.onResponse(response.getStatusCode(), response, null);
    }
}
