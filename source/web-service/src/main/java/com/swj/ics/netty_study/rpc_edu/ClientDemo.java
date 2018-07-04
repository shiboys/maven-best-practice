package com.swj.ics.netty_study.rpc_edu;

import java.net.InetSocketAddress;

/**
 * Created by swj on 2018/6/10.
 */
public class ClientDemo {
    public static void main(String[] args) {
        int port = 8888;
        InetSocketAddress address = new InetSocketAddress("localhost",port);
        RpcClientProxy proxy = new RpcClientProxy();
        IHelloService service = proxy.getRemoteMsg(IHelloService.class,address);
        String result = service.sayHello("hello from client service");
        System.out.println(result);
    }
}
