package com.swj.ics.RPCSimple;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by swj on 2017/1/3.
 */
public class RpcImporter<T> {
    //客户端通过代理来访问远程的rpc 方法。
    public <T> T importer(final Class<?> serviceClass,final InetSocketAddress addr)
    {
        return (T) java.lang.reflect.Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass.getInterfaces()[0]}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket=null;
                        ObjectOutputStream objectOut=null;
                        ObjectInputStream objectIn=null;
                        try {
                            socket=new Socket();
                            socket.connect(addr);
                            objectOut=new ObjectOutputStream(socket.getOutputStream());
                            //类名
                            objectOut.writeUTF(serviceClass.getName());
                            //方法名
                            objectOut.writeUTF(method.getName());
                            //参数类型，采用writeObject方式
                            objectOut.writeObject(method.getParameterTypes());
                            objectOut.writeObject(args);
                            //等待远程服务方法调用结果
                            objectIn=new ObjectInputStream(socket.getInputStream());
                            return objectIn.readObject();
                        }
                        finally {
                            if(socket!=null)
                                socket.close();
                            if(objectOut!=null)
                                socket.close();
                            if(objectIn!=null)
                                socket.close();
                        }
                    }
                }
        );
    }
}
