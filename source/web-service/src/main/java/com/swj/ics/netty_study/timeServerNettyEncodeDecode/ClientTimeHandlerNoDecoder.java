package com.swj.ics.netty_study.timeServerNettyEncodeDecode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by swj on 2018/6/18.
 */
public class ClientTimeHandlerNoDecoder extends ChannelHandlerAdapter {
    
    private int counter;
    
    private byte[] sendReqByte = null;
    
    public ClientTimeHandlerNoDecoder() {
        sendReqByte = ("query time order" + System.lineSeparator()).getBytes();
    }

    /**
     * 循环100次执行发送消息的函数
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf sendMsgByteBuf = null;
        for(int i=0 ;i <100;i++) {
            //sendMsgByteBuf = Unpooled.copiedBuffer(sendReqByte);
            sendMsgByteBuf = Unpooled.buffer(sendReqByte.length);
            sendMsgByteBuf.writeBytes(sendReqByte);
            ctx.writeAndFlush(sendMsgByteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            readServerMsgWithByteBuf((ByteBuf) msg);
        } else if (msg instanceof String) {
            readServerMsgWithString((String)msg);
        }
    }

    private void readServerMsgWithString(String serverResp) {
        System.out.println("server response is " + serverResp + ",counter" + (++counter));
    }
    
    private void readServerMsgWithByteBuf(ByteBuf byteBuf) {
        byte[] readBytes= new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(readBytes);
        String serverResp = new String(readBytes);
        String body = serverResp.substring(0, serverResp.indexOf(System.lineSeparator()));

        System.out.println("server response is " + body + ",counter" + (++counter));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
