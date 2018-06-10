package com.swj.ics.netty_study.rpc_edu;

/**
 * Created by swj on 2018/6/10.
 */
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String helloMsg) {
        return "hello," + helloMsg;
    }
}
