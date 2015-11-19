package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;

/**
 * Created by palaman on 18/11/15.
 */
public interface RestRequest {

    void execute() throws AlreadyExecutingException;
    void cancel();
    boolean isExecuting();
}
