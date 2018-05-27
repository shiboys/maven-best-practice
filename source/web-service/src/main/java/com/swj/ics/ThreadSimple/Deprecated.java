package com.swj.ics.ThreadSimple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/1.
 * 演示线程已经过期的suspend,resume,stop
 * 不建议使用suspend,resume,stop的原因主要有：以suspend方法为例，在调用后，线程不会释放已经占有的资源
 * (比如锁)，而是占有着资源进入睡眠状态，这样容易引发死锁问题。同样，stop方法在终结一个线程时不会保证
 * 线程的资源正常释放，同城是没有线程完成释放资源工作的机会，因此会导致线程可能工作在不确定的状态下。
 * 下一个Demo演示如何安全的终止线程。ShutdownDemo
 */
public class Deprecated {
    
    public static void main(String[] args) throws InterruptedException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(new Runner(),"PrintThread");
        printThread.setDaemon(true);
        printThread.start();
        
        TimeUnit.SECONDS.sleep(3);
        //将PrintThread进行暂停，输出内容的工作将停止
        printThread.suspend();
        
        System.out.println(String.format("main thread suspend PrintThread at "
                + simpleDateFormat.format(new Date())));
        
        TimeUnit.SECONDS.sleep(3);
        //将 PrintThread进行恢复
        printThread.resume();
        System.out.println(String.format("main thread resume PrintThread at "
                + simpleDateFormat.format(new Date())));

        TimeUnit.SECONDS.sleep(3);
        //将PrintThread进行终止，输出内容停止
        printThread.stop();
        System.out.println(String.format("main thread stop PrintThread at "
                + simpleDateFormat.format(new Date())));
        TimeUnit.SECONDS.sleep(3);
        
    }
    
    static class Runner implements Runnable {
        @Override
        public void run() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                System.out.println(String.format("%s run at %s ",
                        Thread.currentThread().getName(),simpleDateFormat.format(new Date())));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
