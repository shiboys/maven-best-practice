package com.swj.ics.NIO_AIO.NIOPractice;

import java.nio.ByteBuffer;

/**
 * author shiweijie
 * date 2018/11/8 下午4:57
 * 自定义Buffer类中包含读缓冲区和写缓冲区，用于注册通道时的附加对象。
 */
public class Buffers {
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    public Buffers(int readCapacity,int writeCapacity) {
        this.readBuffer = ByteBuffer.allocate(readCapacity);
        this.writeBuffer = ByteBuffer.allocate(writeCapacity);
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public void setReadBuffer(ByteBuffer readBuffer) {
        this.readBuffer = readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public void setWriteBuffer(ByteBuffer writeBuffer) {
        this.writeBuffer = writeBuffer;
    }
}
