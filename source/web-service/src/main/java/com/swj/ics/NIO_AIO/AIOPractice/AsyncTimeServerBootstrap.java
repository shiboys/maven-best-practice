package com.swj.ics.NIO_AIO.AIOPractice;

/**
 * Created by swj on 2018/6/11.
 */
public class AsyncTimeServerBootstrap {
    public static void main(String[] args) {
        int port = 8888;
        AsyncTimeServerHandler servertHandler = new AsyncTimeServerHandler(port);
        new Thread(servertHandler,"AsyncTimeServerHandler-001").start();
    }
}
