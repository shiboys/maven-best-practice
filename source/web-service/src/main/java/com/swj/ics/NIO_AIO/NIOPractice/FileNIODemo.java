package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * author shiweijie
 * date 2018/11/8 下午3:51
 */
public class FileNIODemo {

    private static final String filePath = "/usr/local/temp/nio_file_demo.txt";
    public static final String CHARSET_NAME_UTF8 ="UTF-8";

    public static void main(String[] args) {
        try {
           /* writeFileWithChannel();
            System.out.println("done");*/

            readFileWithChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeFileWithChannel() throws Exception {
        File file = new File(filePath);
        if(!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byte[] destByteString ="hello,file nio buffer \r\n".getBytes(CHARSET_NAME_UTF8);

        byteBuffer.put(destByteString);

        byteBuffer.flip();
        fileChannel.write(byteBuffer);

        //clear 方法使得ByteBuffer的position为0，limit=64；
        byteBuffer.clear();

        destByteString = "another line string".getBytes(CHARSET_NAME_UTF8);
        byteBuffer.put(destByteString);
        byteBuffer.flip();
        fileChannel.write(byteBuffer);

        byteBuffer.clear();
        fileChannel.close();
        fileOutputStream.close();

    }

    private static void readFileWithChannel() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size() + 1);
        //阻塞模式，读取完成才能返回
        fileChannel.read(byteBuffer);
        byteBuffer.flip();

        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);

        System.out.println(new String(bytes,CHARSET_NAME_UTF8));


        byteBuffer.clear();
        fileChannel.close();
        fileInputStream.close();

    }

}
