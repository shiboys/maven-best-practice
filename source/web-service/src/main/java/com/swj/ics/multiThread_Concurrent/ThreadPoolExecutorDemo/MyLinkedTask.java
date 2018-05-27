package com.swj.ics.multiThread_Concurrent.ThreadPoolExecutorDemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by swj on 2018/2/4.
 */
public class MyLinkedTask implements Runnable {
    
    private static AtomicInteger atomicCounter = new AtomicInteger();
    private static AtomicInteger atomicThreadSeqCounter = new AtomicInteger();
    private Integer threadSeqId ;
    
    public MyLinkedTask() {
        threadSeqId = atomicThreadSeqCounter.incrementAndGet();
    }
    
    @Override
    public void run() {
        try {
            int counter = atomicCounter.incrementAndGet();
            System.out.println("线程顺序id:"+ threadSeqId +"任务："+counter);
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
