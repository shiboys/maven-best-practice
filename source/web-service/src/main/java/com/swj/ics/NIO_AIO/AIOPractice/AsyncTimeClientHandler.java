package com.swj.ics.NIO_AIO.AIOPractice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * Created by swj on 2018/6/11.
 */
public class AsyncTimeClientHandler implements Runnable,CompletionHandler<Void,AsyncTimeClientHandler> {
    private String host;
    private int port;
    AsynchronousSocketChannel channel;
    CountDownLatch countDownLatch;
    //todo:要学会查看线程的堆栈
    public AsyncTimeClientHandler(String host, int port) {
        this.port =port;
        this.host = host;
        countDownLatch = new CountDownLatch(1);
        try {
            channel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        channel.connect(new InetSocketAddress(host,port),this,this);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        String msg ="QUERY TIME ORDER";
        try {
            byte[] bytes = msg.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            channel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer byteBuffer) {
                    if(byteBuffer.hasRemaining()) {
                        channel.write(byteBuffer, byteBuffer,this);
                    } else {
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer buffer) {
                                buffer.flip();
                                byte[] readBytes = new byte[buffer.remaining()];
                                buffer.get(readBytes);
                                try {
                                    String msgBody = new String(readBytes,"UTF-8");
                                    System.out.println("client receive msg from server:"+msgBody);
                                    countDownLatch.countDown();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                countDownLatch.countDown();
                                if (channel != null) {
                                    try {
                                        channel.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    countDownLatch.countDown();
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        countDownLatch.countDown();
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
