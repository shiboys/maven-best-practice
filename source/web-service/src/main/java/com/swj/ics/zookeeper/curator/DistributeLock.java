package com.swj.ics.zookeeper.curator;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import com.swj.ics.zookeeper.curator.barrier.CuratorBarrierAsBarrier;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by swj on 2018/2/26.
 */
public class DistributeLock {

    private static final Logger logger = LoggerFactory.getLogger(DistributeLock.class);
    private static final int THREAD_COUNT = 10;
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);
    private static CuratorFramework cf;
    //Curator框架提供的分布式锁。
    private static InterProcessMutex lock = null;
    public static void main(String[] args) throws InterruptedException {

        cf = CuratorHelper.init();
        lock =  new InterProcessMutex(cf,"/super");
        for(int i = 0 ; i < THREAD_COUNT; i++) {
            startThreadToRunCuratorBarrier("t" + i);
        }
        //让子线程都先运行起来
        TimeUnit.SECONDS.sleep(1);
        COUNT_DOWN_LATCH.countDown();
    }

    private static void startThreadToRunCuratorBarrier(String threadName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //先准备，然后开始同时运行，最后同时推出
                try {
                    COUNT_DOWN_LATCH.await();
                    lock.acquire();
                    logger.info("当前线程成功获取锁，开始执行任务");
                    TimeUnit.SECONDS.sleep(1);
                    logger.info("当前线程任务执行完成!");
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        },threadName).start();
    }
}
