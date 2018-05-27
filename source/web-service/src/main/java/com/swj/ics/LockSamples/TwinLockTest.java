package com.swj.ics.LockSamples;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by swj on 2018/1/14.
 * 
 *
 */
public class TwinLockTest {
    static final Lock lock = new TwinsLock();
    
    public static void main(String[] args) throws InterruptedException {
        
        for (int i =0 ;i < 10 ;i++) {
            JobThread jobThread = new JobThread();
            jobThread.setDaemon(true);
            jobThread.start();
        }
//主线程等待子线程执行
        for (int i =0 ;i < 10 ;i++) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println();
        }
    }
    /*打印结果如下：
    * 1515915999567:Thread-0
1515915999567:Thread-1

1515916001567:Thread-2
1515916001567:Thread-5

1515916003567:Thread-6
1515916003567:Thread-3

1515916005568:Thread-4
1515916005568:Thread-7

1515916007568:Thread-9
1515916007568:Thread-8
    * */
    
    static class JobThread extends Thread {
        
        @Override
        public void run() {
            //需要在外循环价格while(true)..不太理解，难道是跟synchronized 一样，每次唤醒需要检查一下释放获取了锁？
          // while (true) {
               lock.lock();//在这个实验中，由于可以同时有2个线程获取锁，则2条线程名称是几乎同时出现的。
               try {
                   try {
                       TimeUnit.SECONDS.sleep(1);
                       System.out.println(System.currentTimeMillis() +":" + Thread.currentThread().getName());
                       TimeUnit.SECONDS.sleep(1);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               } finally {
                   lock.unlock();
               }
        //    }
            
        }
    }
}
