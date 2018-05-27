package com.swj.ics.LockSamples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by swj on 2018/1/14.
 */
public class Mutex implements Lock {
    
    //AbstractQueuedSynchronizer AQS最主要是三个方法，getState(),setState(),compareAndSetState()
    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            //状态为0时，获取锁。
            if (this.compareAndSetState(0,1)) {
                this.setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
             return false;
        }

        //释放锁，将状态设置为0
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException("no lock alread");
            }
            setState(0);
            setExclusiveOwnerThread(null);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
        
        public Condition newCondition() {
            return new ConditionObject();
        }
    }
    
    //lock的所有操作都可以委托到sync上了
    
    private final Sync sync = new Sync();
    
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
