package com.swj.ics.netty_study.rpc_edu;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * Created by swj on 2018/6/10.
 */
public class RpcClientProxy {
    public <T> T getRemoteMsg(Class<T> interfaceCls, InetSocketAddress address) {
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),new Class[]{interfaceCls},
                new RpcClientInvocationHandler(address));
    }
}
