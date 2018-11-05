package com.swj.ics.redislock;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;
import com.swj.ics.redislockframework.CacheLockInterceptor;
import org.junit.Test;

/**
 */
public class SecKillTest {
    
    private static long commodityId1 = 1001L;
    private static long commodityId2 = 1002L;

    
    @Test
    public void  testSecKill() {
        int threadCount = 1000;
        int splitCount = 500;
        CountDownLatch beginCountDown = new CountDownLatch(1);
        CountDownLatch endCountDown = new CountDownLatch(500);
        
        SecKillImpl secKill = new SecKillImpl();
        Thread[] threads = new Thread[threadCount];
        //启动500个线程，来秒杀第一个商品
        for (int i = 0;i < splitCount;i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //等待在beginCountDonw这个信号量上,挂起
                    try {
                        beginCountDown.await();
                        //使用动态代理的方式来调用secKill()方法
                        SecKillInterface proxyInstance = (SecKillInterface)Proxy.newProxyInstance(SecKillInterface.class.getClassLoader(),
                                new Class[] {SecKillInterface.class},new CacheLockInterceptor(secKill)
                                );
                        proxyInstance.secKill("test1",commodityId1);
                        endCountDown.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
        
        //启动另外500个线程来秒杀第二个商品
        for(int i = splitCount;i < threadCount;i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //等待一个信号量，挂起
                        beginCountDown.await();
                        //使用动态代理的方式调用secKill()方法。
                        SecKillInterface proxyInstance = (SecKillInterface) Proxy.newProxyInstance(SecKillInterface.class.getClassLoader(),
                                new Class[]{SecKillInterface.class},new CacheLockInterceptor(secKill)
                        );
                        proxyInstance.secKill("test2",commodityId2);
                        endCountDown.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   
                    
                }
            });
            threads[i].start();
        }
        
        long startTime = System.currentTimeMillis();
        //主线程释放信号量，并等待结束信息号量
        beginCountDown.countDown();

        try {
            //主线程等待结束信号量
            endCountDown.await();
            
            //System.out.println(SecKillImpl.inventory.get(commodityId1));
            //System.out.println(SecKillImpl.inventory.get(commodityId2));
            System.out.println("error count :" + CacheLockInterceptor.ERROR_COUNT);
            System.out.println("total cost time :" +(System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
