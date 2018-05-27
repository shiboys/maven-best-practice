package com.swj.ics.multiThread_Concurrent;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by swj on 2017/12/13.
 */
public class CyclicBarrierTest1 implements Runnable {
    
    private static final int THREADS_COUNT = 4;
    
    CyclicBarrier cyclicBarrier = new CyclicBarrier(THREADS_COUNT,this);
    
    //有4个线程的线程池
    static ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
    
    ConcurrentHashMap<String,Integer> hashMap = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
       // CyclicBarrierTest1 instance = new CyclicBarrierTest1();
       // instance.runMultiThread();

        //demo2();
        test_semaphore();
    }
    
    public void runMultiThread() {
        for (int i = 0;i < THREADS_COUNT ; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //模拟读取每个sheet 的过程，并把结果放入concurrentHashMap中
                    hashMap.put(Thread.currentThread().getName(),1);
                    try {
                        //设置隔离墙
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }        
    }
    
    @Override
    public void run() {
        int result = 0;
        for(Map.Entry<String,Integer> sheet : hashMap.entrySet()) {
            result += sheet.getValue();
        }
        hashMap.put("result",result);
        System.out.println("sheet 汇总后的数值是："+result);
        System.out.println("当前线程阻塞数量为："+cyclicBarrier.getNumberWaiting());
        executorService.shutdownNow();
    }

    /**
     * CyclicBarrierDemo 的isBroken演示，isBroken用来判断被阻塞的线程是否被中断
     */
    public static void demo2() {
        
        CyclicBarrier cyclicBarrier  = new CyclicBarrier(2);
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                   
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
        
        thread.interrupt();
        
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            System.out.println("cyclic barrier isBroken : " + cyclicBarrier.isBroken());
        }
    }
    
    public static void test_semaphore() {
        //每次只能有2个线程获得执行
        Semaphore semaphore = new Semaphore(2);
        for (int i = 0 ;i < THREADS_COUNT ;i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(String.format("%s done",Thread.currentThread().getName()));
                        Thread.sleep(1000);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        executorService.shutdown();
    }
}
