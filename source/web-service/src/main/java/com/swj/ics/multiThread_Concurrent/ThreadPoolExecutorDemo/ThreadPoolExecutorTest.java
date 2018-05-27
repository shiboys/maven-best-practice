package com.swj.ics.multiThread_Concurrent.ThreadPoolExecutorDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/2/4.
 * 线程池的测试类
 * 自定义线程池如果使用的是有界队列，若有新的任务需要执行，如果当前线程数量小于corePoolSize
 * 则创建一个新的线程来执行，如果大于corePoolSize,则将任务加入有界队列，
 * 如果有界队列已满，则在总线程数目不大于maximumPoolSize的情况下，创建新的线程来执行任务，
 * 如果线程数大于maximumPoolSize,则执行饱和/拒绝策略，或者其他自定义情况
 */
public class ThreadPoolExecutorTest {
    
    public static void main(String[] args) {
        //test_ArrayBlockedQueue();
        
        //test_LinkedBlockedQueueThreadPool();

        //test_ThreadPoolExecutor_WithRejectPolicy_DiscardOldestPolicy();
        test_ThreadPoolExecutor_WithCustomRejectPolicy();
    }
        
   private static void test_ArrayBlockedQueue() {
       ThreadPoolExecutor pool = getArrayBlockedQueuePool();
       MyTask mt1 = new MyTask(1,"任务1");
       MyTask mt2 = new MyTask(2,"任务2");
       MyTask mt3 = new MyTask(3,"任务3");
       MyTask mt4 = new MyTask(4,"任务4");
       MyTask mt5 = new MyTask(5,"任务5");

       MyTask mt6 = new MyTask(6,"任务6");
       
       pool.execute(mt1);
       pool.execute(mt2);
       pool.execute(mt3);
       pool.execute(mt4);
       pool.execute(mt5);
       
       //如果在线程达到maximumPoolSize之后，还有任务提交到线程池，
       //则线程池默认直接拒绝
       /*
       * 异常信息如下：
       * Task {id:6,name:任务6} rejected from java.util.concurrent.
       * ThreadPoolExecutor@8efb846[Running, pool size = 2, active threads = 2, 
       * queued tasks = 3, completed tasks = 0]
       * */
       //pool.execute(mt6);
       
       
       //2,3,4 任务会进队列，1和5任务，会被先执行
       pool.shutdown();
       /**
        * 执行结果如下
        * 当前任务的线程id是：1，名称是：任务1
        当前任务的线程id是：5，名称是：任务5
        当前任务的线程id是：2，名称是：任务2
        当前任务的线程id是：3，名称是：任务3
        当前任务的线程id是：4，名称是：任务4
        */
   }
   
   private static void test_LinkedBlockedQueueThreadPool() {
        
        ThreadPoolExecutor pool = getLinkedBlockedQueuePool();
        for (int i = 0;i < 20;i++) {
            pool.execute(new MyLinkedTask());
        }
       try {
            Thread.sleep(1000);
            System.out.println("当前线程池的阻塞队列大小为：" + pool.getQueue().size());
           Thread.sleep(2000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       } finally {
           pool.shutdown();     
       }
       /**
        * 由于线程池的核心线程数量是5，因此队列中的任务是5个一组一起执行的
        * 任务：1
        任务：5
        任务：4
        任务：3
        任务：2
        当前线程池的阻塞队列大小为：15
        任务：6
        任务：7
        任务：8
        任务：10
        任务：9
        任务：12
        任务：14
        任务：13
        任务：15
        任务：11
        任务：16
        任务：18
        任务：20
        任务：19
        任务：17
        */
       
       
       //如果采用有界队列且最大线程数量是10，有界队列的大小是10，则执行结果是
       /**
        * 线程顺序id:1任务：1
        线程顺序id:5任务：5
        线程顺序id:17任务：6
        线程顺序id:16任务：7
        线程顺序id:4任务：4
        线程顺序id:3任务：3
        线程顺序id:2任务：2
        线程顺序id:18任务：9
        线程顺序id:20任务：8
        线程顺序id:19任务：10
        当前线程池的阻塞队列大小为：10
        线程顺序id:7任务：12
        线程顺序id:12任务：17
        线程顺序id:13任务：18
        线程顺序id:11任务：16
        线程顺序id:10任务：15
        线程顺序id:9任务：14
        线程顺序id:8任务：13
        线程顺序id:6任务：11
        线程顺序id:15任务：20
        线程顺序id:14任务：19
        */
   }
   
   private static ThreadPoolExecutor getArrayBlockedQueuePool() {
       ThreadPoolExecutor pool = new ThreadPoolExecutor(
               1,
               2,
               60,
               TimeUnit.SECONDS,
               new ArrayBlockingQueue<Runnable>(3)
       );
       return pool;
   }
   
   private static ThreadPoolExecutor getLinkedBlockedQueuePool() {
       /**
        * LinkedBlockedQueue,与有界对垒相比，除非系统资源耗尽，佛则无界队列的
        * 的任务不存在入列失败的情况。当新任务到达时，如果当前的线程数量小于corePoolSize,
        * 则创建新线程来执行任务，否则就直接加入队列。如果任务的处理和创建的速度差异很大，  
        * 无界队列会保持快速增长，直到耗尽系统内存。
        * 在使用无界队列的线程池中，maximumPoolSize这个参数将不起作用。
        */
       BlockingQueue blockingQueue =  
               //new LinkedBlockingQueue<>();
               new ArrayBlockingQueue(10);
       //如果使用的有界队列，且队列的大小为10，则
       //如果任务个数为20，则队列的前5个和最后5个会先被执行，中间10个再一起执行。
       ThreadPoolExecutor pool = new ThreadPoolExecutor(
               5,
               10,
               120,
               TimeUnit.SECONDS,
               blockingQueue
               );
       return pool;
   }

    /**
     * JDK默认提供拒绝策略有如下：
     * AbortPolicy:直接抛出异常，系统正常工作。
     * CallerRunsPolicy:只要线程池没有关闭，该策略直接在调用者的线程中，运行当前被丢弃的任务。
     * DiscardOldestPolicy:丢弃最老/请求时间最早的一个任务，并尝试提交当前任务。
     * DiscardPolicy:丢弃无法处理的任务，不给予任何处理。
     * 如果需要自定义拒绝策略，则可以实现RejectedExecutionHandler。
     */
   private static void test_ThreadPoolExecutor_WithRejectPolicy_DiscardOldestPolicy(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(3)			//指定一种队列 （有界队列）
                //new LinkedBlockingQueue<Runnable>()
                //, new MyRejected()
                , new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        MyTask mt1 = new MyTask(1, "任务1");
        MyTask mt2 = new MyTask(2, "任务2");
        MyTask mt3 = new MyTask(3, "任务3");
        MyTask mt4 = new MyTask(4, "任务4");
        MyTask mt5 = new MyTask(5, "任务5");
        MyTask mt6 = new MyTask(6, "任务6");

        pool.execute(mt1);
        pool.execute(mt2);
        pool.execute(mt3);
        pool.execute(mt4);
        pool.execute(mt5);
        pool.execute(mt6);

        /**
         * 执行结果如下：任务2为oldest，则直接被丢掉，直接执行当前的请求的任务6
         * 当前任务的线程id是：1，名称是：任务1
         当前任务的线程id是：5，名称是：任务5
         当前任务的线程id是：3，名称是：任务3
         当前任务的线程id是：4，名称是：任务4
         当前任务的线程id是：6，名称是：任务6
         */
        pool.shutdown();
    }

    private static void test_ThreadPoolExecutor_WithCustomRejectPolicy(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(3)			//指定一种队列 （有界队列）
                //new LinkedBlockingQueue<Runnable>()
                ,new MyRejected()
        );

        MyTask mt1 = new MyTask(1, "任务1");
        MyTask mt2 = new MyTask(2, "任务2");
        MyTask mt3 = new MyTask(3, "任务3");
        MyTask mt4 = new MyTask(4, "任务4");
        MyTask mt5 = new MyTask(5, "任务5");
        MyTask mt6 = new MyTask(6, "任务6");

        pool.execute(mt1);
        pool.execute(mt2);
        pool.execute(mt3);
        pool.execute(mt4);
        pool.execute(mt5);
        pool.execute(mt6);

        /**
         自定义处理。。。
         当前被拒绝的任务信息为：{id:6,name:任务6}
         当前任务的线程id是：1，名称是：任务1
         当前任务的线程id是：5，名称是：任务5
         当前任务的线程id是：3，名称是：任务3
         当前任务的线程id是：2，名称是：任务2

         */
        pool.shutdown();
    }
}
