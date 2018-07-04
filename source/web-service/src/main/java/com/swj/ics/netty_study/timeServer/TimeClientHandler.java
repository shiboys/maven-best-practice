package com.swj.ics.netty_study.timeServer;

import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by swj on 2018/6/12.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //当客户端和服务器端TCP链路建立成功之后，Netty的NIO线程会调用channelActive方法，
    //发送查询时间的指令给服务器,
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] msgBytes = "query order time".getBytes("UTF-8");
        ByteBuf byteBuf = Unpooled.copiedBuffer(msgBytes);
        ctx.writeAndFlush(byteBuf);
    }

    //当服务器返回应答消息的时候，channelRead方法被调用，从中读取并打印服务器返回的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        //这里尝试不使用byte进行转换
        String response = byteBuf.toString(Charset.forName("utf-8"));
        System.out.println("server response :" + response);
    }
}
