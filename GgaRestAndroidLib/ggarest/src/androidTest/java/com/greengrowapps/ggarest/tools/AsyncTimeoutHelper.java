package com.greengrowapps.ggarest.tools;

import com.greengrowapps.ggarest.BuildConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncTimeoutHelper {

    private final int timeoutMilis;
    private final AsyncBlockRunner asyncBlockRunner;

    private final CountDownLatch latch = new CountDownLatch(1);

    private AsyncTimeoutHelper(int timeoutMilis, AsyncBlockRunner asyncBlockRunner){
        this.timeoutMilis = timeoutMilis;
        this.asyncBlockRunner = asyncBlockRunner;
    }

    public static void runAndWaitForEnd(int timeoutMilis, AsyncBlockRunner asyncBlockRunner) throws Exception {

        new AsyncTimeoutHelper(timeoutMilis,asyncBlockRunner).run();
    }

    private void run() throws Exception {
        asyncBlockRunner.run(this);

        if(latch.await(timeoutMilis, TimeUnit.MILLISECONDS)){
            asyncBlockRunner.onEndCalled();
        }
        else {
            asyncBlockRunner.onTimeout();
        }
    }

    public void end(){
        latch.countDown();
    }
}
