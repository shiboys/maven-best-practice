package com.swj.ics.netty_study.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by swj on 2017/8/9.
 */
public class Client {
    public static void main(String[] args) {
        EventLoopGroup cGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(cGroup)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        //client 先发送消息
        String msg = "hello,I'm from client msg !";
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",8888).sync();
            //同时给服务器端发送多条消息
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));

           // ChannelFuture cf2 = bootstrap.connect("127.0.0.1",8080).sync();
           // cf2.channel().writeAndFlush(Unpooled.copiedBuffer("client 2's msg1".getBytes()));
            Thread.sleep(500);
           // cf2.channel().writeAndFlush(Unpooled.copiedBuffer("client 2's msg 2".getBytes()));

            //等待关闭
            channelFuture.channel().closeFuture().sync();
           // cf2.channel().closeFuture().sync();

            cGroup.shutdownGracefully();

        } catch (InterruptedException ex) {

        } finally {

        }


    }
}
