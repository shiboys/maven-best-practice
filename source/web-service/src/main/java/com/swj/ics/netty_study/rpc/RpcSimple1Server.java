package com.swj.ics.netty_study.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by swj on 2018/6/9.
 */
public class RpcSimple1Server {

    static ExecutorService threadPool;

    public static void export(String hostName,int port) throws IOException {
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(hostName,port));
            while (true) {
                threadPool.execute(new ExportTask(serverSocket.accept()));
            }            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

class ExportTask implements Runnable {
    private Socket socket = null;
    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    public ExportTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            String className = objectInputStream.readUTF();
            Class<?> service = Class.forName(className);
            String methodName = objectInputStream.readUTF();
            Object[] paramTypes = (Object[]) objectInputStream.readObject();
            Method method = service.getMethod(methodName, (Class<?>[]) paramTypes);
            Object[] args = (Object[]) objectInputStream.readObject();
            //TODO：第一次调用失败的原因是 使用了method.invoke(service,args)
            //正确的打开姿势是 method.invoke(service.newInstance(),args);
            Object result = method.invoke(service.newInstance(), args);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
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
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

