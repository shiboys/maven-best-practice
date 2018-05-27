package com.swj.ics.ThreadSimple;

import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/1.\
 * 中断：其他线程通过调用该线程的interrupt方法对其进行中断操作。
 * 线程通过检查自身是否被中断来进行响应。线程通过isInterrupted方法来进行判断
 */
public class InterruptedDemo {
    
    public static void main(String[] args) throws InterruptedException {
        Thread sleepThread = new Thread(new SleepRunner(),"SleepThread");
        Thread runThread = new Thread(new BusyRunner(),"runThread");
        sleepThread.setDaemon(true);
        runThread.setDaemon(true);
        
        sleepThread.start();
        runThread.start();
        
        //主线程休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);
        
        sleepThread.interrupt();
        runThread.interrupt();
        
        System.out.println("SleepThread interrupted is : "+ sleepThread.isInterrupted());
        System.out.println("RunThread interrupted is : "+ runThread.isInterrupted());

        /**
         * 输入结果为
         * SleepThread interrupted is : false
         RunThread interrupted is : true
         */ 
        //SleepThread在抛出InterruptedException之前，会先把该进程中的中断标示位清除
        //然后抛出InterruptedException,此时调用isInterrupted()方法会返回false
        //而一直忙碌的BusyThread,
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                
            }
        }
    }
}
