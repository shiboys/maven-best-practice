package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.IOException;

/**
 * Created by swj on 2018/6/11.
 */
public class NioTimeClient {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 8888;
        new Thread(new MultiplexerTimerClient(host,port)).start();
        System.in.read();
    }
}
