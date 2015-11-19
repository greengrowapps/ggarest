package com.greengrowapps.ggarest.notifiers;

import com.greengrowapps.ggarest.ResponseImpl;

import java.io.IOException;

public abstract class AbstractNotifier {

    public abstract void processResponseAndNotify(ResponseImpl response) throws IOException;



}
