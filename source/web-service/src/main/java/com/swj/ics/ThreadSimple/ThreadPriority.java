package com.swj.ics.ThreadSimple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/1.
 * 线程的优先级demo+
 */
public class ThreadPriority {
    
    private static boolean notStart= true;
    private static boolean notEnd = true;
    
    /*
    * 线程的优先级设置，在这个演示里面不起作用
    * */
    public static void main(String[] args) throws InterruptedException {

        List<Job> jobList = new ArrayList<>();
        
        for (int i = 0;i < 10;i++) {
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            Thread thread = new Thread(job);
            jobList.add(job);
            thread.setPriority(priority);
            thread.start();
        }
        
        notStart = false;
        //让上面的子线程充分运行
        TimeUnit.SECONDS.sleep(10);
        notEnd = false;
        
        for(Job job : jobList) {
            //18796842
            //19667158
         System.out.println("Job Priority :" + job.priority+",Count :" +job.count);   
        }       
        
    }
    
    static class Job implements Runnable {
        private int priority;
        private int count;
        
        public Job(int priority) {
            this.priority = priority;
        }
        
        @Override
        public void run() {
            while (notStart) {
                //thread.yield()的意思是当前线程从正在运行状态，变成可运行状态。
                //也就是说让当前线程也就是刚刚的那个线程还是有可能会被再次运行
                //该线程会把cpu时间让让掉，让其他线程或者当前线程自己运行。
                Thread.yield();
                //这里这么使用的目的是为了等待。直到主线程将notStart的状态改变为止
            }
            while (notEnd) {
                Thread.yield();
                count++;
            }
        }
    }
}
