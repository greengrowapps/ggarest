package com.greengrowapps.ggarest.tools;

import android.test.AndroidTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncTimeoutHelper {

    private final int timeoutMilis;
    private final AsyncBlockRunner asyncBlockRunner;

    private final CountDownLatch latch = new CountDownLatch(1);
    private String failMessage;
    private Runnable assertions;

    private AsyncTimeoutHelper(int timeoutMilis, AsyncBlockRunner asyncBlockRunner){
        this.timeoutMilis = timeoutMilis;
        this.asyncBlockRunner = asyncBlockRunner;
    }

    public static void runAndWaitForEnd(AndroidTestCase testCase, int timeoutMilis, AsyncBlockRunner asyncBlockRunner) throws Exception {

        new AsyncTimeoutHelper(timeoutMilis,asyncBlockRunner).run();
    }

    private void run() throws Exception {
        asyncBlockRunner.run(this);

        if(latch.await(timeoutMilis, TimeUnit.MILLISECONDS)){
            if(failMessage==null) {
                if(assertions!=null){
                    assertions.run();
                }
                asyncBlockRunner.onEndCalled();
            }
            else{
                AndroidTestCase.fail(failMessage);
            }
        }
        else {
            asyncBlockRunner.onTimeout();
        }
    }

    public void end(){
        latch.countDown();
    }

    public void failSync(String failMessage){
        this.failMessage = failMessage;

        latch.countDown();
    }

    public void performAssertions(Runnable runnable){
        this.assertions = runnable;
    }
}
