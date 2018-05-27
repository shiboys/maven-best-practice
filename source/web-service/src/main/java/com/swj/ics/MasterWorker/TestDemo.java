package com.swj.ics.MasterWorker;

import java.util.Random;

/**
 * Created by swj on 2018/1/28.
 */
public class TestDemo {
    public static void main(String[] args) {
        Worker worker = new Worker();
         
        MasterWorkerMain master = new MasterWorkerMain(worker,10);
        
        //提交100个任务
        Random random = new Random();
        for (int i = 0 ; i < 100 ;i++) {
            Task task = new Task();
            task.setId(i);
            task.setName("task " + i);
            task.setPrice(random.nextInt(100));
            master.submit(task);
        }
        
        //运行线程
        master.execute();
        long start = System.currentTimeMillis();
        while (!master.isCompleted()) {
            //todo nothing waiting sub thread run completely
        }
        long takes = System.currentTimeMillis() - start;
        
        Integer result = master.getResult();
        
        System.out.println(String.format("最终运算结果是:%d,总耗时%d ms",result,takes));
    }
}
