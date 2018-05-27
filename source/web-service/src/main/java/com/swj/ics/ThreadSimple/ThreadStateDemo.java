package com.swj.ics.ThreadSimple;

import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2017/12/28.
 */
public class ThreadStateDemo {
    
    //该线程不断的进行睡眠
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //该线程在Waiting.class上等待。
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    static class Blocked implements Runnable {
        @Override
        public void run() {
            //循环里面持有锁而且并不是否
            synchronized (Blocked.class) {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();
        new Thread(new Waiting(),"WaitingThread").start();
        //BlockThread1获取锁，且没有释放，因此BlockThread2一直是阻塞状态
        new Thread(new Blocked(),"BlockThread-1").start();
        
        new Thread(new Blocked(),"BlockThread-2").start();
    } 
    /*
    * 运行结果如下：
    * "BlockThread-2" #15 prio=5 os_prio=0 tid=0x000000002854a800 nid=0x584 waiting for monitor entry [0x0000000029c2f000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at com.swj.ics.ThreadSimple.ThreadStateDemo$Blocked.run(ThreadStateDemo.java:46)
        - waiting to lock <0x0000000715fb5160> (a java.lang.Class for com.swj.ics.ThreadSimple.ThreadStateDemo$Blocked)
        at java.lang.Thread.run(Thread.java:745)

"BlockThread-1" #14 prio=5 os_prio=0 tid=0x0000000028549800 nid=0x1810 waiting on condition [0x0000000029b2e000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at java.lang.Thread.sleep(Thread.java:340)
        at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
        at com.swj.ics.ThreadSimple.ThreadStateDemo$Blocked.run(ThreadStateDemo.java:46)
        - locked <0x0000000715fb5160> (a java.lang.Class for com.swj.ics.ThreadSimple.ThreadStateDemo$Blocked)
        at java.lang.Thread.run(Thread.java:745)

"WaitingThread" #13 prio=5 os_prio=0 tid=0x0000000028549000 nid=0x1658 in Object.wait() [0x0000000029a2e000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x0000000715fb23b8> (a java.lang.Class for com.swj.ics.ThreadSimple.ThreadStateDemo$Waiting)
        at java.lang.Object.wait(Object.java:502)
        at com.swj.ics.ThreadSimple.ThreadStateDemo$Waiting.run(ThreadStateDemo.java:30)
        - locked <0x0000000715fb23b8> (a java.lang.Class for com.swj.ics.ThreadSimple.ThreadStateDemo$Waiting)
        at java.lang.Thread.run(Thread.java:745)

"TimeWaitingThread" #12 prio=5 os_prio=0 tid=0x0000000028544000 nid=0x2220 waiting on condition [0x000000002992e000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at java.lang.Thread.sleep(Thread.java:340)
        at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
        at com.swj.ics.ThreadSimple.ThreadStateDemo$TimeWaiting.run(ThreadStateDemo.java:16)
        at java.lang.Thread.run(Thread.java:745)
    * */
}
