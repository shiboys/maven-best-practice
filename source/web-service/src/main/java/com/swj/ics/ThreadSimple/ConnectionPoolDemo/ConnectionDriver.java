package com.swj.ics.ThreadSimple.ConnectionPoolDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * Created by swj on 2018/1/7.
 * 由于java.sql.connection只是一个接口，最终实现是由具体的数据库驱动方案来实现的。
 * 考虑到本示例只是演示，我们通过JDK的动态代理来构造一个Connection,
 * 该Connection的commit方法只是调用线程休眠100秒钟，
 */
public class ConnectionDriver {
    static class ConnectionHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("commit")) {
                TimeUnit.MICROSECONDS.sleep(100);
            }
            return null;
        }
    }
    
    public static Connection createConnection() {
        return (Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},new ConnectionHandler()
                );
    }
}
