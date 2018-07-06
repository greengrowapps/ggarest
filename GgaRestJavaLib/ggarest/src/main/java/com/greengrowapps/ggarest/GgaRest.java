package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.serialization.JsonSerializer;
import com.greengrowapps.ggarest.serialization.Serializer;
import com.greengrowapps.ggarest.streams.StreamConverter;
import com.greengrowapps.ggarest.streams.StreamConverterImpl;
import com.greengrowapps.ggarest.webservice.RequestExecutionFactoryJava;

public class GgaRest {

    private GgaRest(){

    }

    public static Webservice ws() {
        return new WebserviceImpl(new RequestExecutionFactoryJava(), new JsonSerializer(), new StreamConverterImpl("UTF-8"));
    }
    public static Webservice ws(Serializer serializer) {
        return new WebserviceImpl(new RequestExecutionFactoryJava(), serializer, new StreamConverterImpl("UTF-8"));
    }
    public static Webservice ws(StreamConverter streamConverter) {
        return new WebserviceImpl(new RequestExecutionFactoryJava(), new JsonSerializer(), streamConverter);
    }
    public static Webservice ws(Serializer serializer, StreamConverter streamConverter) {
        return new WebserviceImpl(new RequestExecutionFactoryJava(), serializer, streamConverter);
    }
}
