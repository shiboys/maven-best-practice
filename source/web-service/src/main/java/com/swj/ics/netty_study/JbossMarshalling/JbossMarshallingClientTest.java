package com.swj.ics.netty_study.JbossMarshalling;

/**
 * author shiweijie
 * date 2018/11/6 上午9:57
 */
public class JbossMarshallingClientTest {
    public static void main(String[] args) {

        int port = 8080;
        String host = "localhost";

        JBossMarshallingClient client = new JBossMarshallingClient();

        client.bind(port,host);


    }
}
