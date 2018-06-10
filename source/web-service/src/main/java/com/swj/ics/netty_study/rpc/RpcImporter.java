package com.swj.ics.netty_study.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Created by swj on 2018/6/9.
 */
public class RpcImporter<T> {
    public T importer(Class serviceClass, InetSocketAddress inetAddress) {
        return (T)Proxy.newProxyInstance(serviceClass.getClassLoader(),
                serviceClass.getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectInputStream objectInputStream = null;
                        ObjectOutputStream objectOutputStream = null;
                        
                        try {
                            socket = new Socket();
                            socket.connect(inetAddress);
                            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeUTF(serviceClass.getName());
                            objectOutputStream.writeUTF(method.getName());
                            objectOutputStream.writeObject(method.getParameterTypes());
                            objectOutputStream.writeObject(args);
                            
                            objectInputStream = new ObjectInputStream(socket.getInputStream());
                            return objectInputStream.readObject();
                            
                            //objectInputStream
                        } finally {
                            if(objectInputStream != null) {
                                objectInputStream.close();
                            } if (objectOutputStream != null) {
                                objectOutputStream.close();
                            } if (socket != null) {
                                socket.close();
                            }
                            
                        }
                         
                    }
                }
        );
    }
}
