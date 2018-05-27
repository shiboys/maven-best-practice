package com.swj.ics.MasterWorker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by swj on 2018/1/28.
 */
public class Worker implements Runnable {
    
    private ConcurrentLinkedQueue<Task> taskQueue;
    
    private ConcurrentHashMap<String,Object> resultMap;

    public void setTaskQueue(ConcurrentLinkedQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public void run() {
        while (true) { //从队列里面获取元素
            Task task = taskQueue.poll();
            if (task == null) {
                break;
            }
            Object result = handle(task);
            this.resultMap.put(String.valueOf(task.getId()),result);
        }
    }

    private Object handle(Task task) {
        Object result = null;
        //模拟线程处理业务
        try {
            Thread.sleep(500);
            result = task.getPrice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
