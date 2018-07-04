package com.swj.ics.netty_study.rpc;

/**
 * Created by swj on 2018/6/9.
 */
public class EchoServiceImpl implements IEchoService {
    @Override
    public String echo(String reqMsg) {
       return reqMsg + "--> I am ok" ;
    }
}
