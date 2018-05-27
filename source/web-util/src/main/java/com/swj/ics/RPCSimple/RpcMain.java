package com.swj.ics.RPCSimple;

import java.net.InetSocketAddress;

/**
 * Created by swj on 2017/1/3.
 */
public class RpcMain {
    public static void main(String[] args)
    {
        String hostName="localhost";
        int port=8088;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter(hostName,port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        RpcImporter<IEchoService> importer=new RpcImporter() ;
        IEchoService echo= importer.importer(EchoServiceImpl.class,new InetSocketAddress(hostName,port));
        System.out.println(echo.echo("hello from rpc client "));

    }
}
