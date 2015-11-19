package com.greengrowapps.ggarest;


import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnObjResponseListener;

public class Sample {

    void method() throws AlreadyExecutingException {

        GgaRest.ws().get("http://www.google.es").onSuccess(String.class, new OnObjResponseListener<String>() {
            @Override
            public void onResponse(int code, String object, Response fullResponse) {
                //Do stuff
            }
        }).execute();

    }

}
