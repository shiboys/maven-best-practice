package com.swj.ics.netty_study.JbossMarshalling;

/**
 * Created by swj on 2018/11/5.
 */
public class JbossMarshallingTest {
    public static void main(String[] args) throws InterruptedException {
        JBossMarshallingServer server = new JBossMarshallingServer();
        int port = 8080;
        String host = "localhost";

        server.bind(port);


    }
}
