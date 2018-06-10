package com.swj.ics.netty_study.rpc_edu;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by swj on 2018/6/10.
 */
public class RpcClientInvocationHandler implements InvocationHandler {
    
    private  InetSocketAddress addr;
    public RpcClientInvocationHandler(InetSocketAddress address) {
        this.addr = address;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            socket = new Socket(addr.getHostName(),addr.getPort());
            RpcRequest request = new RpcRequest();
            request.setClassName(method.getDeclaringClass().getName());
            request.setMethodName(method.getName());
            request.setArgs(args);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object result = objectInputStream.readObject();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
