package com.swj.ics.LockSamples;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sun.reflect.misc.FieldUtil;

/**
 * Created by swj on 2018/1/14.
 * 读写锁提升并发性，保证了每次写操作对读操作的可见性
 * 同时简化了编程方式
 */
public class ReadWriteLockDemo {
    static Map<String,Object> map = new HashMap<String,Object>();
    static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Lock readLock = readWriteLock.readLock();
    static Lock writeLock = readWriteLock.writeLock();
    
    public static final Object get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }
    
    public static final Object put(String key,Object object) {
        writeLock.lock();
        try {
            return map.put(key,object);
        } finally {
            writeLock.unlock();
        }
    }
    //清空所有内容。
    public static final void clear() {
        writeLock.lock();
        try {
            map.clear();
        } finally {
            writeLock.unlock();
        }
    }
}
