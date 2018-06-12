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
        //flush的作用是将发送队列中的消息写入SocketChannel中并发给对方，从性能角度考虑,为了防止频繁的唤醒Selector进行消息发送，
        //Netty的write方法不是直接将消息写入SocketChannel中，调用write方法只是将待发送的消息发送到缓冲数组中，再通过调用flush方法，将发送缓冲区中的
        //消息全部写入到SocketChannel中。
        
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
