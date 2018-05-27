package com.swj.ics.RPCSimple;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by swj on 2017/1/3.
 */
public class RpcExporter<T> {
    static Executor threadPool= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static void exporter(String hostName,int port) throws  Exception
    {
        ServerSocket serverSocket=new ServerSocket();
        serverSocket.bind(new InetSocketAddress(hostName,port));

        try {
            while(true)
            {
                Socket clientSocket=serverSocket.accept();
                threadPool.execute(new ExporterThread(clientSocket));
            }
        }
        finally {
            serverSocket.close();
        }

    }
}
class ExporterThread implements  Runnable
{
    private Socket client ;
    public ExporterThread(Socket clientRequest)
    {
        client=clientRequest;
    }
    @Override
    public void run()   {
        InputStream inputStream=null;
        ObjectOutputStream objectOutput=null;
        try {
            inputStream = client.getInputStream();
            ObjectInputStream objectInput=new ObjectInputStream(inputStream);
            String className= objectInput.readUTF();
            Class<?> servicClass=Class.forName(className);
            String methodName=objectInput.readUTF();
            Class<?>[] parameterTypes= (Class<?>[])objectInput.readObject();
            Object[] args=(Object[])objectInput.readObject();
            Method method=servicClass.getMethod(methodName,parameterTypes);
            Object target=servicClass.newInstance();
            Object result= method.invoke(target,args);
            objectOutput=new ObjectOutputStream(client.getOutputStream());
            objectOutput.writeObject(result);//将服务端的方法返回结果序列化，并返回到客户端
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        finally {
            if(inputStream!=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectOutput!=null)
            {
                try {
                    objectOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(client!=null)
            {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
