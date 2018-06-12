package com.swj.ics.netty_study.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * author shiweijie
 * date 2018/6/12 下午7:29
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String req = new String(bytes,"UTF-8");
        String responseMsg = "bad order";
        if(req.equalsIgnoreCase("query order time")) {
            responseMsg = TimeServer.getLocalTimeStr();
        }
        ByteBuf writeBuf = Unpooled.copiedBuffer(responseMsg.getBytes("UTF-8"));
        ctx.write(writeBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
