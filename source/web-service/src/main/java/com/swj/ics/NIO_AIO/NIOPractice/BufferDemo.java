package com.swj.ics.NIO_AIO.NIOPractice;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * author shiweijie
 * date 2018/11/8 下午5:07
 */
public class BufferDemo {

    //使用byteBuffer和CharBuffer来读写数据。
    public static void decode(String str) throws UnsupportedEncodingException {
        ByteBuffer  byteBuffer = ByteBuffer.allocate(128);

        byte[] bytes = str.getBytes(FileNIODemo.CHARSET_NAME_UTF8);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        Charset charsetUTF8 = Charset.forName(FileNIODemo.CHARSET_NAME_UTF8);
        //对byteBuffer中的内容解码,生成charbuffer
        CharBuffer charBuffer = charsetUTF8.decode(byteBuffer);

        //解码之后，有效长度是0～limit
        char[] charArr = Arrays.copyOf(charBuffer.array(),charBuffer.limit());

        System.out.println("decode===>"+new String(charArr));
    }

    //encode(CharBuffer) ==>获得 ByteBuffer

    public static void encode(String str) {
        CharBuffer charBuffer = CharBuffer.allocate(128);
        charBuffer.append(str);
        charBuffer.flip();

        Charset charset = Charset.forName(FileNIODemo.CHARSET_NAME_UTF8);
        ByteBuffer byteBuffer = charset.encode(charBuffer);


        byte[] bytes = Arrays.copyOf(byteBuffer.array(),byteBuffer.limit()) ;

        System.out.println(Arrays.toString(bytes));
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String testStr ="hello,师伟杰";
        //decode(testStr);
        encode(testStr);
    }
}
