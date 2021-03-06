package com.greengrowapps.ggarest;

import com.greengrowapps.ggarest.exceptions.AlreadyExecutingException;
import com.greengrowapps.ggarest.listeners.OnExceptionListener;
import com.greengrowapps.ggarest.listeners.OnResponseListener;
import com.greengrowapps.ggarest.listeners.OnTimeoutListener;
import com.greengrowapps.ggarest.notifiers.AbstractNotifier;
import com.greengrowapps.ggarest.webservice.RequestExecution;
import com.greengrowapps.ggarest.webservice.RequestExecutionCallbacks;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class RestRequestImpl implements RestRequest, RequestExecutionCallbacks{

    private final ConnectionDefinition connectionDefinition;
    private final WebserviceImpl webservice;
    private RequestExecution requestExecution = null;
    private CountDownLatch latch;

    public RestRequestImpl(ConnectionDefinition connectionDefinition, WebserviceImpl webservice){
        this.connectionDefinition = connectionDefinition;
        this.webservice = webservice;
    }

    @Override
    public synchronized void execute() throws AlreadyExecutingException {
        if(isExecuting()){
            throw new AlreadyExecutingException();
        }
        requestExecution = webservice.execute(connectionDefinition,this);
    }

    @Override
    public void executeAndWait() throws AlreadyExecutingException {
        latch = new CountDownLatch(1);
        execute();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean isExecuting() {
        return requestExecution != null;
    }

    @Override
    public synchronized void cancel() {
        releaseLatch();

        if(!isExecuting()){
            return;
        }
        requestExecution.cancel();
        requestExecution = null;
    }

    @Override
    public synchronized void onRequestCompleted(ResponseImpl response) {
        if(!isExecuting()){
            releaseLatch();
            return;
        }
        requestExecution=null;

        int statusCode = response.getStatusCode();

        if(connectionDefinition.hasListenerForStatusCode(statusCode)){
            AbstractNotifier notifier = connectionDefinition.getNotifier(statusCode);
            if(notifier!=null){
                try {
                    notifier.processResponseAndNotify(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    onException(e);
                }
            }
        }
        else {
            OnResponseListener listener = connectionDefinition.getDefaultListener();
            if(listener !=null){
                listener.onResponse(statusCode, response, null);
            }
        }
        releaseLatch();
    }

    private void releaseLatch() {
        if(latch!=null){
            latch.countDown();
        }
    }

    @Override
    public void onException(Exception exception) {
        OnExceptionListener listener = connectionDefinition.getExceptionListener();
        if(listener!=null){
            listener.onExceptionThrown(exception);
        }
        else{
            OnResponseListener timeoutListener = connectionDefinition.getDefaultListener();
            if(timeoutListener !=null){
                timeoutListener.onResponse(0, new ResponseImpl(0,new HashMap<String, String>(),null,null), exception);
            }
        }
        releaseLatch();
    }

    @Override
    public void onTimeout() {
        OnTimeoutListener listener = connectionDefinition.getTimeoutListener();
        if(listener!=null){
            listener.onTimeout();
        }
        else{
            OnResponseListener timeoutListener = connectionDefinition.getDefaultListener();
            if(timeoutListener !=null){
                timeoutListener.onResponse(0, new ResponseImpl(0,new HashMap<String, String>(),null,null), new TimeoutException());
            }
        }
        releaseLatch();
    }
}
