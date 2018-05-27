package com.swj.ics.ThreadSimple.CustomeThreadPoolDemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by swj on 2018/1/10.
 */
public class DefaultThreadPool<Task extends Runnable> implements ThreadPool<Task> {
    
    //线程池最大个数10
    private static final int MAX_WORKER_THREADS = 10;
    
    //线程池最小线程个数
    private static final int MIN_WORKER_THREADS = 1;
    
    //线程池默认线程个数
    private static final int DEFAULT_WORKER_THREADS = 5;
    
    //任务队列，线程从工作/任务队列中获取任务
    private LinkedList<Task> taskList = new LinkedList<Task>();
    
    //干活线程Runnable队列
    private List<WorkerRunnable> workerList = Collections.synchronizedList(new ArrayList<>());
    
    //工作线程的数量
    private int workerThreadCount = DEFAULT_WORKER_THREADS;
    
    //线程编号生成计数器
    private AtomicInteger threadCounter = new AtomicInteger();
     
    
    public DefaultThreadPool () {
        initThreadPool(DEFAULT_WORKER_THREADS);
    }
    
    public DefaultThreadPool(int num) {
        this.workerThreadCount =  num > MAX_WORKER_THREADS ? 
                MAX_WORKER_THREADS : 
                (num < MIN_WORKER_THREADS ? MIN_WORKER_THREADS :num);
        initThreadPool(num);
    }
    
    //初始化线程池，向其中填充线程。
    private void initThreadPool(int num) {
        for(int i = 0;i < num; i++) {
            WorkerRunnable workerRunnable = new WorkerRunnable();
            workerList.add(workerRunnable);
            Thread thread = new Thread(workerRunnable,"Worker-Thread-"+threadCounter.incrementAndGet());
            thread.start();
        }
    }
    
    @Override
    public void execute(Task job) { //执行任务就是向任务队列中增加一个任务，
        // 然后这个任务就某一个正在等待taskList.wait()的线程执行
        if (job != null) {
            //所有需要调用notify()或者notifyAll方法的地方都要先获取锁
            synchronized (taskList) {
                taskList.add(job);
                taskList.notifyAll();//唤醒在taskList上等待的某个线程。
            }          
        }
    }

    @Override
    public void shutdown() {
        for(WorkerRunnable workerRunnable : workerList) {
            workerRunnable.shutdown();//通过volatile类型变量，通知workerThread停止运行。
        }
    }

    @Override
    public void addWorkerThread(int num) {
        synchronized (taskList) { //这个都都是拿的同一把锁，这样可以保证线程的原子性和
            //判断当前的线程数量+Num 是否大于 max
            if (this.workerThreadCount + num > MAX_WORKER_THREADS) {
                num = MAX_WORKER_THREADS - this.workerThreadCount;
            }
            initThreadPool(num);
            this.workerThreadCount += num;
        }
    }

    /**
     * 减少工作线程，其原理是将num个线程所代表的runnable对象从集合中移除，并调用该对象的shutdown()方法。
     * @param num 移除的个数
     */
    @Override
    public void removeWorkerThread(int num) {
        if(num > this.workerThreadCount) {
            throw new IllegalArgumentException(" beyond worker thread count");
        }
        
        int currCounter = 0;
        WorkerRunnable workerRunnable = null;
        Iterator<WorkerRunnable> iterator = workerList.iterator();
        while (iterator.hasNext() && currCounter < num) {
            workerRunnable = iterator.next();
            if (workerRunnable != null) {
                iterator.remove();
                workerRunnable.shutdown();
                currCounter ++;
            }
        }
        
        this.workerThreadCount -= num;
    }

    @Override
    public int getTaskSize() {
        return taskList.size();
    }
    
    //工作线程，一直不停的从工作队列中获取任务来执行
    
    class WorkerRunnable implements Runnable {
        private volatile boolean isRunning = true;
        @Override
        public void run() {
            while (isRunning) { //不停的从任务队列中获取要执行的任务
                Task task = null;
                synchronized (taskList) {
                    while (taskList.isEmpty()) {
                        //所有调用wait的地方，一定要使用while
                        try {
                            //如果任务队列是空的，则继续等待，释放获取的锁。
                            taskList.wait();
                        } catch (InterruptedException e) {
                            //这里响应 外部的中断请求,进行返回操作
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                            return ;//return 只是跳出了内层的while循环，外层的isRunning仍然在继续跑
                            //所以这里使用interrupt()进行线程中断。
                        }
                    }
                    //取出一个task
                     task = taskList.removeFirst();//先进先出
                     if (task != null) {
                         try {
                             //执行任务
                             task.run();
                         } catch (Exception e) {
                            //忽略任务执行过程中的异常
                         }
                     }
                }
            }
        }
        
        public void shutdown() {
            isRunning = false;
        }
    }
}
