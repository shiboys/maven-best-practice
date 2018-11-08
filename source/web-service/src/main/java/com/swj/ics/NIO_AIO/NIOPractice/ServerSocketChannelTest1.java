package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.ibatis.annotations.SelectKey;

/**
 * author shiweijie
 * date 2018/11/8 下午7:45
 */
public class ServerSocketChannelTest1 {

    public static class ServerRunnable implements Runnable {

        private InetSocketAddress localAddress ;

        public ServerRunnable(int port) {
            this.localAddress = new InetSocketAddress(port);
        }

        @Override
        public void run() {

            ServerSocketChannel serverChannel = null;
            Selector selector = null;
            Random random = new Random();
            try {
                serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                selector = Selector.open();

                //设置服务端监听的端口，并设置最大链接缓冲数为100。
                serverChannel.bind(localAddress,100);

                int ops = SelectionKey.OP_ACCEPT;
                // serverSocketChannel 只关心Accept事件
                serverChannel.register(selector, ops);

                while (!Thread.currentThread().isInterrupted()) {
                    int selectResult = selector.select();
                    if (selectResult == 0) { //没有事件进来
                        continue;
                    }
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
                    SelectionKey selectionKey = it.next();
                    it.remove(); //防止下次同一个事件再次到达

                    if(selectionKey.isAcceptable()) {
                        acceptClientChannel(selector);
                    } else if (selectionKey.isReadable()) { //通道感兴趣的读事件且底层有数据可读
                        readDataFromClient(selectionKey);
                    } else if(selectionKey.isWritable()) {

                    }


                    Thread.sleep(500 + random.nextInt(500));
                }

            } catch (IOException e) {
                e.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    serverChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        void acceptClientChannel(Selector selector) {
            try {
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.register(selector, SelectionKey.OP_READ, new Buffers(256, 256));
                //服务端开启的这个 channel也要设置为非阻塞
                socketChannel.configureBlocking(false);
                System.out.println("receive client request from : " + socketChannel.getRemoteAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        void readDataFromClient(SelectionKey selectionKey) throws IOException {
            Buffers buffers = (Buffers)selectionKey.attachment();
            ByteBuffer readBuffer = buffers.getReadBuffer();
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            socketChannel.read(readBuffer);
            readBuffer.flip();

            byte[] bytes = new byte[readBuffer.limit()];
            readBuffer.put(bytes);
            System.out.println("receive client request :" +
                    new String(bytes, Charset.forName(FileNIODemo.CHARSET_NAME_UTF8)));

            //重新讲读取的内容置为0
            readBuffer.rewind();

            ByteBuffer writeBuffer = buffers.getWriteBuffer();
            writeBuffer.put("echo from server : ".getBytes( Charset.forName(FileNIODemo.CHARSET_NAME_UTF8)));
            writeBuffer.put(readBuffer);

            readBuffer.clear();

            selectionKey.interestOps(selectionKey.interestOps() & SelectionKey.OP_WRITE);
        }

        void writeDataToClient(SelectionKey selectionKey) throws IOException {
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            Buffers buffers = (Buffers)selectionKey.attachment();
            ByteBuffer writeBuffer = buffers.getWriteBuffer();

            int writeLength = 0;
            while (writeBuffer.hasRemaining()) {
                writeLength = socketChannel.write(writeBuffer);
                if(writeLength == 0) { //表明底层Socket的写缓冲已满
                    break;
                }
            }
            // 可再次写
            writeBuffer.compact();

            if(writeLength != 0) { //说明数据全部写入底层Socket的写缓冲区
                /*取消通道的写事件*/
                selectionKey.interestOps(selectionKey.interestOps() & (~SelectionKey.OP_WRITE));
            }

        }


    }
}
