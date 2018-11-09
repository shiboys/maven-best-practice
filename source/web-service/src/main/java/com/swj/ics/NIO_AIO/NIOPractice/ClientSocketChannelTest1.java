package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * author shiweijie
 * date 2018/11/9 下午1:56
 */
public class ClientSocketChannelTest1 {

    public static class ClientRunnable implements Runnable {

        private InetSocketAddress remoteAddress = null;
        private String clientName;


        public ClientRunnable(int port,String host,String clientName) {
            remoteAddress = new InetSocketAddress(host,port);
            this.clientName = clientName;
        }

        @Override
        public void run() {
            try {
                Selector selector = Selector.open();
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                int ops = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                socketChannel.register(selector, ops,new Buffers(256,256));

                socketChannel.connect(remoteAddress);
                //等待TCP3次握手完成
                while (!socketChannel.finishConnect()) {
                    ;
                }
                System.out.println(clientName + " finish connect..");
                int counter = 1;

                while (!Thread.currentThread().isInterrupted()) {
                   //阻塞线程的运行
                    selector.select();

                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {

                            readDataFromServer(selectionKey);

                        } else if(selectionKey.isWritable()) {
                            writeDataToServer(selectionKey,counter);
                            counter++;
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        void readDataFromServer(SelectionKey selectionKey) throws IOException {
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            Buffers buffers= (Buffers) selectionKey.attachment();
            ByteBuffer byteBuffer = null;
            if(buffers == null) {
                byteBuffer = ByteBuffer.allocate(1024);
            } else {
                byteBuffer = buffers.getReadBuffer();
            }
            int readLength = socketChannel.read(byteBuffer);
            byte[] bytes = new byte[readLength];
            byteBuffer.flip();
            byteBuffer.get(bytes);
            System.out.println("response msg from server:" +
                    new String(bytes, Charset.forName(FileNIODemo.CHARSET_NAME_UTF8)));

            byteBuffer.clear();
        }

        void writeDataToServer(SelectionKey selectionKey,int counter) throws IOException {
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            Buffers buffers= (Buffers) selectionKey.attachment();
            byte[] writeBytes =
                    (clientName + counter +" request !").getBytes(Charset.forName(FileNIODemo.CHARSET_NAME_UTF8));
            ByteBuffer writeBuffer = buffers.getWriteBuffer();
            writeBuffer.put(writeBytes);
            writeBuffer.flip();

            socketChannel.write(writeBuffer);
            writeBuffer.clear();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        String host = "localhost";
        Thread threadA = new Thread(new ClientRunnable(port,host,"thread-A"));
        Thread threadB = new Thread(new ClientRunnable(port,host,"thread-B"));
        Thread threadC = new Thread(new ClientRunnable(port,host,"thread-C"));
        Thread threadD = new Thread(new ClientRunnable(port,host,"thread-D"));

        threadA.start();
        threadB.start();
        threadC.start();
        Thread.sleep(10000);

        //线程A结束运行
        threadA.interrupt();

        threadD.start();
    }
}

