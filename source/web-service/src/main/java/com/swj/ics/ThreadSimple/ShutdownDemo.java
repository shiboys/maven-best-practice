package com.swj.ics.ThreadSimple;

import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/1.
 * //中断操作是一个简便的线程之间的交互方式，而这种交互方式最适合用来取消或者终止任务。
 * 除了中断以外，还可以使用一个Boolean变量来控制是否需要停止任务或者终止线程。
 */
public class ShutdownDemo {
    
    public static void main(String[] args) throws InterruptedException {
        Runnable one = new Runner();
        Thread countThread = new Thread(one,"Count Thread");
        countThread.start();
        //睡眠1秒钟，main线程会对CountThread线程进行中断，使CountThread可以感知中断而结束。
        
        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();
        
        Runner two = new Runner();
        countThread = new Thread(two,"Count Thread");
        countThread.start();
        TimeUnit.SECONDS.sleep(1);
        
        two.cancel();
        
        
        //main 线程可以通过中断操作和cancel()方法均可以使CountThread得以终止
        //这种通过标示位或者中断操作的方式能够使线程在终止的时候有机会去清理资源，
        //而不是武断的将线程终止，因此这种做法更加的安全和优雅。
        
    }
    
    static class Runner implements Runnable {
        private long count;
        
        private volatile boolean on = true;
        
        public void cancel() {
            on = false;
        }
        
        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                count ++ ;
            }
            System.out.println(" current count is " + count);
        }
    }
}
