package com.swj.ics.ThreadSimple;

import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/7.
 * ThreadLocal 即线程变量，是一个以TheadLocal对象为键，任意对象为值的存储结构。
 * 这个结构被附带在线程上，也就是一个线程可以根据一个ThreadLocal对象查询到绑定到这个线程上的值。
 */
public class ThreadLocalDemo  {
    //第一次调用get方法的时候会初始化，(如果set方法没有被调用的话),每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }
    
    static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }
    
    public static void main(String[] args) throws InterruptedException {
        begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("total cost " + end() +" millseconds");
    }
}
