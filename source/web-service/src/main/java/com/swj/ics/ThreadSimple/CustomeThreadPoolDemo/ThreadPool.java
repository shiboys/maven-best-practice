package com.swj.ics.ThreadSimple.CustomeThreadPoolDemo;

/**
 * Created by swj on 2018/1/10.
 */
public interface ThreadPool<Task extends Runnable> {
    //执行一个job,这个job需要实现Runnable
    void execute(Task job);
    //关闭线程池
    void shutdown();
    
    //增加num个工作线程/干活线程
    void addWorkerThread(int num);
    
    //
    void removeWorkerThread(int num);
    
    //获取正在执行任务的线程数
    int getTaskSize();
    
}

