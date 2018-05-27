package com.swj.ics.MasterWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by swj on 2018/1/28.
 */
public class MasterWorkerMain {
    //1、定义一个任务队列，将所有的任务放到此队列里面。队列的数据结构是
    //ConcurrentLinkedQueue,
    //2、使用hashMap<String,Thread> 作为当前的线程池，存储线程
    //3、使用CurrentHashMap用来存放线程的执行结果。
    
    private ConcurrentLinkedQueue<Task> taskQueue;
    
    private HashMap<String,Thread> threadHashMap;
    
    private ConcurrentHashMap<String,Object> resultMap;
    
    public MasterWorkerMain(Worker worker,int threadCount) {
        if (threadCount < 1 || worker == null) {
            throw new IllegalArgumentException("参数错误");
        }
        taskQueue = new ConcurrentLinkedQueue<>();
        threadHashMap = new HashMap<>();
        resultMap = new ConcurrentHashMap<>();
        worker.setResultMap(resultMap);
        worker.setTaskQueue(taskQueue);
        for (int i = 0;i < threadCount;i++) {
            threadHashMap.put("worker thread" + i,new Thread(worker));
        }
    }
    
    //提交一个任务，就是向任务队列增加一个任务
    public void submit(Task task) {
        this.taskQueue.add(task);
    }
    
    //执行任务，就是遍历线程map，如果有线程不为null,则运行线程
    public void execute() {
        for(Map.Entry<String,Thread> entry : threadHashMap.entrySet()) {
            Thread thread = entry.getValue();
            if (thread != null ) { 
                thread.start();
            }
        }
    }
    
    public boolean isCompleted() {
        boolean isCompleted = true;
        for(Map.Entry<String,Thread> entry : threadHashMap.entrySet()) {
            Thread thread = entry.getValue();
            if (thread != null && thread.getState() != Thread.State.TERMINATED) {
                isCompleted = false;
                break;
            }
        }
        return isCompleted;
    }
    
    public Integer getResult() {
        int result = 0;
        for (Map.Entry<String,Object> entry : resultMap.entrySet()) {
            result  += (Integer) entry.getValue();
        }
        return result;
    } 
}

