package com.swj.ics.multiThread_Concurrent.ThreadPoolExecutorDemo;

import java.util.concurrent.TimeUnit;
import com.mysql.jdbc.TimeUtil;

/**
 * Created by swj on 2018/2/4.
 */
public class MyTask implements Runnable {
    
    private Integer id;
    private String name;

    public MyTask(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public MyTask() {
    }

    @Override
    public String toString() {
        return  "{id:"+this.id+",name:" + name+"}";
    }

    @Override
    public void run() {
        try {
            System.out.println(String.format("当前任务的线程id是：%d，名称是：%s"
                    ,this.id ,this.name));
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
