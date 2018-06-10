package com.swj.ics.NIO_AIO.NIOPractice;

import java.awt.image.VolatileImage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import javafx.scene.paint.Stop;

/**
 * Created by swj on 2018/6/10.
 */
public class MultiplexerTimerClient implements Runnable {
    private String host;
    private int port;
    private SocketChannel socketChannel;
    private Selector selector;
    
    private volatile boolean stop =false;
    
    
    public MultiplexerTimerClient(String hostName,int port) {
        this.host = hostName;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public void stop() {
        this.stop = true;
    }
    
    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        //selector轮询客户单的事件
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keySet.iterator();
                SelectionKey selectionKey = null;
                if(iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    try {
                        handleSelectionKey(selectionKey);
                    } catch (IOException ex) {
                        selectionKey.cancel();
                        if (selectionKey.channel() != null) {
                            selectionKey.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(selector != null) { //关闭多路复用器，注册在上面的Channel和Pipe等资源都会自动关闭
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSelectionKey(SelectionKey key) throws IOException{
        
        if(!key.isValid()) {
            System.out.println("key is invalid");
            return;
        }
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if(key.isConnectable()) {
            if(socketChannel.finishConnect()) {
                socketChannel.register(selector,SelectionKey.OP_READ);
                doWrite(socketChannel);
            } else {
                System.exit(-1); //连接不成功，则退出 
            }
        }
        if (key.isReadable()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(byteBuffer);
            if (readBytes > 0) {
                byte[] bytes = new byte[readBytes];
                byteBuffer.flip();
                byteBuffer.get(bytes);
                String serverMsg = new String (bytes,"UTF-8");
                System.out.println("receive server msg :" + serverMsg);
                this.stop = true;
            } else if (readBytes < 0) {
                key.cancel();
                socketChannel.close();
            } else { //读到0字节，则忽略
                
            }
        }
    }

    private void doConnect() throws IOException {
        boolean connected = socketChannel.connect(new InetSocketAddress(host,port));
        if(connected) { //如果连接成功，则注册OP_Read,写请求，读取应答
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else { //注册connect的监听事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] reqBytes = "query time order".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(reqBytes.length);
        byteBuffer.put(reqBytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        if(!byteBuffer.hasRemaining()) {
           System.out.println("send order to server succeed !"); 
        }
    }
}
