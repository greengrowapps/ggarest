package com.greengrowapps.ggarest.tools;


public interface AsyncBlockRunner {

    void run( AsyncTimeoutHelper asyncTimeoutHelper) throws Exception;

    void onEndCalled();

    void onTimeout();
}
