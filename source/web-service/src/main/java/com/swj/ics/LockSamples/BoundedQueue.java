package com.swj.ics.LockSamples;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by swj on 2018/1/18.
 * 使用有界队列来的示例来深入了解Condition的使用方式。
 * 有界队列是一种特殊的队列，当队列为空时，队列的获取操作将会阻塞获取线程
 * 直到队列中有新增元素。
 * 当队列满时，队列的插入操作将会阻塞插入线程，直到队列出现"空位"。
 */
public class BoundedQueue<T> {
    
    private ReentrantLock lock = new ReentrantLock();
    
    private Condition fullCondition = lock.newCondition();
    
    private Condition emptyCondition = lock.newCondition();
    
    private Object[] objects = null;
    
    private int count,addIndex,removeIndex;
    
    public BoundedQueue(int size) {
        objects = new Object[size];
    }
    
    //添加一个元素，如果数组满，则添加线程进入等待状态，知道队列不满。
    public void add (T instance) {
        lock.lock();
        try {
            //这里使用while循环，防止过早或者意外通知
            while (count == objects.length) {
                fullCondition.await();
            }
            objects[addIndex] = instance;
            addIndex ++;
            if (addIndex == objects.length) {
                addIndex = 0;//这句话的作用是，如果当期插入的操作以及到队尾
                //则重新从队首开始，因为代码能走到这里，肯定是之前有remove操作执行
                //remove操作也是先从队首进行移除。
            }
            count++ ;
            //通知 不满队列等待条件，可以进行移除
            emptyCondition.signal();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    
    public T remove() {
        lock.lock();
        T instance = null;
        try {
            while (count == 0) {
                emptyCondition.wait();
            }
            instance = (T)objects[removeIndex];
            removeIndex++ ;
            if (removeIndex == objects.length) {
                removeIndex = 0;
            }
            count -- ;
            //如果因为当前队列满而导致的跑在add上的线程等待，则通知它，现在已经不满了
            //可以继续add了
            fullCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
            
        } finally {
            lock.unlock();
        }
        return instance;
    }
}
