package com.swj.ics.zookeeper.distributeLock;

import java.util.concurrent.CountDownLatch;

/**
 * Created by swj on 2018/6/18.
 */
public class LockTest {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for(int i=0;i<10;i++) {
            new Thread(()->{
                try {
                    countDownLatch.await();
                    DistributeLock distributeLock = new DistributeLock();
                    distributeLock.lock();//争抢并尝试获取锁
                   
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Thread-"+i).start();
            countDownLatch.countDown();
        }
    }
}
