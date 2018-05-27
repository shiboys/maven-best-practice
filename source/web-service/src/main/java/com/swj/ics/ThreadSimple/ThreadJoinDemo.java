package com.swj.ics.ThreadSimple;

import java.util.concurrent.TimeUnit;
import com.mysql.jdbc.TimeUtil;

/**
 * Created by swj on 2018/1/7.
 * Thread.join的使用
 * 如果线程A使用了thrad.join语句，其含义是，当前线程A等待thread终止之后才从thread.join返回
 * 本示例展示的描述如下：
 * 创建10个线程，编号0-9，每个线程调用前一个线程的join方法。也就是0线程结束了，1线程才能从join方法中返回。
 * 而线程0需要等待main线程结束。
 */
public class ThreadJoinDemo {
    
    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        
        for (int i = 0;i < 10 ;i++) {
            Thread thread = new Thread(new Domino(previous),i+"");
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(String.format("%s terminate",Thread.currentThread().getName()));
    }
    
    static class Domino implements Runnable {
        private Thread previous ;
        public Domino(Thread previous) {
            this.previous = previous;
        }
        @Override
        public void run() {
            try {
                previous.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println(String.format("%s terminate !",Thread.currentThread().getName()));
        }
    }
}

/**
 * 输出结果如下
 *  main terminate
 0 terminate !
 1 terminate !
 2 terminate !
 3 terminate !
 4 terminate !
 5 terminate !
 6 terminate !
 7 terminate !
 8 terminate !
 9 terminate !
 从输出结果来看，每个线程终止的前提是前驱线程的终止
 每个线程等待前驱线程终止之后，才会join方法返回。这里涉及了等待通知机制。
 
 */
