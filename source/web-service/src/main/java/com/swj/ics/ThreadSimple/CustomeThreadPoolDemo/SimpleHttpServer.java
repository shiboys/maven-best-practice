package com.swj.ics.ThreadSimple.CustomeThreadPoolDemo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by swj on 2018/1/11.
 */
public class SimpleHttpServer {
    
    //先试试一个线程
    private static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<>(1);

    //http服务器的监听端口
    private static int port = 8080;
    //http服务器的根目录
    private static String basePath;
    
    public static void setPort(int port) {
        SimpleHttpServer.port = port;
    }

    /**
     * 设置Http服务器的根路径
     * @param basePath
     */
    public static void setBasePath(String basePath) {
        File filePath = new File(basePath);
        if (basePath != null && filePath.exists() && filePath.isDirectory()) {
            SimpleHttpServer.basePath = basePath;
        } else {
            throw new RuntimeException("basePath not exists !");
        }
    }
    
    

    /**
     * 启动HttpServer，使用ServerSocket监听客户端的请求，使用线程池进行处理
     */
    public static void start() throws  Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket =null;
        //当接收到一个客户端的请求的时候，
        //生成一个HttpRequestHandler对象，并放入线程池。
        while ((clientSocket = serverSocket.accept()) != null) {
            threadPool.execute(new HttpRequestHandler(clientSocket));
        }
        serverSocket.close();
    }
    
    //建立一个任务Job 能实现Runnable接口的run方法。
    //在run方法里面接受到一个客户请求，使用线程池的线程来模拟服务器端的响应
    //目前只能做到响应图片和文字，图片使用输出流写会客户端响应
    //文字使用普通文本输出。
    //最后要关闭相关的流
    
    static class HttpRequestHandler implements Runnable {
        
        private Socket socket;
        
        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            PrintWriter writer = null;
            BufferedReader reader = null;//读取客户端的Http请求
            
            InputStream in = null;
            BufferedReader fileReader = null; //读取服务器上的文本文件
            String line = null;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                String headerFirstLine = reader.readLine();
                //GET https://qnwww2.autoimg.cn/newsdfs/g9/M12/4E/B6/autohomecar__wKjBzlpWwHWAPnDpAAYUN7FKpwk116.jpg HTTP/1.1
                String filePath = basePath + headerFirstLine.split(" ")[1];
                in = new FileInputStream(filePath);
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) { //文件输出
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int read = 0;
                    while ((read = in.read()) != -1) {
                        baos.write(read);
                    }
                    baos.flush();
                    byte[] fileBytes = baos.toByteArray();
                    
                    //先打印响应头部
                    printHttpResponseHeader(writer,"image/jpeg",fileBytes.length);
                    //在输出图片流
                    //经测试发现一个问题，图片的输出，IE浏览器没问题，大名鼎鼎的Chrome有问题。
                    
                    socket.getOutputStream().write(fileBytes,0,fileBytes.length);
                    baos.close();
                    
                   // socket.getOutputStream().flush();
                } else { //普通文本输出
                    //先打印响应头部
                    printHttpResponseHeader(writer,"text/html; charset=UTF-8",-1);
                    fileReader = new BufferedReader(new InputStreamReader(in));
                    while ((line = fileReader.readLine()) != null ) {
                        writer.println(line);
                    }
                }
                writer.flush();
            } catch (Exception e) {
                //返回500错误
                writer.println("HTTP/1.1 500");
                writer.println("");
                writer.flush();
            } finally {
                close(reader,writer,in,fileReader,socket);
            }
        }
        
        void printHttpResponseHeader(PrintWriter writer,String contentType,int contentLength) {
            writer.println("HTTP/1.1 200 OK");
            writer.println("Server: SimpleHttpServer");
            writer.println("Content-Type: "+contentType);
            if (contentLength > 0 ) {
                writer.println("Content-Length: "+contentLength);
            }
            writer.println("");
        }
        
        //todo 关闭
        void close(Closeable...arr) {
            if (arr != null && arr.length > 0) {
                for(Closeable item : arr) {
                    try {
                        item.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    
}
