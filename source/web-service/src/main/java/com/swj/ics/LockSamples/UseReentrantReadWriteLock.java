package com.swj.ics.LockSamples;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by swj on 2018/2/4.
 */
public class UseReentrantReadWriteLock {
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    
    public void read() {
        try {
            readLock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+"，进入read方法。。");
            Thread.sleep(3000);
            System.out.println("当前线程："+Thread.currentThread().getName()+"，退出");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write() {
        try {
            writeLock.lock();
            System.out.println("当前线程："+Thread.currentThread().getName()+"，进入write方法。。");
            Thread.sleep(3000);
            System.out.println("当前线程："+Thread.currentThread().getName()+"，退出");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }
    
    public static void main(String[] args) {
        UseReentrantReadWriteLock instance = new UseReentrantReadWriteLock();
        
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.read();
            }
        },"t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.read();
            }
        },"t2");

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.write();
            }
        },"t3");

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                instance.write();
            }
        },"t4");
        
        //演示两个贤臣同时读取
       /* t1.start();
        t2.start();*/
       //演示读写互斥
        
      
        t3.start();//写。写需要读完成之后
        t1.start();//读
        
      
      //写写互斥
      /*  t3.start();
        t4.start();*/
        //
    }
}
