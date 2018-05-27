package com.swj.ics.LockSamples;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by swj on 2018/2/4.
 * lock.getHoldCount只能在调用线程的内部使用，不能在其他线程中使用。
 * 那么我们可以在m1方法里面调用m2方法，同时m1和m2方法都持有lock的锁，即可以holdCount数递增
 */
public class TestHoldCount {
    private ReentrantLock lock = new ReentrantLock();
    
    private  void m1() {
        try {
            lock.lock();
            System.out.println("进入方法m1,当前线程holdCount为:"+lock.getHoldCount());
            //在m1里面调用m2
            m2();
        } finally {
            lock.unlock();
        }
    }
    
    private  void m2() {
        try {
            lock.lock();
            System.out.println("进入方法m2,当前线程holdCount为:"+lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        TestHoldCount instance = new TestHoldCount();
        instance.m1();
    }
}
