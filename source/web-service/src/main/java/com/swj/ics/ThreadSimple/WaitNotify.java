package com.swj.ics.ThreadSimple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/1.
 */
public class WaitNotify {
    
    static Object lock = new Object();
    static boolean flag = true;
    
    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(),"Wait Thread");
        waitThread.start();
        
        //先让wait Thread运行，
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(),"Notify Thread");
        notifyThread.start();
    }
    
    static class Wait implements Runnable {
        @Override
        public void run() {
            // wait线程先进行加锁，获取lock对象上的Monitor
            synchronized (lock) {
                while(flag) {
                    try {
                       System.out.println(String.format("%s flag is true,wait @ %s",
                               Thread.currentThread().getName(),
                               new SimpleDateFormat("HH:mm:ss").format(new Date())
                               )); 
                        lock.wait();
                        //等待并释放lock锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //条件满足的时候，完成工作
                System.out.println(String.format("%s flag is false,running @ %s",
                        Thread.currentThread().getName(),
                        new SimpleDateFormat("HH:mm:ss").format(new Date())
                ));
            }
        }
    }
    
    static class Notify implements Runnable {
        @Override
        public void run() {
            //先拿到lock上面的Monitor锁
            synchronized (lock) { 
                //获取lock上面的锁，然后进行通知，通知时不会释放lock上的锁。
                //知道当前线程释放了lock之后，WaitThread才能从wait方法中返回。
                System.out.println(String.format("%s hlod the lock,notify @ %s",
                        Thread.currentThread().getName(),
                        new SimpleDateFormat("HH:mm:ss").format(new Date())
                        ));
                lock.notifyAll();
                flag = false;
                //通知之后，当前线程并没有释放锁，因此waitThread仍然得不到运行
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            //到此已经释放了lock锁，但是仍然可以重新获取lock锁，此时wait 和notify自身那个先获取，就不一定了。
            synchronized (lock) {
                System.out.println(String.format("%s hlod the lock again,notify @ %s",
                        Thread.currentThread().getName(),
                        new SimpleDateFormat("HH:mm:ss").format(new Date())
                ));
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}

/*
* 等待通知的经典范式
* 等待方遵循如下的锁
* 1：获取对象的锁
* 2：如果条件不满足，则调用对象的wait()方法，被通知后仍要检查条件 (这里一般使用while)
* 3：条件满足则执行对于的逻辑
*对应等待伪代码如下
* synchronized(对象) {
* while(条件不满足) {
*       对象.wait()
*   }
*   处理对应的逻辑
* }
* 
* 通知方遵循如下的原则。
* 1、获取对象的锁。
* 2、改变条件
* 3、通知所有等待在对象上的线程。
* synchronized(对象) {
* 改变条件
* 对象.notifyAll();
* }
* */
