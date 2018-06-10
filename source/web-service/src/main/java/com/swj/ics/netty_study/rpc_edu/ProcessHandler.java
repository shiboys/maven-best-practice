package com.swj.ics.netty_study.rpc_edu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.spi.Invoker;

/**
 * Created by swj on 2018/6/10.
 */
public class ProcessHandler implements Runnable {
    
    private Socket socket;
    private Object service;
    public ProcessHandler(Socket socket,Object service) {
        this.socket = socket;
        this.service = service;
    }
    
    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            Object result = invoke(rpcRequest);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Object invoke(RpcRequest rpcRequest) throws Exception {
      
        Class<?> clazz = service.getClass();
        Object[] args = rpcRequest.getArgs();
        Class<?>[] paraTypes = new Class<?>[args.length];
        for(int i = 0,len = args.length;i < len;i++) {
            paraTypes[i] = args[i].getClass();
        }
        Method method = clazz.getMethod(rpcRequest.getMethodName(),paraTypes);
        return method.invoke(service,args);
    }
}
