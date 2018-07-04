package com.swj.ics.netty_study.rpc_edu;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by swj on 2018/6/10.
 */
public class RpcServer {
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    public void publish(final Object service,int port) {
        ServerSocket serverSocket =null;
        try {
            serverSocket =  new ServerSocket(port);
            while (true) {
                THREAD_POOL.execute(new ProcessHandler(serverSocket.accept(),service));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
