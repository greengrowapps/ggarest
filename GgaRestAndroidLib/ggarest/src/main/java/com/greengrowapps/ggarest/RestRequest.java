package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;

public interface RestRequest {

    void execute() throws AlreadyExecutingException;
    void executeAndWait() throws AlreadyExecutingException;
    void cancel();
    boolean isExecuting();
}
