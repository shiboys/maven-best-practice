package com.swj.ics.netty_study.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by swj on 2018/6/9.
 */
public class RpcTest {
    public static void main(String[] args) {
        int port = 10086;
        String hostName = "localhost";
        InetSocketAddress addr = new InetSocketAddress(hostName,port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcSimple1Server.export(hostName,port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

       /* try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        RpcImporter<IEchoService> echoServiceRpcImporter = new RpcImporter<>();
        IEchoService echoService = echoServiceRpcImporter.importer(EchoServiceImpl.class,addr);
        System.out.println("call remote result : " + echoService.echo("Are you OK ? "));
    }
}
