package com.swj.ics.ThreadSimple.ConnectionPoolDemo;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Created by swj on 2018/1/7.
 */
public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<>();
    
    public ConnectionPool(int initialSize) { //连接池初始化指定数量的连接
        if (initialSize > 0 ) {
            for (int i = 0;i < initialSize ;i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }
    
    public void releaseConnection (Connection connection) {
        synchronized (pool) {
            pool.addLast(connection);
            //连接释放之后，需要进行通知，这样等待的线程就知道了连接池中有一个连接归还。
            pool.notifyAll();
        }
    }

    /**
     * 
     * @param millseconds 获取连接等待的毫秒数
     * @return 在指定的毫秒内，如果获取到连接，则返回该链接，否则返回null
     */
    public Connection fetchConnection(long millseconds) throws InterruptedException {
        synchronized (pool) {
            if (millseconds < 0) { //完全超时？？
                while (pool.size() < 1) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long future = millseconds + System.currentTimeMillis();
                long remaining = millseconds;
                while (remaining > 0 && pool.isEmpty()) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                //等待超时之后如果连接池还是没有连接，则返回null,否则取一个连接
                if (!pool.isEmpty()) {
                    return pool.removeFirst();
                }
                return null;
            }
                
        }
    }
}
