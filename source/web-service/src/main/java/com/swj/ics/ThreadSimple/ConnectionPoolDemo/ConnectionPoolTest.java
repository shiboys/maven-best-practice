package com.swj.ics.ThreadSimple.ConnectionPoolDemo;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by swj on 2018/1/7.
 * 为了 测试连接池获取连接的请看，使用多线程来模拟同时获取连接的情况
 * 连接池的容量为10，线程的数量为30
 */
public class ConnectionPoolTest {
    
    private static final int THREAD_COUNT = 1000;//电脑性能太好，
    // 100以下的线程数量根本没有获取不到连接的情况。
    
    private static final int POOL_SIZE = 10;    
    
    static CountDownLatch start = new CountDownLatch(1);
    static CountDownLatch end = null;
    
    static ConnectionPool pool = new ConnectionPool(POOL_SIZE);
    
    public static void main(String[] args) throws InterruptedException {
        end =  new CountDownLatch(THREAD_COUNT);
        int loopCount = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger noGot = new AtomicInteger();
        
        for(int i = 0; i< THREAD_COUNT; i++) {
            Thread thread = new Thread(new ConnectionRunner(loopCount,got,noGot),"ConnectionRunnerThread"+i);
            thread.start();
        }
        start.countDown();//告诉所有的使用start.await()的线程，开始运行
        
        end.await(); //等待end的 计数器等于0。也就是所有的子线程运行完毕。
        System.out.println("total invoke fetchConnection times :" + THREAD_COUNT * loopCount);
        System.out.println("got connection times : " + got.get());
        System.out.println("not got connection times : " + noGot.get());
    }
    
    static class ConnectionRunner implements Runnable {
        
        private int count;//获取连接的尝试次数
        private AtomicInteger got; //从连接池获取到连接的计数
        private AtomicInteger noGot; // 从连接池获未能获取到连接的计数
        
        public ConnectionRunner(int count,AtomicInteger got,AtomicInteger noGot) {
            this.count = count;
            this.got = got;
            this.noGot = noGot;
        }
        @Override
        public void run() {
            try {
                start.await();//等待主线程统一发号施令，开始运行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (count > 0) {
                //如果1000毫秒之内从连接池货到线程，则执行该线程，获取成功的次数加1
                //否则未获取成功的次数加1
                try {
                    Connection connection =  pool.fetchConnection(1000);
                    if (connection != null) {
                        try {
                            connection.createStatement();//这里会报异常，因此需要finally
                            connection.commit();
                        } finally {
                            pool.releaseConnection(connection);
                            got.incrementAndGet();
                        }

                    } else {
                        noGot.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    count--;    
                }                
            }
            
            //告知end等待线程，当前线程已经运行完毕
            end.countDown();
        }
    }
            
}
