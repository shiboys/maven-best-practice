package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * Created by swj on 2018/6/10.
 */
public class MultiplexerTimerServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop = false;

    public MultiplexerTimerServer(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println("the time server started on port : " + port);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                //轮询和休眠一秒钟。无论是否有读写等事件发生，selector都会每隔1s被唤醒一次。当有就绪状态的Channel时，Selector将返回该Channel的SelectionKey集合。
                //通过对SelectionKey集合的遍历，可以进行网络的异步读写操作。
                selector.select(1000);
                //Set<SelectionKey> keySet = selector.keys();
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keySet.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleSelectionKey(key);
                    } catch (IOException ex) {
                        key.cancel();
                        if (key.channel() != null) {
                            key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) { //关闭多路复用器之后，所有在上面注册的Channel和Pipe等资源都被
            //自动去注册和关闭，所以不需要重复释放资源。
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSelectionKey(SelectionKey key) throws IOException {
        if (key.isValid()) {

            if (key.isAcceptable()) { //有新的连接请求，注册到selector,注册事件为SelectionKey.OP_Read
                //因为注册accept事件的通道类型为ServerSocketChannel,所以这个转换不会失败
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) { //读取客户端的请求
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                //byte[] bytes =
                //注册到read事件的通道类型为SocketChannel，所以这个转换也不会失败
                SocketChannel socketChannel = (SocketChannel) key.channel();
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0) { //能从通道读到消息
                    byteBuffer.flip();//读写转换，将缓冲区的limit设置为position,position设置为0
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String requestMsg = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order from client: " + requestMsg);
                    String serverTime = "BAD ORDER";
                    if ("query time order".equalsIgnoreCase(requestMsg)) {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        serverTime = formatter.format(now);
                    }
                    //通道复用的优势就体现出来了，不需要向BIO那样，通过socket.getOutputStream()打开写入流
                    doWrite(socketChannel, serverTime);
                } else if (readBytes < 0) {
                    //需要进行对端链路关闭
                    key.cancel();
                    socketChannel.close();
                } else //等于0的情况不处理。 
                    ;
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String serverTime) throws IOException {
        if (!StringUtils.isEmpty(serverTime)) {
            byte[] bytes = serverTime.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        }

    }
}
