package com.swj.ics.netty_study.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by swj on 2017/8/9.
 */
public class ServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channel active..");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将客户端发送到服务器端的消息打印出来
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        String clientMsg = new String(bytes,"utf-8");
        System.out.println("server receive :" + clientMsg);
        String response = "返回给客户端的响应：this is the server msg";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes("utf-8")))
       // .addListener(ChannelFutureListener.CLOSE) //在写完信息给客户端后，增加监听器，如果已经写完，则关闭跟客户端的通信通道
        ;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read complete !");
        ctx.flush();
    }
}
