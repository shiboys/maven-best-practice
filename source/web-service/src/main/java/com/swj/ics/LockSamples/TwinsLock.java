package com.swj.ics.LockSamples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by swj on 2018/1/14.
 * Sync内部类实现了tryAcquireShared和tryReleaseShared来实现共享锁
 */
public class TwinsLock implements Lock {
    
    static final int LOCK_COUNT = 2;
    
    static class Sync extends AbstractQueuedSynchronizer {
        
        public Sync(int count) {
            if (count < 0){
                throw  new IllegalArgumentException(" count must be larger then 0");
            } 
            setState(count);
        }
        
        @Override
        protected int tryAcquireShared(int arg) {

            for (;;) {
                int current = getState();
                int remainCount = current - arg;
                //remainCount < 0 表示获取锁不成功，
                if (remainCount < 0 || this.compareAndSetState(current,remainCount)) {
                    return remainCount;
                } 
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;) {
                int current = getState();
                int newState = current + arg;
                if (compareAndSetState(current,newState )) {
                    return true;
                }
           }
        }
        
        public Condition newCondition() {
            return new ConditionObject();
        }
    }
    
    
    private Sync sync = new Sync(LOCK_COUNT);
    
    @Override
    public void lock() {
        sync.acquireShared(1);//调用的是共享锁
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1) >= 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        //释放共享锁
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition() ;
    }
}
