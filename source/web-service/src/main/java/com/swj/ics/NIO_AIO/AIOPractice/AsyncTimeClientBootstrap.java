package com.swj.ics.NIO_AIO.AIOPractice;

/**
 * Created by swj on 2018/6/11.
 */
public class AsyncTimeClientBootstrap {
    public static void main(String[] args) {
        String host ="localhost";
        int port = 8888;
        AsyncTimeClientHandler clientHandler = new AsyncTimeClientHandler(host,port);
        new Thread(clientHandler,"AsyncTimeClientHandler-001").start();
    }
}
