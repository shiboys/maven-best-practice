package com.swj.ics.LockSamples;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by swj on 2018/1/14.
 */
public class ReentrantLock2 implements Lock {
    
    class SyncReentrant extends AbstractQueuedSynchronizer {
        
        //可重入锁的非公平重入实现
        //如果state = 0 ，则首次获取，调用compareAndSetState
        // else 如果获取锁的线程还是当前线程，则仍然可以获取锁,判断加上acquire之后，是否达到最大获取次数
        //其他情况返回false
        final boolean notFairTryAcqire(int acquire) {
            int state = getState();
            final Thread currentThread = Thread.currentThread();
            if (state == 0) { //第一次获取锁的时候调用compareAndSetState
                if (compareAndSetState(state,acquire)) {
                    setExclusiveOwnerThread(currentThread);
                }
                return true;
            } else if(currentThread == getExclusiveOwnerThread()) {//当前线程仍然是获取锁的线程
                int newState = state + acquire;//最大值之后，再加就是小于0了
                if (newState < 0) {
                    throw new Error("maximuum lock count exceeded !");
                }
                setState(newState);
                return true;
            }
            return false;
        }
        
        @Override
        protected boolean tryAcquire(int arg) {
            return super.tryAcquire(arg);
        }

        /**
         * 释放锁。如果当前线程是锁的获取线程，则将state - arg，如果结果为0 则返回true,并且将当前锁的
         * 线程设置为 null,否则返回false;
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            int state = getState();
            int remainState = state - arg;
            boolean isReleased = false;
            if (remainState == 0) {
                isReleased = true;
                setExclusiveOwnerThread(null);
            }
            setState(remainState);
            return isReleased;
        }
    } 
    
    @Override
    public void lock() {
        
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
