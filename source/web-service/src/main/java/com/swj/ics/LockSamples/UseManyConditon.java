package com.swj.ics.LockSamples;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by swj on 2018/2/4.
 */
public class UseManyConditon {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition c1 = lock.newCondition();
    private static Condition c2 = lock.newCondition();
    
    private static void m1 () {
        try {
            lock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+",进入方法m1");
            try {
                c1.await();//释放锁
                System.out.println("当前线程："+Thread.currentThread().getName()+",m1方法继续");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    private static void m2() {
        try {
            lock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+",进入方法m2");
            try {
                c1.await();//释放锁
                System.out.println("当前线程："+Thread.currentThread().getName()+",m2方法继续");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    private static void m3 () {
        try {
            lock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+",进入方法m3");
            try {
                c2.await();//释放锁
                System.out.println("当前线程："+Thread.currentThread().getName()+",m3方法继续");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    private static void m4 () {
        try {
            lock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+",进入方法m4,进行唤醒c1等待的线程");
            c1.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private static void m5 () {
        try {
            lock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+",进入方法m5,进行唤醒c2等待的线程");
            c2.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                m1();
            }
        },"t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                m2();
            }
        },"t2");

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                m3();
            }
        },"t3");
        
        t1.start();
        t2.start();
        t3.start();

        try {
            //当前主线程等待2秒
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                m4();
            }
        },"t4");

        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                m5();
            }
        },"t5");
        
        t4.start();//唤醒t1和t2
        t5.start();//唤醒t3
    } 
        
}
