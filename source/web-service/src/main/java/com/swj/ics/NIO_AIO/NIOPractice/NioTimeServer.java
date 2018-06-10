package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.IOException;

/**
 * Created by swj on 2018/6/10.
 */
public class NioTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8888;
        new Thread(new MultiplexerTimerServer(port),"MultiplexerTimerServer-Thread0").start();
        System.in.read();
    }
}
