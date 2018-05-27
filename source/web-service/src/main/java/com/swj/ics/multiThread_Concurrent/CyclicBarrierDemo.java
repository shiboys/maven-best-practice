package com.swj.ics.multiThread_Concurrent;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by swj on 2018/1/30.
 */
public class CyclicBarrierDemo {
    //
    
    public static void main(String[] args) {
        //test1();
       // test2();
        test3();
    }

    /**
     * CyclicBarrier的表面意思是循环的屏障。它要做的事情是：让一组线程达到一个屏障
     * (也可以叫同步点)时阻塞，知道最后一个线程到达屏障的时候，屏障才会开门，
     * 所有被屏障连接的线程才会继续运行。
     * CyclicBarrier 的默认构造函数是 CyclicBarrier(int parties)，参数表示屏障拦截的
     * 线程数量。每个线程调用await()方法，告诉cyclicBarrier我已经到达了屏障，然后当前
     * 线程被阻塞。
     */
    static CyclicBarrier cb = new CyclicBarrier(2);

    /**
     * 这个测试 的打印结果是 1,2或者2,1都有可能，因为线程的调度室由CPU决定的
     * 2个线程都有肯可能先执行。如果把cb = new CyclicBarrier(3);
     * 因为没有第三个线程执行await()方法，即没有第三个线程到达屏障，因此主线程和子线程
     * 都不会执行。
     * 
     */
    private static void test1() {
        //2个线程一起到达屏障
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cb.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("1");
            }
        }).start();

        try {
            cb.await();
            System.out.println(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    /**
     * CyclicBarrier提供了一个更高级的构造函数CyclicBarrier(int parties,Runnable barrierAction)
     * 用于在线程到达屏障时，有限制性barrierAction，方便处理复杂业务逻辑
     */
    
    static CyclicBarrier cb2 = new CyclicBarrier(2,new A());

    /**
     * 因为cb2设置了拦截线程的数量是2，所以必须等待代码中的第一个线程和线程A都
     * 执行完了，才会继续执行主线程，然后输出2，所以代码的输出为3，2，1
     */
    private static void test2() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cb2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println(1);
                }
            })    .start();

        try {
            cb2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }
    
    static class A implements  Runnable {
        @Override
        public void run() {
            System.out.println(3);
        }
    }

    /**
     * cyclicBarrier 示例3，cyclicBarrier可用于多线程计算，最后合并计算结果的场景。
     * 比如，用一个Excel保存了用户的所有银行流水，每个sheet保存了一个账户近一年的每笔流水
     * 现在需要统计用户的日均流水，先用多线程处理每个sheet里的银行流水，都执行完了之后，
     * 得到每个sheet的日均流水，最后，再用barrierAction用这些线程的计算结果，计算出整个Excel
     * 的日均银行流水。
     */
    private static CyclicBarrier cb3 = new CyclicBarrier(4,new CyclicBarrieRunnable());
    //假设只有4个sheet，所以只启动四个想吃的那个。
    private static ExecutorService threadPool = Executors.newFixedThreadPool(4);
    
    private static ConcurrentHashMap<String,Integer> resultMap = new ConcurrentHashMap<>();
    
    static void test3() {
        for(int i = 0;i < 4; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    resultMap.put(Thread.currentThread().getName(),1);
                    //结果执行完毕，插入一个屏障
                    try {
                        cb3.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });   
        }
    }
    
    static  class CyclicBarrieRunnable implements Runnable {
        @Override
        public void run() {
            Integer result = 0;
            for (Map.Entry<String,Integer> entry : resultMap.entrySet()) {
                result += entry.getValue();
            }
            System.out.println("result is " + result);
        }
    }
}
