package com.swj.ics.netty_study.rpc_edu;

/**
 * Created by swj on 2018/6/10.
 */
public class RpcServerDemo {
    
    public static void main(String[] args) {
        int port= 8888;
        RpcServer server = new RpcServer();
        IHelloService helloService = new HelloServiceImpl();
        server.publish(helloService,port);
    }
}
