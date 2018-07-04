package com.swj.ics.NIO_AIO.AIOPractice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

/**
 * Created by swj on 2018/6/11.
 */
public class AsyncTimeServerHandler implements Runnable {
    private int port;
    CountDownLatch countDownLatch;
    AsynchronousServerSocketChannel assChannel;

    public AsyncTimeServerHandler(int port) {
        try {
            this.port = port;
            assChannel = AsynchronousServerSocketChannel.open();
            assChannel.bind(new InetSocketAddress(port));
            System.out.println("the time server start at port :" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        //接受客户端请求。
        doAccept();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        assChannel.accept(this,new AcceptCompleteHandler());
    }
    
    
}

class AcceptCompleteHandler implements CompletionHandler<AsynchronousSocketChannel,
        AsyncTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        //重复调用accept的原因在于：调用该方法之后,如果有新的客户端连接接入，系统将回调我们
        // CompletionHandler实例的complete方法，表示新的客户端已经接入成功。因为一个 AsynchronousSocketChannel
        //可以接受成千上万个客户端，所有需要继续调用其accept方法，接受其他客户端连接，最终形成一个循环。
        //每当接受一个新的客户端连接成功之后，再异步接受新的客户端连接。
        attachment.assChannel.accept(attachment,this);
        
        //服务端先读取连接上来的客户端的请求
        //然后再跟根据请求的内容将server time 写回客户端。
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //第一个参数是byteBuffer,第二个参数是将byteBuffer作为attachment传入到回调函数中
        result.read(byteBuffer,byteBuffer,new ReadCompleteHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        //通过调用countdownlatch,来停止主线程
        attachment.countDownLatch.countDown();
    }
}

class ReadCompleteHandler implements CompletionHandler<Integer,ByteBuffer> {
    
    private AsynchronousSocketChannel channel;
    public ReadCompleteHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] bytes = new byte[ attachment.remaining()];
        attachment.get(bytes);
        try {
            String clientReq = new String(bytes,"UTF-8");
            System.out.println("receive client msg " + clientReq);
            String responseMsg ="bad order";
            if("query time order".equalsIgnoreCase(clientReq)) {
                responseMsg= getLocalTimeStr();
            }
            doWrite(responseMsg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String responseMsg) {
        try {
            byte[] bytes = responseMsg.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            channel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if(attachment.hasRemaining()) { //如果消息一次没写完，则继续写
                        channel.write(attachment,attachment,this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    //写消息到客户端失败,则关闭通道
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       
    }

    private String getLocalTimeStr() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

