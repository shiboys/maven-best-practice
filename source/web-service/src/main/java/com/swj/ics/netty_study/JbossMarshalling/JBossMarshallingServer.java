package com.swj.ics.netty_study.JbossMarshalling;

import com.swj.ics.netty_study.protobuf.ServerProtoBufMsgHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * author shiweijie
 * date 2018/11/6 上午8:53
 */
public class JBossMarshallingServer {
    public void bind(int port) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup,workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG,100)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(JBossMarshallingFactory.buildMarshallingDecoder());

                    socketChannel.pipeline().addLast(JBossMarshallingFactory.buildMarshallingEncoder());

                    socketChannel.pipeline().addLast(new ServerJbossMarshallingMsgHandler());
                }
            });


            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
        }

    }


}
