package com.swj.ics.netty_study.timeServerNettyEncodeDecode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import com.swj.ics.netty_study.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by swj on 2018/6/18.
 */
public class ServerTimeHandlerNoDecoder extends ChannelHandlerAdapter {
    
    private int counter;
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            handleReadWithByteBuf(ctx, (ByteBuf) msg);
        } else if (msg instanceof  String) {
            handleReadWithString(ctx,(String)msg);
        }
    }

    private void handleReadWithString(ChannelHandlerContext ctx, String msg) throws UnsupportedEncodingException {
         
        //String request = msg.substring(0,msg.length() -  System.lineSeparator().length());
        System.out.println("the time server receive request order " + msg + ",and the counter is "
                +(++counter));
        String timeServer = "query time order".equalsIgnoreCase(msg) ? NettyUtils.getNowTimeStr() : "bad order";
        timeServer +=  System.lineSeparator();

        ByteBuf writeBuf = Unpooled.copiedBuffer(timeServer.getBytes("UTF-8"));
        ctx.writeAndFlush(writeBuf);
    }
    private void handleReadWithByteBuf(ChannelHandlerContext ctx, ByteBuf byteBuf) throws UnsupportedEncodingException {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String request = new String(bytes, Charset.forName("UTF-8"))
                .substring(0,bytes.length -  System.lineSeparator().getBytes().length);

        /**
         * 使用 String.substring(0,string.indexOf(System.lineSeperator())是看不出效果的的
         * 这么使用是有问题的。
         */
        System.out.println("the time server receive request order " + request + ",and the counter is "
        +(++counter));
        String timeServer = "query time order".equalsIgnoreCase(request) ? NettyUtils.getNowTimeStr() : "bad order" 
                + System.lineSeparator();

        ByteBuf writeBuf = Unpooled.copiedBuffer(timeServer.getBytes("UTF-8"));
        ctx.writeAndFlush(writeBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
